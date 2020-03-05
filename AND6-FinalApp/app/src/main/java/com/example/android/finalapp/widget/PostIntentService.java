package com.example.android.finalapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.example.android.finalapp.models.Post;
import com.example.android.finalapp.network.GsonPostDeserializer;
import com.example.android.finalapp.network.RedditApi;
import com.example.android.finalapp.network.RedditApiFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.android.finalapp.Constants.SUBREDDIT_POPULAR;

public class PostIntentService extends IntentService {

    public static final String ACTION_GET_POST = "com.example.android.finalapp.widget.action.get_post";

    public PostIntentService() {
        super("PostIntentService");
    }

    public static void startPostIntentService(Context context) {
        Intent intent = new Intent(context, PostIntentService.class);
        intent.setAction(ACTION_GET_POST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_POST.equals(action)) {
                getPost();
            }
        }
    }

    private void getPost() {
        List<Post> posts = new ArrayList<>();

        final Type redditPostListType = new TypeToken<List<Post>>(){}.getType();
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(redditPostListType, new GsonPostDeserializer())
                .create();

        RedditApi client = RedditApiFactory.create(gson);
        Call<List<Post>> call = client.getPosts(SUBREDDIT_POPULAR);

        try {
            Response<List<Post>> response = call.execute();
            posts = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (posts == null){
            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,PostWidget.class));

        PostWidget.updateRecipe(this,appWidgetManager,appWidgetIds,posts.get(0));
    }

}