package com.example.android.finalapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class RedditContract {

    public static final String AUTHORITY = "com.example.android.finalapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_REDDITS = "reddit";

    /* Inner class that defines the contents of the table -> BASE COLUMNS PROVIDES _ID*/
    public static final class RedditEntry implements BaseColumns {

        // content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REDDITS).build();
        public static final String TABLE_NAME = "reddit_table";

        //COLUMNS
        public static final String COLUMN_TITLE = "title";
        //public static final String _ID = "_id"; -> provided by Base Columns


        //CREATE TABLE
        //SQLITE DATATYPES: INTEGER, TEXT, BLOB, REAL (decimal), NUMERIC (boolean,dates)
        public static final String CREATE_REDDIT_TABLE  = "CREATE TABLE "  + TABLE_NAME + " (" +
                                        _ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                        COLUMN_TITLE           + " TEXT);";

        //DELETE TABLE
        public static final String DELETE_REDDIT_TABLE  = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }
}
