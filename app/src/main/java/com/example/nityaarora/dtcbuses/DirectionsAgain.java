package com.example.nityaarora.dtcbuses;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class DirectionsAgain extends AppCompatActivity {

    static LatLng origin;
    static LatLng destination;
    int fare, walk;
    EditText get1, get2, get3, get4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_again);
        Log.i("asusss", "qwertyyyy");

        get1 = findViewById(R.id.fare);
        get2 = findViewById(R.id.walk);
        get3 = findViewById(R.id.weather);
        get4 = findViewById(R.id.traffic);

        Intent intent = getIntent();
        Bundle bundle = intent.getParcelableExtra("bundle");
        LatLng from = bundle.getParcelable("origin");
        LatLng to = bundle.getParcelable("dest");

        run(from, to);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
        Log.i("poop00","lllllll");

        int arr[] = data.getIntArrayExtra("array");
        Log.i("poop123456",arr.toString());
        Intent goBack = new Intent();
        goBack.putExtra("array1", arr[0]);
        goBack.putExtra("array2",arr[1]);
        goBack.putExtra("array3",arr[2]);
        goBack.putExtra("array4",arr[3]);
        setResult(Activity.RESULT_OK,goBack);

        finish();
    }}

    public void run(final LatLng origin, final LatLng destination) {
        this.origin = origin;
        this.destination = destination;

        String url = getDirectionsUrl(origin, destination);
        Log.i("blapp", origin + "   " + destination);

        DownloadTask downloadTask = new DownloadTask(new DownloadTask.AsyncResponse() {
            @Override
            public void processFinish(Object o) {
                ParserTask parserTask = new ParserTask(new ParserTask.AsyncResponse() {
                    @Override
                    public void processFinish(DataType dataType) {
                        Log.i("poop1", dataType.toString());

                        Intent intent = new Intent(DirectionsAgain.this, Traffic1.class);
                        intent.putExtra("list", (Serializable) dataType.routes);
                        intent.putExtra("fare", dataType.fare);
                        intent.putExtra("walk", dataType.walk);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("origin", origin);
                        bundle.putParcelable("dest", destination);
                        intent.putExtra("bundle", bundle);
                        startActivityForResult(intent,0);

                        /*Traffic1 obj = new Traffic1();
                        int arr1[] = obj.call(dataType.routes, origin, destination);
                        Log.i("pooppppppp",arr1.toString());*/
                        //int traffic=arr1[0];
                        //int weather=arr1[1];
                        //get3.setText(String.valueOf(weather));
                        //get4.setText(String.valueOf(traffic));

                    }
                });
                parserTask.execute(o.toString());
            }
        });
        downloadTask.execute(url);

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

            String data = "";
            try {
                data = downloadUrl((String) objects[0]);
                Log.i("poop",data.toString());
            } catch (Exception e) {
                Log.i("Background task", e.toString());
            }

            return data;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            //ParserTask parserTask = new ParserTask();

            //parserTask.execute(o.toString());
            delegate.processFinish(o);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    static class ParserTask extends AsyncTask<String, String, DataType> {

        public interface AsyncResponse{
            void processFinish(DataType dataType);
        }

        public ParserTask.AsyncResponse delegate=null;

        public ParserTask(AsyncResponse delegate){
            this.delegate=delegate;
        }


        @Override
        protected DataType doInBackground(String... strings) {

            JSONObject jobject;
            DataType dataType=null;

            try {
                jobject = new JSONObject(strings[0]);
                DirectionsJSONParserAgain parser = new DirectionsJSONParserAgain();
                dataType=parser.parse(jobject);
                Log.i("pooppp",dataType.toString());
                //Log.i("blaa2", routes.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dataType;

        }

        @Override
        protected void onPostExecute(DataType lists) {

            delegate.processFinish(lists);


        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //String str_origin = "origin=" + origin;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //String str_dest = "destination=" + dest;

        String mode = "mode=transit";
        String sensor = "sensor=false";
        String dep = "departure_time=1543588581";
        String key = "AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
        String transit_mode = "transit_mode=bus";
        String alt = "alternatives=false";

        String parameters = mode + "&" + transit_mode + "&" + dep + "&" + alt + "&" + str_origin + "&" + str_dest;

        String output = "json";


        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key + "&" + sensor;
        Log.i("bla", url);

        return url;
    }

    private static String downloadUrl(String strUrl) throws IOException {
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
}
