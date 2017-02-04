package com.popularmovies.extras;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.popularmovies.models.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.popularmovies.extras.Util.getPosterUrl;

/**
 * Created by Psych on 2/3/17.
 */

public class FetchMoviePoster extends AsyncTask<Movie, Void, Bitmap> {

    private static final String LOG_TAG = FetchMoviePoster.class.getName();

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

    /**
     * Fetch an image from url
     * @param urlStr
     * @return Bitmap
     */
    private Bitmap fetchImageFromUrl(String urlStr) {

        HttpURLConnection urlConnection = null;
        Bitmap bitmap = null;
        try {

            URL url = new URL(urlStr);
//            Log.d(LOG_TAG, "URL= " + urlStr);
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
}
