package com.example.android.bakingapp.models;

import java.io.Serializable;

public class Step implements Serializable{

    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mStringUrl;

    public Step(int mId, String mShortDescription, String mDescription, String mStringUrl) {
        this.mId = mId;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mStringUrl = mStringUrl;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmStringUrl() {
        return mStringUrl;
    }

    public void setmStringUrl(String mStringUrl) {
        this.mStringUrl = mStringUrl;
    }

    @Override
    public String toString() {
        return "Step{" +
                "mId=" + mId +
                ", mShortDescription='" + mShortDescription + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mStringUrl='" + mStringUrl + '\'' +
                '}';
    }
}
