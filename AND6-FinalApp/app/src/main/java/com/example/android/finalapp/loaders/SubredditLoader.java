package com.example.android.finalapp.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.android.finalapp.models.Subreddit;
import com.example.android.finalapp.network.GsonSubredditDeserializer;
import com.example.android.finalapp.network.RedditApi;
import com.example.android.finalapp.network.RedditApiFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class SubredditLoader extends android.support.v4.content.AsyncTaskLoader<Subreddit> {


    private Subreddit subreddit;
    private String subredditString;

    public SubredditLoader(@NonNull Context context, String query) {
        super(context);
        this.subredditString = query;
    }

    @Nullable
    @Override
    public Subreddit loadInBackground() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Subreddit.class, new GsonSubredditDeserializer(subredditString))
                .create();

        RedditApi client = RedditApiFactory.create(gson);
        Call<Subreddit> call = client.getSubreddit(subredditString);

        try {
            Response<Subreddit> response = call.execute();
            subreddit = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subreddit;

    }

    @Override
    protected void onStartLoading() {
        if (subreddit != null) {
            // Use cached data
            deliverResult(subreddit);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
}
