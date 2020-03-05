package com.example.android.finalapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.finalapp.fragments.HomeFeedFragment;
import com.example.android.finalapp.fragments.SearchSubredditsFragment;
import com.example.android.finalapp.fragments.SubredditPostsFragment;
import com.example.android.finalapp.fragments.SubscribedSubredditsFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.finalapp.Constants.ARG_SUBREDDIT;
import static com.example.android.finalapp.Constants.QUERY_STRING;
import static com.example.android.finalapp.Constants.SUBREDDIT_POPULAR;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FragmentManager fragmentManager;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(ARG_SUBREDDIT,SUBREDDIT_POPULAR);
        final SubredditPostsFragment subredditPostsFragment = new SubredditPostsFragment();
        subredditPostsFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout,subredditPostsFragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_popular:
                                fragmentManager.beginTransaction().replace(R.id.frameLayout,subredditPostsFragment).commit();
                                displayInterstitial();
                                break;
                            case R.id.menu_home:
                                fragmentManager.beginTransaction().replace(R.id.frameLayout,new HomeFeedFragment()).commit();
                                displayInterstitial();
                                break;
                            case R.id.menu_subreddits:
                                fragmentManager.beginTransaction().replace(R.id.frameLayout,new SubscribedSubredditsFragment()).commit();
                                displayInterstitial();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.setFocusable(true);
                searchViewAndroidActionBar.requestFocusFromTouch();

                searchViewAndroidActionBar.clearFocus();
                Bundle bundle = new Bundle();
                bundle.putString(QUERY_STRING,query);
                SearchSubredditsFragment searchSubredditsFragment = new SearchSubredditsFragment();
                searchSubredditsFragment.setArguments(bundle);

                //LOG QUERIES WITH ANALYTICS
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                fragmentManager.beginTransaction().replace(R.id.frameLayout,searchSubredditsFragment).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void displayInterstitial(){
        if (count == 10) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            count = 0;
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }else {
            count++;
        }
    }


}
