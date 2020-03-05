package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    /* Inner class that defines the contents of the table -> BASE COLUMNS PROVIDES _ID*/
    public static final class MovieEntry implements BaseColumns {

        // content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "movies";

        //COLUMNS
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_IMAGE = "image";
        //public static final String _ID = "_id"; -> provided by Base Columns


        //CREATE TABLE
        //SQLITE DATATYPES: INTEGER, TEXT, BLOB, REAL (decimal), NUMERIC (boolean,dates)
        public static final String CREATE_MOVIE_TABLE  = "CREATE TABLE "  + TABLE_NAME + " (" +
                                        _ID                    + " INTEGER PRIMARY KEY, " +
                                        COLUMN_TITLE           + " TEXT, " +
                                        COLUMN_OVERVIEW        + " TEXT, " +
                                        COLUMN_VOTE_AVERAGE    + " REAL, " +
                                        COLUMN_RELEASE_DATE    + " TEXT, " +
                                        COLUMN_IMAGE           + " BLOB);";

        //DELETE TABLE
        public static final String DELETE_MOVIE_TABLE  = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }
}
