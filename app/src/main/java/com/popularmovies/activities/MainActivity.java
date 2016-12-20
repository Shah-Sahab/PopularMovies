package com.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.popularmovies.R;
import com.popularmovies.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "1: onCreated called");

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new MoviesFragment())
                            .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "2: onStart called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "3: onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "4: onResume called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "5: onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "6: onDestroy called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
