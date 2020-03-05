package com.example.android.finalapp.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.example.android.finalapp.models.Post;
import com.example.android.finalapp.models.Subreddit;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonSubredditDeserializer implements JsonDeserializer<Subreddit> {

    private String subreddit;

    public GsonSubredditDeserializer(String subredditString) {
        this.subreddit = subredditString;
    }

    @Override
    public Subreddit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Post> posts = new ArrayList<>();

        JsonObject jsonBase = json.getAsJsonObject();

        JsonObject data = jsonBase.getAsJsonObject("data");

        String description = data.get("public_description").getAsString();
        int subscribers = data.get("subscribers").getAsInt();
        int activeUsers = data.get("active_user_count").getAsInt();

        return new Subreddit(subreddit,description,subscribers,activeUsers);
    }
}

