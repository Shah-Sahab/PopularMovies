package com.popularmovies.adapters;

import android.content.Context;
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

    private ArrayList<Trailer> trailerArrayList;
    private LayoutInflater layoutInflater;

    public  TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        this.trailerArrayList = trailerArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_trailer_recycler, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerTextView.setText(trailerArrayList.get(position).getTrailerName());
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


}
