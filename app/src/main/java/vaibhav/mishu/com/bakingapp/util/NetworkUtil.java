package vaibhav.mishu.com.bakingapp.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Vaibhav on 12/5/2017.
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Builds the URL used to talk to the movie server.
     * @return The URL to use to query the tmdb server.
     */
    public static URL buildURL() {
        Uri builtUri = Uri.parse(BASE_URL);
        Uri finalUri = builtUri.buildUpon().build();

        Log.i(TAG,"This is the built uri: " + finalUri.toString());

        try {
            return new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            Log.i(TAG,"error buildUrl");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
