package com.saeedsanslimits.findidealweather.data;

import lombok.Data;

import java.util.List;
@Data
public class WeatherForecast {

    private double lat;
    private double lon;
    private List<HourlyForecast> hourly;

}

