package com.example.android.finalapp.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.finalapp.db.RedditContract;

import java.util.ArrayList;
import java.util.List;

public class SubscribedSubredditsLoader extends AsyncTaskLoader<List<String>> {

    public SubscribedSubredditsLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        return getSubreddits();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private Cursor getRedditCursor() {
        String[] projection = {"*"};
        return getContext().getContentResolver().query(RedditContract.RedditEntry.CONTENT_URI,projection,null,null,null,null);
    }

    public List<String> getSubreddits(){
        List<String> subreddits = new ArrayList<>();
        final Cursor cursor = getRedditCursor();

        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(RedditContract.RedditEntry.COLUMN_TITLE);
            final String title = cursor.getString(titleIndex);
            subreddits.add(title);
        }

        cursor.close();
        return subreddits;
    }
}
