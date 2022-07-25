package com.example.earthquakemonitor.api;

import com.squareup.moshi.Json;

public class Properties {

    @Json(name="mag") //Del json viene con el atributo mag y en java lo cambiamos a magnitude
    private double magnitude;
    private String place;
    private Long time;

    public double getMag() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public Long getTime() {
        return time;
    }
}
