package com.example.android.popularmovies.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieAdapterLocal;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;

public class LocalMoviesFragment extends Fragment{

    private Parcelable recyclerViewState;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerViewLayout;
    private MovieDbHelper movieDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public LocalMoviesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.local_movies_layout,null);
        recyclerViewLayout = view.findViewById(R.id.grid_movies);
        recyclerViewLayout.hasFixedSize();

        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLayout.setLayoutManager(linearLayoutManager);

        movieDbHelper = new MovieDbHelper(getActivity());
        sqLiteDatabase = movieDbHelper.getWritableDatabase();

        final Cursor cursor = getMoviesCursor();

        final MovieAdapterLocal movieAdapterLocal = new MovieAdapterLocal(getActivity(), cursor);
        recyclerViewLayout.setLayoutManager(linearLayoutManager);
        recyclerViewLayout.setAdapter(movieAdapterLocal);

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mySwipeRefreshLayout.setRefreshing(false);
                movieAdapterLocal.swapCursor(getMoviesCursor());
            }}
        );

        return view;
    }

    private Cursor getMoviesCursor() {
        String[] projection = {"*"};
        return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection,null,null,null,null);
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = recyclerViewLayout.getLayoutManager().onSaveInstanceState();//save
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerViewLayout.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
    }

}
