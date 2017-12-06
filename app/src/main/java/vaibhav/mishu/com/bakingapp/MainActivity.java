package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;

import vaibhav.mishu.com.bakingapp.util.JsonUtil;
import vaibhav.mishu.com.bakingapp.util.NetworkUtil;

public class MainActivity extends AppCompatActivity {

    static ArrayList<JsonUtil.Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetRecipesTask task = new GetRecipesTask(this);
        task.execute();

        final ArrayList<String> recipeNames = new ArrayList<>();
        recipeNames.add("Nutella Pie");
        recipeNames.add("Brownies");
        recipeNames.add("Yellow Cake");
        recipeNames.add("Cheese Cake");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.recipe_item, R.id.recipe_name);
        adapter.addAll(recipeNames);
        ListView listView = findViewById(R.id.recipe_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(recipes!=null){
                    Intent i = new Intent(MainActivity.this,DetailActivity.class);
                    i.putExtra("recipe",recipes.get(position));
                    startActivity(i);
                }
                Log.i("haha","recipes is this: " + recipes);
            }
        });
    }

    static class GetRecipesTask extends AsyncTask<Void,Void,ArrayList<JsonUtil.Recipe>>{

        private WeakReference<MainActivity> activityReference;

        GetRecipesTask(MainActivity context){
            activityReference = new WeakReference<>(context);
        }

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
             Log.i("haha",recipes + "");
        }
    }
}
