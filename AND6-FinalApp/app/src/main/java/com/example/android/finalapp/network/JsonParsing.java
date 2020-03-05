package com.example.android.finalapp.network;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParsing {

    private static final String LOG_TAG = JsonParsing.class.getName();

    public static List<String> JSONtoSearchSubredditList(String string) {

        if (TextUtils.isEmpty(string)) {
            return null;
        }

        List<String> subreddits = new ArrayList<>();

        try {
            JSONObject jsonBase = new JSONObject(string);

            JSONObject data = jsonBase.getJSONObject("data");

            JSONArray jsonArray = data.getJSONArray("children");

            for(int i= 0; i< jsonArray.length(); i++){
                JSONObject childrenBase = jsonArray.getJSONObject(i);
                JSONObject current = childrenBase.getJSONObject("data");
                String subreddit = current.getString("display_name_prefixed");

                subreddits.add(subreddit);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON", e);
        }

        return subreddits;
    }
}
