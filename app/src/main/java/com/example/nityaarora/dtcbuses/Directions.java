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

    static GoogleMap mMap;
    String origin,destination;

    public String run(GoogleMap mMap,String origin,String destination){
        this.mMap=mMap;
        this.origin=origin;
        this.destination=destination;

        String url=getDirectionsUrl(origin,destination);
        Log.i("blapp",origin+"   "+destination);

        //DownloadTask downloadTask=new DownloadTask();
        //downloadTask.execute(url);
        return url;


    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
     static class DownloadTask extends AsyncTask {

        public interface AsyncResponse{
            void processFinish(Object o);
        }

        public DownloadTask.AsyncResponse delegate=null;

        public DownloadTask(AsyncResponse delegate){
            this.delegate=delegate;
        }

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

            //ParserTask parserTask=new ParserTask();

            //parserTask.execute(o.toString());
            delegate.processFinish(o);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
     static class ParserTask extends AsyncTask<String, String, Type2> {

        public interface AsyncResponse{
            void processFinish(Type2 type2);
        }

        public ParserTask.AsyncResponse delegate=null;

        public ParserTask(AsyncResponse delegate){
            this.delegate=delegate;
        }

        @Override
        protected Type2 doInBackground(String... strings) {

            JSONObject jobject;
            List<List<HashMap<String, String>>> routes=null;
            Type2 type2=null;

            try{
                jobject=new JSONObject(strings[0]);
                DirectionJSONParser parser=new DirectionJSONParser();
                type2=parser.parse(jobject);
                //Log.i("blaa2",routes.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return type2;

        }

        @Override
        protected void onPostExecute(Type2 type2) {
            ArrayList points=null;
            PolylineOptions lineOptions=null;
            List<List<HashMap<String, String>>> lists=type2.routes;

            Log.i("bla99",lists.toString());

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
            delegate.processFinish(type2);
        }

    }

    private String getDirectionsUrl(String origin,String dest){

        //String str_origin="origin="+origin.latitude+","+origin.longitude;
        String str_origin="origin="+origin;

        //String str_dest="destination="+dest.latitude+","+dest.longitude;
        String str_dest="destination="+dest;

        String mode="mode=transit";
        String sensor="sensor=false";
        String dep="departure_time=1542799483";
        String key="AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
        String transit_mode="transit_mode=bus";
        String alt="alternatives=false";

        String parameters=mode+"&"+transit_mode+"&"+dep+"&"+alt+"&"+str_origin+"&"+str_dest;

        String output="json";


        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+key+"&"+sensor;
        Log.i("bla",url);

        return url;
    }

    private static String downloadUrl(String strUrl) throws IOException {
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
