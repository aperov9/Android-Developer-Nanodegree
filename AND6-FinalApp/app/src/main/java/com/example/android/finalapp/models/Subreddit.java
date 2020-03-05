package com.example.android.finalapp.models;

public class Subreddit {

    private String mTitle;
    private String mDescription;
    private int mSubscribers;
    private int mActiveUsers;

    public Subreddit(String mTitle, String mDescription, int mSubscribers, int mActiveUsers) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mSubscribers = mSubscribers;
        this.mActiveUsers = mActiveUsers;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmSubscribers() {
        return mSubscribers;
    }

    public int getmActiveUsers() {
        return mActiveUsers;
    }

    @Override
    public String toString() {
        return "Subreddit{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSubscribers=" + mSubscribers +
                ", mActiveUsers=" + mActiveUsers +
                '}';
    }
}
