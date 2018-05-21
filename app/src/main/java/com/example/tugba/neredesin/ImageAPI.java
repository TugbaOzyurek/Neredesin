package com.example.tugba.neredesin;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface ImageAPI {

    @POST("/api/find")
    public void find(@Body String aranan, Callback<String> callback);

    @POST("/api/img")
    public void imgSend(@Body String img, Callback<String> callback);

}
