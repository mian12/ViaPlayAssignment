package com.example.best.viaplayassignment.Commmon;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.best.viaplayassignment.remote.IViaPlayApi;
import com.example.best.viaplayassignment.remote.RetrofitClient;


public class Common {



    // main flicker url fo geting data
    public static final String BASE_URL = "https://content.viaplay.se/";

    ///calling retrofit class and make a call according to base url
    public static IViaPlayApi getSections() {

        return RetrofitClient.getRetrofit(BASE_URL).create(IViaPlayApi.class);
    }


//    public static Boolean isConnectedToInternet(Context context) {
//        // checking inernet connection available or not
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//
//
//        return false;
//    }


}
