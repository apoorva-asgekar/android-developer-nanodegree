package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage1.Movie;
import com.example.android.popularmoviesstage1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apoorva on 4/5/17.
 * Class which converts JSON strings to an ArryList of Movie Objects.
 */

public final class MovieJsonUtils {
    private final static String LOG_TAG = MovieJsonUtils.class.getSimpleName();

    private final static String JSON_PAGE = "page";
    private final static String JSON_RESULTS = "results";
    private final static String JSON_TOTAL_PAGES = "total_pages";
    private final static String JSON_POSTER_PATH = "poster_path";
    private final static String JSON_MOVIE_ID = "id";
    private final static String JSON_MOVIE_TITLE = "title";
    private final static String JSON_MOVIE_PLOT = "overview";
    private final static String JSON_MOVIE_USER_RATING = "vote_average";
    private final static String JSON_MOVIE_RELEASE_DATE = "release_date";

    public static List<Movie> getMovieDetails(String requestUrl)
            throws JSONException {

        List<Movie> movieResultsArrayList = new ArrayList<Movie>();

        //Getting response from page 1 of the API to get the total number of pages.
        String pageOneResponse = getMoviesFromMovieDb(requestUrl, "1");

        JSONObject movieResponseObject = new JSONObject(pageOneResponse);
        int totalPages = movieResponseObject.getInt(JSON_TOTAL_PAGES);

        //Currently assuming that only the top 20 top_rated and popular movies are being displayed.

        JSONArray resultsArray = movieResponseObject.getJSONArray(JSON_RESULTS);
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);

            int movieId = resultObject.getInt(JSON_MOVIE_ID);
            String movieTitle = resultObject.getString(JSON_MOVIE_TITLE);
            String posterPath = resultObject.getString(JSON_POSTER_PATH);
            String moviePlot = resultObject.getString(JSON_MOVIE_PLOT);
            Float movieUserRating =
                    Float.valueOf((float) resultObject.getDouble(JSON_MOVIE_USER_RATING));
            String movieReleaseDate = resultObject.getString(JSON_MOVIE_RELEASE_DATE);

            Movie newMovie = new Movie(movieId, movieTitle, posterPath, moviePlot, movieUserRating,
                    movieReleaseDate);
            movieResultsArrayList.add(newMovie);

        }
        return movieResultsArrayList;
    }

    public static String getMoviesFromMovieDb(String requestUrlString, String pageNumber)
            throws JSONException {

        URL requestUrl = NetworkUtils.buildUrl(requestUrlString, pageNumber);
        String responseStr = null;
        try {
            responseStr = NetworkUtils.getResponseFromUrl(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOEXception while querying the API.", e);
        }

        return responseStr;
    }

    public static String getCompletePosterLink(String posterPath) {
        return NetworkUtils.IMAGE_BASE_URL + NetworkUtils.IMAGE_FILE_SIZE
                + posterPath;
    }

    public static String getRequestUrlWithPreference(String sortOrder, Context context) {
        String requestUrlString = NetworkUtils.API_BASE_URL;
        String prefPopular = context.getResources().getString(R.string.pref_sort_popular_value);
        String prefRatings = context.getResources().getString(R.string.pref_sort_ratings_value);
        if (sortOrder.equals(prefPopular)) {
            requestUrlString += NetworkUtils.API_POPULAR_ENDPOINT;
        } else if (sortOrder.equals(prefRatings)) {
            requestUrlString += NetworkUtils.API_TOP_RATED_ENDPOINT;
        } else {
            return null;
        }
        return requestUrlString;
    }

}
