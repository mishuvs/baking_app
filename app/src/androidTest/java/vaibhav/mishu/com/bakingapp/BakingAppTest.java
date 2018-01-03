package vaibhav.mishu.com.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Vaibhav on 1/3/2018.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BakingAppTest {

    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    /**
     * Clicks on a ListView item and checks it opens up the DetailActivity with the correct details.
     */
    @Test
    public void checkRecipeNameCorrect() {

        onData(anything()).inAdapterView(withId(R.id.recipe_list))
                .atPosition(1)
                .onChildView(withId(R.id.recipe_name))
                .check(matches(withText(RECIPE_NAME)));

        onData(anything()).inAdapterView(withId(R.id.recipe_list))
                .atPosition(1)
                .perform(click());

        /*Something like this can be used for recyclerview
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
         */

    }

    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */
    @Test
    public void clickRecipeListViewItem_OpensDetailActivity() {

        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // gridview item and clicks it.
        onData(anything()).inAdapterView(withId(R.id.recipe_list)).atPosition(1).perform(click());

        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withId(R.id.title_recipe_name)).check(matches(withText(RECIPE_NAME)));

    }
}
