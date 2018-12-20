package com.example.nityaarora.dtcbuses;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionJSONTraffic {

        public int parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            int traffic=0;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();
                    List path_transit = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        //jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("");
                        traffic=(int)((JSONObject)((JSONObject)jLegs.get(j)).get("duration_in_traffic")).get("value");



                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("pleaseee5",String.valueOf(traffic));

            return traffic;

        }

}
