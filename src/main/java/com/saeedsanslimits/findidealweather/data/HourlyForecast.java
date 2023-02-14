package com.saeedsanslimits.findidealweather.data;

import lombok.Data;

@Data
public class HourlyForecast {
    private int dt;
    private double temp;
    private double feels_like;
    private int pressure;
    private int humidity;
    private int uvi;
    private int clouds;
    private int visibility;
    private double wind_speed;
    private int wind_deg;
}
