package com.chaemil.hgms;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaemil.hgms.db.AudioDBContract;
import com.chaemil.hgms.db.AudioDBContract.DownloadedAudio;
import com.chaemil.hgms.db.AudioDBHelper;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.concurrent.Future;

import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;


public class ListDownloadedAudio extends Activity {

    private LinearLayout audioGrid;
    private at.markushi.ui.CircleButton download;
    private TextView downloadCount;
    private ProgressBar progressBar;
    private LinearLayout downloadUI;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private long percent;
    private SQLiteDatabase db;
    private AudioDBHelper helper;
    private TextView downloadedAudioName;
    private Activity activity;

    public static final int NOTIFICATION_ID = 1;


    Future<File> downloading;

    private String getVideoId(Bundle b) {
        String s = b.getString(Basic.BUNDLE_VIDEO_LINK);
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
    }

    private String getVideoName(Bundle b) {
        return b.getString(Basic.VIDEO_NAME);
    }

    private String getAudioFileName(Bundle b, boolean fake) {
        if (fake) {
            return getVideoId(b) + Basic.EXTENSION_AUDIO;
        } else {
            return getVideoId(b) + Basic.EXTENSION_MP3;
        }

    }

    private String getAudioThumbFileName(Bundle b) {
        return getVideoId(b) + Basic.EXTENSION_JPG;
    }

    private String getVideoUrl(Bundle b) {
        return b.getString(Basic.VIDEO_LINK);
    }

    private String getVideoDate(Bundle b) {
        return b.getString(Basic.VIDEO_DATE);
    }

    void resetDownload() {
        // cancel any pending download
        downloading.cancel(true);
        downloading = null;
        MyApp.isDownloadingAudio = false;

        // reset the ui
        mNotifyManager.cancel(NOTIFICATION_ID);
        downloadCount.setText(null);
        progressBar.setProgress(0);
        downloadUI.setVisibility(View.GONE);
    }

    public void saveAudioToDb() {
        Bundle extras = getIntent().getExtras();

        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_FILE,
                getAudioFileName(extras, true));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_NAME,
                getVideoName(extras));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_THUMB,
                getAudioThumbFileName(extras)
                        .replace(Basic.EXTENSION_JPG, Basic.EXTENSION_THUMB));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_DATE,
                getVideoDate(extras));

        db.insert(DownloadedAudio.TABLE_NAME, null, values);
    }

    private void downloadAudio() {
        Bundle extras = getIntent().getExtras();
        String audioUrl = getVideoUrl(extras)
                .replace(Basic.EXTENSION_MP4, Basic.EXTENSION_MP3)
                .replace(Basic.EXTENSION_WEBM, Basic.EXTENSION_MP3);
        String thumbUrl = audioUrl
                .replace(Basic.EXTENSION_MP3, Basic.EXTENSION_JPG);
        String filename = audioUrl
                .substring(audioUrl.lastIndexOf("/") + 1, audioUrl.length())
                .replace(Basic.EXTENSION_MP3, Basic.EXTENSION_AUDIO);
        String thumbFilename = filename
                .replace(Basic.EXTENSION_AUDIO, Basic.EXTENSION_THUMB);

        helper = new AudioDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        if (!AudioDBHelper.audioFileExists(db, getAudioFileName(extras, true))) {

            MyApp.isDownloadingAudio = true;

            downloadedAudioName = (TextView) findViewById(R.id.downloadedAudioName);
            downloadedAudioName.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                    getIntent().getExtras().getString(Basic.VIDEO_NAME),
                    Basic.FONT_REGULAR_UPRIGHT));

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.downloading_audio_in_background),
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ListDownloadedAudio.class);

            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(getVideoName(extras))
                    .setContentText(getResources().getString(R.string.downloading_audio))
                    .setProgress(100, 0, false)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_white_logo)
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));
            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            while (MyApp.isDownloadingAudio) {
                                // Sets the progress indicator to a max value, the
                                // current completion percentage, and "determinate"
                                // state
                                mBuilder.setProgress(100, (int) percent, false);
                                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                                Log.i("percent", String.valueOf(percent));
                                try {
                                    // Sleep for 3 seconds
                                    Thread.sleep(3 * 1000);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                    // Starts the thread by calling the run() method in its Runnable
            ).start();


            File audioFile = new File(getExternalFilesDir(null), filename);
            File thumbFile = new File(getExternalFilesDir(null), thumbFilename);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            if (downloading != null && !downloading.isCancelled()) {
                resetDownload();
                return;
            }

            download = (at.markushi.ui.CircleButton) findViewById(R.id.download);
            downloadCount = (TextView) findViewById(R.id.downloadCount);
            downloadUI = (LinearLayout) findViewById(R.id.downloadUI);

            //downloadUI.setVisibility(View.VISIBLE);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDownload();
                }
            });

            downloading = Ion.with(ListDownloadedAudio.this)
                    .load(thumbUrl)
                    .write(thumbFile);

            downloading = Ion.with(ListDownloadedAudio.this)
                    .load(audioUrl)
                            // attach the percentage report to a progress bar.
                            // can also attach to a ProgressDialog with progressDialog.
                    .progressBar(progressBar)
                            // callbacks on progress can happen on the UI thread
                            // via progressHandler. This is useful if you need to update a TextView.
                            // Updates to TextViews MUST happen on the UI thread.
                    .progressHandler(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            //downloadCount.setText("" + downloaded + " / " + total);
                            percent = (long) ((float) downloaded / total * 100);
                            downloadCount.setText(percent + "%");
                            // update notification
                            mBuilder.setProgress(100, (int) percent, false);
                        }
                    })
                            // write to a file
                    .write(audioFile)
                            // run a callback on completion
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            resetDownload();
                            downloadUI.setVisibility(View.GONE);
                            if (e != null) {
                                Toast.makeText(ListDownloadedAudio.this,
                                        getResources().getString(R.string.error_downloading_audiofile),
                                        Toast.LENGTH_LONG).show();
                                return;
                            }


                            Log.i("filepath", String.valueOf(result.getAbsoluteFile()));

                            if (!AudioDBHelper.audioFileExists(db,
                                    getAudioFileName(getIntent().getExtras(), true))) {
                                Log.i("audioFileExists", "false, saving new record");
                                saveAudioToDb();
                            } else {
                                Log.i("audioFileExists", "true, doing nothing");
                            }

                            mBuilder.setContentText(getResources().getString(R.string.download_audiofile_completed))
                                    // Removes the progress bar
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setAutoCancel(true);
                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());

                            loadData();

                            LinearLayout noDownloadedAudioMessage = (LinearLayout)
                                    findViewById(R.id.noDownloadedAudioMessage);
                            if (noDownloadedAudioMessage != null) {
                                noDownloadedAudioMessage.setVisibility(View.GONE);
                            }

                            Toast.makeText(ListDownloadedAudio.this,
                                    getResources().getString(R.string.download_audiofile_completed),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(ListDownloadedAudio.this, getResources().getString(R.string.already_downloaded), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_downloaded_audio);

        downloadUI = (LinearLayout) findViewById(R.id.downloadUI);
        activity = this;

        setActionStatusBarTint(getWindow(), this, Basic.AUDIOPLAYER_STATUSBAR_COLOR,
                Basic.AUDIOPLAYER_ACTIONBAR_COLOR);

        if(getActionBar() !=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("download")) {
                if (extras.getBoolean("download")) {
                    downloadAudio();
                }
            }
        }

        audioGrid = (LinearLayout) findViewById(R.id.audioGrid);


        final Runnable r = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };

        r.run();

        Log.i("MyApp.isDownloading", String.valueOf(MyApp.isDownloadingAudio));

        if (MyApp.isDownloadingAudio) {
            downloadUI.setVisibility(View.VISIBLE);
        }

    }

    private void loadData() {
        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();


        String[] projection = {
                AudioDBContract.DownloadedAudio._ID,
                AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_NAME,
                AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_FILE,
                AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_DATE,
                AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_THUMB
        };

        String sortOrder = AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_DATE + " DESC";

        Cursor cursor = db.query(
                AudioDBContract.DownloadedAudio.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View view = inflater.inflate(R.layout.home_block, audioGrid, false);

                final String audioFileName = cursor.getString(
                        cursor.getColumnIndex(AudioDBContract
                                .DownloadedAudio.COLUMN_NAME_AUDIO_NAME));
                final String thumbFileName = getExternalFilesDir(null)
                        + "/" + cursor.getString(cursor.getColumnIndex(
                        AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_THUMB));
                final String audioDate = cursor.getString(cursor.getColumnIndex(
                        AudioDBContract.DownloadedAudio.COLUMN_NAME_AUDIO_DATE));
                final String audioFile = cursor.getString(cursor.getColumnIndex(
                        DownloadedAudio.COLUMN_NAME_AUDIO_FILE));

                TextView audioFileNameElement = (TextView) view.findViewById(R.id.videoName);
                audioFileNameElement.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                        audioFileName, "Titillium-BoldUpright.otf"));

                TextView dateElement = (TextView) view.findViewById(R.id.videoDate);
                dateElement.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                        audioDate, "Titillium-RegularUpright.otf"));


                long fileSize = new File(Uri.parse(getExternalFilesDir(null) + "/") + audioFile).length();
                Log.i("fileSize", String.valueOf(fileSize));

                TextView fileSizeElement = (TextView) view.findViewById(R.id.videoViews);
                fileSizeElement.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                        String.valueOf(fileSize / 1024 / 1024) + " Mb", "Titillium-RegularUpright.otf"));

                ImageView thumb = (ImageView) view.findViewById(R.id.thumb);
                thumb.setImageURI(Uri.parse(thumbFileName));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent audioPlayer = new Intent(ListDownloadedAudio.this, AudioPlayer.class);
                        audioPlayer.putExtra(Basic.AUDIO_FILE, audioFile);
                        audioPlayer.putExtra(Basic.AUDIO_FILE_NAME, audioFileName);
                        audioPlayer.putExtra(Basic.AUDIO_FILE_THUMB, thumbFileName);
                        audioPlayer.putExtra(Basic.AUDIO_FILE_DATE, audioDate);
                        startActivity(audioPlayer);
                        Utils.goForwardAnimation(activity);
                        //Toast.makeText(getApplicationContext(), audioFile, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                audioGrid.addView(view);

                cursor.moveToNext();
            }
        } else {
            RelativeLayout mainView = (RelativeLayout) findViewById(R.id.mainView);
            LinearLayout noAudioView = (LinearLayout) getLayoutInflater()
                    .inflate(R.layout.no_downloaded_audio_message, null);
            mainView.addView(noAudioView);
        }
    }

    @Override
    public void onBackPressed() {
        if (!MyApp.isDownloadingAudio) {
            finish();
            Utils.goBackwardAnimation(this);

        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApp.isDownloadingAudio) {
            resetDownload();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Utils.goBackwardAnimation(this);
                return true;
            default:
                return true;
        }
    }
}
