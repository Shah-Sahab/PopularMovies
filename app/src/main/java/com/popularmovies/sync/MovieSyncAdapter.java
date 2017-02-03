package com.popularmovies.sync;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.popularmovies.R;
import com.popularmovies.data.MovieContract;
import com.popularmovies.extras.Util;
import com.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

/**
 * Created by Psych on 1/3/17.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = MovieSyncAdapter.class.getName();
    ContentResolver contentResolver;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String popularMoviesJson = null;

        try {

            // Get the url to the movie server (OpenDBMovie)
            URL url = new URL(Util.getMovieServerUrl(getContext()));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            popularMoviesJson = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        parseMoviesJson(popularMoviesJson);
    }

    /**
     * Returns a parsed Arraylist of Movie objects
     * @param json
     * @return
     */
    private void parseMoviesJson(String json) {

        Vector<ContentValues> contentValuesVector = new Vector<>();

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        try {
            JSONObject moviesJson = new JSONObject(json);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            for (int i=0; i<moviesArray.length(); i++) {
                // parse the data here
                Movie movie = new Movie(moviesArray.getJSONObject(i));
                // Put the data in ContentValues & add it to the vector
                contentValuesVector.add(movie.getContentValues());
            }

            int inserted = 0;
            // add to database
            if ( contentValuesVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
                contentValuesVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
                // Delete the movies which are not favourite

//                getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
//                                MovieContract.MovieEntry.COLUMN_FAVOURITE + " == ?",
//                                new String[] { "0" });

//                notifyMovies();
            }

            Log.d(LOG_TAG, "Insertion: " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e("MoviesFragment", "Error while parsing Movies JSON");
            e.printStackTrace();
        }

    }

    public void notifyMovies() {
//        Context context = getContext();
//        //checking the last update and notify if it' the first of the day
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String lastNotificationKey = context.getString(R.string.pref_last_notification);
//        long lastSync = prefs.getLong(lastNotificationKey, 0);
//
//        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
//
//
//            Uri moviesUri = MovieContract.MovieEntry.buildMovieUri();
//
//            Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());
//
//            // we'll query our contentProvider, as always
//            Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);
//
//            if (cursor.moveToFirst()) {
//                int weatherId = cursor.getInt(INDEX_WEATHER_ID);
//                double high = cursor.getDouble(INDEX_MAX_TEMP);
//                double low = cursor.getDouble(INDEX_MIN_TEMP);
//                String desc = cursor.getString(INDEX_SHORT_DESC);
//
//                int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
//                String title = context.getString(R.string.app_name);
//
//                // Define the text of the forecast.
//                String contentText = String.format(context.getString(R.string.format_notification),
//                                desc,
//                                Utility.formatTemperature(context, high),
//                                Utility.formatTemperature(context, low));
//
//
//                if (Utility.isNotificationEnabled(context)) {
//                    //build your notification here.
//                    // Create a notification
//                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext());
//                    notificationBuilder.setSmallIcon(iconId)
//                                    .setContentTitle(title)
//                                    .setContentText(contentText);
//
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//
//                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
//                    stackBuilder.addNextIntent(intent);
//                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                    notificationBuilder.setContentIntent(pendingIntent);
//                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
//                    notificationManagerCompat.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());
//                }
//
//
//                //refreshing last sync
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putLong(lastNotificationKey, System.currentTimeMillis());
//                editor.commit();
//            }
//        }

    }

}
