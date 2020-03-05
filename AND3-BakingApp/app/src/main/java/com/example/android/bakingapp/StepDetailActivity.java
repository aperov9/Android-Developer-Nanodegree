package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.sweet.bakingapp.R;

import static com.example.android.bakingapp.Constants.ARG_RECIPE;
import static com.example.android.bakingapp.Constants.ARG_STEP;

// -> only for phones
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Create the detail fragment and add it to the activity
        //if fragment was created previously (e.g.portrait) there is no need to create new one
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(ARG_STEP, getIntent().getSerializableExtra(ARG_STEP));
            arguments.putSerializable(ARG_RECIPE, getIntent().getSerializableExtra(ARG_RECIPE));

            StepDetailFragment fragment = new StepDetailFragment();
            //setArguments = alternative to fragment newInstance(arguments)
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().add(R.id.step_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
