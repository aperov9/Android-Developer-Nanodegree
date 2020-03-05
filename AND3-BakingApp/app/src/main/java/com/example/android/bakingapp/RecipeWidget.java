package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.network.RecipeIntentService;
import com.example.sweet.bakingapp.R;


public class RecipeWidget extends AppWidgetProvider{


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        //set Recipe title
        views.setTextViewText(R.id.widget_recipe_name,recipe.getmName());

        //set Ingredients
        StringBuilder ingredientsString = new StringBuilder();
        for (Ingredient current : recipe.getmIngredients()) {
            ingredientsString.append(" - ");
            ingredientsString.append(current.getmMeasure());
            ingredientsString.append(" ");
            ingredientsString.append(current.getmQuantity());
            ingredientsString.append(" ");
            ingredientsString.append(current.getmName());
            ingredientsString.append("\n");
        }
        views.setTextViewText(R.id.widget_ingredients_list,ingredientsString);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeIntentService.startRecipeIntentService(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }
}

