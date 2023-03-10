package com.saeedsanslimits.findidealweather.web;

import com.saeedsanslimits.findidealweather.data.HourlyForecast;
import com.saeedsanslimits.findidealweather.data.WeatherForecast;
import com.saeedsanslimits.findidealweather.service.WeatherService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherServiceTest {
    private final WeatherService weatherService;

    WeatherServiceTest(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Test
    void testGetIdealHours_valid() {
        WeatherForecast forecastInfo = buildWeatherForecast();

        List<HourlyForecast> idealHours = weatherService.returnIdealForecastedHours(forecastInfo, "hot", null, null);

        assertEquals(1, idealHours.size());
        assertEquals(30, idealHours.get(0).getTemp());

        idealHours = weatherService.returnIdealForecastedHours(forecastInfo, "mild", null, null);

        assertEquals(1, idealHours.size());
        assertEquals(15, idealHours.get(0).getTemp());

        idealHours = weatherService.returnIdealForecastedHours(forecastInfo, "cold", null, null);

        assertEquals(1, idealHours.size());
        assertEquals(5, idealHours.get(0).getTemp());
    }

    private WeatherForecast buildWeatherForecast() {
        WeatherForecast forecastInfo = new WeatherForecast();
        List<HourlyForecast> hourlyForecasts = new ArrayList<>();
        HourlyForecast hourlyForecast1 = new HourlyForecast();
        HourlyForecast hourlyForecast2 = new HourlyForecast();
        HourlyForecast hourlyForecast3 = new HourlyForecast();
        hourlyForecast1.setTemp(5);
        hourlyForecast2.setTemp(15);
        hourlyForecast3.setTemp(30);
        hourlyForecasts.add(hourlyForecast1);
        hourlyForecasts.add(hourlyForecast2);
        hourlyForecasts.add(hourlyForecast3);
        forecastInfo.setHourly(hourlyForecasts);
        return forecastInfo;
    }
}
