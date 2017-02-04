package com.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.popularmovies.R;
import com.popularmovies.activities.DetailActivity;
import com.popularmovies.adapters.MoviesAdapter;
import com.popularmovies.data.MovieContract;
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;
import com.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Syed Ahmed Hussain on 7/9/16.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    public static final String LOG_TAG = MoviesFragment.class.getName();

    ProgressDialog progressDialog;
    MoviesAdapter moviesAdapter;
    GridView moviesGrid;

    public MoviesFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        moviesAdapter = new MoviesAdapter(getContext(), new ArrayList<Movie>());
        moviesGrid.setAdapter(moviesAdapter);

        moviesGrid.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_REFERRER, moviesAdapter.getItem(i));
//                Log.e(LOG_TAG, "Movie Faav : " + moviesAdapter.getItem(i).isFavorite());

                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.isOnline(getContext())) {
            updateMovies();
        } else {
            Toast.makeText(getContext(), getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calls an AsyncTask to fetch the movies
     */
    private void updateMovies() {
        progressDialog = ProgressDialog.show(getContext(), getString(R.string.please_wait), getString(R.string.loading_movies));
        // For some reason each time I am loading a loader, I have to force load it!!! Ba'a'a'a'd!!!!
        getLoaderManager().initLoader(FetchPopularMoviesLoader.ID, null, this).forceLoad();
    }

    /**
     *
     * http://api.themoviedb.org/3/discover/movie?api_key=YourApiKey&sort_by=popularity.desc
     *
     * Returns the url
     * @return
     */
    private String getPopularMoviesUrl() {
        Uri.Builder builder = new Uri.Builder();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default_value));

        builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(sortBy)
                        .appendQueryParameter("api_key", getString(R.string.api_key));

        return builder.build().toString();
    }

    // ------------------------------------------------------------------------------------
    // Fetch all the movies

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new FetchPopularMoviesLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (data == null) {
            return;
        }

        moviesAdapter.clear();
        moviesAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) { }

    // ------------------------------------------------------------------------------------

    public static class FetchPopularMoviesLoader extends AsyncTaskLoader<ArrayList<Movie>> {

        public static final int ID = 301;

        public FetchPopularMoviesLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Movie> loadInBackground() {
            ArrayList<Movie> movieArrayList = new ArrayList<>();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String sortBy = preferences.getString(getContext().getString(R.string.pref_sort_key), getContext().getString(R.string.pref_sort_default_value));

            // If the sorting criteria is Favorite movies
            if (sortBy.equalsIgnoreCase(getContext().getString(R.string.favorite_movie_type))) {

                String selectionClause = MovieContract.MovieEntry.COLUMN_FAVOURITE + " = ?";
                String[] selectionArgs = { "1" };

                Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
                                selectionClause, selectionArgs, null);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        Movie movie = new Movie();
                        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
                        movie.setAverageVote(String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVG))));
                        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVOURITE)) != 0);
                        movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)));
                        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                        movie.setImageUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                        movie.setUserRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
                        movie.setMovieType(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TYPE)));
                        movieArrayList.add(movie);

                    }
                    cursor.close();
                }

                return movieArrayList;
            }

            // If sort by is either Top Rated or Popular Movies
            return fetchAllMovies(movieArrayList, sortBy);
        }

        /**
         *
         * @param movieArrayList
         * @param sortBy
         */
        private ArrayList<Movie> fetchAllMovies(ArrayList<Movie> movieArrayList, String sortBy) {
            movieArrayList.addAll(fetchMoviesFromServer());

            Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry.COLUMN_TYPE + " == ?", new String[] { sortBy }, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Movie movie = new Movie();
                    movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
                    movie.setAverageVote(String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVG))));
                    movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVOURITE)) != 0);
                    movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                    movie.setImageUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                    movie.setUserRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
                    movie.setMovieType(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TYPE)));

                    if (movieArrayList.contains(movie)) {
                        movieArrayList.remove(movie);
                    }
                    movieArrayList.add(movie);

                }
                cursor.close();
            }
            return movieArrayList;
        }

        private ArrayList<Movie> fetchMoviesFromServer() {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String popularMoviesJson = null;

            try {
                URL url = new URL(Util.getMovieServerUrl(getContext()));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularMoviesJson = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

//            Log.d(LOG_TAG, popularMoviesJson);
            return parseMoviesJson(popularMoviesJson);
        }

        /**
         * Returns a parsed Arraylist of Movie objects
         * @param json
         * @return
         */
        private ArrayList<Movie> parseMoviesJson(String json) {

            ArrayList<Movie> movieArrayList = new ArrayList<>();

            try {
                JSONObject moviesJson = new JSONObject(json);
                JSONArray moviesArray = moviesJson.getJSONArray("results");
                for (int i=0; i<moviesArray.length(); i++) {
                    Movie movie = new Movie(moviesArray.getJSONObject(i));
                    movie.setMovieType(Util.getMovieType(getContext()));
                    movieArrayList.add(movie);
                }

            } catch (JSONException e) {
                Log.e("MoviesFragment", "Error while parsing Movies JSON");
                e.printStackTrace();
            }

            return movieArrayList;

        }
    }


    /*
     * TODO: Add a broadcast receiver to listen to the internet connection.
     * Important: That way whenever an internet connection comes up we will be able to receive data from
     * server.
     */

}
