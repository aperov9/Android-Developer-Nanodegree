package com.example.android.bakingapp.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.models.Recipe;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>>{

    public static String sourceStringUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private List<Recipe> recipes;

    public RecipeLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground() {
        return JsonParsing.fetchRecipes(sourceStringUrl);
    }

    @Override
    protected void onStartLoading() {
        if (recipes != null) {
            deliverResult(recipes);
        } else {
            forceLoad();
        }
    }
}
