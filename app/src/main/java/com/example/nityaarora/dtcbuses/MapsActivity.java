package com.example.nityaarora.dtcbuses;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    ArrayList markerpoints=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng=new LatLng(latitude,longitude);
                    Geocoder geocoder=new Geocoder(getApplicationContext());

                    try {
                        List<Address> list=geocoder.getFromLocation(latitude,longitude,1);
                        String str=list.get(0).getLocality()+",";
                        str+=list.get(0).getCountryCode();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
                        markerpoints.add(latLng);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
            });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(this, "map appeared", Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onMapClick(LatLng latLng) {
                if(markerpoints.size()>1)
                {
                    mMap.clear();
                    markerpoints.clear();
                }
                if(markerpoints.size()==0)
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markerpoints.add(latLng);
                //MarkerOptions options=new MarkerOptions();
                //options.position(latLng);

                //if(markerpoints.size()==1)
                    //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    //mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                if(markerpoints.size()==2)
                    //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                //mMap.addMarker(options);

                if(markerpoints.size()>=2)
                {
                    LatLng origin=(LatLng) markerpoints.get(0);
                    LatLng dest =(LatLng)markerpoints.get(1);

                    String url=getDirectionsUrl(origin,dest);

                    DownloadTask downloadTask=new DownloadTask();

                    downloadTask.execute(url);
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class DownloadTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {

            String data="";
            try {
                data = downloadUrl((String) objects[0]);
            }
            catch (Exception e)
            {
                Log.i("Background task",e.toString());
            }

            return data;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ParserTask parserTask=new ParserTask();

            parserTask.execute(o.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class ParserTask extends AsyncTask<String, String, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jobject;
            List<List<HashMap<String, String>>> routes=null;

            try{
                jobject=new JSONObject(strings[0]);
                DirectionJSONParser parser=new DirectionJSONParser();
                routes=parser.parse(jobject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;

        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points=null;
            PolylineOptions lineOptions=null;
            MarkerOptions markerOptions=new MarkerOptions();
            for(int i=0;i<lists.size();i++) {
                points=new ArrayList();
                lineOptions=new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);
                for(int j=0;j<path.size();j++)
                {
                    HashMap point=path.get(j);

                    double lat=Double.parseDouble((String) point.get("lat"));
                    double lng=Double.parseDouble((String) point.get("lng"));
                    LatLng position=new LatLng(lat,lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }
            mMap.addPolyline(lineOptions);
        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest){

        String str_origin="origin="+origin.latitude+","+origin.longitude;

        String str_dest="destination="+dest.latitude+","+dest.longitude;

        String sensor="sensor=false";
        String mode="mode=transit";
        //String dep="departure_time=now";
        String key="AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
        //String transit_mode="transit_mode=bus";

        String parameters=str_origin+"&"+str_dest+"&"+mode;

        String output="json";
        String alt="alternatives=false";

        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"?"+alt+"&key="+key+"&"+sensor;
        Log.i("bla",url);

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data="";
        InputStream iStream=null;
        HttpURLConnection urlConnection=null;
        try{
            URL url=new URL(strUrl);

            urlConnection=(HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream=urlConnection.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb=new StringBuffer();

            String line="";
            while((line=br.readLine())!=null){
                sb.append(line);
            }

            data=sb.toString();

            br.close();
        }
        catch (Exception e)
        {
            Log.i("Exception",e.toString());
        }
        finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }

}