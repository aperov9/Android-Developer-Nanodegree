package com.example.android.finalapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.finalapp.PostDetailActivity;
import com.example.android.finalapp.R;
import com.example.android.finalapp.models.Post;

import java.util.List;

import static com.example.android.finalapp.Constants.ARG_POST;
import static com.example.android.finalapp.models.Post.TYPE_PICTURE;
import static com.example.android.finalapp.models.Post.TYPE_TEXT;
import static com.example.android.finalapp.network.Utilities.checkNumber;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final Post post = mPosts.get(pos);

        holder.tv_title.setText(post.getmTitle());
        holder.tv_subreddit.setText(post.getmSubreddit());
        String ups = checkNumber(String.valueOf(post.getmScore()));
        holder.tv_ups.setText(ups);
        String comments = checkNumber(String.valueOf(post.getmCommentsCount()));
        holder.tv_comments.setText(comments);

        switch (post.getmType()){
            case TYPE_TEXT:
                Glide.with(mContext).clear(holder.iv_image);
                break;
            case TYPE_PICTURE:
                Glide.with(mContext).load(Uri.parse(post.getmUrl())).into(holder.iv_image);
                break;
        }


        holder.post_openDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(ARG_POST,post);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title, tv_subreddit, tv_ups, tv_comments;
        private ImageView iv_image;
        private LinearLayout post_openDetails;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.post_title);
            tv_subreddit = itemView.findViewById(R.id.post_subreddit);
            tv_ups = itemView.findViewById(R.id.post_ups);
            tv_comments = itemView.findViewById(R.id.post_comments);
            iv_image = itemView.findViewById(R.id.post_image);
            post_openDetails = itemView.findViewById(R.id.post_openDetails);

        }
    }
}
