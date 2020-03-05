package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getName();

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = new Sandwich();

        try {
            JSONObject jsonBaseObject = new JSONObject(json);

            sandwich.setPlaceOfOrigin(jsonBaseObject.getString("placeOfOrigin"));
            sandwich.setDescription(jsonBaseObject.getString("description"));
            sandwich.setImage(jsonBaseObject.getString("image"));

            JSONObject nameObj = jsonBaseObject.getJSONObject("name");
                sandwich.setMainName(nameObj.getString("mainName"));
                JSONArray alsoKnownAs = nameObj.getJSONArray("alsoKnownAs");
                    ArrayList<String> alsoKnownAsList = new ArrayList<>();
                    if (alsoKnownAs.length() > 0) {
                        for (int i = 0; i < alsoKnownAs.length(); i++) {
                            alsoKnownAsList.add(alsoKnownAs.getString(i));
                        }
                    }
                    sandwich.setAlsoKnownAs(alsoKnownAsList);


            JSONArray ingredients = jsonBaseObject.getJSONArray("ingredients");
                ArrayList<String> ingredientsList = new ArrayList<>();
                if (ingredients.length() > 0) {
                    //why does JSONArray not support enhanced for loops?
                    for (int i = 0; i < ingredients.length(); i++) {
                        ingredientsList.add(ingredients.getString(i));
                    }
                }
                sandwich.setIngredients(ingredientsList);


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON", e);
        }

        return sandwich;
    }
}
