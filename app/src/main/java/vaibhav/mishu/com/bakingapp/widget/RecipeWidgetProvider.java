package vaibhav.mishu.com.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import vaibhav.mishu.com.bakingapp.R;
import vaibhav.mishu.com.bakingapp.util.FetchRecipes;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static String ACTION_NEXT_RECIPE = "GET_NEXT_RECIPE";
    public static String ACTION_PREVIOUS_RECIPE = "GET_PREVIOUS_RECIPE";
    private static ArrayList<JsonUtil.Recipe> recipes;
    private static int recipeIndex;
    private static RemoteViews rv;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        recipes = FetchRecipes.getRecipes(context);

        // update each of the app widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {

            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, WidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // Instantiate the RemoteViews object for the app widget layout.
            rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(R.id.grid_view, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            rv.setEmptyView(R.id.grid_view, R.id.empty_view);

            recipeIndex = PreferenceManager.getDefaultSharedPreferences(context).getInt("widgetRecipeIndex",0);
            rv.setTextViewText(R.id.widget_recipe_name, recipes.get(recipeIndex).name);

            //next and previous button:
            Intent changeRecipe = new Intent(context, RecipeWidgetProvider.class);
            changeRecipe.setAction(ACTION_NEXT_RECIPE);
            PendingIntent nextRecipePI = PendingIntent.getBroadcast(context, 0, changeRecipe, 0);
            rv.setOnClickPendingIntent(R.id.next_recipe, nextRecipePI);
            changeRecipe.setAction(ACTION_PREVIOUS_RECIPE);
            PendingIntent previousRecipePI = PendingIntent.getBroadcast(context, 0, changeRecipe, 0);
            rv.setOnClickPendingIntent(R.id.previous_recipe, previousRecipePI);

            //
            // Do additional processing specific to this app widget...
            //

            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (intent.getAction().equals(ACTION_NEXT_RECIPE)) {
            if(recipeIndex < recipes.size() - 1) recipeIndex++;
            sharedPref.edit().putInt("widgetRecipeIndex",recipeIndex).apply();
        }
        else if(intent.getAction().equals(ACTION_PREVIOUS_RECIPE)){
            if(recipeIndex > 0) recipeIndex--;
            sharedPref.edit().putInt("widgetRecipeIndex",recipeIndex).apply();
        }
        rv.setTextViewText(R.id.widget_recipe_name, recipes.get(recipeIndex).name);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        //notify data set changed
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.grid_view);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
