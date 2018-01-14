package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import vaibhav.mishu.com.bakingapp.util.FetchRecipes;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class MainActivity extends AppCompatActivity {

    static ArrayList<JsonUtil.Recipe> recipes;
    @BindView(R.id.recipe_list) ListView listView;
    @BindView(R.id.empty_view) View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<String> recipeNames = new ArrayList<>();

        recipes = FetchRecipes.getRecipes(this);

        if(recipes!=null && recipes.size()>0){
            for (JsonUtil.Recipe recipe: recipes
                 ) {
                recipeNames.add(recipe.name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.recipe_item, R.id.recipe_name);
        adapter.addAll(recipeNames);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(recipes.size()==0 || recipes==null) {
                    Toast.makeText(MainActivity.this,"Something went wrong! :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MainActivity.this,DetailActivity.class);
                i.putExtra("recipe",recipes.get(position));
                startActivity(i);
            }
        });
    }
}
