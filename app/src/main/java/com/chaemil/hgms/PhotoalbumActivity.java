package com.chaemil.hgms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.chaemil.hgms.Adapters.PhotoalbumAdapter;

import static com.chaemil.hgms.Utils.Utils.fetchPhotoalbum;
import static com.chaemil.hgms.Utils.Utils.getScreenWidth;


public class PhotoalbumActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum);


        Bundle extras = getIntent().getExtras();

        final String albumId = extras.getString("albumId");


        Log.i("albumId", albumId);

        GridView photoThumbsGrid = (GridView) findViewById(R.id.photoThumbsGrid);
        PhotoalbumAdapter mPhotoalbumAdapter = new PhotoalbumAdapter(getApplicationContext(),R.layout.photo_thumb);

        fetchPhotoalbum(getApplicationContext(),mPhotoalbumAdapter,albumId);

        Log.i("count", String.valueOf(mPhotoalbumAdapter.getCount()));

        photoThumbsGrid.setAdapter(mPhotoalbumAdapter);


        Log.i("screenWidth", String.valueOf(getScreenWidth(getApplicationContext())));
        Log.i("screenWidth / 3", String.valueOf(getScreenWidth(getApplicationContext())/3));
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
