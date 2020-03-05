package com.example.android.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataExtraction;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;

public class TrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movie>{

    private Movie currentMovie;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static TrailersFragment newInstance(Movie currentMovie) {
        TrailersFragment trailersFragment = new TrailersFragment();

        Bundle args = new Bundle();
        args.putSerializable("currentMovie", currentMovie);
        trailersFragment.setArguments(args);

        return trailersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trailers_fragment,null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_reviews);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // being forced to have an emtpy constructor seems a little bit off, any way around this?
        // or do i have to check all arguments from new instance for null?
        // do i have to save the state or something for recreation?
        currentMovie = (Movie) getArguments().getSerializable("currentMovie");

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(555,null,this);

        return view;
    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, @Nullable Bundle args) {
        return new TrailerGetter(getActivity(),currentMovie);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {
        recyclerView.setAdapter(new TrailerAdapter(data));
        Log.i("TRAILERS", data.toString());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {}


    private static class TrailerGetter extends AsyncTaskLoader<Movie> {

        private Movie movie;

        private TrailerGetter(@NonNull Context context, Movie movie) {
            super(context);
            this.movie = movie;
        }

        @Nullable
        @Override
        public Movie loadInBackground() {
            return DataExtraction.addTrailersToMovie(movie);
        }

        @Override
        protected void onStartLoading() {
            if (movie.getYoutubeLinks() != null) {
                deliverResult(movie);
            } else {
                forceLoad();
            }
        }
    }

    private class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

        private Movie movie;

        private TrailerAdapter(Movie movie) {
            this.movie = movie;
        }


        @NonNull
        @Override
        public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {
            final Trailer trailer = movie.getYoutubeLinks().get(holder.getAdapterPosition());
            holder.title.setText(trailer.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getLink()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + trailer.getLink()));
                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return movie.getYoutubeLinks().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);

                title =  itemView.findViewById(R.id.trailer_title);

            }
        }
    }
}
