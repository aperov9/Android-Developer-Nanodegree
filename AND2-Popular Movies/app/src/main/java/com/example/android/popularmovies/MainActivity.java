package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.popularmovies.fragments.LocalMoviesFragment;
import com.example.android.popularmovies.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    private MoviesFragment popularMovies;
    private MoviesFragment bestMovies;
    private LocalMoviesFragment localMoviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        popularMovies = new MoviesFragment();
        popularMovies.loadPopularMovies();
        fragmentManager.beginTransaction().add(R.id.main_frame,popularMovies).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_popularMovies:
                                popularMovies = new MoviesFragment();
                                popularMovies.loadPopularMovies();
                                fragmentManager.beginTransaction().replace(R.id.main_frame, popularMovies).commit();
                                break;
                            case R.id.menu_highestRatedMovies:
                                bestMovies = new MoviesFragment();
                                bestMovies.loadBestMovies();
                                fragmentManager.beginTransaction().replace(R.id.main_frame, bestMovies).commit();
                                break;
                            case R.id.menu_favorites:
                                localMoviesFragment = new LocalMoviesFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_frame, localMoviesFragment).commit();
                                break;
                        }
                        return true;
                    }});
    }


}