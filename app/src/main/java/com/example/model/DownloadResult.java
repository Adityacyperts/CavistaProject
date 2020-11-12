package com.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DownloadResult implements Serializable {
    @SerializedName("data")
    @Expose
    public ArrayList<MyResult> uploadResults = null;

    @SerializedName("Errmsg")
    @Expose
    public String success;

    public String status;




    public ArrayList<MyResult> getUploadResults() {
        return uploadResults;
    }

    public void setUploadResults(ArrayList<MyResult> uploadResults) {
        this.uploadResults = uploadResults;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
