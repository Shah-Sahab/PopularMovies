package com.popularmovies.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.popularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String movieType;

    private boolean isFavorite;

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
        movieType       = in.readString();
        isFavorite      = in.readByte() != 0; // isFavorite == true if byte != 0
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
        parcel.writeString(movieType);
        parcel.writeByte((byte) (isFavorite ? 1 : 0)); // if isFavorite == true, byte == 1

    }

    /**
     * Returns the content values
     * @return ContentValues
     */
    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, imageUrl);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, averageVote);
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, plotSynopsis);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, userRating);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVOURITE, isFavorite);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TYPE, movieType);

        return contentValues;

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Movie && ((Movie) o).getId() == this.id) {
            return true;
        }
        return false;
    }
}
