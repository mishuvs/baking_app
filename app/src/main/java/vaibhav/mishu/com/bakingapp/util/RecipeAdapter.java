package vaibhav.mishu.com.bakingapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vaibhav.mishu.com.bakingapp.DetailActivity;
import vaibhav.mishu.com.bakingapp.R;
import vaibhav.mishu.com.bakingapp.StepActivity;
import vaibhav.mishu.com.bakingapp.StepFragment;

/**
 * Created by Vaibhav on 12/6/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemHolder> implements StepFragment.StepInterface {

    private JsonUtil.Recipe recipe;
    private Context mContext;

    public RecipeAdapter(Context context){
        mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        switch (position){

            case 0:
                holder.text.setText("Ingredients");
                break;

            default: holder.text.setText(recipe.steps.get(position-1).shortDescription);

        }

    }

    @Override
    public int getItemCount() {
        int count;
        if(recipe==null) count=0;
        else count = recipe.steps.size() + 1;
        return count;
    }

    public void swap(JsonUtil.Recipe newRecipe)
    {
        if(newRecipe!=null){
            recipe = newRecipe;
            notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView text;

        ItemHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != 0){
                onStepSelected(getAdapterPosition()-1);
            }
        }
    }

    @Override
    public void onStepSelected(int position) {
        StepFragment displayFrag = (StepFragment) ((DetailActivity) mContext).getFragmentManager()
                .findFragmentById(R.id.step_fragment);
        if (displayFrag == null) {
            // DisplayFragment (Fragment B) is not in the layout (handset layout),
            // so start DisplayActivity (Activity B)
            // and pass it the info about the selected item
            Intent i = new Intent(mContext, StepActivity.class);
            i.putExtra("recipe",recipe);
            i.putExtra("index",position);
            mContext.startActivity(i);
        } else {
            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            Bundle recipeInfo = new Bundle();
            recipeInfo.putSerializable("recipe",recipe);
            recipeInfo.putInt("index",position);
            displayFrag.updateContent(recipeInfo);
        }
    }

}
