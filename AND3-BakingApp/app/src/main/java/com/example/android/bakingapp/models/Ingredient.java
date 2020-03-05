package com.example.android.bakingapp.models;

import java.io.Serializable;

public class Ingredient implements Serializable{

    private String mName;
    private double mQuantity;
    private String mMeasure;

    public Ingredient(String mName, double mQuantity, String mMeasure) {
        this.mName = mName;
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "mName='" + mName + '\'' +
                ", mQuantity=" + mQuantity +
                ", mMeasure='" + mMeasure + '\'' +
                '}';
    }
}
