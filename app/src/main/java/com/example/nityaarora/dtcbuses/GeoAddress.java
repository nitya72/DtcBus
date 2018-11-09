package com.example.nityaarora.dtcbuses;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.nityaarora.dtcbuses.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GeoAddress {

    public String getAddress(Double latitude, Double longitude, Context context){
        Geocoder geocoder=new Geocoder(context);
        LatLng latLng=new LatLng(latitude,longitude);
        String str="";

        try {
            List<Address> list=geocoder.getFromLocation(latitude,longitude,1);
            String addressLine=list.get(0).getAddressLine(0);
            String city=list.get(0).getLocality()+",";
            String state = list.get(0).getAdminArea();
            String countryCode=list.get(0).getCountryCode();
            str=addressLine+","+city+","+state+","+countryCode;


        } catch (IOException e) {
            Log.i("blaaaaaaa",e.getMessage());
            e.printStackTrace();
        }
        return str;
    }

    public LatLng getCoordinates(String str,Context context)
    {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng=null;

        try {
            address = coder.getFromLocationName(str,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);

            latLng=new LatLng(location.getLatitude(),location.getLongitude());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return latLng;
    }

}
