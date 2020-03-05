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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.finalapp.R;
import com.example.android.finalapp.adapters.SubscribedSubredditsAdapter;
import com.example.android.finalapp.loaders.SubscribedSubredditsLoader;
import com.example.android.finalapp.network.Utilities;

import java.util.List;

import static com.example.android.finalapp.Constants.SUBREDDIT_LOCAL_LOADER;

public class SubscribedSubredditsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>>{

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plain_recyclerview,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        if(Utilities.isNetworkOnline(getActivity())) {
            loaderManager.restartLoader(SUBREDDIT_LOCAL_LOADER, null, SubscribedSubredditsFragment.this);
        }else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator);
            Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

        return view;
    }

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
        return new SubscribedSubredditsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
        if (data == null){
            return;
        }
        recyclerView.setAdapter(new SubscribedSubredditsAdapter(getActivity(),data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

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
