package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vaibhav.mishu.com.bakingapp.util.JsonUtil;
import vaibhav.mishu.com.bakingapp.util.RecipeAdapter;

/**
 * Created by Vaibhav on 1/1/2018.
 */

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent i = getActivity().getIntent();
        JsonUtil.Recipe recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");

        ((TextView) getView().findViewById(R.id.title_recipe_name)).setText(recipe.name);

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        RecipeAdapter adapter = new RecipeAdapter(getActivity());
        adapter.swap(recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
