package com.example.android.finalapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.finalapp.R;
import com.example.android.finalapp.fragments.ViewSubredditFragment;

import java.util.List;

import static com.example.android.finalapp.Constants.ARG_SUBREDDIT;

public class SearchSubredditsAdapter extends RecyclerView.Adapter<SearchSubredditsAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mSubreddits;

    public SearchSubredditsAdapter(Context mContext, List<String> mSubreddits) {
        this.mContext = mContext;
        this.mSubreddits = mSubreddits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mName.setText(mSubreddits.get(holder.getAdapterPosition()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String subreddit = mSubreddits.get(holder.getAdapterPosition());
                subreddit = subreddit.substring(2,subreddit.length());
                bundle.putString(ARG_SUBREDDIT,subreddit);

                ViewSubredditFragment viewSubredditFragment = new ViewSubredditFragment();
                viewSubredditFragment.setArguments(bundle);

                FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frameLayout,viewSubredditFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubreddits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = (TextView)itemView.findViewById(R.id.search_tv_name);
        }
    }
}
