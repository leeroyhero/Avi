package com.example.avi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.avi.item.MapAirportInfo;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapAirportInfo pskov=new MapAirportInfo("PKV", "Pskov", new LatLng(57.8182098f, 28.3307043f));
        MapAirportInfo paris=new MapAirportInfo("PAR", "Paris", new LatLng(48.85634f, 2.342587f));
        MapAirportInfo washington=new MapAirportInfo("WAS", "Washington", new LatLng(38.895112, -77.036366));
        MapAirportInfo stambul=new MapAirportInfo("STA", "Stambul", new LatLng(41.015669, 28.9742363));

        MapFragment mapFragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("start_city", paris);
        bundle.putSerializable("end_city", pskov);
        mapFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mapFragment).commit();
    }
}
