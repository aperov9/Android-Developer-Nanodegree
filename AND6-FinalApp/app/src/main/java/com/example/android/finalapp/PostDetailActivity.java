package com.example.android.finalapp;

import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.finalapp.models.Post;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.finalapp.Constants.ARG_POST;
import static com.example.android.finalapp.Constants.REDDIT_BASE;
import static com.example.android.finalapp.models.Post.TYPE_PICTURE;
import static com.example.android.finalapp.models.Post.TYPE_TEXT;
import static com.example.android.finalapp.network.Utilities.checkNumber;
import static com.example.android.finalapp.network.Utilities.isNetworkOnline;

public class PostDetailActivity extends AppCompatActivity {
    @BindView(R.id.postDetail_title)
    TextView tv_title;
    @BindView(R.id.postDetail_image)
    ImageView iv_image;
    @BindView(R.id.postDetail_ups)
    TextView tv_ups;
    @BindView(R.id.postDetail_comments)
    TextView tv_comments;

    private Post mCurrentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        if (!getIntent().hasExtra(ARG_POST) && isNetworkOnline(PostDetailActivity.this)) {
            finish();
        }

        mCurrentPost = (Post) getIntent().getExtras().getSerializable(ARG_POST);
        getSupportActionBar().setTitle(mCurrentPost.getmSubreddit());

        tv_title.setText(mCurrentPost.getmTitle());
        String ups = checkNumber(String.valueOf(mCurrentPost.getmScore()));
        tv_ups.setText(ups);
        String comments = checkNumber(String.valueOf(mCurrentPost.getmCommentsCount()));
        tv_comments.setText(comments);

        switch (mCurrentPost.getmType()) {
            case TYPE_TEXT:
                Glide.with(PostDetailActivity.this).clear(iv_image);
                break;
            case TYPE_PICTURE:
                Glide.with(PostDetailActivity.this).load(Uri.parse(mCurrentPost.getmUrl())).into(iv_image);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_open_link:
                openLink();
                break;
            case R.id.menu_share_post:
                sharePost();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    private void sharePost() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Check this out:\n");
        i.putExtra(Intent.EXTRA_TEXT, REDDIT_BASE + mCurrentPost.getmPermaLink());
        startActivity(Intent.createChooser(i, "Share"));
    }

    private void openLink() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            i.setData(Uri.parse(REDDIT_BASE + mCurrentPost.getmPermaLink()));
            startActivity(i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
