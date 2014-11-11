package com.chaemil.hgms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by chaemil on 30.10.14.
 */
public class PhotoalbumGallery extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new PhotoalbumSliderItem();

            Bundle extra = getIntent().getExtras();
            int albumId = extra.getInt("albumId");

            Bundle fragmentExtra = new Bundle();
            fragmentExtra.putInt("photoNum", position);
            fragmentExtra.putInt("albumId", albumId);

            fragment.setArguments(extra);

            return fragment;
        }
    }
}