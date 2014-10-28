package com.chaemil.hgms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.chaemil.hgms.Adapters.PhotoalbumAdapter;

import static com.chaemil.hgms.Utils.Utils.fetchPhotoalbum;


public class PhotoalbumActivity extends Activity {

    private PhotoalbumAdapter photoalbumAdapter;

    private String getAlbumId(Bundle b) {
        String s = b.getString("albumId");
        return s.substring(s.lastIndexOf("album=")+6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum);

        Bundle extras = getIntent().getExtras();

        String albumId = getAlbumId(extras);

        GridView photoThumbsGrid = (GridView) findViewById(R.id.photoThumbsGrid);
        photoalbumAdapter = new PhotoalbumAdapter(this);

        photoThumbsGrid.setAdapter(photoalbumAdapter);

        Log.i("photoalbum", getApplicationContext().getResources().getString(R.string.mainServerJson) + "?page=photoalbum&albumId=" + albumId);
        fetchPhotoalbum(getApplicationContext(), photoalbumAdapter, albumId);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photoalbum, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}