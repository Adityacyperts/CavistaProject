package com.example.imagegallery.Activity.presenter;

import android.content.Context;

import com.example.ApiUtils;
import com.example.api_services.ApiService;
import com.example.imagegallery.Activity.contract.GalleryContract;
import com.example.imagegallery.Activity.model.MyData;
import com.example.model.DownloadResult;
import com.example.model.MyImage;
import com.example.model.MyResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryPresenter extends GalleryContract.Presenter {
    Context context;
    public GalleryPresenter(Context mContext) {
        this.context=mContext;

    }

    @Override
    public void getData(String key) {
        view.showProgress("Loading Please Wait...");
        ApiService getOtpAPI = ApiUtils.getRetrofitClient(context).create(ApiService.class);
        Call<DownloadResult> call = getOtpAPI.getAllData(key);
        call.enqueue(new Callback<DownloadResult>() {

            @Override
            public void onResponse(Call<DownloadResult> call, final Response<DownloadResult> response) {
                if (response.isSuccessful()) {
                DownloadResult downloadResult=response.body();
                if(downloadResult!=null)
                {
                    view.onGetJsonData(downloadResult.getUploadResults());
                    view.dismissProgress();
                }
                } else {
                }
            }

            @Override
            public void onFailure(Call<DownloadResult> call, Throwable t) {
//                    view.onUploadSaleSGoodReturnDataFailed();
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void saveData(MyResult result, MyImage image) {
        MyData.saveData(result,image);
        view.onSaveSucess();
    }

    @Override
    public void updateData(MyResult result, MyImage myImage) {
        MyData.updateData(result,myImage);
        view.onSaveSucess();
    }
}
