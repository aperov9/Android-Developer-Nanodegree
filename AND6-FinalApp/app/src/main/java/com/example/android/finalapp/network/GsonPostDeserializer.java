package com.example.android.finalapp.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.example.android.finalapp.models.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.finalapp.models.Post.TYPE_PICTURE;
import static com.example.android.finalapp.models.Post.TYPE_TEXT;

public class GsonPostDeserializer implements JsonDeserializer<List<Post>> {

    @Override
    public List<Post> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Post> posts = new ArrayList<>();

        JsonObject jsonBase = json.getAsJsonObject();

        JsonObject data = jsonBase.getAsJsonObject("data");

        String after = data.get("after").getAsString();

        JsonArray jsonArray = data.getAsJsonArray("children");

        for(int i= 0; i< jsonArray.size(); i++){
            JsonObject childrenBase = jsonArray.get(i).getAsJsonObject();
            JsonObject current = childrenBase.getAsJsonObject("data");

            String subreddit = current.get("subreddit_name_prefixed").getAsString();
            String title = current.get("title").getAsString();;
            String selfText = current.get("selftext").getAsString();
            String author = current.get("author").getAsString();
            int score = current.get("score").getAsInt();;
            int comments = current.get("num_comments").getAsInt();
            String thumbnail = current.get("thumbnail").getAsString();
            String url = current.get("url").getAsString();
            String permalink = current.get("permalink").getAsString();

            Boolean is_video = current.get("is_video").getAsBoolean();

            int type = TYPE_TEXT;
            if (!url.isEmpty() && url.endsWith(".jpg") || url.endsWith(".png") || !url.endsWith(".gifv")){
                type = TYPE_PICTURE;
            }
            if (!is_video ){
                type = TYPE_PICTURE;
                posts.add(new Post(subreddit,title,selfText,author,score,comments,thumbnail,url,after,type,permalink));
            }
        }

        return posts;
    }
}
