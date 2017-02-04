package com.popularmovies.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.popularmovies.R;
import com.popularmovies.models.Movie;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Psych on 7/10/16.
 */
public class Util {

    /**
     * Lazy loading/Caching
     * @return a Bitmap
     */
    public static void fetchBitmap(Movie movie, ImageView imageView) {
        FetchMoviePoster fetchMoviePoster = new FetchMoviePoster(imageView);
        fetchMoviePoster.execute(movie);
    }

    public static String getPosterUrl(String posterRelativePath) {
        // image.tmdb.org/t/p/
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w185")
                        .appendPath(posterRelativePath).build();
        return builder.toString();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * http://api.themoviedb.org/3/movie/129/videos?api_key=YourApiKey
     * Will be using "/movie/{id}/videos" instead
     * Returns the url
     * @return
     */
    public static String getMovieTrailersUrl(Context context, int movieId) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(String.valueOf(movieId))
                        .appendPath("videos")
                        .appendQueryParameter("api_key", context.getString(R.string.api_key));

        return builder.build().toString();
    }

    /**
     * http://api.themoviedb.org/3/movie/129/reviews?api_key=YourApiKey
     * Will be using "/movie/{id}/reviews" instead
     * Returns the url
     * @return
     */
    public static String getMovieReviewsUrl(Context context, int movieId) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(String.valueOf(movieId))
                        .appendPath("reviews")
                        .appendQueryParameter("api_key", context.getString(R.string.api_key));

        return builder.build().toString();
    }


    /**
     *
     * http://api.themoviedb.org/3/discover/movie?api_key=YourApiKey&sort_by=popularity.desc
     *
     * Returns the url to the server being that we will hit to get the movies
     * @return
     */
    public static String getMovieServerUrl(Context context) {
        Uri.Builder builder = new Uri.Builder();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortBy = preferences.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value));

        builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(sortBy)
                        .appendQueryParameter("api_key", context.getString(R.string.api_key));

        return builder.build().toString();
    }

    public static String getMovieType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String movieType = preferences.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value));
        return movieType;
    }


}
