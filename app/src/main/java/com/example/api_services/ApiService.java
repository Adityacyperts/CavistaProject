package com.example.api_services;

import com.example.model.DownloadResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("gallery/search/1?q=vanilla")
    Call<DownloadResult> getAllData(@Header("Authorization") String auth);
}
