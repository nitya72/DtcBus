package com.example.nityaarora.dtcbuses;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nityaarora.dtcbuses.GeoAddress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList markerpoints=new ArrayList();
    EditText from; EditText to;
    Button get;
    String lfrom; String lto;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.length>0){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        get = findViewById(R.id.get);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap = googleMap;

        Toast.makeText(this, "map appeared", Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16f));

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("yeahhh",location.toString());
                Double latitude=location.getLatitude();
                Double longitude=location.getLongitude();
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("updated one"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f));

                GeoAddress obj=new GeoAddress();
                String str=obj.getAddress(latitude,longitude,getApplicationContext());

                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f));
                markerpoints.add(latLng);
                Log.i("bla",latLng.toString()+"\t"+str);
                from.setText(str);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location xlocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng user=new LatLng(xlocation.getLatitude(),xlocation.getLongitude());
            Log.i("yeahh",user.toString());
            mMap.addMarker(new MarkerOptions().position(user).title("my"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,16f));
        }

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lfrom=from.getText().toString();
                lto=to.getText().toString();

                if(!lfrom.equals("") && !lto.equals("")){

                    GeoAddress obj=new GeoAddress();
                    LatLng fcoor=obj.getCoordinates(lfrom,getApplicationContext());
                    LatLng tcoor=obj.getCoordinates(lto,getApplicationContext());

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(fcoor).title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    mMap.addMarker(new MarkerOptions().position(tcoor).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                    Directions directions=new Directions();
                    directions.run(mMap,lfrom,lto);
                }
                else{
                    //Dialog Box Alert here
                }
            }
        });

    }


}