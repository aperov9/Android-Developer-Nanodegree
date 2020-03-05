
package com.example.android.finalapp.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.finalapp.R;
import com.example.android.finalapp.db.RedditContract;
import com.example.android.finalapp.db.RedditDbHelper;
import com.example.android.finalapp.loaders.SubredditLoader;
import com.example.android.finalapp.models.Subreddit;
import com.example.android.finalapp.network.Utilities;

import static com.example.android.finalapp.Constants.ARG_SUBREDDIT;
import static com.example.android.finalapp.Constants.SUBREDDIT_LOADER;
import static com.example.android.finalapp.db.RedditContract.RedditEntry.COLUMN_TITLE;

public class ViewSubredditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Subreddit>{

    private LoaderManager loaderManager;
    private String mSubreddit = "";
    private TextView tv_title, tv_description, tv_subscribers;
    private ImageView iv_favorite;
    private ProgressBar progressBar;
    private LinearLayout ll_subreddit;

    public ViewSubredditFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SUBREDDIT)) {
            mSubreddit = (String) getArguments().getString(ARG_SUBREDDIT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_subreddit, container, false);

        tv_title = (TextView)view.findViewById(R.id.subreddit_title);
        tv_description = (TextView)view.findViewById(R.id.subreddit_description);
        tv_subscribers = (TextView)view.findViewById(R.id.subreddit_subscribers);
        progressBar = (ProgressBar)view.findViewById(R.id.subreddit_progressbar);
        ll_subreddit = (LinearLayout) view.findViewById(R.id.subreddit_frame);
        iv_favorite = (ImageView)view.findViewById(R.id.subreddit_favorite);

        if (!mSubreddit.isEmpty() && Utilities.isNetworkOnline(getActivity())) {
            //LOAD SUBREDDIT
            loaderManager = getLoaderManager();
            loaderManager.restartLoader(SUBREDDIT_LOADER, null, ViewSubredditFragment.this);

            //LOAD POSTS
            Bundle bundle = new Bundle();
            bundle.putString(ARG_SUBREDDIT,mSubreddit);
            SubredditPostsFragment subredditPostsFragment = new SubredditPostsFragment();
            subredditPostsFragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.subreddit_posts, subredditPostsFragment).commit();

            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(subredditExistsAlready(mSubreddit)){
                        deleteSubreddit();
                    }else {
                        insertSubreddit();
                    }
                }
            });
        }else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator);
            Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

        return view;
    }

    @NonNull
    @Override
    public Loader<Subreddit> onCreateLoader(int id, @Nullable Bundle args) {
        return new SubredditLoader(getActivity(),mSubreddit);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Subreddit> loader, Subreddit data) {
        if(data == null){
          return;
        }
        setSubredditData(data);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Subreddit> loader) {

    }

    private void setSubredditData(Subreddit data) {
        if(subredditExistsAlready(mSubreddit)){
            iv_favorite.setImageResource(R.drawable.ic_star_black_24dp);
        }
        tv_title.setText("r/"+data.getmTitle());
        tv_description.setText(data.getmDescription());
        tv_subscribers.setText(data.getmSubscribers() + " subscribers");
        progressBar.setVisibility(View.INVISIBLE);
        ll_subreddit.setVisibility(View.VISIBLE);
        ll_subreddit.requestFocus();
    }

    private boolean subredditExistsAlready(String subreddit) {
        String[] tables = {RedditContract.RedditEntry.COLUMN_TITLE};
        String[] selectionArgs = {subreddit};

        RedditDbHelper dbHelper = new RedditDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(RedditContract.RedditEntry.TABLE_NAME, tables,"title=?",selectionArgs,null,null,null);

        int count = cursor.getCount();
        cursor.close();

        return(count > 0);
    }

    private void insertSubreddit(){
        Uri uri = RedditContract.RedditEntry.CONTENT_URI;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, mSubreddit);

        Uri returnUri = getActivity().getContentResolver().insert(uri,contentValues);

        if(returnUri != null) {
            iv_favorite.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    private void deleteSubreddit(){
        RedditDbHelper dbHelper = new RedditDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deletedRows = db.delete(RedditContract.RedditEntry.TABLE_NAME,"title=?",new String[]{mSubreddit});


        if(deletedRows > 0) {
            iv_favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }


}
