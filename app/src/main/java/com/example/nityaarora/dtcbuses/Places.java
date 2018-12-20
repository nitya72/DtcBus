package com.example.nityaarora.dtcbuses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Places {


    public String findbus(LatLng latLng) {

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=" + latLng.latitude + "," + latLng.longitude);
        stringBuilder.append("&radius=" + 1500);
        stringBuilder.append("&type=" + "bus_station");
        //stringBuilder.append("&keyword="+"bus_station");
        stringBuilder.append("&key=" + "AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s");


        String url = stringBuilder.toString();
        Log.i("blah", url);

        return url;
    }
}

