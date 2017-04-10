package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.visibility;
import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.getMoviesFromMovieDb;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;

    private SharedPreferences sharedPreferences;

    private static final int LOADER_ID = 11;

    private static boolean prefUpdated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int orientation = GridLayout.VERTICAL;
        int span = 3;
        boolean reverseLayout = false;

        GridLayoutManager layoutManager = new GridLayoutManager(this, span, orientation, reverseLayout);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * If preferences have been updated only then start a new load.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(prefUpdated) {
            prefUpdated = false;
            invalidateData();
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    /**
     * Override the onDestroy method and unregister the change listener.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /** This method is to respond to clicks on each of the movie items in the Grid.
     *
     * @param movie - Movie Object containing the details of the selected movie poster.
     */
    @Override
    public void onClick(Movie movie) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        //TODOCOMPLETED - Add movie object to the intent.
        Bundle b = new Bundle();
        b.putParcelable("MOVIE", movie);
        detailActivityIntent.putExtra("movieBundle", b);
        startActivity(detailActivityIntent);
    }

    //Sets null data in the adapter
    private void invalidateData() { mMovieAdapter.setMovieData(null); }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> mListOfMovies;

            @Override
            protected void onStartLoading() {
                if(mListOfMovies != null) {
                    deliverResult(mListOfMovies);
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                List<Movie> listOfMovies = null;
                try {
                    //By default movies will be sorted by popularity.
                    String sortOrderPref = sharedPreferences
                            .getString(getString(R.string.pref_sort_key)
                                    , getString(R.string.pref_sort_popular_value));

                    String movieUrl = MovieJsonUtils.getRequestUrlWithPreference(sortOrderPref,
                            getContext());
                    if (isNetworkConnected(getContext())) {
                        listOfMovies = MovieJsonUtils.getMovieDetails(movieUrl);
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "JSONException thrown by the network request.", e);
                }
                return listOfMovies;
            }

            @Override
            public void deliverResult(List<Movie> data) {
                mListOfMovies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieData(movies);
        if (movies == null) {
            showErrorMessage();
        } else {
            showMovies();
        }
    }

    /**
     * Not using this method, but it needs to be overridden when implementing
     * loader callbacks interface.
     */
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent settingsActIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsActIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Setting the flag stating preferences have changed to true.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        prefUpdated = true;
    }

    //Check if internet connection is currently available.
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * This method will make the movies visible and the error message invisible.
     */
    private void showMovies() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie data.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
}
