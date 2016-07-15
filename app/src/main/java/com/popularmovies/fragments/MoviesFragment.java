package com.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;
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

/**
 * Created by Psych on 7/9/16.
 */
public class MoviesFragment extends Fragment {

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
        FetchPopularMovies fetchPopularMovies = new FetchPopularMovies();
        fetchPopularMovies.execute();
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

    /**
     * Gets all the popular movies
     */
    public class FetchPopularMovies extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String popularMoviesJson = null;

            try {
                URL url = new URL(getPopularMoviesUrl());
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

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            moviesAdapter.clear();
            moviesAdapter.addAll(movies);
        }
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
                movieArrayList.add(movie);
            }

        } catch (JSONException e) {
            Log.e("MoviesFragment", "Error while parsing Movies JSON");
            e.printStackTrace();
        }

        return movieArrayList;

    }

}
