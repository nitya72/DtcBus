package com.example.nityaarora.dtcbuses;

import android.icu.util.TimeUnit;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class nearby extends AsyncTask<Object, String, String> {

    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;

    public interface AsyncResponse{
        void processFinish(LatLng[] output);
    }

    public AsyncResponse delegate=null;

    public nearby(AsyncResponse delegate){
        this.delegate=delegate;
    }


    @Override
    protected String doInBackground(Object... objects) {
        url = (String) objects[0];

        try {
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) myurl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("data-url", data);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsarray = parentObject.getJSONArray("results");
            int d = resultsarray.length();
            double latitude = 0.0f;
            double longitude = 0.0f;
            String name_b = "";
            // float l;
            LatLng latLng[] = new LatLng[d];
            String[] n = new String[d];
            Log.i("stringj", "stringj");
            for (int i = 0; i < resultsarray.length(); i++) {
                JSONObject jsonObject = resultsarray.getJSONObject(i);
                JSONObject locationobj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                latitude = Double.parseDouble(locationobj.getString("lat"));

                longitude = Double.parseDouble(locationobj.getString("lng"));
                latLng[i] = new LatLng(Double.valueOf(latitude).doubleValue(), Double.valueOf(longitude).doubleValue());
                Log.i("qwerty", latLng[i].toString());

                JSONObject nameObject = resultsarray.getJSONObject(i);
                name_b = nameObject.getString("name");
                n[i] = name_b;


            }

            delegate.processFinish(latLng);

            //b=new LatLng[d];

            Log.i("here1", "here1");
            //Log.i("blahhh", this.b[0].toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}


