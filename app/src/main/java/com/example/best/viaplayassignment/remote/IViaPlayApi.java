package com.example.best.viaplayassignment.remote;


import com.example.best.viaplayassignment.model.ViaPlayResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IViaPlayApi {


    @GET("androiddash-se")
    Call<ViaPlayResponse> getViaPlayResponse();
}
