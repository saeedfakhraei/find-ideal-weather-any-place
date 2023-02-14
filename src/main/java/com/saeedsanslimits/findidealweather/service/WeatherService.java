package com.saeedsanslimits.findidealweather.service;

import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    public List<HourlyForecast> getIdealHours(WeatherForecast forecastInfo, String temperature) {

        return forecastInfo.getHourly().stream()
                .filter(hourlyWeather -> satisfies(hourlyWeather, temperature))
                .collect(Collectors.toList());
    }

    public boolean satisfies(HourlyForecast hourlyWeather, String temperature) {
        return switch (temperature) {
            case "hot" -> hourlyWeather.getTemp() >= 25;
            case "mild" -> 10 <= hourlyWeather.getTemp() && hourlyWeather.getTemp() < 25;
            case "cold" -> hourlyWeather.getTemp() < 10;
            default -> false;
        };
    }
}
