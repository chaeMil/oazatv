package com.chaemil.hgms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.utils.Utils.fetchPhotoalbum;


public class PhotoalbumSlideshow extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum_slideshow);

        Bundle extras = getIntent().getExtras();
        String albumId = extras.getString(Basic.ALBUM_ID);
        final String photoId = extras.getString(Basic.PHOTO_ID);

        final ListView slideshow = (ListView) findViewById(R.id.slideshow);
        PhotoalbumAdapter slideshowAdapter = new PhotoalbumAdapter(getApplicationContext(),R.layout.photoalbum_photo);

        Utils.log("scrollTo", photoId);
        fetchPhotoalbum(getApplicationContext(), slideshowAdapter, albumId);

        slideshow.setAdapter(slideshowAdapter);

        slideshow.post(new Runnable() {
            public void run() {
                slideshow.setSelection(Integer.parseInt(photoId));
            }
        });

        //slideshow.setSelection(Integer.parseInt(photoId));
    }
}
