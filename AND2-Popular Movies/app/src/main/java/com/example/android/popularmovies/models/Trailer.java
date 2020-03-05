package com.example.android.popularmovies.models;

import java.io.Serializable;

public class Trailer implements Serializable {

    private String link;
    private String title;

    public Trailer(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
