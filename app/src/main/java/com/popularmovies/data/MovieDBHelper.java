package com.popularmovies.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Psych on 12/24/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MovieDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MovieDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_FAVOURITE + " INTEGER NOT NULL DEFAULT 0 "
                        + ");"
        ;

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                        MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.TrailerEntry.COLUMN_COUNTRY_CODE + " TEXT NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
//                        MovieContract.TrailerEntry.COLUMN_RESULTS + " TEXT NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_SIZE + " REAL NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                        " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " ) REFERENCES " +
                        MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ")";



        final String SQL_CREATE_REVIEW_PAGE_TABLE = "CREATE TABLE " + MovieContract.ReviewPageEntry.TABLE_NAME + " (" +
                        MovieContract.ReviewPageEntry._ID + " INTEGER PRIMARY KEY, " +
//                        MovieContract.ReviewPageEntry.COLUMN_RESULTS + " TEXT NOT NULL, " +
                        MovieContract.ReviewPageEntry.COLUMN_PAGE + " REAL NOT NULL, " +
                        MovieContract.ReviewPageEntry.COLUMN_TOTAL_PAGES + " REAL NOT NULL, " +
                        MovieContract.ReviewPageEntry.COLUMN_TOTAL_RESULTS + " REAL NOT NULL, " +
                        " FOREIGN KEY (" + MovieContract.ReviewPageEntry.COLUMN_MOVIE_ID + " ) REFERENCES " +
                        MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ")";


        final String SQL_CREATE_REVIEW = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                        MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                        MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_PAGE_KEY + " ) REFERENCES " +
                        MovieContract.ReviewPageEntry.TABLE_NAME + " (" + MovieContract.ReviewPageEntry._ID + ")";


        // Example on Referencing Foreign key and Replace on Conflict
//                        // Set up the location column as a foreign key to location table.
//                        " FOREIGN KEY (" + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
//                        WeatherContract.LocationEntry.TABLE_NAME + " (" + WeatherContract.LocationEntry._ID + "), " +
//
//                        // To assure the application have just one weather entry per day
//                        // per location, it's created a UNIQUE constraint with REPLACE strategy
//                        " UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ", " +
//                        WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_PAGE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewPageEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
