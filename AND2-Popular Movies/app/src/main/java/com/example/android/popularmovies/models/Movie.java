package com.example.android.popularmovies.models;

import java.io.Serializable;
import java.util.List;
import static com.example.android.popularmovies.data.StaticData.*;

public class Movie implements Serializable{

    private String title = "";
    private int id = -1;
    private String overview = "";
    private double vote_average = 0.0;
    private String release_date = "";
    private String image_location = "https://image.tmdb.org/t/p/w780";
    private String trailers_link;
    private List<Trailer> youtubeLinks = null;

    public Movie() {
    }

    public String getTrailers_link() {
        return trailers_link;
    }

    public void setTrailers_link(String id) {
        this.trailers_link = "https://api.themoviedb.org/3/movie/" + id + "/videos" + mdbi_api_key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = this.image_location + image_location;
    }

    public List<Trailer> getYoutubeLinks() {
        return youtubeLinks;
    }

    public void setYoutubeLinks(List<Trailer> youtubeLinks) {
        this.youtubeLinks = youtubeLinks;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", image_location='" + image_location + '\'' +
                ", trailers_link='" + trailers_link + '\'' +
                ", youtubeLinks=" + youtubeLinks +
                '}';
    }
}
