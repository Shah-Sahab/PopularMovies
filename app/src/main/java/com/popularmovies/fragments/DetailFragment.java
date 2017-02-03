package com.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.R;
import com.popularmovies.activities.TrailerPlayerActivity;
import com.popularmovies.adapters.TrailerAdapter;
import com.popularmovies.data.MovieContract;
import com.popularmovies.extras.DividerItemDecoration;
import com.popularmovies.extras.LRUCacheImpl;
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


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>>, TrailerAdapter.TrailerClickListener {

    Movie movie;
    RecyclerView trailerRecycler;
    TrailerAdapter trailerAdapter;

    ProgressDialog progressDialog;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_detail, container, false);
        movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_REFERRER);
        // movie should not be null. If it is, just return the rootView.
        // Later add a message to it.
        if (movie == null) {
            return rootView;
        }

        if (LRUCacheImpl.getInstance().get(movie.getId()) != null) {
            ((ImageView)rootView.findViewById(R.id.poster_image)).setImageBitmap(LRUCacheImpl.getInstance().get(movie.getId()));
        } else {
            Util.fetchBitmap(movie, (ImageView) rootView.findViewById(R.id.poster_image));
        }

        ((TextView)rootView.findViewById(R.id.title_text)).setText(movie.getTitle());
        ((TextView)rootView.findViewById(R.id.overview_text)).setText(movie.getPlotSynopsis());
        ((TextView)rootView.findViewById(R.id.release_date_text)).setText(movie.getReleaseDate());
        ((CheckBox)rootView.findViewById(R.id.checkBox_fav)).setChecked(movie.isFavorite());

        // Always set this later, otherwise this will be called while setting the above check
        ((CheckBox)rootView.findViewById(R.id.checkBox_fav)).setOnCheckedChangeListener(onCheckedChangeListener);

        initializeRecycler(rootView);

        return rootView;
    }

    private void initializeRecycler(View rootView) {
        trailerRecycler = (RecyclerView) rootView.findViewById(R.id.trailer_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        trailerRecycler.setLayoutManager(layoutManager);
        trailerRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        trailerAdapter = new TrailerAdapter(getContext(), new ArrayList<Trailer>());
        trailerRecycler.setAdapter(trailerAdapter);
        trailerAdapter.setTrailerClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressDialog = ProgressDialog.show(getContext(), null, "Please Wait...");
        // For some reason each time I am loading a loader, I have to force load it!!! Ba'a'a'a'd!!!!
        getLoaderManager().initLoader(FetchMovieDetailsLoader.ID, null, this).forceLoad();

    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        return new FetchMovieDetailsLoader(getContext(), movie.getId());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
        dismissProgressDialog();
        if (data == null) {
            return;
        }
        Log.d("DetailFragment", data.toString());
        trailerAdapter.setTrailerArrayList(data);
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClickOfATrailer(Trailer trailer) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?" + trailer.getKey()));
//        startActivity(intent);
        Intent intent = new Intent(getActivity(), TrailerPlayerActivity.class);
        intent.putExtra(TrailerPlayerActivity.KEY, trailer);
        startActivity(intent);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                movie.setFavorite(isChecked);
                Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movie.getContentValues());
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            } else {
                int rowsDeleted = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry._ID + " == ?", new String[] {String.valueOf(movie.getId())});
                if (rowsDeleted > 0) {
                    Toast.makeText(getContext(), "Not Favorite anymore", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    // --------------------------------------------------------------------------------------------
    // Release 2 work
    // Fetching the trailers and reviews
    // --------------------------------------------------------------------------------------------

    /**
     * Release Version 2:
     * You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
     * "/movie/{id}/videos"
     */
    public static class FetchMovieDetailsLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

        public static final int ID = 101;
        public static final String LOG_TAG = FetchMovieDetailsLoader.class.getName();

        private int movieId;

        public FetchMovieDetailsLoader(Context context, int movieId) {
            super(context);
            this.movieId = movieId;
        }

        @Override
        public ArrayList<Trailer> loadInBackground() {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String trailerJson = null;

            try {
                URL url = new URL(Util.getMovieTrailersUrl(getContext(), movieId));
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
                trailerJson = buffer.toString();

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

            return parseTrailerJson(trailerJson);
        }

        /**
         * Parses the JSON and
         * @param json String
         * @return Trailer {@link ArrayList}
         */
        private ArrayList<Trailer> parseTrailerJson(String json) {

            try {
                JSONObject trailersJsonResult = new JSONObject(json);
                JSONArray trailersJsonArray = trailersJsonResult.getJSONArray(Trailer.RESULTS);
                if (trailersJsonArray.length() > 0) {
                    ArrayList<Trailer> trailersArrayList = new ArrayList<>();
                    for (int i = 0; i < trailersJsonArray.length(); i++) {
                        JSONObject trailerJsonObject = trailersJsonArray.getJSONObject(i);
                        Trailer trailer = new Trailer();
                        trailer.setCountryCode(trailerJsonObject.getString(Trailer.COUNTRY_CODE));
                        trailer.setKey(trailerJsonObject.getString(Trailer.KEY));
                        trailer.setTrailerName(trailerJsonObject.getString(Trailer.NAME));
                        trailer.setYoutubeId(trailerJsonObject.getString(Trailer._ID));
                        trailer.setLanguage(trailerJsonObject.getString(Trailer.LANGUAGE));
                        trailer.setSite(trailerJsonObject.getString(Trailer.SITE));
                        trailer.setSize(trailerJsonObject.getInt(Trailer.SIZE));
                        trailer.setType(trailerJsonObject.getString(Trailer.TYPE));
                        trailersArrayList.add(trailer);
//                        Log.e(LOG_TAG, trailer.toString());
                    }
                    return trailersArrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
