package com.example.android.finalapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.finalapp.db.RedditContract.RedditEntry.TABLE_NAME;

public class RedditContentProvider extends ContentProvider {

    private RedditDbHelper redditDbHelper;

    public static final int SUBREDDITS = 100;
    public static final int SUBREDDIT_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(RedditContract.AUTHORITY, RedditContract.PATH_REDDITS, SUBREDDITS);
        uriMatcher.addURI(RedditContract.AUTHORITY, RedditContract.PATH_REDDITS + "/#", SUBREDDIT_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        redditDbHelper = new RedditDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = redditDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case SUBREDDITS:
                returnCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case SUBREDDIT_WITH_ID:
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor =  db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = redditDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SUBREDDITS:

                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(RedditContract.RedditEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = redditDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int deletedCount;

        switch (match) {
            case SUBREDDIT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deletedCount = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = redditDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksUpdatedCount;

        switch (match) {
            case SUBREDDIT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksUpdatedCount = db.update(TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdatedCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksUpdatedCount;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case SUBREDDITS:
                // directory
                return "vnd.android.cursor.dir" + "/" + RedditContract.AUTHORITY + "/" + RedditContract.PATH_REDDITS;
            case SUBREDDIT_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + RedditContract.AUTHORITY + "/" + RedditContract.PATH_REDDITS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
