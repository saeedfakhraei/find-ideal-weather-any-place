package com.saeedsanslimits.findidealweather.data;

import lombok.Data;

import java.util.List;
@Data
public class WeatherForecast {

    private List<HourlyForecast> hourly;

}

