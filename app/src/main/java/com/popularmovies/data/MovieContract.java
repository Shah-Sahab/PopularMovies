package com.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Psych on 12/24/16.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.popularmovies";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REVIEW_PAGE = "review_page";
    public static final String PATH_TRAILER     = "trailer";
    public static final String PATH_REVIEW      = "review";
    public static final String PATH_MOVIE       = "movie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                        BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME               = "Movie";

        public static final String COLUMN_POSTER_PATH       = "poster_path";
        public static final String COLUMN_ORIGINAL_TITLE    = "original_title";
        public static final String COLUMN_RELEASE_DATE      = "release_date";
        public static final String COLUMN_POPULARITY        = "popularity";
        public static final String COLUMN_VOTE_AVG          = "vote_average";
        public static final String COLUMN_SYNOPSIS          = "overview";
        public static final String COLUMN_FAVOURITE         = "favourite_movies";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                        BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;


        // Table name
        public static final String TABLE_NAME           = "Trailer";

        public static final String COLUMN_KEY           = "key";
        public static final String COLUMN_NAME          = "name";
        public static final String COLUMN_SITE          = "site";
        public static final String COLUMN_SIZE          = "size";
        public static final String COLUMN_TYPE          = "type";
        public static final String COLUMN_RESULTS       = "results";
        public static final String COLUMN_LANGUAGE      = "iso_639_1";
        // Movie ID Foreign Key
        public static final String COLUMN_MOVIE_ID      = "movie_id";
        public static final String COLUMN_COUNTRY_CODE  = "iso_3166_1";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class ReviewPageEntry implements BaseColumns  {

        public static final Uri CONTENT_URI =
                        BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW_PAGE).build();

        public static final String CONTENT_TYPE =
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW_PAGE;
        public static final String CONTENT_ITEM_TYPE =
                        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW_PAGE;

        // Table name
        public static final String TABLE_NAME           = "Review_Page";

        public static final String COLUMN_PAGE          = "page";
        public static final String COLUMN_RESULTS       = "results";
        // Movie ID Foreign Key
        public static final String COLUMN_MOVIE_ID      = "movie_id";
        public static final String COLUMN_TOTAL_PAGES   = "total_pages";
        public static final String COLUMN_TOTAL_RESULTS = "total_results";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                        BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Table name
        public static final String TABLE_NAME       = "Review";
        public static final String COLUMN_URL       = "url";
        public static final String COLUMN_AUTHOR    = "author";
        public static final String COLUMN_CONTENT   = "content";
        // Review Page Foreign Key
        public static final String COLUMN_PAGE_KEY  = "page_id";

        public static Uri buildReviewEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
