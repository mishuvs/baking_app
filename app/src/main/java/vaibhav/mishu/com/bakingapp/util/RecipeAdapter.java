package vaibhav.mishu.com.bakingapp.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vaibhav.mishu.com.bakingapp.DetailActivity;
import vaibhav.mishu.com.bakingapp.IngredientsActivity;
import vaibhav.mishu.com.bakingapp.IngredientsFragment;
import vaibhav.mishu.com.bakingapp.R;
import vaibhav.mishu.com.bakingapp.StepActivity;
import vaibhav.mishu.com.bakingapp.StepFragment;

/**
 * Created by Vaibhav on 12/6/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemHolder> implements StepFragment.StepInterface {

    private JsonUtil.Recipe recipe;
    private Context mContext;

    private FragmentManager fm;
    private Boolean isSinglePane = true;
    public Boolean shouldInitiateIngredients = true;
    private Fragment displayFrag;
    private View frag_container;

    public RecipeAdapter(Context context){
        mContext = context;
        // get fragment manager
        fm = ((DetailActivity) mContext).getFragmentManager();
        frag_container = ((DetailActivity) mContext).findViewById(R.id.step_fragment);
        if(frag_container!=null) isSinglePane = false;
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

            //initiate ingredients_fragment if not single-pane
            if(!isSinglePane && shouldInitiateIngredients){
                initiateIngredientsFrag();
            }
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
            displayFrag = fm.findFragmentById(R.id.step_fragment);
            if(getAdapterPosition() != 0){
                onStepSelected(getAdapterPosition()-1);
            }
            else{
                if(isSinglePane){
                    Intent i = new Intent(mContext, IngredientsActivity.class);
                    i.putExtra("recipe",recipe);
                    mContext.startActivity(i);
                }
                else{
                    //if fragment not already ingredients fragment:
                    if( !(displayFrag instanceof IngredientsFragment) ) initiateIngredientsFrag();
                }
            }
        }
    }

    private void initiateIngredientsFrag(){
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putSerializable("recipe", recipe);
        ingredientsFragment.setArguments(args);

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.step_fragment, ingredientsFragment);
        ft.commit();
    }

    @Override
    public void onStepSelected(int position) {
        if (isSinglePane) {
            // DisplayFragment (Fragment B) is not in the layout (handset layout),
            // so start DisplayActivity (Activity B)
            // and pass it the info about the selected item
            Intent i = new Intent(mContext, StepActivity.class);
            i.putExtra("recipe",recipe);
            i.putExtra("index",position);
            mContext.startActivity(i);
        } else {
            //if fragment not already step fragment:
            if(!(displayFrag instanceof StepFragment))
            {
                Log.i("haha","not an instance of step fragment");
                // replace
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.step_fragment,  new StepFragment());
                ft.commit();
                fm.executePendingTransactions();
                displayFrag = fm.findFragmentById(R.id.step_fragment);

                Log.i("haha","display frag is stepfrag ? " + (displayFrag instanceof StepFragment));
            }

            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            Bundle recipeInfo = new Bundle();
            recipeInfo.putSerializable("recipe",recipe);
            recipeInfo.putInt("index",position);
            ((StepFragment) displayFrag).updateContent(recipeInfo);
        }
    }

}
