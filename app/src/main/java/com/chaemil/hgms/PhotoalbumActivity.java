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
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.utils.Utils.fetchPhotoalbum;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;
import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;


public class PhotoalbumActivity extends Activity {

    private String getAlbumId() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.ALBUM_ID);
    }

    private String getAlbumName() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.ALBUM_NAME);
    }

    private String getAlbumDate() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.ALBUM_DATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum);

        setActionStatusBarTint(getWindow(),this,null,"#58C2FB");

        final String albumId = getAlbumId();


        Log.i(Basic.ALBUM_ID, albumId);

        GridView photoThumbsGrid = (GridView) findViewById(R.id.photoThumbsGrid);
        PhotoalbumAdapter mPhotoalbumAdapter = new PhotoalbumAdapter(getApplicationContext(),
                R.layout.photo_thumb);

        if(getActionBar() != null) {
            getActionBar().setTitle(getAlbumName());
            getActionBar().setSubtitle(getAlbumDate());
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fetchPhotoalbum(getApplicationContext(),mPhotoalbumAdapter,albumId);

        Log.i("count", String.valueOf(mPhotoalbumAdapter.getCount()));

        photoThumbsGrid.setAdapter(mPhotoalbumAdapter);
        photoThumbsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView photoIdElement =  (TextView) v.findViewById(R.id.photoId);
                CharSequence photoId = photoIdElement.getText();
                TextView photoUrlElement = (TextView) v.findViewById(R.id.photoUrl);
                CharSequence photoUrl = photoUrlElement.getText();
                //Intent intent = new Intent(getApplicationContext(), PhotoalbumSlideshow.class);
                Intent intent = new Intent(getApplicationContext(), SinglePhoto.class);
                intent.putExtra(Basic.PHOTO_ID,photoId);
                intent.putExtra(Basic.ALBUM_ID,albumId);
                intent.putExtra(Basic.PHOTO_URL,photoUrl);
                startActivity(intent);
            }
        });


        Log.i("screenWidth", String.valueOf(getScreenWidth(getApplicationContext())));
        Log.i("screenWidth / "+String.valueOf(R.integer.gallery_columns),
                String.valueOf(getScreenWidth(getApplicationContext())/R.integer.gallery_columns));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photoalbum, menu);
        return true;
    }

    public void shareLink() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getAlbumName());
        share.putExtra(Intent.EXTRA_TEXT, Basic.MAIN_SERVER_PHOTOALBUM_LINK_PREFIX + getAlbumId());
        startActivity(Intent.createChooser(share, getResources().getString(R.string.action_share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                Utils.goBackwardAnimation(this);
                return true;
            case R.id.action_share_link:
                shareLink();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.goBackwardAnimation(this);
    }
}
