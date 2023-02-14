package com.saeedsanslimits.findidealweather.web;

import com.saeedsanslimits.findidealweather.data.CityInfo;
import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import com.saeedsanslimits.findidealweather.service.WeatherService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    private final RestTemplate restTemplate;

    public WebServiceController(WeatherService weatherService, RestTemplate restTemplate) {
        this.weatherService = weatherService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/attr")
    public ResponseEntity<WeatherForecast> getReservations(@RequestParam(value = "city") String cityName
            , @RequestParam(value = "temperature", required = false) String temperature
            , @RequestParam(value = "wind", required = false) String wind
            , @RequestParam(value = "cloud", required = false) String cloud) {


        List<CityInfo> resultList = bestResultsBasedOnCityName(cityName);
        if (resultList == null || resultList.isEmpty()) {
            return createBadRequestResponse("City not found");
        }
        CityInfo bestMatchedCity = resultList.get(0);

        WeatherForecast weatherForecast = getWeatherForecast(bestMatchedCity);
        if (Objects.isNull(weatherForecast)) {
            return createBadRequestResponse("No forecast available");
        }

        List<HourlyForecast> hourlyForecasts = weatherService.returnIdealForecastedHours(weatherForecast, temperature, wind, cloud);
        weatherForecast.setHourly(hourlyForecasts);
        return new ResponseEntity<>(weatherForecast, HttpStatus.OK);
    }

    private ResponseEntity<WeatherForecast> createBadRequestResponse(String message) {
        return ResponseEntity.badRequest().header("error-message", message).build();
    }

    private WeatherForecast getWeatherForecast(CityInfo cityInfo) {
        String weatherForecastJSON = "https://api.openweathermap.org/data/3.0/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,minutely,daily,alerts&appid=6a6ca0c6ee76b3894259100bede87209";
        return restTemplate.getForObject(weatherForecastJSON, WeatherForecast.class, cityInfo.getLat(), cityInfo.getLon());
    }

    private List<CityInfo> bestResultsBasedOnCityName(String cityName) {
        String cityInfoUrl = "http://api.openweathermap.org/geo/1.0/direct?q={cityName}&limit=1&appid=6a6ca0c6ee76b3894259100bede87209";
        ResponseEntity<List<CityInfo>> response = restTemplate.exchange(cityInfoUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<CityInfo>>() {
        }, cityName);
        return response.getBody();
    }
}