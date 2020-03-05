package com.example.android.finalapp;

public class Constants {

    public static final String ARG_SUBREDDIT = "subreddit";
    public static final String ARG_POST = "post";

    public static final int SEARCH_SUBREDDITS_LOADER = 0;
    public static final int SUBREDDIT_LOADER = 1;
    public static final int SUBREDDIT_POSTS_LOADER = 3;
    public static final int SUBREDDIT_LOCAL_LOADER = 4;

    public static final String QUERY_STRING = "query";

    public static final String REDDIT_BASE = "https://www.reddit.com";
    public static final String SEARCH_SUBREDDITS_BASE = "https://www.reddit.com/subreddits/search.json?q=";
    public static final String SUBREDDIT_POPULAR = "popular";

    //ADD DETAIL SCREEN?

    //FUTURE
    //TODO add Comments to posts
    //TODO add Filtering
    //TODO add endless scrolling via paging library
    //TODO add staggered gridview to landscape mode
    //TODO add categories to saved subreddits
    //TODO put searchview inside extra fragment, add backstack for fragments
    //TODO add indicators for loading, errors etc

}
