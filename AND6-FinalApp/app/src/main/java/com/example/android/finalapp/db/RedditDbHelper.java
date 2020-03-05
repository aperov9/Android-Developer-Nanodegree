package com.example.android.finalapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.finalapp.db.RedditContract.RedditEntry.COLUMN_TITLE;
import static com.example.android.finalapp.db.RedditContract.RedditEntry.CREATE_REDDIT_TABLE;
import static com.example.android.finalapp.db.RedditContract.RedditEntry.DELETE_REDDIT_TABLE;
import static com.example.android.finalapp.db.RedditContract.RedditEntry.TABLE_NAME;


public class RedditDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit.db";

    private static final int DATABASE_VERSION = 6;

    public RedditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REDDIT_TABLE);
        String addDummySubreddits = "INSERT INTO " +TABLE_NAME + " (" + COLUMN_TITLE + ") VALUES ('funny'), ('aww'), ('cats');";
        db.execSQL(addDummySubreddits);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_REDDIT_TABLE);
        onCreate(db);

    }
}
