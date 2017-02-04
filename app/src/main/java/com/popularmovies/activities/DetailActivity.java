package com.popularmovies.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;
import com.popularmovies.R;
import com.popularmovies.fragments.DetailFragment;
import com.popularmovies.fragments.ReviewsFragment;
import com.popularmovies.models.Review;

public class DetailActivity extends AppCompatActivity implements ReviewsFragment.OnListFragmentInteractionListener {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        if (savedInstanceState == null) {
            CustomFragmentPagerAdapter customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(customFragmentPagerAdapter);

            // Bind the tabs to the ViewPager
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabs.setViewPager(viewPager);
//        }

        setupActionBar();

    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onListFragmentInteraction(Review item) {
        // For now do nothing
    }

    public class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] fragmentArray;

        String[] pageTitleArray = new String[] {
            "Overview", "Reviews"
        };

        public CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentArray = new Fragment[] {
                new DetailFragment(), new ReviewsFragment()
            };
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArray[position];
        }

        @Override
        public int getCount() {
            return fragmentArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitleArray[position];
        }
    }
}
