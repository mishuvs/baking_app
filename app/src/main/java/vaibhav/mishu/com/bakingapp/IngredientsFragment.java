package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import vaibhav.mishu.com.bakingapp.util.JsonUtil;

/**
 * Created by Vaibhav on 1/1/2018.
 */

public class IngredientsFragment extends Fragment {

    ArrayList<String> ingredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredient,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ingredients = ((JsonUtil.Recipe) bundle.getSerializable("recipe")).ingredients;
        }
        else {
            ingredients = ((JsonUtil.Recipe) getActivity().getIntent().getSerializableExtra("recipe")).ingredients;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.recipe_item, R.id.recipe_name);
        adapter.addAll(ingredients);
        ListView listView = (ListView) getView();
        listView.setAdapter(adapter);
    }
}
