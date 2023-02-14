package com.saeedsanslimits.findidealweather.web;

import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import com.saeedsanslimits.findidealweather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class WebServiceController {
    private final WeatherService weatherService;

    public WebServiceController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/attr")
    public List<HourlyForecast> getReservations(@RequestParam(value = "city") String cityName
            , @RequestParam(value = "temperature", required = false) String temperature) {

        RestTemplate restTemplate = new RestTemplate();

        String cityInfoJSON = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=6a6ca0c6ee76b3894259100bede87209";
        List<LinkedHashMap> cityInfo = restTemplate.getForObject(cityInfoJSON, ArrayList.class);
        String weatherForecastJSON = "https://api.openweathermap.org/data/3.0/onecall?lat=" + cityInfo.get(0).get("lat") + "&lon=" + cityInfo.get(0).get("lon") + "&units=metric&exclude=current,minutely,daily,alerts&appid=6a6ca0c6ee76b3894259100bede87209";
        WeatherForecast weatherForecast = restTemplate.getForObject(weatherForecastJSON, WeatherForecast.class);

        return weatherService.getIdealHours(weatherForecast, temperature);
    }
}
