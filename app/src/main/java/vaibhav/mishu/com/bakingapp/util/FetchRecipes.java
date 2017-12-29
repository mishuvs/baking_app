package vaibhav.mishu.com.bakingapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import vaibhav.mishu.com.bakingapp.MainActivity;

/**
 * Created by Vaibhav on 12/29/2017.
 */

public class FetchRecipes {

    static ArrayList<JsonUtil.Recipe> recipes;
    private static String TAG = FetchRecipes.class.getSimpleName();

    public static ArrayList<JsonUtil.Recipe> getRecipes(Context context){
        GetRecipesTask task = new GetRecipesTask();
        try {
            recipes = task.execute().get();
            return recipes;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "exception occurred" );
        return null;
    }

    static class GetRecipesTask extends AsyncTask<Void,Void,ArrayList<JsonUtil.Recipe>> {

        @Override
        protected ArrayList<JsonUtil.Recipe> doInBackground(Void... voids) {

            ArrayList<JsonUtil.Recipe> newRecipes = new ArrayList<>();

            try {
                newRecipes = JsonUtil.jsonStingToRecipes(
                        NetworkUtil.getResponseFromHttpUrl(NetworkUtil.buildURL())
                );
                return newRecipes;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newRecipes;
        }

        @Override
        protected void onPostExecute(ArrayList<JsonUtil.Recipe> newRecipes) {
            super.onPostExecute(newRecipes);
            recipes = newRecipes;
        }
    }

}
