package com.example.nityaarora.dtcbuses;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String from,to;
    Spinner spin1,spin2,spin3,spin4,spin5,spin6;

    Button bt;
    float a1[]=new float[6];
    float x[][];
    int index[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index=new int[6];

        bt=findViewById(R.id.button);
        spin1=findViewById(R.id.spin1);
        spin2=findViewById(R.id.spin2);
        spin3=findViewById(R.id.spin3);
        spin4=findViewById(R.id.spin4);
        spin5=findViewById(R.id.spin5);
        spin6=findViewById(R.id.spin6);

        Intent intent=getIntent();
        from=intent.getStringExtra("from");
        to=intent.getStringExtra("to");

        ArrayList arrayList=new ArrayList<String>();
        arrayList.add("Equal");
        arrayList.add("Moderate");
        arrayList.add("Strong");
        arrayList.add("Very Strong");
        arrayList.add("Extreme");
        Log.i("gg:","bhghghgghghghghg");

        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
        spin1.setAdapter(arrayAdapter);
        spin2.setAdapter(arrayAdapter);
        spin3.setAdapter(arrayAdapter);
        spin4.setAdapter(arrayAdapter);
        spin5.setAdapter(arrayAdapter);
        spin6.setAdapter(arrayAdapter);

        android.app.AlertDialog.Builder alertDialog=new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage("Wanna prioritize your ride ?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                set();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //yeah-yeah
                for (int k=0;k<6;k++)
                    a1[k]=1.0f;
                Log.i("index1",from+to);
                Intent intent = new Intent(MainActivity.this, Qwerty.class);
                intent.putExtra("a1[]",a1);
                intent.putExtra("from",from);
                intent.putExtra("to",to);

                startActivity(intent);


            }
        });
        alertDialog.show();

    }

    public void set(){
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[0]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[1]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[2]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[3]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[4]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index[5]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void yo(View view) {
        Log.i("index1","yo");


        Log.i("index11",String.valueOf(index[0])+String.valueOf(index[1])+String.valueOf(index[2])+String.valueOf(index[3])+String.valueOf(index[4])+String.valueOf(index[5]));



        if (index[0]==0)
        {
            a1[0]=1f;
        }
        else if (index[0]==1) {
            a1[0] =3f;
        }
        else if (index[0]==2)
        {a1[0]= 5f;}
        else if (index[0]==3)
        {a1[0]=7f;}
        else if (index[0]==4){

            a1[0]=9f;

        }
        if (index[1]==0)
        {
            a1[1]=1f;
        }
        else if (index[1]==1) {
            a1[1] =3f;
        }
        else if (index[1]==2)
        {a1[1]= 5f;}
        else if (index[1]==3)
        {a1[1]=7f;}
        else if (index[1]==4){

            a1[1]=9f;

        }
        if (index[2]==0)
        {
            a1[2]=1f;
        }
        else if (index[2]==1) {
            a1[2] =3f;
        }
        else if (index[2]==2)
        {a1[2]= 5f;}
        else if (index[2]==3)
        {a1[2]=7f;}
        else if (index[2]==4){

            a1[2]=9f;

        }
        if (index[3]==0)
        {
            a1[3]=1f;
        }
        else if (index[3]==1) {
            a1[3] =3f;
        }
        else if (index[3]==2)
        {a1[3]= 5f;}
        else if (index[3]==3)
        {a1[3]=7f;}
        else if (index[3]==4){

            a1[3]=9f;

        }
        if (index[4]==0)
        {
            a1[4]=1f;
        }
        else if (index[4]==1) {
            a1[4] =3f;
        }
        else if (index[4]==2)
        {a1[4]= 5f;}
        else if (index[4]==3)
        {a1[4]=7f;}
        else if (index[4]==4){

            a1[4]=9f;

        }
        if (index[5]==0)
        {
            a1[5]=1f;
        }
        else if (index[5]==1) {
            a1[5] =3f;
        }
        else if (index[5]==2)
        {a1[5]= 5f;}
        else if (index[5]==3)
        {a1[5]=7f;}
        else if (index[5]==4){

            a1[5]=9f;

        }


        //x[0][1]=a1;
        Intent intent = new Intent(MainActivity.this, Qwerty.class);
        intent.putExtra("a1[]",a1);
        intent.putExtra("from",from);
        intent.putExtra("to",to);

        startActivity(intent);

    }
}