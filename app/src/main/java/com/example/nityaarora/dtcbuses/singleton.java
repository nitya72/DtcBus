package com.example.nityaarora.dtcbuses;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class singleton {
    private static singleton minstance;
    private RequestQueue requestQueue;
    private static Context mctx;
    private singleton(Context context)
    {
        mctx=context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue==null)
        {
            requestQueue=Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized singleton getInstance(Context context){
        if (minstance==null)
        {
            minstance=new singleton(context);
        }
        return minstance;
    }
    public void addtoRequestQue(Request request)
    {
        requestQueue.add(request);
    }

}
