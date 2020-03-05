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

public class SubscribedSubredditsAdapter extends RecyclerView.Adapter<SubscribedSubredditsAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mSubreddits;

    public SubscribedSubredditsAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.mSubreddits = list;
    }

    @NonNull
    @Override
    public SubscribedSubredditsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedSubredditsAdapter.ViewHolder holder, int position) {
        final String title = mSubreddits.get(holder.getAdapterPosition());

        holder.tv_title.setText("r/"+title);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ARG_SUBREDDIT,title);

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

        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title =  itemView.findViewById(R.id.search_tv_name);

        }
    }
}
