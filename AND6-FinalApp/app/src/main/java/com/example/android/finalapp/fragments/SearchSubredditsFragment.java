package com.example.android.finalapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.finalapp.Constants;
import com.example.android.finalapp.R;
import com.example.android.finalapp.adapters.SearchSubredditsAdapter;
import com.example.android.finalapp.network.JsonParsing;
import com.example.android.finalapp.network.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.android.finalapp.Constants.QUERY_STRING;
import static com.example.android.finalapp.Constants.SEARCH_SUBREDDITS_LOADER;

public class SearchSubredditsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>> {

    private LoaderManager loaderManager;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plain_recyclerview,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        String query = getArguments().getString(QUERY_STRING);
        bundle.putString(QUERY_STRING,query);

        if(Utilities.isNetworkOnline(getActivity())) {
            loaderManager.restartLoader(SEARCH_SUBREDDITS_LOADER, bundle, SearchSubredditsFragment.this);
        }else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator);
            Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }
        return view;
    }

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
        return new SubredditQueryLoader(getActivity(),args.getString(QUERY_STRING));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
        if(data == null){
            return;
        }
        recyclerView.setAdapter(new SearchSubredditsAdapter(getActivity(),data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {
        recyclerView.setAdapter(null);
        loader.startLoading();
    }

    private static class SubredditQueryLoader extends AsyncTaskLoader<List<String>> {

        private String mQueryString;

        private SubredditQueryLoader(@NonNull Context context, String mQueryString) {
            super(context);
            this.mQueryString = mQueryString;
        }

        @Nullable
        @Override
        public List<String> loadInBackground() {

            String response = "";
            URL url = Utilities.createUrl(Constants.SEARCH_SUBREDDITS_BASE+mQueryString);
            try {
                response = Utilities.makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return JsonParsing.JSONtoSearchSubredditList(response);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }
}
