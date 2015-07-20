package com.chaemil.hgms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;


public class PhotoalbumSlideshow extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum_slideshow);

        Bundle extras = getIntent().getExtras();
        String albumId = extras.getString(Constants.ALBUM_ID);
        final String photoId = extras.getString(Constants.PHOTO_ID);

        final ListView slideshow = (ListView) findViewById(R.id.slideshow);
        PhotoalbumAdapter slideshowAdapter = new PhotoalbumAdapter(
                getApplicationContext(),R.layout.photoalbum_photo);

        SmartLog.log("scrollTo", photoId);

        slideshow.setAdapter(slideshowAdapter);

        slideshow.post(new Runnable() {
            public void run() {
                slideshow.setSelection(Integer.parseInt(photoId));
            }
        });

        //slideshow.setSelection(Integer.parseInt(photoId));
    }
}
