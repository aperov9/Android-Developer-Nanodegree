package com.example.android.bakingapp.network;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.network.Utilities.createUrl;
import static com.example.android.bakingapp.network.Utilities.makeHttpRequest;

public class JsonParsing {

    private static final String LOG_TAG = JsonParsing.class.getName();

    public static List<Recipe> fetchRecipes(String stringUrl) {

        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractRecipesFromJson(jsonResponse);
    }

    private static List<Recipe> extractRecipesFromJson(String baseJSON) {

        if (TextUtils.isEmpty(baseJSON)) {
            return null;
        }

        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(baseJSON);

            for(int i= 0; i< jsonArray.length(); i++){
                JSONObject recipeBase = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(recipeBase.getString("id"));
                String name = recipeBase.getString("name");
                int servings = Integer.parseInt(recipeBase.getString("servings"));

                //INGREDIENTS
                List<Ingredient> ingredientList = new ArrayList<>();
                JSONArray ingredients = recipeBase.getJSONArray("ingredients");

                for(int j = 0; j < ingredients.length(); j++){
                    JSONObject currentIngredient = ingredients.getJSONObject(j);

                    double quantity = Double.parseDouble(currentIngredient.getString("quantity"));
                    String measure = currentIngredient.getString("measure");
                    String ingredient_name = currentIngredient.getString("ingredient");

                    ingredientList.add(new Ingredient(ingredient_name,quantity,measure));
                }

                //STEPS
                List<Step> stepList = new ArrayList<>();
                JSONArray steps = recipeBase.getJSONArray("steps");

                for(int j = 0; j < steps.length(); j++){
                    JSONObject currentStep = steps.getJSONObject(j);

                    int step_id = Integer.parseInt(currentStep.getString("id"));
                    String shortDescription = currentStep.getString("shortDescription");
                    String description = currentStep.getString("description");
                    String url = currentStep.getString("videoURL");

                    stepList.add(new Step(step_id,shortDescription,description,url));
                }

                //CREATE RECIPE AND ADD TO LIST
                recipes.add(new Recipe(id,name,servings,ingredientList,stepList));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON", e);
        }

        return recipes;
    }
}
