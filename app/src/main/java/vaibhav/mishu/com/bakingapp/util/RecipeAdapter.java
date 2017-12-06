package vaibhav.mishu.com.bakingapp.util;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vaibhav.mishu.com.bakingapp.R;
import vaibhav.mishu.com.bakingapp.StepActivity;

/**
 * Created by Vaibhav on 12/6/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemHolder> {

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
                Intent i = new Intent(mContext, StepActivity.class);
                i.putExtra("recipe",recipe);
                i.putExtra("index",getAdapterPosition());
                mContext.startActivity(i);
            }
        }
    }
}
