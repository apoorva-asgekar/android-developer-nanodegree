package com.example.android.popularmoviesstage1.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage1.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by apoorva on 4/4/17.
 * Class used to connect to the MovieDb API and fetch data being displayed by the App.
 */

public final class NetworkUtils {
    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();

    public final static String API_BASE_URL = "https://api.themoviedb.org/3/";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_FILE_SIZE = "w185/";

    public final static String API_POPULAR_ENDPOINT = "movie/popular";
    public final static String API_TOP_RATED_ENDPOINT = "movie/top_rated";

    private final static String API_KEY = "";

    private final static String QUERY_PARAM_API_KEY = "api_key";
    private final static String QUERY_PARAM_PAGE = "page";

    /** Returns a java URL object needed to make the network call to the MovieDb API.
     *
     * @param requestQuery - String form of the url
     * @return URL - java object of type URL
     */
    public static URL buildUrl(String requestQuery, String pageNumber) {
        Uri builtUri = Uri.parse(requestQuery).buildUpon()
                .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_PARAM_PAGE, pageNumber)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed URL Excpetion in buildUrl", e);
        }
        return url;
    }

    /** Connects to the MovieDb API and gets the response to the Http request.
     *
     * @param url - URL for the API request
     * @return String which contains the API response
     * @throws IOException
     */
    public static String getResponseFromUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String resultJson = null;

        try {
            if (url != null) {
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            if (urlConnection != null) {
                inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");

                    if (scanner.hasNext()) {
                        resultJson = scanner.next();
                    }
                }
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return resultJson;
                } else {
                    Log.e(LOG_TAG, "Http request returned with an error code: " + resultJson);
                }
            }
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
