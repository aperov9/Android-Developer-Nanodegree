package com.example.android.finalapp.models;

import java.io.Serializable;

public class Post implements Serializable{

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PICTURE = 1;

    private String mSubreddit;
    private String mTitle;
    private String mSelfText;
    private String mAuthor;
    private int mScore;
    private int mCommentsCount;
    private String mThumbnail;
    private String mUrl;
    private String mAfter;
    private int mType;
    private String mPermaLink;

    public Post(String mSubreddit, String mTitle, String mSelfText, String mAuthor, int mScore, int mCommentsCount, String mThumbnail, String mUrl, String mAfter, int mType, String mPermaLink) {
        this.mSubreddit = mSubreddit;
        this.mTitle = mTitle;
        this.mSelfText = mSelfText;
        this.mAuthor = mAuthor;
        this.mScore = mScore;
        this.mCommentsCount = mCommentsCount;
        this.mThumbnail = mThumbnail;
        this.mUrl = mUrl;
        this.mAfter = mAfter;
        this.mType = mType;
        this.mPermaLink = mPermaLink;
    }

    @Override
    public String toString() {
        return "Post{" +
                "mSubreddit='" + mSubreddit + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mSelfText='" + mSelfText + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mScore=" + mScore +
                ", mCommentsCount=" + mCommentsCount +
                ", mThumbnail='" + mThumbnail + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mAfter='" + mAfter + '\'' +
                ", mType=" + mType +
                ", mPermaLink='" + mPermaLink + '\'' +
                '}';
    }

    public static int getTypeText() {
        return TYPE_TEXT;
    }

    public static int getTypePicture() {
        return TYPE_PICTURE;
    }

    public String getmSubreddit() {
        return mSubreddit;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSelfText() {
        return mSelfText;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public int getmScore() {
        return mScore;
    }

    public int getmCommentsCount() {
        return mCommentsCount;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAfter() {
        return mAfter;
    }

    public int getmType() {
        return mType;
    }

    public String getmPermaLink() {
        return mPermaLink;
    }
}
