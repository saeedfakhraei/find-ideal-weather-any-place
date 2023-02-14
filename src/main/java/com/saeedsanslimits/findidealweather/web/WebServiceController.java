package com.saeedsanslimits.findidealweather.web;

import com.saeedsanslimits.findidealweather.data.CityInfo;
import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import com.saeedsanslimits.findidealweather.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api")
public class WebServiceController {
    private final WeatherService weatherService;

    public WebServiceController(WeatherService weatherService, RestTemplate restTemplate) {
        this.weatherService = weatherService;
    }

    @GetMapping("/attr")
    public ResponseEntity<WeatherForecast> findYourIdealHoursWeatherWise(@RequestParam(value = "city") String cityName
            , @RequestParam(value = "temperature", required = false) String temperature
            , @RequestParam(value = "wind", required = false) String wind
            , @RequestParam(value = "sky", required = false) String sky) {


        CityInfo cityInfo = weatherService.bestResultsBasedOnCityName(cityName);
        if (cityInfo == null) {
            return createBadRequestResponse("City not found");
        }


        WeatherForecast weatherForecast = weatherService.getWeatherForecast(cityInfo);
        if (Objects.isNull(weatherForecast)) {
            return createBadRequestResponse("No forecast available");
        }

        List<HourlyForecast> hourlyForecasts = weatherService.returnIdealForecastedHours(weatherForecast, temperature, wind, sky);
        weatherForecast.setHourly(hourlyForecasts);
        return new ResponseEntity<>(weatherForecast, HttpStatus.OK);
    }

    private ResponseEntity<WeatherForecast> createBadRequestResponse(String message) {
        return ResponseEntity.badRequest().header("error-message", message).build();
    }
}