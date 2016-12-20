package com.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Psych on 12/19/16.
 */
public class Review {

    /*
     * "id":"529bc23719c2957215011e7b",
     * "author":"BradFlix",
     * "content":"I just plain love this movie!",
     * "url":"https://www.themoviedb.org/review/529bc23719c2957215011e7b"
     */

    public static final String JSON_ID = "id";
    public static final String JSON_AUTHOR = "author";
    public static final String JSON_CONTENT = "content";
    public static final String JSON_URL = "url";

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Review{" +
                        "id='" + id + '\'' +
                        ", author='" + author + '\'' +
                        ", content='" + content + '\'' +
                        ", url='" + url + '\'' +
                        '}';
    }

    public static Review parseJSON(JSONObject jsonObject) {
        try {

            Review review = new Review();
            review.setAuthor(jsonObject.getString(JSON_AUTHOR));
            review.setId(jsonObject.getString(JSON_ID));
            review.setContent(jsonObject.getString(JSON_CONTENT));
            review.setUrl(jsonObject.getString(JSON_URL));
            return review;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
