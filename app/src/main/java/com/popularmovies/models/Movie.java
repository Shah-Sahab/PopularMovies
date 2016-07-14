package com.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Psych on 7/9/16.
 */
public class Movie implements Parcelable {

    public static final String M_POSTER_PATH    = "poster_path";
    public static final String M_ORIGINAL_TITLE = "original_title";
    public static final String M_RELEASE_DATE   = "release_date";
    public static final String M_POPULARITY     = "popularity";
    public static final String M_VOTE_AVG       = "vote_average";
    public static final String M_SYNOPSIS       = "overview";
    public static final String M_ID             = "id";

    private int id;
    private String title;
    private String imageUrl;
    private String averageVote;
    private String plotSynopsis;
    private String userRating;

    private String releaseDate;

    /**
     * No Args-Constructor
     */
    public Movie() {
    }

    /**
     * Parse the json
     * @param movieJson
     */
    public Movie(JSONObject movieJson) {

        try {
            id = movieJson.getInt(M_ID);
            title = movieJson.getString(M_ORIGINAL_TITLE);
            imageUrl = movieJson.getString(M_POSTER_PATH);
            averageVote = movieJson.getString(M_VOTE_AVG);
            plotSynopsis = movieJson.getString(M_SYNOPSIS);
            userRating = movieJson.getString(M_POPULARITY);
            releaseDate = movieJson.getString(M_RELEASE_DATE);

            imageUrl = imageUrl.replace("/", "").trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
     * -------------------------------------------------------------------------------------------
     * Parcelable Implementation
     */

    protected Movie(Parcel in) {
        id              = in.readInt();
        title           = in.readString();
        imageUrl        = in.readString();
        averageVote     = in.readString();
        plotSynopsis    = in.readString();
        userRating      = in.readString();
        releaseDate     = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imageUrl);
        parcel.writeString(averageVote);
        parcel.writeString(plotSynopsis);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);

    }

    /*
     * -------------------------------------------------------------------------------------------
     * Getters & Setters
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(String averageVote) {
        this.averageVote = averageVote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
