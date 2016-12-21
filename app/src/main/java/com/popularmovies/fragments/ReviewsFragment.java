package com.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.R;
import com.popularmovies.adapters.ReviewRecyclerViewAdapter;
import com.popularmovies.extras.DividerItemDecoration;
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;
import com.popularmovies.models.Review;
import com.popularmovies.models.TotalReviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<TotalReviews> {

    private static final String ARG_MOVIE = "movie";
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    private Movie movie;
    private OnListFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReviewsFragment() {
    }

    @SuppressWarnings("unused")
    public static ReviewsFragment newInstance(Movie movie) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        } else {
            movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_REFERRER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(new ArrayList<Review>(), mListener);
        recyclerView.setAdapter(reviewRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                            + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = ProgressDialog.show(getContext(), null, "Please Wait...");
        // For some reason each time I am loading a loader, I have to force load it!!! Ba'a'a'a'd!!!!
        getLoaderManager().initLoader(FetchMovieReviewsLoader.ID, null, this).forceLoad();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<TotalReviews> onCreateLoader(int id, Bundle args) {
        return new FetchMovieReviewsLoader(getContext(), movie.getId());
    }

    @Override
    public void onLoadFinished(Loader<TotalReviews> loader, TotalReviews data) {
        if (data != null && data.getResultsArrayList() != null) {
            getView().findViewById(R.id.warning_text).setVisibility(View.GONE);
            reviewRecyclerViewAdapter.getmValues().clear();
            reviewRecyclerViewAdapter.getmValues().addAll(data.getResultsArrayList());
            reviewRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            getView().findViewById(R.id.warning_text).setVisibility(View.VISIBLE);
        }
        dismissProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<TotalReviews> loader) {

    }


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Review item);
    }

    /**
     * Release Version 2:
     * You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
     * "/movie/{id}/videos"
     */
    public static class FetchMovieReviewsLoader extends AsyncTaskLoader<TotalReviews> {

        public static final int ID = 102;
        public static final String LOG_TAG = FetchMovieReviewsLoader.class.getName();

        private int movieId;

        public FetchMovieReviewsLoader(Context context, int movieId) {
            super(context);
            this.movieId = movieId;
        }

        @Override
        public TotalReviews loadInBackground() {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String movieReviewJson = null;

            try {
                URL url = new URL(Util.getMovieReviewsUrl(getContext(), movieId));
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
                movieReviewJson = buffer.toString();

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

            return TotalReviews.parseJSON(movieReviewJson);
        }

    }

}
