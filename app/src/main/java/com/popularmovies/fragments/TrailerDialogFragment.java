package com.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.popularmovies.R;
import com.popularmovies.models.Trailer;

/**
 * Created by Psych on 12/18/16.
 */

public class TrailerDialogFragment extends DialogFragment implements YouTubePlayer.OnInitializedListener {

    private static final String KEY = "Trailer";
    private static final int RECOVERY_REQUEST = 1;
    public static final String TAG = TrailerDialogFragment.class.getName();

    YouTubePlayerView youTubePlayerView;
    Trailer trailer;

    public static TrailerDialogFragment getInstance(Trailer trailer) {
        TrailerDialogFragment trailerDialogFragment = new TrailerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY, trailer);
        trailerDialogFragment.setArguments(bundle);
        return trailerDialogFragment;
    }

    public TrailerDialogFragment() {
        // Empty Constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        youTubePlayerView = (YouTubePlayerView) inflater.inflate(R.layout.dialog_trailer, container, false);
        youTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
        trailer = getArguments().getParcelable(KEY);
        return youTubePlayerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(trailer.getYoutubeId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_REQUEST).show();
        } else {
            String error = getString(R.string.player_error) + youTubeInitializationResult.toString();
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        }
    }
}
