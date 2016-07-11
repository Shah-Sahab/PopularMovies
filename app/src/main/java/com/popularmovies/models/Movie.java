package com.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Psych on 7/9/16.
 */
public class Movie {

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

    private Date releaseDate;

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
//                releaseDate = (Date) movie.get(M_RELEASE_DATE);

            imageUrl = imageUrl.replace("/", "").trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
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
