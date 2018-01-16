package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import vaibhav.mishu.com.bakingapp.util.FetchRecipes;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class MainActivity extends AppCompatActivity {

    static ArrayList<JsonUtil.Recipe> recipes;
    @BindView(R.id.recipe_list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Recipe List");

        if (savedInstanceState == null) recipes = FetchRecipes.getRecipes(this);
        else recipes = (ArrayList<JsonUtil.Recipe>) savedInstanceState.getSerializable("recipeList");


        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        RecipeListAdapter adapter = new RecipeListAdapter();
        if (isTablet) {
            GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 4);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("recipeList", recipes);
        super.onSaveInstanceState(outState);
    }


    private class RecipeListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RecipeHolder recipeHolder = (RecipeHolder)holder;
            recipeHolder.recipeName.setText(recipes.get(position).name);
            if(recipes.get(position).imageUrl!=null && !recipes.get(position).imageUrl.isEmpty()){
                Picasso.with(getApplicationContext()).load(recipes.get(position).imageUrl).into(recipeHolder.recipeImage);
            }
            else recipeHolder.recipeImage.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return (recipes != null) ? recipes.size() : 0;
        }

        class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView recipeName;
            ImageView recipeImage;

            RecipeHolder(View itemView) {
                super(itemView);
                recipeName = itemView.findViewById(R.id.recipe_name);
                recipeImage = itemView.findViewById(R.id.recipe_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (recipes.size() == 0 || recipes == null) {
                    Toast.makeText(MainActivity.this, "Something went wrong! :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("recipe", recipes.get(getAdapterPosition()));
                startActivity(i);
            }
        }
    }

}
