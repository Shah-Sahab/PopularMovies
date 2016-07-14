package com.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.R;
import com.popularmovies.extras.LRUCacheImpl;
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;


public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_detail, container, false);
        Movie movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_REFERRER);
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
        ((TextView)rootView.findViewById(R.id.releease_date_text)).setText(movie.getReleaseDate());
        ((TextView)rootView.findViewById(R.id.rating_text)).setText(movie.getUserRating());

        return rootView;
    }

}
