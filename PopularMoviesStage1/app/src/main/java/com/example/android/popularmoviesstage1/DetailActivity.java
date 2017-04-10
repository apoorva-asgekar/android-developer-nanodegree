package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final static String LOG_TAG = DetailActivity.class.getSimpleName();

    private ImageView mPosterThumbnail;
    private TextView mMovieTitle;
    private TextView mMoviePlot;
    private TextView mMovieUserRating;
    private TextView mMovieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie currentMovie = null;

        final int THUMBSIZE = 64;



        mPosterThumbnail = (ImageView) findViewById(R.id.iv_poster_thumbnail);
        mMovieTitle = (TextView) findViewById(R.id.tv_title);
        mMoviePlot = (TextView) findViewById(R.id.tv_plot);
        mMovieUserRating = (TextView) findViewById(R.id.tv_user_rating);
        mMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Bundle b = getIntent().getBundleExtra("movieBundle");
        if(b != null) {
            currentMovie = b.getParcelable("MOVIE");
            Log.v(LOG_TAG, "Movie Title: " + currentMovie.getTitle());
            Log.v(LOG_TAG, "Movie Poster: " + String.valueOf(currentMovie.getPosterLink()));
            Log.v(LOG_TAG, "Movie Plot: " + currentMovie.getPlot());
            Log.v(LOG_TAG, "Movie User Rating: " + currentMovie.getUserRating());
            Log.v(LOG_TAG, "Movie Release Date: " + currentMovie.getReleaseDate());

            String moviePosterLink =
                    MovieJsonUtils.getCompletePosterLink(String.valueOf(currentMovie.getPosterLink()));
            Log.d(LOG_TAG, moviePosterLink);
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(moviePosterLink),
                    THUMBSIZE, THUMBSIZE);

            Picasso.with(this).load(moviePosterLink).into(mPosterThumbnail);
            mMovieTitle.setText(currentMovie.getTitle());
            mMoviePlot.setText(currentMovie.getPlot());
            mMovieUserRating.setText(String.valueOf(currentMovie.getUserRating()));
            mMovieReleaseDate.setText(currentMovie.getReleaseDate());
        }



    }
}
