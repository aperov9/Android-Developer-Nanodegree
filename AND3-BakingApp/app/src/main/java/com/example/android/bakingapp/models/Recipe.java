package com.example.android.bakingapp.models;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    private int mId;
    private String mName;
    private int mServings;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;


    public Recipe(int mId, String mName, int mServings, List<Ingredient> mIngredients, List<Step> mSteps) {
        this.mId = mId;
        this.mName = mName;
        this.mServings = mServings;
        this.mIngredients = mIngredients;
        this.mSteps = mSteps;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Step> getmSteps() {
        return mSteps;
    }

    public void setmSteps(List<Step> mSteps) {
        this.mSteps = mSteps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mServings='" + mServings + '\'' +
                ", mIngredients=" + mIngredients +
                ", mSteps=" + mSteps +
                '}';
    }
}
