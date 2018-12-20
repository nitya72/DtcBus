package com.example.nityaarora.dtcbuses;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Places1 extends AppCompatActivity {

    public LatLng[] findbus(final LatLng latLng)
    {
        final int[] d = new int[1];
        final LatLng[][] latLng1 = new LatLng[1][1];
        StringBuilder stringBuilder= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location="+latLng.latitude+","+latLng.longitude);
        stringBuilder.append("&radius="+1500);
        stringBuilder.append("&type="+"bus_station");
        //stringBuilder.append("&keyword="+"bus_station");
        stringBuilder.append("&key="+"AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s");


        String url=stringBuilder.toString();
        Log.i("blah",url);
        //Object dataTransfer[]=new Object[2];
        //dataTransfer[0]=mMap;
        //dataTransfer[1]=url;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsarray=response.getJSONArray("results");
                            d[0] =resultsarray.length();
                            String latitude="";
                            String longitude="";
                            String name_b="";
                            Log.i("jigya","hai");
                            // float l;
                            String []l = new String[d[0]];
                            String []lg = new String[d[0]];
                            String []n=new String[d[0]];
                            latLng1[0] =new LatLng[d[0]];
                            for (int i=0;i<resultsarray.length();i++)
                            {
                                JSONObject jsonObject=resultsarray.getJSONObject(i);
                                JSONObject locationobj=jsonObject.getJSONObject("geometry").getJSONObject("location");

                                latitude=locationobj.getString("lat");
                                l[i]=latitude;

                                longitude=locationobj.getString("lng");
                                lg[i]=longitude;
                                latLng1[0][i] = new LatLng(Double.valueOf(latitude).doubleValue(), Double.valueOf(longitude).doubleValue());


                                JSONObject nameObject=resultsarray.getJSONObject(i);
                                name_b=nameObject.getString("name");
                                n[i]=name_b;
                            }
                            for(int j=0;j<resultsarray.length();j++)
                            {
                                Log.i("lat:",l[j]);
                                Log.i("lng:",lg[j]);
                                Log.i("name:",n[j]);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("locha","hai");
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("erroe","erroe");
                    }
                }
        );
        singleton.getInstance(Places1.this).addtoRequestQue(jsonObjectRequest);

        //nearby nrby=new nearby();
        //nrby.execute(dataTransfer);
        return latLng1[0];
    }


}
