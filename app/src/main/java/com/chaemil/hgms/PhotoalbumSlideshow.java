package com.chaemil.hgms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.chaemil.hgms.Adapters.PhotoalbumAdapter;

import static com.chaemil.hgms.Utils.Utils.fetchPhotoalbum;


public class PhotoalbumSlideshow extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum_slideshow);

        Bundle extras = getIntent().getExtras();
        String albumId = extras.getString("albumId");
        final String photoId = extras.getString("photoId");

        final ListView slideshow = (ListView) findViewById(R.id.slideshow);
        PhotoalbumAdapter slideshowAdapter = new PhotoalbumAdapter(getApplicationContext(),R.layout.photoalbum_photo);

        Log.i("scrollTo",photoId);
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
