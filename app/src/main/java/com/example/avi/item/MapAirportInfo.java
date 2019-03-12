package com.example.avi.item;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MapAirportInfo implements Serializable {
    private String iata;
    private String city;
    private LatLng latLng;


    public MapAirportInfo(String iata, String city, LatLng latLng) {
        this.iata = iata;
        this.city = city;
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getIata() {
        return iata;
    }

    public String getCity() {
        return city;
    }

}
