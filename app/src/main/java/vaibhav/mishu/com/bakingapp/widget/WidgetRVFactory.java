package vaibhav.mishu.com.bakingapp.widget;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import vaibhav.mishu.com.bakingapp.R;
import vaibhav.mishu.com.bakingapp.util.FetchRecipes;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

/**
 * Created by Vaibhav on 12/9/2017.
 * RemoteViewsFactory for widget
 */

public class WidgetRVFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private static ArrayList<JsonUtil.Recipe> recipes;
    private static JsonUtil.Recipe currentRecipe;
    private static int recipeIndex;

    WidgetRVFactory(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        recipes = FetchRecipes.getRecipes(mContext);
    }

    //called on start and when widget updated
    //to update the widget manually --> notifyAppWidgetViewData change is similar to the notifyDataSetChanged in adapter.
    @Override
    public void onDataSetChanged() {
        //start an intent service here in order to fetch the results.
        recipeIndex = PreferenceManager.getDefaultSharedPreferences(mContext).getInt("widgetRecipeIndex",0);
        currentRecipe = recipes.get(recipeIndex);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return currentRecipe.steps.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Construct a remote views item based on the app widget item XML file,
        // and set the text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.item, currentRecipe.steps.get(position).shortDescription);

        //set Fill-In-Intent:
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("index",position);
        rv.setOnClickFillInIntent(R.id.item, fillInIntent);

        // Return the remote views object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
