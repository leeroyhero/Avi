package com.example.avi.item;

import com.google.android.gms.maps.model.LatLng;

public class PathItem {
    private LatLng latLng;
    private float angle;

    public PathItem(LatLng latLng, float angle) {
        this.latLng = latLng;
        this.angle = angle;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public float getAngle() {
        return angle;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
