package com.example.android.popularmovies.data;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.data.NetworkUtilities.*;
import static com.example.android.popularmovies.data.StaticData.*;

public class DataExtraction {

    private static final String LOG_TAG = DataExtraction.class.getName();

    public static ArrayList<Movie> fetchMovies(String stringUrl) {

        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractMoviesFromJson(jsonResponse);
    }

    public static Movie addTrailersToMovie(Movie movie){
        URL url = createUrl(movie.getTrailers_link());
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractTrailersFromJson(jsonResponse, movie);
    }

    public static List<Review> getReviewByMovieID(int id){

        String stringUrl = mdbi_baseString+ id + "/reviews" + mdbi_api_key;
        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractReviewsFromJson(jsonResponse);
    }

    private static List<Review> extractReviewsFromJson(String baseJSON) {
        if (TextUtils.isEmpty(baseJSON)) {
            return null;
        }

        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject= new JSONObject(baseJSON);

            JSONArray reviewArray =jsonObject.getJSONArray("results");

            for(int i= 0; i< reviewArray.length(); i++){
                if(i== 5) break;
                JSONObject reviewBase = reviewArray.getJSONObject(i);
                String author = reviewBase.getString("author");
                String content = reviewBase.getString("content");
                reviews.add(new Review(author,content));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing Reviews", e);
        }

        return reviews;
    }

    private static Movie extractTrailersFromJson(String baseJSON, Movie movie) {

        if (TextUtils.isEmpty(baseJSON)) {
            return null;
        }

        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject= new JSONObject(baseJSON);

            JSONArray trailerArray =jsonObject.getJSONArray("results");

            for(int i= 0; i< trailerArray.length(); i++){
                if(i== 3) break;
                JSONObject trailerBase = trailerArray.getJSONObject(i);
                String link = trailerBase.getString("key");
                String name = trailerBase.getString("name");
                trailers.add(new Trailer(link,name));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing Trailers", e);
        }
        movie.setYoutubeLinks(trailers);
        return movie;

    }


    private static ArrayList<Movie> extractMoviesFromJson(String baseJSON) {

        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

        if (TextUtils.isEmpty(baseJSON)) {
            return null;
        }

        try {
            JSONObject jsonObject= new JSONObject(baseJSON);

            JSONArray moviesArray =jsonObject.getJSONArray("results");

            for(int i= 0; i< moviesArray.length(); i++){
                Movie currentMovie = new Movie();

                JSONObject movieBase = moviesArray.getJSONObject(i);
                currentMovie.setTitle(movieBase.getString("title"));
                currentMovie.setOverview(movieBase.getString("overview"));
                currentMovie.setVote_average(movieBase.getDouble("vote_average"));
                currentMovie.setImage_location(movieBase.getString("poster_path"));
                currentMovie.setRelease_date(movieBase.getString("release_date"));

                //is this secure enough to assume that the ids from the api should be unique?
                currentMovie.setId(Integer.parseInt(movieBase.getString("id")));
                currentMovie.setTrailers_link(movieBase.getString("id"));
                movieArrayList.add(currentMovie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing Movies", e);
        }
        return movieArrayList;
    }
}
