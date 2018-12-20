package com.example.nityaarora.dtcbuses;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class Qwerty extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float value[]=new float[6];
    float x[][]=new float[4][4];
    String from,to;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qwerty);
        textView=findViewById(R.id.qwerty);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        value = intent.getFloatArrayExtra("a1[]");
        from=intent.getStringExtra("from");
        to=intent.getStringExtra("to");
        Log.i("bbbbbb",String.valueOf(value[0]));

        for(int i=0;i<4;i++)
        {
            for (int j=0;j<4;j++)
            {
                if(i==j)
                {x[i][j]=1f;}
            }
        }
        x[0][1]=value[0];
        x[1][0]=(float)1/value[0];
        x[0][2]=value[1];
        x[2][0]=(float)1/value[1];
        x[0][3]=value[2];
        x[3][0]=(float)1/value[2];
        x[1][2]=value[3];
        x[2][1]=(float)1/value[3];
        x[1][3]=value[4];
        x[3][1]=(float)1/value[4];
        x[2][3]=value[5];
        x[3][2]=(float)1/value[5];
        float sum=0;
        float a[]=new float[4];
        for(int k=0;k<4;k++)
        {
            for(int h=0;h<4;h++)
            {
                sum=sum+x[h][k];
            }
            a[k]=sum;
            sum=0;

        }
        for(int k=0;k<4;k++)
        {
            for(int h=0;h<4;h++)
            {
                x[h][k]=x[h][k]/a[k];

            }


        }
        float w[]=new float[4];
        for(int i=0;i<4;i++)
        {
            w[i]=0;
        }
        for(int k=0;k<4;k++)
        {
            for(int h=0;h<4;h++)
            {
                w[k]=w[k]+x[k][h];
            }


        }




    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"appeared",Toast.LENGTH_LONG);
        mMap = googleMap;

        Intent intent=getIntent();

        from=intent.getStringExtra("from");
        to=intent.getStringExtra("to");

        GeoAddress obj = new GeoAddress();
        LatLng q = obj.getCoordinates(from, getApplicationContext());
        LatLng q1=obj.getCoordinates(to,getApplicationContext());

        mMap.addMarker(new MarkerOptions().position(q).title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(q1).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE )));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(q, 13f));

        Directions directions=new Directions();
        String url=directions.run(mMap,from,to);

        Directions.DownloadTask downloadTask=new Directions.DownloadTask(new Directions.DownloadTask.AsyncResponse() {
            @Override
            public void processFinish(Object o) {
                Directions.ParserTask parserTask=new Directions.ParserTask(new Directions.ParserTask.AsyncResponse() {
                    @Override
                    public void processFinish(Type2 type2) {
                        String str=type2.html_walk;
                        Random random=new Random();
                        int walk=random.nextInt(type2.duration_walk-150)+150;
                        str+="\nWalking Distance: "+(walk/60)+" mins";
                        str+="\n"+type2.html_transit;
                        str+="\nRoute No.: "+type2.bus;
                        str+="\nDuration in Bus: "+(type2.duration_trans/60)+" mins";
                        str+="\nNumber of Stops in between: "+type2.num_stops;
                        str+="\nTotal fare: "+type2.fare;
                        int traffic=random.nextInt(60-20)+20;
                        str+="\nDuration in Traffic: "+traffic+" mins";
                        int weather=random.nextInt(5-1)+1;
                        str+="\nWeather: "+weather;
                        textView.setText(str);

                    }
                });
                parserTask.execute(o.toString());
            }
        });
        downloadTask.execute(url);


    }
}
