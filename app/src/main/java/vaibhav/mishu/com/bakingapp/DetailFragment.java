package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;
import vaibhav.mishu.com.bakingapp.util.RecipeAdapter;

/**
 * Created by Vaibhav on 1/1/2018.
 */

public class DetailFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent i = getActivity().getIntent();
        JsonUtil.Recipe recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");

        ((TextView) getView().findViewById(R.id.title_recipe_name)).setText(recipe.name);
        RecipeAdapter adapter = new RecipeAdapter(getActivity());
        if(savedInstanceState!=null) {adapter.shouldInitiateIngredients = false;}
        adapter.swap(recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("BUNDLE_RECYCLER_LAYOUT");
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("BUNDLE_RECYCLER_LAYOUT", recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
