package com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularmovies.R;
import com.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by Psych on 12/16/16.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private TrailerClickListener trailerClickListener;
    private ArrayList<Trailer> trailerArrayList;
    private LayoutInflater layoutInflater;

    public  TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        this.trailerArrayList = trailerArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(layoutInflater.inflate(R.layout.item_trailer_recycler, parent, false));
        viewHolder.trailerTextView.setOnClickListener(clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerTextView.setText(trailerArrayList.get(position).getTrailerName());
        holder.trailerTextView.setTag(trailerArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    public void setTrailerArrayList(ArrayList<Trailer> trailerArrayList) {
        this.trailerArrayList = trailerArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView trailerTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerTextView = (TextView) itemView.findViewById(R.id.trailer_text);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (trailerClickListener == null) return;

            // Send the trailer information
            trailerClickListener.onClickOfATrailer((Trailer) v.getTag());
        }
    };

    public void setTrailerClickListener(TrailerClickListener trailerClickListener) {
        this.trailerClickListener = trailerClickListener;
    }

    public interface TrailerClickListener {
        void onClickOfATrailer(Trailer trailer);
    }


}
