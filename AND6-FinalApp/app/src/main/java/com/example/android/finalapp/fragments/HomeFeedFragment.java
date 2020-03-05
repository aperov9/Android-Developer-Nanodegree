package com.example.android.finalapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.finalapp.R;
import com.example.android.finalapp.adapters.PostAdapter;
import com.example.android.finalapp.loaders.PostsLoader;
import com.example.android.finalapp.loaders.SubscribedSubredditsLoader;
import com.example.android.finalapp.models.Post;
import com.example.android.finalapp.network.Utilities;

import java.util.List;

import static com.example.android.finalapp.Constants.SUBREDDIT_POSTS_LOADER;

public class HomeFeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Post>> {

    private LoaderManager loaderManager;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plain_recyclerview, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loaderManager = getLoaderManager();

        if(Utilities.isNetworkOnline(getActivity())) {
            loaderManager.restartLoader(SUBREDDIT_POSTS_LOADER, null, HomeFeedFragment.this);
        }else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator);
            Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public Loader<List<Post>> onCreateLoader(int id, @Nullable Bundle args) {
        SubscribedSubredditsLoader loader = new SubscribedSubredditsLoader(getActivity());
        List<String> subreddits = loader.getSubreddits();
        String subredditsString = "";
        for (int i = 0; i <subreddits.size(); i++) {
            if(i == subreddits.size()-1){
                subredditsString += subreddits.get(i);
            }else {
                subredditsString += subreddits.get(i) + "+";
            }
        }

        return new PostsLoader(getActivity(), subredditsString);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Post>> loader, List<Post> data) {
        if(data == null){
            return;
        }
        recyclerView.setAdapter(new PostAdapter(getActivity(), data));

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Post>> loader) {
        recyclerView.setAdapter(null);
        loader.startLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//save
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
    }

}