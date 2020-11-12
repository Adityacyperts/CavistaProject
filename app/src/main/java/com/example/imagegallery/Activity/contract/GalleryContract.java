package com.example.imagegallery.Activity.contract;

import com.example.imagegallery.BasePresenter;
import com.example.imagegallery.BaseView;
import com.example.model.MyImage;
import com.example.model.MyResult;

import java.util.ArrayList;

public class GalleryContract {
    public interface View extends BaseView {
        void onGetJsonData(ArrayList<MyResult> uploadResults);

        void showProgress(String s);

        void dismissProgress();

        void onSaveSucess();
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void getData(String key);

        public abstract void saveData(MyResult result, MyImage image);

        public abstract void updateData(MyResult result, MyImage myImage);
    }


}
