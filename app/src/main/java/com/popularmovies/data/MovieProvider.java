package com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    public static final int REVIEW_PAGE = 300;
    public static final int TRAILER = 200;
    public static final int REVIEW = 400;
    public static final int MOVIE = 100;

    public static final int MOVIE_WITH_TRAILERS = 201;
    public static final int MOVIE_WITH_REVIEWS = 202;
    public static final int MOVIE_WITH_REVIEWS_AND_TRAILERS = 500;

//    private static final SQLiteQueryBuilder weatherByLocationSettingQueryBuilder;

//    static {
//        weatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

//        //This is an inner join which looks like
//        //weather INNER JOIN location ON weather.location_id = location._id
//        weatherByLocationSettingQueryBuilder.setTables(
//                        WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
//                                        WeatherContract.LocationEntry.TABLE_NAME +
//                                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
//                                        "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
//                                        " = " + WeatherContract.LocationEntry.TABLE_NAME +
//                                        "." + WeatherContract.LocationEntry._ID);
//    }

//    private static final SQLiteQueryBuilder

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_TRAILERS);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/#", MOVIE_WITH_REVIEWS_AND_TRAILERS);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW_PAGE, REVIEW_PAGE);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int numberOfRowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {

            case MOVIE: {
                numberOfRowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            }

            case TRAILER: {
                numberOfRowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            }

            case REVIEW_PAGE: {
                numberOfRowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            }

            case REVIEW: {
                numberOfRowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME,
                                selection,
                                selectionArgs);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = uriMatcher.match(uri);

        switch (match) {

            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW_PAGE:
                return MovieContract.ReviewPageEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case MOVIE_WITH_REVIEWS_AND_TRAILERS:
            case MOVIE_WITH_REVIEWS:
            case MOVIE_WITH_TRAILERS:
                throw new UnsupportedOperationException("Provider Type Not yet implemented");
        }
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
//                normalizeDate(values);
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TRAILER: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEW_PAGE: {

                long _id = db.insert(MovieContract.ReviewPageEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewPageEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert a row into " + uri);
                break;
            }

            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert a row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            // "Movie/*/*"

//      case WEATHER_WITH_LOCATION_AND_DATE: {
//                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
//                break;
//            }
            // "weather/*"
//            case WEATHER_WITH_LOCATION: {
//                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
//                break;
//            }

            // "movie"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            // "trailer"
            case TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.TrailerEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            // "review page"
            case REVIEW_PAGE:

                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.ReviewPageEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);

                break;

            // "review"
            case REVIEW:

                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.ReviewEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
//                        normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int numberOfRowsUpdated;

        switch (match) {

            case MOVIE: {
//                normalizeDate(values);
                numberOfRowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs
                );
                break;
            }

            case TRAILER: {
                numberOfRowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                break;
            }

            case REVIEW_PAGE: {
                numberOfRowsUpdated = db.update(MovieContract.ReviewPageEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                break;
            }

            case REVIEW: {
                numberOfRowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;

    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
