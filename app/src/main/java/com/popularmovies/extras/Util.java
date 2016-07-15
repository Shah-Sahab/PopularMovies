package com.popularmovies.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.popularmovies.models.Movie;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Psych on 7/10/16.
 */
public class Util {

    private static final String LOG_TAG = Util.class.getName();

    /**
     * Lazy loading/Caching
     * @return a Bitmap
     */
    public static void fetchBitmap(Movie movie, ImageView imageView) {
        FetchMoviePoster fetchMoviePoster = new FetchMoviePoster(imageView);
        fetchMoviePoster.execute(movie);
    }

    public static class FetchMoviePoster extends AsyncTask<Movie, Void, Bitmap> {

        ImageView imageView;

        public FetchMoviePoster(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Movie... movies) {
            String urlStr = getPosterUrl(movies[0].getImageUrl());
            Bitmap bitmap = fetchImageFromUrl(urlStr);
            LRUCacheImpl.getInstance().put(movies[0].getId(), bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageView != null) {
                this.imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * Fetch an image from url
     * @param urlStr
     * @return Bitmap
     */
    private static Bitmap fetchImageFromUrl(String urlStr) {

        HttpURLConnection urlConnection = null;
        Bitmap bitmap = null;
        try {

            URL url = new URL(urlStr);
            Log.d(LOG_TAG, "URL= " + urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bitmap;
    }

    public static String getPosterUrl(String posterRelativePath) {
        // image.tmdb.org/t/p/
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w185")
                        .appendPath(posterRelativePath).build();
        return builder.toString();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
