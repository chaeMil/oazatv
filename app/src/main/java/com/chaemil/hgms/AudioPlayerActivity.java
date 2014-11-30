package com.chaemil.hgms;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chaemil.hgms.db.AudioDBContract;
import com.chaemil.hgms.db.AudioDBContract.DownloadedAudio;
import com.chaemil.hgms.db.AudioDBHelper;
import com.squareup.picasso.Picasso;


public class AudioPlayerActivity extends Activity {

    private LinearLayout audioGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);


        audioGrid = (LinearLayout) findViewById(R.id.audioGrid);


        final Runnable r = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };

        r.run();

    }

    private void loadData() {
        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();


        String[] projection = {
                DownloadedAudio._ID,
                DownloadedAudio.COLUMN_NAME_AUDIO_NAME,
                DownloadedAudio.COLUMN_NAME_AUDIO_FILE,
                DownloadedAudio.COLUMN_NAME_AUDIO_DATE,
                DownloadedAudio.COLUMN_NAME_AUDIO_THUMB
        };

        String sortOrder = DownloadedAudio.COLUMN_NAME_AUDIO_DATE + " DESC";

        Cursor cursor = db.query(
                DownloadedAudio.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View view = inflater.inflate(R.layout.home_block, audioGrid, false);

                String audioFileName = cursor.getString(cursor.getColumnIndex(DownloadedAudio.COLUMN_NAME_AUDIO_NAME));
                String thumbFileName = getExternalFilesDir(null) + "/" + cursor.getString(cursor.getColumnIndex(DownloadedAudio.COLUMN_NAME_AUDIO_THUMB));
                String audioDate = cursor.getString(cursor.getColumnIndex(DownloadedAudio.COLUMN_NAME_AUDIO_DATE));

                TextView audioFileNameElement = (TextView) view.findViewById(R.id.videoName);
                audioFileNameElement.setText(audioFileName);

                TextView dateElement = (TextView) view.findViewById(R.id.videoDate);
                dateElement.setText(audioDate);

                ImageView thumb = (ImageView) view.findViewById(R.id.thumb);
                thumb.setImageURI(Uri.parse(thumbFileName));

                audioGrid.addView(view);

                cursor.moveToNext();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
