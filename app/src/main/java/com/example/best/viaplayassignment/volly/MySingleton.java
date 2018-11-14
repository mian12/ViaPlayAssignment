package com.example.best.viaplayassignment.volly;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MySingleton extends Application {

    // Singeltion class for volley networking liberary
    //create instance only one time and reuse it again this object without creating new
    //one everytime

    private RequestQueue mRequestQueue;
    private static MySingleton mInstance;

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        getReqQueue();
    }


    public static synchronized MySingleton getInstance() {

        if (mInstance == null) {

            mInstance = new MySingleton();
        }
        return mInstance;


    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
