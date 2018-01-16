package vaibhav.mishu.com.bakingapp.util;

import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vaibhav on 12/5/2017.
 * To parse the json
 */

public class JsonUtil {

    private static final String LOG_TAG = JsonUtil.class.getSimpleName();

    public static ArrayList<Recipe> jsonStingToRecipes(String jsonResponse) {
        JSONArray rootJsonArray, ingredientsArray, stepsArray;
        JSONObject recipeJson, ingredientJson, stepJson;
        ArrayList<String> ingredientList;
        ArrayList<Step> stepList;
        String recipeName, recipeImageString;
        int recipeServings;
        ArrayList<Recipe> recipesList = new ArrayList<Recipe>();

        try {
            rootJsonArray = new JSONArray(jsonResponse);
            for(int i=0; i<rootJsonArray.length(); i++){ //loop through recipes list

                ingredientList = new ArrayList<String>();
                stepList = new ArrayList<Step>();

                recipeJson = rootJsonArray.getJSONObject(i);
                ingredientsArray = recipeJson.getJSONArray("ingredients");
                stepsArray = recipeJson.getJSONArray("steps");

                for (int j=0; j<ingredientsArray.length(); j++){ //loop through INGREDIENTS
                    ingredientJson = ingredientsArray.getJSONObject(j);
                    ingredientList.add(
                            ingredientJson.getString("ingredient") + " (" +
                                    ingredientJson.getString("quantity") + " " +
                                    ingredientJson.getString("measure") + ")"
                    );
                }
                for (int j=0; j<stepsArray.length(); j++){ //loop through STEPS
                    stepJson = stepsArray.getJSONObject(j);
                    stepList.add(new Step(
                            stepJson.getString("shortDescription"),
                            stepJson.getString("description"),
                            stepJson.getString("videoURL"),
                            stepJson.getString("thumbnailURL")
                    ));
                }

                recipeName = recipeJson.getString("name");
                recipeServings = recipeJson.getInt("servings");
                recipeImageString = recipeJson.getString("image");

                //add to recipes list:
                recipesList.add(new Recipe(recipeName, ingredientList, stepList, recipeServings, recipeImageString));

            }

            return recipesList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem converting to JSON array: " + e);
            e.printStackTrace();
        }
        return null;
    }

    public static class Recipe implements Serializable{
        public String name;
        public ArrayList<String> ingredients;
        public ArrayList<Step> steps;
        public int servings;
        public String imageUrl;

        Recipe(String recipeName, ArrayList<String> recipeIngedients, ArrayList<Step> recipeSteps, int serves, String imageUrl){
            name = recipeName;
            ingredients = recipeIngedients;
            steps = recipeSteps;
            servings = serves;
        }
    }

    public static class Step implements Serializable {
        public String shortDescription, description, videoURL, thumbnailURL;

        Step(String shDescr, String descr, String video, String thumb){
            shortDescription = shDescr;
            description = descr;
            videoURL = video;
            thumbnailURL = thumb;
        }
    }

}