package com.example.nityaarora.dtcbuses;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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

public class Directions {

    GoogleMap mMap;
    String origin,destination;

    public void run(GoogleMap mMap,String origin,String destination){
        this.mMap=mMap;
        this.origin=origin;
        this.destination=destination;

        String url=getDirectionsUrl(origin,destination);
        Log.i("blapp",origin+"   "+destination);

        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute(url);

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class DownloadTask extends AsyncTask {

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
                Log.i("blaa2",routes.toString());

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

            Log.i("bla99",lists.toString());

            Traffic obj=new Traffic();
            obj.call(lists,origin,destination);

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

    private String getDirectionsUrl(String origin,String dest){

        //String str_origin="origin="+origin.latitude+","+origin.longitude;
        String str_origin="origin="+origin;

        //String str_dest="destination="+dest.latitude+","+dest.longitude;
        String str_dest="destination="+dest;

        String mode="mode=transit";
        String sensor="sensor=false";
        String dep="departure_time=1542381651";
        String key="AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
        String transit_mode="transit_mode=bus";
        String alt="alternatives=false";

        String parameters=mode+"&"+transit_mode+"&"+dep+"&"+alt+"&"+str_origin+"&"+str_dest;

        String output="json";


        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+key+"&"+sensor;
        Log.i("bla",url);

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
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
