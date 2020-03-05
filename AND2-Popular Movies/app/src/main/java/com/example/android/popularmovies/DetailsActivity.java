package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.fragments.ReviewFragment;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.example.android.popularmovies.fragments.TrailersFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    private MenuItem menu_favoriteMovie;

    private TextView movieDetail_title, movieDetail_overview, movieDetail_releaseDate, movieDetail_voteAverage;
    private ImageView movieDetail_poster, movieDetail_circle;
    private Movie currentMovie = null;
    ContentValues contentValues = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(getIntent().getExtras().get("currentMovie") == null){
            Toast.makeText(DetailsActivity.this,getString(R.string.utilities_genericError),Toast.LENGTH_LONG).show();
            finish();
        }

        movieDetail_poster = findViewById(R.id.movieDetail_poster);
        movieDetail_title = findViewById(R.id.movieDetail_title);
        movieDetail_overview = findViewById(R.id.movieDetail_overview);
        movieDetail_releaseDate = findViewById(R.id.movieDetail_releaseDate);
        movieDetail_voteAverage = findViewById(R.id.movieDetail_voteAverage);
        movieDetail_circle = findViewById(R.id.movieDetail_circle);

        currentMovie = (Movie) getIntent().getExtras().get("currentMovie");

        Picasso.get().load(currentMovie.getImage_location()).placeholder(R.drawable.placeholder).into(movieDetail_poster);
        movieDetail_title.setText(currentMovie.getTitle());
        movieDetail_overview.setText(currentMovie.getOverview());
        movieDetail_releaseDate.setText(currentMovie.getRelease_date());

        Double doubleRating = currentMovie.getVote_average();
        int intRating = (int) Math.floor(doubleRating);
        movieDetail_voteAverage.setText(intRating+"");
        movieDetail_circle.setColorFilter(ContextCompat.getColor(DetailsActivity.this, getColorResourceId(doubleRating)), android.graphics.PorterDuff.Mode.SRC_IN);

        FragmentManager fragmentManager = getSupportFragmentManager();
        TrailersFragment trailersFragment = TrailersFragment.newInstance(currentMovie);
        ReviewFragment reviewFragment = ReviewFragment.newInstance(currentMovie.getId());

        fragmentManager.beginTransaction().add(R.id.movieDetail_trailerContainer,trailersFragment).commit();
        fragmentManager.beginTransaction().add(R.id.movieDetail_reviewContainer,reviewFragment).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        menu_favoriteMovie = menu.findItem(R.id.menu_favoriteMovie);
        setFavoriteMenuItem();
        return true;
    }

    private void setFavoriteMenuItem() {
        int id = currentMovie.getId();

        if(checkMovieExistence(id)){
            menu_favoriteMovie.setIcon(R.drawable.ic_favorite_white_24dp);
        }else {
            menu_favoriteMovie.setIcon(R.drawable.ic_favorite_border_white_24dp);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_favoriteMovie:

                int movieId = currentMovie.getId();

                if(checkMovieExistence(movieId)){
                    deleteMovie(movieId);
                }else {
                    addMovie();
                }
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    private void deleteMovie(int movieId) {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movieId)).build();

        long count = getContentResolver().delete(uri,null,null);

        if(count > 0){
            menu_favoriteMovie.setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void addMovie() {

        contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry._ID, currentMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, currentMovie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, currentMovie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, currentMovie.getVote_average());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, currentMovie.getRelease_date());

        new ImageLoader().execute(currentMovie.getImage_location());

    }

    private class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            String url = params[0];
            try {
                image = Picasso.get().load(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, currentMovie.getImage_location());

            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = getContentResolver().insert(uri,contentValues);

            if(uri != null) {
                menu_favoriteMovie.setIcon(R.drawable.ic_favorite_white_24dp);
            }
        }
    }

    private boolean checkMovieExistence(int id) {
        String stringId = Integer.toString(id);
        String[] tables = {MovieContract.MovieEntry._ID};
        String[] selectionArgs = {stringId};

        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, tables,"_id=?",selectionArgs,null,null,null);

        int count = cursor.getCount();
        cursor.close();

        return(count > 0);
    }

    private int getColorResourceId(double rating) {
        int ratingColorResourceId = R.color.colorWhite;
        int floor = (int) Math.floor(rating);
        switch (floor) {
            case 0:
            case 1:
                ratingColorResourceId = R.color.rating1;
                break;
            case 2:
                ratingColorResourceId = R.color.rating2;
                break;
            case 3:
                ratingColorResourceId = R.color.rating3;
                break;
            case 4:
                ratingColorResourceId = R.color.rating4;
                break;
            case 5:
                ratingColorResourceId = R.color.rating5;
                break;
            case 6:
                ratingColorResourceId = R.color.rating6;
                break;
            case 7:
                ratingColorResourceId = R.color.rating7;
                break;
            case 8:
                ratingColorResourceId = R.color.rating8;
                break;
            case 9:
            case 10:
                ratingColorResourceId = R.color.rating9;
                break;
        }
        return ratingColorResourceId;
    }
}
