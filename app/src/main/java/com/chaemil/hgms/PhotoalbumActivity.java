package com.chaemil.hgms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.utils.Basic;

import static com.chaemil.hgms.utils.Utils.fetchPhotoalbum;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;


public class PhotoalbumActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum);


        Bundle extras = getIntent().getExtras();

        final String albumId = extras.getString(Basic.ALBUM_ID);


        Log.i(Basic.ALBUM_ID, albumId);

        GridView photoThumbsGrid = (GridView) findViewById(R.id.photoThumbsGrid);
        PhotoalbumAdapter mPhotoalbumAdapter = new PhotoalbumAdapter(getApplicationContext(),R.layout.photo_thumb);

        fetchPhotoalbum(getApplicationContext(),mPhotoalbumAdapter,albumId);

        Log.i("count", String.valueOf(mPhotoalbumAdapter.getCount()));

        photoThumbsGrid.setAdapter(mPhotoalbumAdapter);
        photoThumbsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView photoIdElement =  (TextView) v.findViewById(R.id.photoId);
                CharSequence photoId = photoIdElement.getText();
                Intent intent = new Intent(getApplicationContext(), PhotoalbumSlideshow.class);
                intent.putExtra(Basic.PHOTO_ID,photoId);
                intent.putExtra(Basic.ALBUM_ID,albumId);
                startActivity(intent);
            }
        });


        Log.i("screenWidth", String.valueOf(getScreenWidth(getApplicationContext())));
        Log.i("screenWidth / "+String.valueOf(R.integer.gallery_columns), String.valueOf(getScreenWidth(getApplicationContext())/R.integer.gallery_columns));
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
