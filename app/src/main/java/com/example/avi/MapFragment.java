package com.example.avi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.avi.item.MapAirportInfo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final Dot DOT = new Dot();
    private static final Gap GAP = new Gap(12);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
    private static final String TAG = "MapFragment";
    private SupportMapFragment mapFragment;
    private static final float STEP = 0.05f;
    private static final int PLANE_SPEED = 100;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapAirportInfo airportInfoStart = (MapAirportInfo) getArguments().getSerializable("start_city");
        MapAirportInfo airportInfoEnd = (MapAirportInfo) getArguments().getSerializable("end_city");

        ArrayList<LatLng> path = getPath(airportInfoStart.getLatLng(), airportInfoEnd.getLatLng());

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(path)
                .geodesic(true)
                .width(12)
                .color(Color.BLUE);
        Polyline polyline = googleMap.addPolyline(polylineOptions);
        polyline.setPattern(PATTERN_DOTTED);
        polyline.setJointType(JointType.ROUND);
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(airportInfoStart.getLatLng());
        builder.include(airportInfoEnd.getLatLng());
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding);
        googleMap.moveCamera(cameraUpdate);

        startPlane(path, googleMap);
    }

    private void startPlane(final ArrayList<LatLng> path, GoogleMap googleMap) {
        final Marker plane = googleMap.addMarker(new MarkerOptions()
                .position(path.get(0))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane)));

        final int[] pos = {0};
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pos[0]++;
                plane.setPosition(path.get(pos[0]));
                handler.postDelayed(this, PLANE_SPEED);
            }
        }, PLANE_SPEED);
    }

    private ArrayList<LatLng> getPath(LatLng startLatLang, LatLng endLatLang) {
        ArrayList<LatLng> list = new ArrayList<>();

        float length = (float) Math.sqrt(Math.pow(startLatLang.latitude - endLatLang.latitude, 2) + Math.pow(startLatLang.longitude - endLatLang.longitude, 2));
        float angle = (float) ((startLatLang.latitude - endLatLang.latitude) / (startLatLang.longitude - endLatLang.longitude));


        Log.d(TAG, "length: " + length + " angle: " + angle);

        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);


        list.add(startLatLang);
        for (float i = 0; i < length - 1; i += STEP) {
            float x = (startLatLang.longitude - endLatLang.longitude) > 0 ? (i * -1) : i;

            float latitude = (float) (Math.sin((x * 2 * Math.PI) / length));
            float longitude = x;

            float newLat = (float) (((longitude * sin + latitude * cos)) + startLatLang.latitude);
            float newLon = (float) (((longitude * cos + latitude * sin)) + startLatLang.longitude);

            list.add(new LatLng(newLat, newLon));
        }
        list.add(endLatLang);


        return list;
    }


}
