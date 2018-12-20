package com.example.nityaarora.dtcbuses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Loading extends AppCompatActivity {

    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Intent intent = getIntent();
        final String from = intent.getStringExtra("from");
        final String to = intent.getStringExtra("to");

        final Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDoalog.incrementProgressBy(1);
            }
        };

         progressDoalog = new ProgressDialog(Loading.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDoalog.getProgress() <= progressDoalog
                            .getMax()) {
                        Thread.sleep(40);
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDoalog.getProgress() == progressDoalog
                                .getMax()) {
                            progressDoalog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        handle.sendMessage(handle.obtainMessage());
        run(from,to);


    }
    public void run(final String from, final String to){
        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent openStartingPoint = new Intent(Loading.this,MainActivity.class);
                    openStartingPoint.putExtra("from",from);
                    openStartingPoint.putExtra("to",to);
                    startActivity(openStartingPoint);
                }
            }


        };
        timer.start();
    }
}

