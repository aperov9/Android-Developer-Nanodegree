package com.example.android.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.network.RecipeLoader;
import com.example.android.bakingapp.network.Utilities;
import com.example.sweet.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.Constants.ARG_RECIPE;
import static com.example.android.bakingapp.Constants.ARG_RECIPE_ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recipes_list) RecyclerView recyclerView;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        isTablet = getResources().getBoolean(R.bool.isTablet);

        LoaderManager loaderManager = getSupportLoaderManager();

        if(Utilities.isNetworkOnline(getApplicationContext())) {
            loaderManager.initLoader(0, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecipeLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        if(isTablet){
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }
        recyclerView.setAdapter(new RecipeRVAdapter(data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) { }


    public static class RecipeRVAdapter extends RecyclerView.Adapter<RecipeRVAdapter.ViewHolder> {

        private List<Recipe> mRecipes;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe currentRecipe = (Recipe) view.getTag();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepListActivity.class);
                    intent.putExtra(ARG_RECIPE_ID, String.valueOf(currentRecipe.getmId()));
                    intent.putExtra(ARG_RECIPE, currentRecipe);

                    context.startActivity(intent);
                }

        };

        private RecipeRVAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.name.setText(mRecipes.get(holder.getAdapterPosition()).getmName());

            //setTag(Object object) attach current item to a view
            //getTag(Object object) retrieve it via view (inside onClick)
            holder.itemView.setTag(mRecipes.get(holder.getAdapterPosition()));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.content) TextView name;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }
    }
}

