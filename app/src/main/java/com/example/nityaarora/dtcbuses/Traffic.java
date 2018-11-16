package com.example.nityaarora.dtcbuses;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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

public class Traffic {

    //List<List<HashMap<String, String>>> routes = null;

    public void call(List<List<HashMap<String, String>>> routes,String origin,String destination) {

        String url = getDirectionsUrl(routes,origin,destination);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(List<List<HashMap<String, String>>> routes,String origin,String destination) {

        //Log.i("bla98",routes.toString());

        String way="&waypoints=";

        for(int i=0;i<routes.size();i++) {

            List<HashMap<String, String>> path = routes.get(i);

            for(int j=0;j<path.size();j+=Math.ceil(path.size()/(20*routes.size())))
            {
                HashMap point=path.get(j);

                double lat=Double.parseDouble((String) point.get("lat"));
                double lng=Double.parseDouble((String) point.get("lng"));
                way=way+"via:"+Double.toString(lat)+","+Double.toString(lng)+"|";

                Log.i("blaa23",way);

            }
        }

        //String str_origin="origin="+origin.latitude+","+origin.longitude;
        String str_origin = "origin=" + origin;

        //String str_dest="destination="+dest.latitude+","+dest.longitude;
        String str_dest = "destination=" + destination;

        String mode = "mode=driving";
        String sensor = "sensor=false";
        String key = "AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
        String dep="departure_time=1542381651";

        String parameters = mode + "&" + dep+"&"+str_origin + "&" + str_dest+way;

        String output = "json";


        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key + "&" + sensor;
        Log.i("bla444", url);

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
        } catch (Exception e) {
            Log.i("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class DownloadTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            String data = "";
            try {
                data = downloadUrl((String) objects[0]);
            } catch (Exception e) {
                Log.i("Background task", e.toString());
            }

            return data;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(o.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class ParserTask extends AsyncTask<String, String, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jobject;
            List<List<HashMap<String, String>>> routes=null;

            /*try {
                jobject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jobject);
                Log.i("blaa2", routes.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return routes;


        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            }

        }

}