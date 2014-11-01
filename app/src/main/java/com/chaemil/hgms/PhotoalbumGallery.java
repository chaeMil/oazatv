package com.chaemil.hgms;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.chaemil.hgms.Adapters.HomePageAdapters.PhotoalbumsAdapter;
import com.chaemil.hgms.Adapters.PhotoalbumAdapter;

/**
 * Created by chaemil on 30.10.14.
 */
public class PhotoalbumGallery extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        PhotoalbumAdapter mPagerAdapter = new PhotoalbumAdapter(getApplicationContext(),R.layout.photoalbum_photo);
        //mPager.setAdapter(mPagerAdapter);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    /*private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PhotoalbumSlider();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }*/
}