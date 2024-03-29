package com.example.nityaarora.dtcbuses;

import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionJSONParser {
    public Type2 parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        int value=0,value1=0,num_stops=0;
        String short_name1="";
        String html="",html1="";
        String fare="";
        int c=0;int c1=0;
        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
                fare=(String) ((JSONObject)((JSONObject)jRoutes.get(i)).get("fare")).get("text");

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){

                        String polyline = "",travel_mode="";

                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");

                        travel_mode = (String)((JSONObject)jSteps.get(k)).get("travel_mode");

                        List list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l <list.size();l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                        if(travel_mode.equals("WALKING")){
                            if(c==0) {
                                html = (String) ((JSONObject) jSteps.get(k)).get("html_instructions");
                                value = (int) ((JSONObject) ((JSONObject) jSteps.get(k)).get("duration")).get("value");
                                c++;
                            }
                            }
                        else if(travel_mode.equals("TRANSIT")){
                            if(c1==0){
                                num_stops=(int)((JSONObject)((JSONObject)jSteps.get(k)).get("transit_details")).get("num_stops");
                                html1=(String)((JSONObject)jSteps.get(k)).get("html_instructions");
                                value1 = (int)((JSONObject)((JSONObject)jSteps.get(k)).get("duration")).get("value");
                                JSONObject line= (JSONObject) ((JSONObject)((JSONObject)jSteps.get(k)).get("transit_details")).get("line");
                                short_name1 =(String) ((JSONObject)line).get("short_name");
                                }
                            }
                        }


                    }
                    routes.add(path);
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {

        }
        Log.i("asdf",html+"   "+html1+"  "+String.valueOf(value)+"  "+String.valueOf(value1)+"\n"+String.valueOf(num_stops)+" "+short_name1);
        Type2 type2=new Type2(routes,html,value,num_stops,html1,value1,short_name1,fare);

        return type2;
    }

    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
