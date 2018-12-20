package com.example.nityaarora.dtcbuses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.widget.EditText;

public class Traffic1 extends AppCompatActivity {
    EditText fare1,walk1,editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic1);
        editText=findViewById(R.id.poop);
        fare1=findViewById(R.id.fare);
        walk1=findViewById(R.id.walk);

        Intent intent=getIntent();
        Bundle bundle=intent.getParcelableExtra("bundle");
        LatLng origin=bundle.getParcelable("origin");
        LatLng dest=bundle.getParcelable("dest");
        List<List<HashMap<String, String>>> routes=(List<List<HashMap<String, String>>>)intent.getSerializableExtra("list");
        int fare=intent.getIntExtra("fare",0);
        int walk=intent.getIntExtra("walk",0);
        Log.i("abcde",String.valueOf(fare)+"    "+String.valueOf(walk));
        fare1.setText(String.valueOf(fare));
        walk1.setText(String.valueOf(walk));

        call(routes,origin,dest);



    }


    public void call(List<List<HashMap<String, String>>> routes, LatLng origin, LatLng destination) {

            String url[] = getDirectionsUrl(routes, origin, destination);
            Log.i("poopppppppppp",url[0]);

            DownloadTask downloadTask=new DownloadTask(new DownloadTask.AsyncResponse() {
                @Override
                public void processFinish(Object o) {
                    ParserTask parserTask=new ParserTask(new ParserTask.AsyncResponse() {
                        @Override
                        public void processFinish(int traffic) {
                            Log.i("poop111111",String.valueOf(traffic));
                            editText.setText(String.valueOf(traffic));
                        }
                    });
                    //parserTask.execute(o.toString());
                }
            });
            //downloadTask.execute(url[0]);
            Random random=new Random();
            int l=random.nextInt(400-100)+100;
            //int traffic=Integer.parseInt(editText.getText().toString());
            int traffic=l;
            //int weather=Integer.parseInt(url[1]);
            int weather=random.nextInt(5-1)+1;
            Log.i("poop1",String.valueOf(traffic));
            Log.i("poop2",String.valueOf(weather));
            int arr[]=new int[4] ;
            arr[0]=traffic;
             arr[1]=weather;
             int fare=Integer.parseInt(fare1.getText().toString());
             int walk=Integer.parseInt(walk1.getText().toString());
             arr[2]=fare;
             arr[3]=walk;
             Log.i("poop89",arr.toString());

            Intent goBack=new Intent();
            Log.i("poop90","qwertyyyyyy");
            goBack.putExtra("array",arr);
            setResult(RESULT_OK,goBack);
            finish();
        }


        private String[] getDirectionsUrl(List<List<HashMap<String, String>>> routes,LatLng origin,LatLng destination) {

            //Log.i("bla98",routes.toString());

            String way="&waypoints=";

            String s[]=new String[2];

            Log.i("qwerty",String.valueOf(routes.size()));

            for(int i=0;i<routes.size();i++) {

                List<HashMap<String, String>> path = routes.get(i);
                Log.i("qwerty12",String.valueOf(path.size()));
                int v=(int)Math.ceil(path.size()/(20*routes.size()));
                if((int)Math.ceil(path.size()/(20*routes.size()))==0)
                {
                    v=(int)Math.ceil(path.size()/(20*routes.size()))+1;
                }
                Log.i("vaaaa",String.valueOf(Math.ceil(path.size()/(20*routes.size()))));

                for(int j=0;j<path.size();j+=v)
                {
                    HashMap point=path.get(j);
                    double lat=Double.parseDouble((String) point.get("lat"));
                    double lng=Double.parseDouble((String) point.get("lng"));
                    /*if (i==0)
                    {
                        int x=path.size()/2;
                        if (j>=x&&j<=x+Math.ceil(path.size()/(20*routes.size())))
                        {
                            LatLng latLng=new LatLng(lat,lng);
                            MainActivity1 obj=new MainActivity1();
                            s[1]=obj.weather(latLng);
                        }

                    }*/
                    way=way+"via:"+Double.toString(lat)+","+Double.toString(lng)+"|";

                    Log.i("blaa23",way);

                }
            }

            String str_origin="origin="+origin.latitude+","+origin.longitude;
            //String str_origin = "origin=" + origin;

            String str_dest="destination="+destination.latitude+","+destination.longitude;
            //String str_dest = "destination=" + destination;

            String mode = "mode=driving";
            String sensor = "sensor=false";
            String key = "AIzaSyAnbNZrnr0cfOB0ba15vcIjCxfn8-3Dt3s";
            String dep="departure_time=1543588581";

            String parameters = mode + "&" + dep+"&"+str_origin + "&" + str_dest+way;

            String output = "json";


            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key + "&" + sensor;
            s[0]=url;
            Log.i("bla444", url);

            return s;
        }

        private static String downloadUrl(String strUrl) throws IOException {
            String data0 = "";
            InputStream iStream0=null ;
            HttpURLConnection urlConnection0 = null;
            try {
                Log.i("Exception1xx","tryyyy");
                URL url = new URL(strUrl);
                Log.i("abcdefg","yo1");


                urlConnection0 = (HttpURLConnection) url.openConnection();
                Log.i("abcdefgh",urlConnection0.toString());

                urlConnection0.setRequestMethod("GET");;
                urlConnection0.setReadTimeout(10000);
                urlConnection0.setDoInput(true);
                urlConnection0.connect();


                Log.i("abcdefghiiii","yo3");

                iStream0 = urlConnection0.getInputStream();
                Log.i("abcdef","qwerty");


                BufferedReader br = new BufferedReader(new InputStreamReader(iStream0));
                Log.i("abcde",br.toString());

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    Log.i("abcd",line);
                }

                data0 = sb.toString();

                br.close();
            } catch (Exception e) {
                Log.i("Exception123", e.toString());
            } finally {
                Log.i("Exception12","qwerty");
                iStream0.close();
                urlConnection0.disconnect();
            }
            Log.i("Exception12345",data0);
            return data0;

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
                } catch (Exception e) {
                    Log.i("Background task", e.toString());
                }

                return data;

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i("poop1234",o.toString());

                delegate.processFinish(o);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        static class ParserTask extends AsyncTask<String, String, Integer> {

            public interface AsyncResponse{
                void processFinish(int traffic);
            }

            public ParserTask.AsyncResponse delegate=null;

            public ParserTask(AsyncResponse delegate){
                this.delegate=delegate;
            }

            @Override
            protected Integer doInBackground(String... strings) {

                JSONObject jobject;
                int traffic=0;
                List<List<HashMap<String, String>>> routes=null;

                try {
                    jobject = new JSONObject(strings[0]);
                    DirectionJSONTraffic parser = new DirectionJSONTraffic();
                    traffic= parser.parse(jobject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return traffic;

            }

            @Override
            protected void onPostExecute(Integer traffic) {
                Log.i("poop12345",String.valueOf(traffic));


                delegate.processFinish(traffic);
            }

        }


}


