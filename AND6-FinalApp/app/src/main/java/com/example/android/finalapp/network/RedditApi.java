package com.example.android.finalapp.network;

import com.example.android.finalapp.models.Post;
import com.example.android.finalapp.models.Subreddit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditApi {

    //https://www.reddit.com/r/cats.json
    @GET("/r/{query}.json")
    Call<List<Post>> getPosts(@Path("query") String query);

    //https://www.reddit.com/r/cats/about.json
    @GET("/r/{query}/about.json")
    Call<Subreddit> getSubreddit(@Path("query") String query);
}
