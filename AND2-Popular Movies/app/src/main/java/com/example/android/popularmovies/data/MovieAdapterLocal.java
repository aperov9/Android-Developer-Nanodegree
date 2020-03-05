package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;

public class MovieAdapterLocal extends RecyclerView.Adapter<MovieAdapterLocal.ViewHolder>{

    private Context cx;
    private Cursor mCursor;

    //CONSTRUCTOR
    public MovieAdapterLocal(Context cx, Cursor mCursor) {
        this.cx = cx;
        this.mCursor = mCursor;
    }

    //FIRST TIME CREATION
    @NonNull
    @Override
    public MovieAdapterLocal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cx).inflate(R.layout.favorites_item, parent, false);
        return new MovieAdapterLocal.ViewHolder(view);
    }

    //BINDING DATA TO VIEW
    @Override
    public void onBindViewHolder(@NonNull final MovieAdapterLocal.ViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        final int movieID = mCursor.getInt(idIndex);
        int titelIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        String movieTitle = mCursor.getString(titelIndex);

        holder.tv_favoriteTitle.setText(movieTitle);
        holder.iv_favoriteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(String.valueOf(movieID)).build();

                long count = cx.getContentResolver().delete(uri,null,null);

                if(count > 0){
                    //this is not very elegant, how to update correctly?
                    swapCursor(getMoviesCursor());
                }
            }
        });

    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if(newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    private Cursor getMoviesCursor() {
        String[] projection = {"*"};
        return cx.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection,null,null,null,null);
    }

    //NUMBER OF ITEMS
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    //INNER CLASS VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout favorite_item;
        TextView tv_favoriteTitle;
        ImageView iv_favoriteDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_favoriteTitle =  itemView.findViewById(R.id.favorite_title);
            iv_favoriteDelete = itemView.findViewById(R.id.favorite_delete);
            favorite_item = itemView.findViewById(R.id.favorite_item);

        }
    }
}
