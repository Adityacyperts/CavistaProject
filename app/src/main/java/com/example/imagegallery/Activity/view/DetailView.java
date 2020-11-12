package com.example.imagegallery.Activity.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.imagegallery.Activity.contract.GalleryContract;
import com.example.imagegallery.Activity.model.MyData;
import com.example.imagegallery.Activity.presenter.GalleryPresenter;
import com.example.imagegallery.Activity.view.adapter.GalleryDataAdapter;
import com.example.imagegallery.R;
import com.example.model.MyResult;
import com.example.utility.PiccasoTrustAll;
import com.example.utility.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity implements GalleryContract.View, View.OnClickListener {
    Button btnSave;
    EditText edtRemark;
    ImageView img_view;
    ProgressBar pbLoading;
    Context context;
    GalleryContract.Presenter mpresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        initiliseView();
        initilisePresenter();

    }

    private void initilisePresenter() {
        mpresenter = new GalleryPresenter(context);
        mpresenter.attachView(this);

    }

    private void initiliseView() {
        context = this;
        btnSave = findViewById(R.id.btnNext);
        edtRemark = findViewById(R.id.edt_remark);
        img_view = findViewById(R.id.img_view);
        pbLoading = findViewById(R.id.pb_loading_bar);
        btnSave.setOnClickListener(this);
        setData();
    }

    MyData myData;

    private void setData() {
        myData = MyData.getData(GalleryDataAdapter.result, GalleryDataAdapter.myImage);
        if (myData != null && !Utils.checkString(myData.getDescription())) {
            edtRemark.setText(myData.getDescription());
            btnSave.setText("Update");
        }
        if (GalleryDataAdapter.result != null) {
            if (Utils.isNetworkConnected(context) && !Utils.checkString(GalleryDataAdapter.result.getLink())) {
                pbLoading.setVisibility(View.VISIBLE);
                PiccasoTrustAll.getInstance(context).
                        load(Utils.getUrl(GalleryDataAdapter.result.getLink())).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(img_view, new Callback() {
                            @Override
                            public void onSuccess() {
                                pbLoading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                pbLoading.setVisibility(View.GONE);
                                img_view.setImageResource(R.mipmap.image_not_found);

                            }
                        });
            } else {
                pbLoading.setVisibility(View.GONE);
            }
        }
        else if(GalleryDataAdapter.myImage!=null)
        {
            if (Utils.isNetworkConnected(context) && !Utils.checkString(GalleryDataAdapter.myImage.getLink())) {
                pbLoading.setVisibility(View.VISIBLE);
                PiccasoTrustAll.getInstance(context).
                        load(Utils.getUrl(GalleryDataAdapter.myImage.getLink())).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(img_view, new Callback() {
                            @Override
                            public void onSuccess() {
                                pbLoading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                pbLoading.setVisibility(View.GONE);
                                img_view.setImageResource(R.mipmap.image_not_found);

                            }
                        });
            } else {
                pbLoading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onGetJsonData(ArrayList<MyResult> uploadResults) {

    }

    @Override
    public void showProgress(String s) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void onSaveSucess() {
        try {
            GalleryDataAdapter.result = null;
            GalleryDataAdapter.myImage = null;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext: {
                if (myData == null && validation() ) {
                    dataSet();
                    mpresenter.saveData(GalleryDataAdapter.result, GalleryDataAdapter.myImage);
                } else if (validation()) {
                    dataSet();
                    mpresenter.updateData(GalleryDataAdapter.result, GalleryDataAdapter.myImage);
                }

                break;
            }
        }

    }

    private void dataSet() {
        if (!Utils.checkString(edtRemark.getText().toString())) {
            if (GalleryDataAdapter.result != null)
                GalleryDataAdapter.result.setDescription(edtRemark.getText().toString());
            if (GalleryDataAdapter.myImage != null)
                GalleryDataAdapter.myImage.setDescription(edtRemark.getText().toString());


        }

    }

    private boolean validation() {
        if (Utils.checkString(edtRemark.getText().toString())) {
            Toast.makeText(context, "Remark can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            GalleryDataAdapter.result = null;
            GalleryDataAdapter.myImage = null;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}