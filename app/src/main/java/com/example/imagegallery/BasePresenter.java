package com.example.imagegallery;

import android.content.SharedPreferences;

import retrofit2.Retrofit;

public abstract class BasePresenter  <V extends BaseView >{
    protected V view;
    protected Retrofit retrofit;
    protected Retrofit retrofit1;
    protected SharedPreferences sharedPreferences;

    public void attachView(V view)
    {
        this.view = view;
    }

    public void attachApiClient(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public void attachApiClient1(Retrofit retrofit1) {
        this.retrofit1 = retrofit1;
    }

    public void attachpreferences(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }
}
