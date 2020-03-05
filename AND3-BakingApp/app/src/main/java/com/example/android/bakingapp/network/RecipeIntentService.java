package com.example.android.bakingapp.network;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.example.android.bakingapp.RecipeWidget;
import com.example.android.bakingapp.models.Recipe;

import java.util.List;

public class RecipeIntentService extends IntentService {
    public static final String ACTION_GET_RECIPE = "com.example.android.bakingapp.network.action.get_recipe";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    public static void startRecipeIntentService(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_GET_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RECIPE.equals(action)) {
                getRecipe();
            }
        }
    }

    private void getRecipe() {
        List<Recipe> recipes= JsonParsing.fetchRecipes(RecipeLoader.sourceStringUrl);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,RecipeWidget.class));

        //TO-DO implement random number generator with recipes.size()
        RecipeWidget.updateRecipe(this,appWidgetManager,appWidgetIds,recipes.get(0));
    }

}
