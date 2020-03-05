package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.sweet.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.Constants.ARG_RECIPE;
import static com.example.android.bakingapp.Constants.ARG_RECIPE_ID;
import static com.example.android.bakingapp.Constants.ARG_STEP;


public class StepListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.step_list) RecyclerView stepListRV;
    @BindView(R.id.step_ingredients) RecyclerView ingredientsRV;

    private boolean mTwoPane;
    private Recipe mRecipe;
    private String mRecipe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);


        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        if(getIntent().hasExtra(ARG_RECIPE) && getIntent().hasExtra(ARG_RECIPE_ID)){
            mRecipe = (Recipe) getIntent().getExtras().getSerializable(ARG_RECIPE);
            mRecipe_id = getIntent().getExtras().getString(ARG_RECIPE_ID);
        }else {
            return;
        }

        toolbar.setTitle(mRecipe.getmName());
        setSupportActionBar(toolbar);

        stepListRV.setNestedScrollingEnabled(false);
        stepListRV.setAdapter(new StepsRVAdapter(this, mRecipe, mTwoPane));

        ingredientsRV.setNestedScrollingEnabled(false);
        ingredientsRV.setAdapter(new IngredientsAdapter(mRecipe.getmIngredients()));
    }


    public static class StepsRVAdapter extends RecyclerView.Adapter<StepListActivity.StepsRVAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private Recipe mRecipe;
        private final boolean mTwoPane;

        private StepsRVAdapter(StepListActivity parent, Recipe recipe, boolean twoPane) {
            mRecipe = recipe;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public StepListActivity.StepsRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
            return new StepsRVAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final StepListActivity.StepsRVAdapter.ViewHolder holder, int position) {
            final int currentPosition = holder.getAdapterPosition();
            holder.name.setText(mRecipe.getmSteps().get(currentPosition).getmShortDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*
                    set different clicks actions for different layouts
                    if its a phone -> onClick open new Activity
                    if its a tablet -> onClick replace Fragment on the right side
                    */
                    Step currentStep = mRecipe.getmSteps().get(currentPosition);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(ARG_RECIPE, mRecipe);
                        arguments.putSerializable(ARG_STEP, currentStep);
                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, fragment).commit();

                    } else {
                        Intent intent = new Intent(mParentActivity, StepDetailActivity.class);
                        intent.putExtra(ARG_RECIPE, mRecipe);
                        intent.putExtra(ARG_STEP, currentStep);

                        mParentActivity.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecipe.getmSteps().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.content) TextView name;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this,view);
            }
        }
    }

    public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.VH>{

        private List<Ingredient> ingredientList;

        public IngredientsAdapter(List<Ingredient> ingredientList) {
            this.ingredientList = ingredientList;
        }

        @NonNull
        @Override
        public IngredientsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StepListActivity.this).inflate(R.layout.ingredient_item,parent,false);
            return new IngredientsAdapter.VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientsAdapter.VH holder, int position) {

            StringBuilder stringBuilder = new StringBuilder();

            String quantity = String.valueOf(ingredientList.get(holder.getAdapterPosition()).getmQuantity());
            if (quantity.endsWith(".0")){
                Double d = ingredientList.get(holder.getAdapterPosition()).getmQuantity();
                stringBuilder.append(d.intValue());
            }else {
                stringBuilder.append(quantity);
            }
            stringBuilder.append(" ");
            stringBuilder.append(ingredientList.get(holder.getAdapterPosition()).getmMeasure());

            holder.unit.setText(stringBuilder.toString());

            holder.ingredient.setText(ingredientList.get(holder.getAdapterPosition()).getmName());
        }

        @Override
        public int getItemCount() {
            return ingredientList.size();
        }

        public class VH extends RecyclerView.ViewHolder{
            @BindView(R.id.ingredient_unit) TextView unit;
            @BindView(R.id.ingredient_name) TextView ingredient;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
