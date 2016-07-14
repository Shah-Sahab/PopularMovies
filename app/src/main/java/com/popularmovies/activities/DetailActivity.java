package com.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.popularmovies.R;
import com.popularmovies.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                            .add(R.id.container , new DetailFragment())
                            .commit();
        }

    }
}
