package com.example;

import android.content.Context;

import com.example.imagegallery.Activity.GalleryApplication;

import retrofit2.Retrofit;

public class ApiUtils {
    public static GalleryApplication context;
    private Retrofit retrofit;
    public static Retrofit getRetrofitClient(Context context) {
        return ( (GalleryApplication)context.getApplicationContext()).getRetrofit();
    }

    public static GalleryApplication getContext() {
        return context;
    }

    public static void setContext(GalleryApplication context) {
        ApiUtils.context = context;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public static GalleryApplication getInstance() {
        return context;
    }
}
