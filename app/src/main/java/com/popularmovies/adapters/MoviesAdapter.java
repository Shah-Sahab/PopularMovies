package com.popularmovies.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.popularmovies.R;
import com.popularmovies.extras.LRUCacheImpl;
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Psych on 7/9/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MoviesAdapter.class.getName();
    Random random;

    public MoviesAdapter(Context context, ArrayList<Movie> movieArrayList) {
        super(context, 0, movieArrayList);
        random = new Random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.movieNameTextView.setText(getItem(position).getTitle());
        viewHolder.movieInfoLayout.setBackgroundColor(Color.argb(200, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        convertView.setTag(viewHolder);


        if (LRUCacheImpl.getInstance().get(getItem(position).getId()) != null) {
            Log.d(LOG_TAG, "retrieving from LRU Cache");
            viewHolder.posterImageView.setImageBitmap(LRUCacheImpl.getInstance().get(getItem(position).getId()));
        } else {
            Util.fetchBitmap(getItem(position), viewHolder.posterImageView);
        }

        return convertView;
    }

    public class ViewHolder {

        ImageView posterImageView;
        TextView movieNameTextView;
        //        RatingBar movieRatingRatingBar;
        RelativeLayout movieInfoLayout;

        public ViewHolder(View itemView) {
            movieInfoLayout = (RelativeLayout) itemView.findViewById(R.id.movie_info_layout);
            movieNameTextView = (TextView) itemView.findViewById(R.id.text_movie_name);
            posterImageView = (ImageView) itemView.findViewById(R.id.image_poster);
        }

    }

}

