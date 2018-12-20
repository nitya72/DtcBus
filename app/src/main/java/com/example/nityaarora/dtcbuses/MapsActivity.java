package com.example.nityaarora.dtcbuses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.annotation.Nullable;
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
import com.google.android.gms.common.SignInButton;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList markerpoints = new ArrayList();
    EditText from;
    EditText to;
    Button get;
    String lfrom;
    String lto;
    EditText a1, a2, a3, a4, a5;
    int arr[];
    int params[][];
    int counter;
    LatLng q[][];

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
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
        arr=new int[4];
        params=new int[25][4];
        counter=0;

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        get = findViewById(R.id.get);
        a1 = findViewById(R.id.from1);
        a2 = findViewById(R.id.from2);
        a3 = findViewById(R.id.from3);
        a4 = findViewById(R.id.from4);
        a5 = findViewById(R.id.from5);
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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("yeahhh", location.toString());
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("updated one"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));

                GeoAddress obj = new GeoAddress();
                String str = obj.getAddress(latitude, longitude, getApplicationContext());

                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
                markerpoints.add(latLng);
                Log.i("bla", latLng.toString() + "\t" + str);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location xlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng user = new LatLng(xlocation.getLatitude(), xlocation.getLongitude());
            GeoAddress obj = new GeoAddress();
            String str = obj.getAddress(user.latitude, user.longitude, getApplicationContext());

            mMap.addMarker(new MarkerOptions().position(user).title(str));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 17f));
            from.setText(str);
            Log.i("yeahh", user.toString());
            mMap.addMarker(new MarkerOptions().position(user).title("my"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 16f));
        }

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lfrom = from.getText().toString();
                lto = to.getText().toString();

                if (!lfrom.equals("") && !lto.equals("")) {

                    GeoAddress obj = new GeoAddress();
                    LatLng fcoor = obj.getCoordinates(lfrom, getApplicationContext());
                    final LatLng[] tcoor = {obj.getCoordinates(lto, getApplicationContext())};

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(fcoor).title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    mMap.addMarker(new MarkerOptions().position(tcoor[0]).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                    //Directions directions = new Directions();
                    //directions.run(mMap, lfrom, lto);

                    Places1 object = new Places1();

                    nearby nearby = new nearby(new nearby.AsyncResponse() {

                        @Override
                        public void processFinish(LatLng[] output) {
                            Log.i("qwertyy0000",String.valueOf(output.length));
                            a1.setText(output[0].toString());
                            a2.setText(output[1].toString());
                            a3.setText(output[3].toString());
                            a4.setText(output[4].toString());
                            a5.setText(output[5].toString());

                        }
                    });

                    String get1[]=new String[5];

                    get1[0] = a1.getText().toString();
                    get1[1] = a2.getText().toString();
                    get1[2] = a3.getText().toString();
                    get1[3] = a4.getText().toString();
                    get1[4] = a5.getText().toString();
                   // nearby.execute(url);
                    LatLng c[] = new LatLng[5];
                    Log.i("softy555", get1[0]+"pp");

                    for (int i = 0; i < 5; i++)
                        c[i] = latlany(get1[i]);
                    Log.i("softy00", c[0].toString());

                    Places mapsActivity1 = new Places();
                    String url1 = mapsActivity1.findbus(tcoor[0]);
                    nearby nearby1 = new nearby(new nearby.AsyncResponse() {

                        @Override
                        public void processFinish(LatLng[] output) {
                            //Log.i("qwertyy77", output[0].toString());
                            a1.setText(output[0].toString());
                            a2.setText(output[1].toString());
                            a3.setText(output[3].toString());
                            a4.setText(output[4].toString());
                            a5.setText(output[5].toString());
                        }
                    });
                    nearby1.execute(url1);
                    String to[] = new String[5];
                    to[0] = a1.getText().toString();
                    to[1] = a2.getText().toString();
                    to[2] = a3.getText().toString();
                    to[3] = a4.getText().toString();
                    to[4] = a5.getText().toString();
                    LatLng c1[] = new LatLng[5];
                    for (int i = 0; i < 5; i++)
                        c1[i] = latlany(to[i]);
                    Log.i("softy", c.toString());

                    Log.i("qwerty99999",c[2].toString());
                    //int a=s[0].length;
                    LatLng q[][] = new LatLng[25][2];
                    int x=0;

                    /*for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            q[x][0] = c[i];
                            q[x][1]=c1[j];
                            Intent intent=new Intent(MapsActivity.this,DirectionsAgain.class);
                            Bundle bundle=new Bundle();
                            bundle.putParcelable("origin",q[x][0]);
                            bundle.putParcelable("dest",q[x][1]);
                            intent.putExtra("bundle",bundle);
                            startActivityForResult(intent,1);
                            x++;
                        }
                    }
                    Log.i("qwerty", q.toString());*/

                    Intent intent=new Intent(MapsActivity.this,Loading.class);
                    intent.putExtra("from",lfrom);
                    intent.putExtra("to",lto);
                    startActivity(intent);

                }
                else {
                    //Dialog Box Alert here
                    AlertDialog alertDialog=new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("OOPS!");
                    alertDialog.setMessage("Please fill Source and Destination !");
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //yeah-yeah

                        }
                    });
                    alertDialog.show();

                }

            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode==Activity.RESULT_OK){

                int a1 = data.getIntExtra("array1", 0);
                int a2 = data.getIntExtra("array2", 0);
                int a3 = data.getIntExtra("array3", 0);
                int a4 = data.getIntExtra("array4", 0);
                params[counter][0]=a1;
                params[counter][1]=a2;
                params[counter][2]=a3;
                params[counter][3]=a4;

                Log.i("poopycount",String.valueOf(counter));
                Log.i("pooppppyyyy", String.valueOf(params[counter][0]));
                counter++;
                if (counter==25)
                {

                    Intent ic=new Intent(MapsActivity.this,MainActivity.class);
                    ic.putExtra("very",(Serializable) params);
                    ic.putExtra("from",lfrom);
                    ic.putExtra("to",lto);
                    //ic.putExtra("map", String.valueOf(mMap));
                    startActivity(ic);
                }
            }
        }
    }

    public LatLng latlany(String s) {
        Log.i("where123 ", "are");
        String s1[] = new String[2];
        Log.i("where ", "are");

        int j = 0;
        s1[0] = "";
        s1[1] = "";
        Log.i("where345 ", "are");

        double d = 0.0f, d0 = 0.0f;
        int flag = 0;
        Log.i("where678 ", String.valueOf(s.length()));

        for (int i = 1; i < s.length() - 1; i++) {
            Log.i("geting","yesss");
            if (s.charAt(i - 1) == '(')
                flag = 1;
            if (flag == 1) {
                if (s.charAt(i) == ',') {
                    j++;
                    continue;
                }

                s1[j] += s.charAt(i);
            }

        }
        Log.i("softyyy", s1[0]);
        Log.i("soft", s1[1]);
        try {
            d = Double.parseDouble(s1[0].trim());
            Log.i("laty", String.valueOf(d));
            d0 = Double.parseDouble(s1[1].trim());
            Log.i("laty", String.valueOf(d0));

        } catch (NumberFormatException e) {

        }
        return new LatLng(d, d0);


    }
}
