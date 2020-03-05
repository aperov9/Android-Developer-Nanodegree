package com.example.android.popularmovies.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.data.MovieAdapter;
import com.example.android.popularmovies.data.MovieLoader;

import java.util.ArrayList;

import static com.example.android.popularmovies.data.StaticData.mdbi_api_key;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    private Parcelable recyclerViewState;
    private static int currentLoader;
    private static final int LOADER_ID_POPULAR_MOVIES = 0;
    private static final int LOADER_ID_TOP_RATED_MOVIES = 1;

    public void loadPopularMovies() {
        currentLoader = LOADER_ID_POPULAR_MOVIES;
    }

    public void loadBestMovies() {
        currentLoader = LOADER_ID_TOP_RATED_MOVIES;
    }

    private static final String POPULAR_MOVIES_STRING = "https://api.themoviedb.org/3/movie/popular" + mdbi_api_key;
    private static final String TOP_RATED_MOVIES_STRING = "https://api.themoviedb.org/3/movie/top_rated" + mdbi_api_key;

    private MovieAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerViewLayout;
    private ProgressBar progressBar;
    private TextView grid_emptyText;

    private LoaderManager loaderManager;

    public MoviesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_layout,null);
        recyclerViewLayout = view.findViewById(R.id.grid_movies);
        recyclerViewLayout.hasFixedSize();
        progressBar = view.findViewById(R.id.grid_progress);
        grid_emptyText = view.findViewById(R.id.grid_emptyText);
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerViewLayout.setLayoutManager(gridLayoutManager);

        initLoader(currentLoader);

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mySwipeRefreshLayout.setRefreshing(false);
                initLoader(currentLoader);
            }}
        );

        return view;
    }

    private void initLoader(int loaderID) {
        showLoading();

        if(loaderID == LOADER_ID_POPULAR_MOVIES || loaderID == LOADER_ID_TOP_RATED_MOVIES){
            if(!isNetworkOnline()){
                showNoInternet();
                return;
            }
        }
        loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(loaderID, null, this);

    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_ID_POPULAR_MOVIES:
                return new MovieLoader(getActivity(), POPULAR_MOVIES_STRING);
            case LOADER_ID_TOP_RATED_MOVIES:
                return new MovieLoader(getActivity(), TOP_RATED_MOVIES_STRING);
            default:
                return new MovieLoader(getActivity(), POPULAR_MOVIES_STRING);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            if (data != null && !data.isEmpty()) {
                mAdapter = new MovieAdapter(getActivity(),data);
                recyclerViewLayout.setLayoutManager(gridLayoutManager);
                recyclerViewLayout.setAdapter(mAdapter);
                showGrid();
            }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) { }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }

    // IS THERE A BETTER WAY OF DOING THIS SHOWING AND HIDING OF ELEMENTS?
    public void showNoInternet(){
        progressBar.setVisibility(View.GONE);
        recyclerViewLayout.setVisibility(View.GONE);
        grid_emptyText.setVisibility(View.VISIBLE);
    }

    public void showGrid(){
        progressBar.setVisibility(View.GONE);
        grid_emptyText.setVisibility(View.GONE);
        recyclerViewLayout.setVisibility(View.VISIBLE);
    }

    public void showLoading(){
        grid_emptyText.setVisibility(View.GONE);
        recyclerViewLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
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
