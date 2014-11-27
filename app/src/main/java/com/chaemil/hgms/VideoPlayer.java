package com.chaemil.hgms;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.chaemil.hgms.DB.AudioDBContract.DownloadedAudio;
import com.chaemil.hgms.DB.AudioDBHelper;
import com.wefika.flowlayout.FlowLayout;

import static com.chaemil.hgms.Utils.Utils.displayVideoTags;
import static com.chaemil.hgms.Utils.Utils.getScreenWidth;
import static com.chaemil.hgms.Utils.Utils.hideSystemUI;
import static com.chaemil.hgms.Utils.Utils.showSystemUI;


/**
 * Created by chaemil on 23.10.14.
 */
public class VideoPlayer extends FragmentActivity {

    private VideoView mVideoView;
    private Fragment fragment;
    private LinearLayout videoInfo;
    private SQLiteDatabase db;


    private String getVideoId(Bundle b) {
        String s = b.getString("videoLink");
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
    }

    private String getVideoName(Bundle b) {
        return b.getString("videoName");
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
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_ID, 0);
        values.put(DownloadedAudio.COLUMN_NAME_AUDIO_FILE, getVideoName(extras));

        db.insert(DownloadedAudio.TABLE_NAME,null,values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download_audio) {

            /*Bundle extras = getIntent().getExtras();
            String url = getVideoUrl(extras).replace("mp4","mp3").replace("webm","mp3");
            String filename = url.substring(url.lastIndexOf("/"),url.length());

            String downloadComplete = getResources().getString(R.string.download_complete);
            String title = getVideoName(extras);
            String downloadingAudio = getResources().getString(R.string.downloading_audio);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription(downloadingAudio);
            request.setTitle(title);
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalFilesDir(getApplicationContext(),Environment.DIRECTORY_DOWNLOADS, filename.replace(".mp3",".oazaAudio"));
            request.setVisibleInDownloadsUi(false);

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);*/


            saveAudioToDb();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
