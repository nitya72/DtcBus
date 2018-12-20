package com.example.nityaarora.dtcbuses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent openStartingPoint = new Intent(Splash.this,MapsActivity.class);
                    startActivity(openStartingPoint);
                }
            }


        };
        timer.start();
    }
}
