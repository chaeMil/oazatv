package com.chaemil.hgms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.chaemil.hgms.db.AudioDBContract.DownloadedAudio;
import com.chaemil.hgms.db.AudioDBHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.wefika.flowlayout.FlowLayout;

import java.io.File;
import java.util.concurrent.Future;

import static com.chaemil.hgms.utils.Utils.displayVideoTags;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;
import static com.chaemil.hgms.utils.Utils.hideSystemUI;
import static com.chaemil.hgms.utils.Utils.showSystemUI;


/**
 * Created by chaemil on 23.10.14.
 */
public class VideoPlayer extends FragmentActivity {

    private VideoView mVideoView;
    private Fragment fragment;
    private LinearLayout videoInfo;
    private Button download;
    private TextView downloadCount;
    private ProgressBar progressBar;
    private LinearLayout downloadUI;

    private SQLiteDatabase db;
    private AudioDBHelper helper;


    Future<File> downloading;


    private String getVideoId(Bundle b) {
        String s = b.getString("videoLink");
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
    }

    private String getVideoName(Bundle b) {
        return b.getString("videoName");
    }

    private String getAudioFileName(Bundle b, boolean fake) {
        if (fake) {
            return getVideoId(b)+".mp3";
        }
        else {
            return getVideoId(b)+".audio";
        }

    }

    private String getAudioThumbFileName(Bundle b) {
        return getVideoId(b)+".jpg";
    }

    private String getVideoUrl(Bundle b) {
        return b.getString("videoLink");
    }

    private String getVideoDate(Bundle b) {
        return b.getString("videoDate");
    }

    private String getVideoViews(Bundle b) {
        return b.getString("videoViews");
    }

    void resetDownload() {
        // cancel any pending download
        downloading.cancel(true);
        downloading = null;

        // reset the ui
        download.setText("Download");
        downloadCount.setText(null);
        progressBar.setProgress(0);
    }

    private void downloadAudio() {
        Bundle extras = getIntent().getExtras();
        String audioUrl = getVideoUrl(extras).replace("mp4","mp3").replace("webm","mp3");
        String thumbUrl = audioUrl.replace(".mp3",".jpg");
        String filename = audioUrl.substring(audioUrl.lastIndexOf("/")+1,audioUrl.length()).replace(".mp3",".audio");
        String thumbFilename = filename.replace(".audio",".thumb");

        helper = new AudioDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        if (!AudioDBHelper.audioFileExists(db, getAudioFileName(extras,true))) {

            File audioFile = new File(getExternalFilesDir(null), filename);
            File thumbFile = new File(getExternalFilesDir(null), thumbFilename);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            if (downloading != null && !downloading.isCancelled()) {
                resetDownload();
                return;
            }

            download = (Button) findViewById(R.id.download);
            downloadCount = (TextView) findViewById(R.id.downloadCount);
            downloadUI = (LinearLayout) findViewById(R.id.downloadUI);

            downloadUI.setVisibility(View.VISIBLE);

            download.setText(getResources().getString(R.string.cancel_audio_download));
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetDownload();
                }
            });

            downloading = Ion.with(VideoPlayer.this)
                .load(thumbUrl)
                .write(thumbFile);

            downloading = Ion.with(VideoPlayer.this)
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
                        long percent = (long) ((float) downloaded / total * 100);
                        downloadCount.setText(percent + "%");
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
                            Toast.makeText(VideoPlayer.this, getResources().getString(R.string.error_downloading_audiofile), Toast.LENGTH_LONG).show();
                            return;
                        }


                        Log.i("filepath", String.valueOf(result.getAbsoluteFile()));

                        if (!AudioDBHelper.audioFileExists(db, getAudioFileName(getIntent().getExtras(),true))) {
                            Log.i("audioFileExists", "false, saving new record");
                            saveAudioToDb();
                        } else {
                            Log.i("audioFileExists", "true, doing nothing");
                        }

                        Toast.makeText(VideoPlayer.this, getResources().getString(R.string.download_audiofile_completed), Toast.LENGTH_LONG).show();
                    }
                });
        }
        else {
            Toast.makeText(VideoPlayer.this, getResources().getString(R.string.already_downloaded), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        //if (!LibsChecker.checkVitamioLibs(this))
        //    return;

        Bundle extras = getIntent().getExtras();
        String videoID = getVideoId(extras);
        String videoName = getVideoName(extras);
        String videoViews = getVideoViews(extras);
        String videoDate = getVideoDate(extras);
        String videoURL = getVideoUrl(extras);

        if(getActionBar() != null) {
            getActionBar().setTitle(videoName);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //tabletový rozhraní
        if (findViewById(R.id.rightFrag) != null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            FragmentManager fragmentManager = getFragmentManager();

            fragment = new SimilarVideosFragment();

            Bundle args = new Bundle();
            args.putString("videoLink", getVideoUrl(extras));

            fragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.rightFrag, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        TextView videoViewsElement = (TextView) findViewById(R.id.videoViews);
        videoViewsElement.setText(videoViews);
        TextView videoDateElement = (TextView) findViewById(R.id.videoDate);
        videoDateElement.setText(videoDate);
        videoInfo = (LinearLayout) findViewById(R.id.videoInfo);

        FlowLayout videoTags = (FlowLayout) findViewById(R.id.videoTags);

        displayVideoTags(getApplicationContext(), videoID, videoTags);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);

        Uri video;

        // Lollipop video hack
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= Build.VERSION_CODES.KITKAT){
            video = Uri.parse(videoURL);
        } else{
            video = Uri.parse(videoURL.replace(".webm",".mp4"));
        }

        //Uri video = Uri.parse("http://oaza.tv/root/db/2014-11-23-lxaAhP.webm");
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(video);
        mVideoView.start();

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSystemUI(getParent(), getCurrentFocus());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hideSystemUI(getParent(), getCurrentFocus());

                        Log.i("screenOrientation","tappedVideo");

                    }
                }, 1000);
            }
        });

        screenOrientation();

        /*mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setBufferSize(1024*1024*8);
        mVideoView.setVideoURI(Uri.parse(videoURL));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
                setProgressBarIndeterminateVisibility(false);
            }
        });*/
    }

    public void screenOrientation()
    {
        if (findViewById(R.id.rightFrag) == null) {
            Display getOrient = getWindowManager().getDefaultDisplay();
            //int orientation = Configuration.ORIENTATION_UNDEFINED;
            if (getOrient.getWidth() == getOrient.getHeight()) {
                //orientation = Configuration.ORIENTATION_SQUARE;
            } else {
                if (getOrient.getWidth() < getOrient.getHeight()) {
                    //orientation = Configuration.ORIENTATION_PORTRAIT;
                    showSystemUI(this, getCurrentFocus());
                    videoInfo.setVisibility(View.VISIBLE);
                    mVideoView.getLayoutParams().width = getScreenWidth(getApplicationContext());
                    //mVideoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;


                    //mVideoView.invalidate();

                } else {
                    //orientation = Configuration.ORIENTATION_LANDSCAPE;
                    hideSystemUI(this, getCurrentFocus());
                    videoInfo.setVisibility(View.GONE);
                    //mVideoView.getLayoutParams().width = getScreenWidth(getApplicationContext());
                    mVideoView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    //mVideoView.getLayoutParams().height = getScreenHeight(getApplicationContext());

                    //mVideoView.invalidate();

                }
            }
        }
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getCurrentFocus().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        screenOrientation();

    }



    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_video_player, menu);

        return true;
    }

    public void saveAudioToDb() {
        Bundle extras = getIntent().getExtras();

        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_FILE, getAudioFileName(extras,true));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_NAME, getVideoName(extras));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_THUMB, getAudioThumbFileName(extras).replace(".jpg",".thumb"));
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_DATE, getVideoDate(extras));

        db.insert(DownloadedAudio.TABLE_NAME,null,values);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(downloading != null) {
            resetDownload();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download_audio) {



            downloadAudio();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
