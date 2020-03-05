package com.example.android.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.models.Movie;

import java.util.ArrayList;

public class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private ArrayList<Movie> mData;
    private String mStringURL;

    public MovieLoader(@NonNull Context context, String mStringURL) {
        super(context);
        this.mStringURL = mStringURL;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        mData = DataExtraction.fetchMovies(mStringURL);
        return mData;
    }
}
