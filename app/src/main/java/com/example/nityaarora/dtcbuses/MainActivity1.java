package com.example.nityaarora.dtcbuses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity1 extends AppCompatActivity {


    public String weather(LatLng latLng)
    {
        String myurl="https://api.openaq.org/v1/measurements?coordinates="+latLng.latitude+latLng.longitude+"&limit=10";
        final String[] s = {"-"};
        final JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, myurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {
                            JSONArray ja_data = response.getJSONArray("results");
                            ArrayList<String> Ingredients_names = new ArrayList<>();
                            HashMap hm = new HashMap();
                            for(int i=0; i<ja_data.length(); i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                String k=jObj.getString("parameter");
                                Ingredients_names.add(k);
                                String j=jObj.getString("value");
                                hm.put(k,j);
                            }

                            //String results=response.getString("results");
                            //TextView textView=findViewById(R.id.text);
                                    /*textView.setText(results);
                                    JSONObject result=new JSONObject(results);
                                    String par=result.getString("parameter");*/
                            //HashMap<String,Double> h=new HashMap<>();

                            float f=0;
                            if (hm.containsKey("o3"))
                            {
                                String j=hm.get("o3").toString();
                                float j1=Float.parseFloat(j);
                                float j2=Float.valueOf(j1).floatValue();
                                j2=(float)((100 * j2)/196.32);
                                if (j2>f)
                                {
                                    f=j2;
                                    s[0] ="o3";//
                                }
                                //h.put("o3",d);
                            }
                            if (hm.containsKey("no2"))
                            {
                                String j=hm.get("no2").toString();
                                float j1=Float.parseFloat(j);
                                float j2=Float.valueOf(j1).floatValue();
                                j2=(float)((100 * j2)/225.794994);
                                if (j2>f)
                                {
                                    f=j2;
                                    s[0] ="no2";//3.4
                                }
                                //h.put("o3",d);
                            }
                            if (hm.containsKey("pm10"))
                            {
                                String j=hm.get("pm10").toString();
                                float j1=Float.parseFloat(j);
                                float j2=Float.valueOf(j1).floatValue();
                                j2=(float)((100 * j2)/50);
                                if (j2>f)
                                {
                                    f=j2;
                                    s[0] ="pm10";//256.58
                                }
                                //h.put("o3",d);
                            }
                            if (hm.containsKey("pm25"))
                            {
                                String j=hm.get("pm25").toString();
                                float j1=Float.parseFloat(j);
                                float j2=Float.valueOf(j1).floatValue();
                                j2=(float)((100 * j2)/25);
                                if (j2>f)
                                {
                                    f=j2;
                                    s[0] ="pm25";//269.52
                                }
                                //h.put("o3",d);
                            }
                            if (hm.containsKey("co"))
                            {
                                String j=hm.get("co").toString();
                                float j1=Float.parseFloat(j);
                                float j2=Float.valueOf(j1).floatValue();
                                j2=(float)((100 * j2)/10310.481);
                                if (j2>f)
                                {
                                    f=j2;
                                    s[0] ="co";//23.180
                                }
                                //h.put("o3",d);
                            }

                            if (f>=0 && f<=33)
                                s[0] ="1";
                            else if (f>=34&&f<=66)
                                s[0] ="2";
                            else if (f>=67&&f<=99)
                                s[0] ="3";
                            else if (f>=100&&f<=149)
                                s[0] ="4";
                            else if (f>=150)
                                s[0] ="5";

                        } catch (JSONException e) {
                            e.printStackTrace();
                                    /*TextView textView=findViewById(R.id.text);
                                    textView.setText(e.toString());*/
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("erroe","something something"+error);
                    }
                });
        singleton.getInstance(MainActivity1.this).addtoRequestQue(jsonObjectRequest);
        return s[0];
    }
}
