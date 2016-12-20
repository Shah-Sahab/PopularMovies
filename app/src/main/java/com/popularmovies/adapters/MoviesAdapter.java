package com.popularmovies.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
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

/**
 * Created by Psych on 7/9/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MoviesAdapter.class.getName();

    public MoviesAdapter(Context context, ArrayList<Movie> movieArrayList) {
        super(context, 0, movieArrayList);
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
        viewHolder.artistNameTextView.setText(getItem(position).getTitle());
        convertView.setTag(viewHolder);


        if (LRUCacheImpl.getInstance().get(getItem(position).getId()) != null) {
//            Log.d(LOG_TAG, "retrieving from LRU Cache");
            viewHolder.posterImageView.setImageBitmap(LRUCacheImpl.getInstance().get(getItem(position).getId()));
        } else {
            Util.fetchBitmap(getItem(position), viewHolder.posterImageView);
        }

        setTitleBackgroundColor(viewHolder);

        return convertView;
    }

    private void setTitleBackgroundColor(final ViewHolder viewHolder) {
        Palette.from(((BitmapDrawable)viewHolder.posterImageView.getDrawable()).getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getMutedColor(getContext().getResources().getColor(android.R.color.black));
                int translucentBgColor = adjustAlpha(bgColor, 0.5f);
                viewHolder.movieInfoLayout.setBackgroundColor(translucentBgColor);
            }
        });
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public class ViewHolder {

        ImageView posterImageView;
        TextView artistNameTextView;
        RelativeLayout movieInfoLayout;

        public ViewHolder(View itemView) {
            movieInfoLayout = (RelativeLayout) itemView.findViewById(R.id.movie_info_layout);
            artistNameTextView = (TextView) itemView.findViewById(R.id.text_movie_name);
            posterImageView = (ImageView) itemView.findViewById(R.id.image_poster);
        }

    }

}

