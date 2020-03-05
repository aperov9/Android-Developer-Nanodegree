package com.example.android.popularmovies.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.DetailsActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context cx;
    private ArrayList<Movie> movieArrayList;


    //CONSTRUCTOR
    public MovieAdapter(Context cx, ArrayList<Movie> movieArrayList) {
        this.cx = cx;
        this.movieArrayList = movieArrayList;
    }

    //FIRST TIME CREATION
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cx).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    //BINDING DATA TO VIEW
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie currentMovie = movieArrayList.get(holder.getAdapterPosition());

        //Picasso.get().load(movieArrayList.get(position).getImage_location()).placeholder(R.drawable.placeholder).into(holder.picture);
        Picasso.get().load(movieArrayList.get(holder.getAdapterPosition()).getImage_location()).into(holder.picture);

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cx, DetailsActivity.class);
                intent.putExtra("currentMovie",currentMovie);
                cx.startActivity(intent);
            }
        });
    }

    //NUMBER OF ITEMS
    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    //INNER CLASS VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picture;

        public ViewHolder(View itemView) {
            super(itemView);

            picture =  itemView.findViewById(R.id.movie_image);

        }
    }

}
