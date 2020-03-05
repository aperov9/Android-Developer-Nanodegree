package com.example.android.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.network.Utilities;
import com.example.sweet.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.Constants.ARG_RECIPE;
import static com.example.android.bakingapp.Constants.ARG_STEP;

public class StepDetailFragment extends Fragment {
    @BindView(R.id.step_detail_description) TextView mDescription;
    @BindView(R.id.step_detail_video_player) PlayerView mPlayerView;
    //@BindView(R.id.toolbar_layout) CollapsingToolbarLayout appBarLayout;

    private Step mStep;
    private Recipe mRecipe;
    private SimpleExoPlayer mExoPlayer;
    private Uri mediaUri;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP) && getArguments().containsKey(ARG_RECIPE)) {

            mStep = (Step) getArguments().getSerializable(ARG_STEP);
            mRecipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getmName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (mStep != null) {

            mDescription.setText(mStep.getmDescription());

            if(!mStep.getmStringUrl().isEmpty() && Utilities.isNetworkOnline(getActivity())) {
                mediaUri = Uri.parse(mStep.getmStringUrl());
                initializePlayer();
            }else{
                mPlayerView.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getActivity(), getActivity().getPackageName());
            MediaSource videoSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent)).createMediaSource(mediaUri);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(false);
        }

    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            long position = getArguments().getLong("position");
            boolean playWhenReady = getArguments().getBoolean("playWhenReady");
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer();
            long position = getArguments().getLong("position");
            boolean playWhenReady = getArguments().getBoolean("playWhenReady");
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Bundle outState = new Bundle();
        outState.putBoolean("playWhenReady", mExoPlayer.getPlayWhenReady());
        outState.putLong("position", mExoPlayer.getCurrentPosition());
        onSaveInstanceState(outState);

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Bundle outState = new Bundle();
        outState.putBoolean("playWhenReady", mExoPlayer.getPlayWhenReady());
        outState.putLong("position", mExoPlayer.getCurrentPosition());
        onSaveInstanceState(outState);

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

}
