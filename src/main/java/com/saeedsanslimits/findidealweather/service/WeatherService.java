package com.saeedsanslimits.findidealweather.service;

import com.saeedsanslimits.findidealweather.data.CityInfo;
import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private static final double HOT_TEMP = 25.0;
    private static final double COLD_TEMP = 10.0;
    private static final double STORMY_WIND = 30.0;
    private static final double CALM_WIND = 5.0;
    private static final double OVERCAST_CLOUD = 30.0;
    private static final double CLEAR_CLOUD = 5.0;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherForecast getWeatherForecast(CityInfo cityInfo) {
        String weatherForecastJSON = "https://api.openweathermap.org/data/3.0/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,minutely,daily,alerts&appid=6a6ca0c6ee76b3894259100bede87209";
        return restTemplate.getForObject(weatherForecastJSON, WeatherForecast.class, cityInfo.getLat(), cityInfo.getLon());
    }

    // returns the best match for the city name from a list of potential matches
    public CityInfo bestResultsBasedOnCityName(String cityName) {
        String cityInfoUrl = "http://api.openweathermap.org/geo/1.0/direct?q={cityName}&limit=1&appid=6a6ca0c6ee76b3894259100bede87209";
        ResponseEntity<List<CityInfo>> response = restTemplate.exchange(cityInfoUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<CityInfo>>() {
        }, cityName);
        List<CityInfo> bestResults = response.getBody();
        if (bestResults == null || bestResults.isEmpty()) {
            return null;
        }
        return bestResults.get(0);
    }
    public List<HourlyForecast> returnIdealForecastedHours(WeatherForecast forecastInfo, String temperature, String wind, String sky) {

        return forecastInfo.getHourly().stream()
                .filter(hourlyWeather -> isIdeal(hourlyWeather, temperature, wind, sky))
                .collect(Collectors.toList());
    }

    private boolean isIdeal(HourlyForecast hourlyWeather, String temperature, String wind, String cloud) {
        return isTemperatureIdeal(hourlyWeather, temperature)
                && isWindIdeal(hourlyWeather, wind)
                && isSkyIdeal(hourlyWeather, cloud);
    }

    public boolean isTemperatureIdeal(HourlyForecast hourlyWeather, String temperature) {
        if (!StringUtils.hasText(temperature)) return true;
        return switch (temperature) {
            case "hot" -> hourlyWeather.getTemp() >= HOT_TEMP;
            case "mild" -> COLD_TEMP <= hourlyWeather.getTemp() && hourlyWeather.getTemp() < HOT_TEMP;
            case "cold" -> hourlyWeather.getTemp() < COLD_TEMP;
            default -> false;
        };
    }

    public boolean isWindIdeal(HourlyForecast hourlyWeather, String wind) {
        if (!StringUtils.hasText(wind)) return true;
        return switch (wind) {
            case "stormy" -> hourlyWeather.getWind_speed() >= STORMY_WIND;
            case "Windy" -> CALM_WIND <= hourlyWeather.getWind_speed() && hourlyWeather.getWind_speed() < STORMY_WIND;
            case "calm" -> hourlyWeather.getWind_speed() < CALM_WIND;
            default -> false;
        };
    }

    public boolean isSkyIdeal(HourlyForecast hourlyWeather, String cloud) {
        if (!StringUtils.hasText(cloud)) return true;
        return switch (cloud) {
            case "overcast" -> hourlyWeather.getWind_speed() >= OVERCAST_CLOUD;
            case "cloudy" -> CLEAR_CLOUD <= hourlyWeather.getWind_speed() && hourlyWeather.getWind_speed() < 60;
            case "clear" -> hourlyWeather.getWind_speed() < CLEAR_CLOUD;
            default -> false;
        };
    }
}
