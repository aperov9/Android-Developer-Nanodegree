package com.example.android.finalapp.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.example.android.finalapp.models.Post;
import com.example.android.finalapp.network.GsonPostDeserializer;
import com.example.android.finalapp.network.RedditApi;
import com.example.android.finalapp.network.RedditApiFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class PostsLoader extends AsyncTaskLoader<List<Post>> {

    private List<Post> posts;
    private String mReddit;

    public PostsLoader(@NonNull Context context, String mReddit) {
        super(context);
        this.mReddit = mReddit;
    }

    @Nullable
    @Override
    public List<Post> loadInBackground() {
        if(mReddit.isEmpty()){
            return null;
        }
        final Type redditPostListType = new TypeToken<List<Post>>(){}.getType();
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(redditPostListType, new GsonPostDeserializer())
                .create();

        RedditApi client = RedditApiFactory.create(gson);
        Call<List<Post>> call = client.getPosts(mReddit);

        try {
            Response<List<Post>> response = call.execute();
            posts = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;

    }

    @Override
    protected void onStartLoading() {
        if (posts != null) {
            // Use cached data
            deliverResult(posts);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
}
