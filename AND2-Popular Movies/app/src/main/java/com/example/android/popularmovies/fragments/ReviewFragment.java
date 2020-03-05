package com.example.android.popularmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataExtraction;
import com.example.android.popularmovies.models.Review;

import java.util.List;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Review>> {

    private int movie_id;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static ReviewFragment newInstance(int id) {
        ReviewFragment reviewFragment = new ReviewFragment();

        Bundle args = new Bundle();
        args.putSerializable("movie_id", id);
        reviewFragment.setArguments(args);

        return reviewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_fragment,null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_reviews);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        movie_id = (Integer) getArguments().getSerializable("movie_id");

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(777,null,this);


        return view;
    }

    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ReviewAsyncTaskLoader(getActivity(),movie_id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
        recyclerView.setAdapter(new ReviewAdapter(data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }

    private static class ReviewAsyncTaskLoader extends AsyncTaskLoader<List<Review>>{

        private int movie_id;
        private List<Review> reviews = null;

        private ReviewAsyncTaskLoader(@NonNull Context context, int id) {
            super(context);
            this.movie_id = id;
        }

        @Nullable
        @Override
        public List<Review> loadInBackground() {
            return DataExtraction.getReviewByMovieID(movie_id);
        }

        @Override
        protected void onStartLoading() {
            if (reviews != null) {
                // Use cached data
                deliverResult(reviews);
            } else {
                forceLoad();
            }
        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

        private List<Review> reviews;

        private ReviewAdapter(List<Review> reviews) {
            this.reviews = reviews;
        }


        @NonNull
        @Override
        public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.review_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
            holder.author.setText(reviews.get(holder.getAdapterPosition()).getAuthor()+":");
            holder.content.setText(reviews.get(holder.getAdapterPosition()).getReview());

        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView author, content;

            public ViewHolder(View itemView) {
                super(itemView);

                author =  itemView.findViewById(R.id.review_author);
                content =  itemView.findViewById(R.id.review_content);

            }
        }
    }
}
