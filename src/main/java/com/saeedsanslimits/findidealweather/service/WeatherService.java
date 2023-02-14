package com.saeedsanslimits.findidealweather.service;

import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private static final double HOT_TEMP = 25.0;
    private static final double COLD_TEMP = 10.0;
    private static final double STORMY_WIND = 30.0;
    private static final double CALM_WIND = 5.0;
    private static final double OVERCAST_CLOUD = 30.0;
    private static final double CLEAR_CLOUD = 5.0;

    public List<HourlyForecast> returnIdealForecastedHours(WeatherForecast forecastInfo, String temperature, String wind, String cloud) {

        return forecastInfo.getHourly().stream()
                .filter(hourlyWeather -> temperatureSatisfies(hourlyWeather, temperature))
                .filter(hourlyWeather -> windSatisfies(hourlyWeather, wind))
                .filter(hourlyWeather -> cloudSatisfies(hourlyWeather, cloud))
                .collect(Collectors.toList());
    }

    public boolean temperatureSatisfies(HourlyForecast hourlyWeather, String temperature) {
        if (!StringUtils.hasText(temperature)) {
            return true;
        }
        return switch (temperature) {
            case "hot" -> hourlyWeather.getTemp() >= HOT_TEMP;
            case "mild" -> COLD_TEMP <= hourlyWeather.getTemp() && hourlyWeather.getTemp() < HOT_TEMP;
            case "cold" -> hourlyWeather.getTemp() < COLD_TEMP;
            default -> false;
        };
    }

    public boolean windSatisfies(HourlyForecast hourlyWeather, String wind) {
        if (!StringUtils.hasText(wind)) {
            return true;
        }
        return switch (wind) {
            case "stormy" -> hourlyWeather.getWind_speed() >= STORMY_WIND;
            case "Windy" -> CALM_WIND <= hourlyWeather.getWind_speed() && hourlyWeather.getWind_speed() < STORMY_WIND;
            case "calm" -> hourlyWeather.getWind_speed() < CALM_WIND;
            default -> false;
        };
    }

    public boolean cloudSatisfies(HourlyForecast hourlyWeather, String cloud) {
        if (!StringUtils.hasText(cloud)) {
            return true;
        }
        return switch (cloud) {
            case "overcast" -> hourlyWeather.getWind_speed() >= OVERCAST_CLOUD;
            case "scattered" -> CLEAR_CLOUD <= hourlyWeather.getWind_speed() && hourlyWeather.getWind_speed() < 60;
            case "clear" -> hourlyWeather.getWind_speed() < CLEAR_CLOUD;
            default -> false;
        };
    }
}
