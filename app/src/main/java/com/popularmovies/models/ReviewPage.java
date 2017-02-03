package com.popularmovies.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Psych on 12/19/16.
 */
public class ReviewPage {

    /*
     * {"id":100,
     * "page":1,
     * "results":[{"id":"529bc23719c2957215011e7b",
     * "author":"BradFlix",
     * "content":"I just plain love this movie!","url":"https://www.themoviedb.org/review/529bc23719c2957215011e7b"},
     * {"id":"535856c30e0a26069400064c","author":"Andres Gomez","content":"Far from being a good movie, with tons of flaws but already pointing to the pattern of the whole Ritchie's filmography.",
     * "url":"https://www.themoviedb.org/review/535856c30e0a26069400064c"}],"total_pages":1,"total_results":2}
     */

    public static final String JSON_ID = "id";
    public static final String JSON_PAGE = "page";
    public static final String JSON_RESULTS = "results";
    public static final String JSON_TOTAL_PAGES = "total_pages";
    public static final String JSON_TOTAL_RESULTS = "total_results";

    private int id;
    private int pageNumber;
    private ArrayList<Review> resultsArrayList;
    private int totalPages;
    private int totalResults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ArrayList<Review> getResultsArrayList() {
        return resultsArrayList;
    }

    public void setResultsArrayList(ArrayList<Review> resultsArrayList) {
        this.resultsArrayList = resultsArrayList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public String toString() {
        return "ReviewPage{" +
                        "id=" + id +
                        ", pageNumber=" + pageNumber +
                        ", resultsArrayList=" + resultsArrayList +
                        ", totalPages=" + totalPages +
                        ", totalResults=" + totalResults +
                        '}';
    }

    public static ReviewPage parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            ReviewPage reviewPage = new ReviewPage();
            reviewPage.setId(jsonObject.getInt(JSON_ID));
            reviewPage.setPageNumber(jsonObject.getInt(JSON_PAGE));
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_RESULTS);
            if (jsonArray.length() > 0) {
                ArrayList<Review> reviewArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    reviewArrayList.add(Review.parseJSON(jsonArray.getJSONObject(i)));
                }
                reviewPage.setResultsArrayList(reviewArrayList);
            }
            reviewPage.setTotalPages(jsonObject.getInt(JSON_TOTAL_PAGES));
            reviewPage.setTotalResults(jsonObject.getInt(JSON_TOTAL_RESULTS));
//            Log.e("ReviewPage", reviewPage.toString());
            return reviewPage;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
