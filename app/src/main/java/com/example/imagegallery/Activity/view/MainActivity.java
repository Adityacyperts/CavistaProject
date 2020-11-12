package com.example.imagegallery.Activity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ApiUtils;
import com.example.imagegallery.Activity.contract.GalleryContract;
import com.example.imagegallery.Activity.presenter.GalleryPresenter;
import com.example.imagegallery.Activity.view.adapter.GalleryDataAdapter;
import com.example.imagegallery.R;
import com.example.model.MyImage;
import com.example.model.MyResult;
import com.example.utility.Utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity implements GalleryContract.View {

    GalleryContract.Presenter mpresenter;
    RecyclerView rv_mainList;
    RecyclerView.LayoutManager layoutManager;
    GalleryDataAdapter mAdapter;
    List<MyResult>mArrayList;
    Context mContext;
    private ProgressDialog progressDialog;
    Retrofit retrofit;
    EditText edSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        createRetrofitConfiguration();
        GalleryDataAdapter.myImage=null;
        GalleryDataAdapter.result=null;
        initializeView();
        initializePresenter();


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
    private void initializeView()
    {
      rv_mainList=findViewById(R.id.rv_main_list);
      edSearch=findViewById(R.id.ed_search);
      layoutManager=new LinearLayoutManager(mContext);
      rv_mainList.setLayoutManager(layoutManager);
      rv_mainList.setHasFixedSize(true);
      edSearch.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              try
              {
                  mAdapter.getFilter().filter(s.toString());
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
          }

          @Override
          public void afterTextChanged(Editable s) {

          }
      });
    }
    private void initializePresenter() {
        mpresenter = new GalleryPresenter(mContext);
        mpresenter.attachView(this);
        mArrayList=new ArrayList<>();
        if(Utils.isNetworkConnected(mContext))
        mpresenter.getData("Client-ID 137cda6b5008a7c");
        else
            Toast.makeText(mContext, "Internet connection not available", Toast.LENGTH_SHORT).show();

        mpresenter.attachApiClient(ApiUtils.getRetrofitClient(mContext));
    }

    @Override
    public void onGetJsonData(ArrayList<MyResult> uploadResults) {
       if(uploadResults!=null&&uploadResults.size()>0)
           setAdapterData(uploadResults);

    }

    private void setAdapterData(ArrayList<MyResult> uploadResults) {
        this.mArrayList=uploadResults;
        try {
            mAdapter=new GalleryDataAdapter(mArrayList,mContext);
            rv_mainList.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProgress(String s) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(s);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dismissProgress() {
        try {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onSaveSucess() {

    }
}