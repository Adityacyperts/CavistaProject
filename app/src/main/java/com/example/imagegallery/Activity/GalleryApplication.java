package com.example.imagegallery.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Build;

import com.example.my_db.DatabaseHelper;
import com.example.utility.Utils;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import com.squareup.picasso.Picasso;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryApplication extends MultiDexApplication {
    private Retrofit retrofit;
    public static GalleryApplication context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        MultiDex.install(this);
//        try {
//            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // add for verbose logging
//        try {
//            Utils.createAllIMPDirectory();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FlowManager.init(FlowConfig.builder(getApplicationContext())
//                .addDatabaseConfig(
//                        DatabaseConfig.builder(DatabaseHelper.class)
//                                .openHelper(new DatabaseConfig.OpenHelperCreator() {
//                                    @Override
//                                    public OpenHelper createHelper(DatabaseDefinition databaseDefinition,
//                                                                   DatabaseHelperListener helperListener) {
//                                        return new DatabaseHelper(databaseDefinition, helperListener);
//                                    }
//                                })
//                                .build())
//                .build());
//        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        createRetrofitConfiguration();
        // TODO: Move this to where you establish a user session
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }


    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public static Context getAppContext() {
        return context;
    }

    private void createRetrofitConfiguration() {



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.sslSocketFactory(getSSLSocketFactory());
        httpClient.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =httpClient
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

    }
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }


    public Retrofit getRetrofit() {
        return retrofit;
    }

    public static GalleryApplication getInstance() {
        return context;
    }





}

