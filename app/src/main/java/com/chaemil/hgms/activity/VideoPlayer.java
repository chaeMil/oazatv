package com.chaemil.hgms.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.chaemil.hgms.App;
import com.chaemil.hgms.R;
import com.chaemil.hgms.db.ArchiveDBHelper;
import com.chaemil.hgms.fragment.SimilarVideosFragment;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;
import com.wefika.flowlayout.FlowLayout;

import static com.chaemil.hgms.utils.Utils.getScreenWidth;
import static com.chaemil.hgms.utils.Utils.hideSystemUI;
import static com.chaemil.hgms.utils.Utils.showSystemUI;


public class VideoPlayer extends ActionBarActivity {

    private VideoView mVideoView;
    private Fragment fragment;
    private LinearLayout videoInfo;
    private MediaController mediaController;
    private ImageButton fullscreenButton;
    private ArchiveItem archiveItem;
    private NoisyAudioStreamReceiver noisyAudioReceiver;
    private ProgressBar progressBar;

    private ArchiveItem getArchiveItem(Bundle b) {
        return b.getParcelable(ArchiveItem.ARCHIVE_ITEM);
    }

    private void pause() {
        mVideoView.pause();
    }
    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                pause();
            }
        }
    }

    private IntentFilter headphonesListener = new IntentFilter(AudioManager
            .ACTION_AUDIO_BECOMING_NOISY);


    private void shareLink() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, archiveItem.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, archiveItem.getVideoURL());
        startActivity(Intent.createChooser(share, getString(R.string.action_share)));
    }

    private int loadVideoTimeFromDB(SQLiteDatabase db, String videoId) {
        if (ArchiveDBHelper.videoRecordExists(db,videoId)) {
            SmartLog.log("loadVideoTimeFromDB",
                    String.valueOf(ArchiveDBHelper.loadVideoTime(db, videoId)));
            return ArchiveDBHelper.loadVideoTime(db,videoId);

        }
        else {
            return 0;
        }
    }

    private void saveVideoTimeToDB(SQLiteDatabase db, String videoId, int videoTime) {
        ArchiveDBHelper.saveVideoTime(db,videoId,videoTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        archiveItem = getArchiveItem(getIntent().getExtras());

        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#000000")));
        }

        ArchiveDBHelper helper = new ArchiveDBHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getReadableDatabase();

        noisyAudioReceiver = new NoisyAudioStreamReceiver();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(archiveItem.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SmartLog.log("archiveItem.getVideoUrl()", archiveItem.getVideoURL());

        String idToSubmitViews = archiveItem.getVideoURL();
        idToSubmitViews = idToSubmitViews
                .substring(idToSubmitViews.lastIndexOf("/")+1,idToSubmitViews.lastIndexOf("."));

        //submit video view
        Utils.sendGet(Constants.MAIN_SERVER + "?page=vp-stats&source=app&video=" + idToSubmitViews,
                getApplicationContext());
        Utils.submitStatistics(getApplicationContext());


        TextView videoViewsElement = (TextView) findViewById(R.id.videoViews);
        videoViewsElement.setText(archiveItem.getVideoViews());
        TextView videoDateElement = (TextView) findViewById(R.id.videoDate);
        videoDateElement.setText(archiveItem.getVideoDate());
        videoInfo = (LinearLayout) findViewById(R.id.videoInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FlowLayout videoTags = (FlowLayout) findViewById(R.id.videoTags);

        //displayVideoTags(getApplicationContext(), this, archiveItem.getVideoDBID(), videoTags);

        mVideoView = (VideoView) findViewById(R.id.videoView);

        mediaController = new MediaController(this);

        fullscreenButton = (ImageButton) findViewById(R.id.fullscreenButton);


        Uri video;

        // Lollipop video hack
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= Build.VERSION_CODES.KITKAT){
            video = Uri.parse(archiveItem.getVideoURL());
        } else{
            video = Uri.parse(archiveItem.getVideoURL().replace(Constants.EXTENSION_WEBM, Constants.EXTENSION_MP4));
        }

        //Uri video = Uri.parse("http://oaza.tv/root/db/2014-11-23-lxaAhP.webm");
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(video);


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mVideoView.seekTo(loadVideoTimeFromDB(db, archiveItem.getVideoDBID()));
                mVideoView.start();
                progressBar.setVisibility(View.GONE);
            }
        });

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSystemUI((ActionBarActivity)getParent());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hideSystemUI((ActionBarActivity)getParent());

                        SmartLog.log("screenOrientation", "tappedVideo");

                    }
                }, 1000);
            }
        });

        //tabletový rozhraní
        if (findViewById(R.id.rightFrag) != null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            FragmentManager fragmentManager = getFragmentManager();

            fragment = new SimilarVideosFragment();

            Bundle args = new Bundle();
            args.putString(Constants.VIDEO_LINK, archiveItem.getVideoURL());

            fragment.setArguments(args);

            //TODO tablet fullscreen video
            /*fullscreenButton.setVisibility(View.VISIBLE);
            fullscreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FrameLayout rightFrag = (FrameLayout) findViewById(R.id.rightFrag);
                    rightFrag.setVisibility(View.GONE);
                    videoInfo.setVisibility(View.GONE);
                    DisplayMetrics metrics = new DisplayMetrics(); getWindowManager()
                            .getDefaultDisplay().getMetrics(metrics);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                            mVideoView.getLayoutParams();
                    params.width =  metrics.widthPixels;
                    params.height = metrics.heightPixels;
                    params.leftMargin = 0;
                    mVideoView.pause();
                    videoTime = mVideoView.getCurrentPosition();
                    mVideoView.setLayoutParams(params);
                    mVideoView.invalidate();
                    mVideoView.resume();
                    mVideoView.seekTo(videoTime);
                    View root = mVideoView.getRootView();
                    root.setBackgroundColor(getResources().getColor(android.R.color.black));
                    fullscreenButton.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_media_fullscreen_shrink));
                }
            });*/

            fragmentManager.beginTransaction()
                    .replace(R.id.rightFrag, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        screenOrientation();

    }

    public void screenOrientation()
    {
        if (findViewById(R.id.rightFrag) == null) {
            Display getOrient = getWindowManager().getDefaultDisplay();
            if (getOrient.getWidth() == getOrient.getHeight()) {
            } else {
                if (getOrient.getWidth() < getOrient.getHeight()) {
                    showSystemUI(this);
                    videoInfo.setVisibility(View.VISIBLE);
                    mVideoView.getLayoutParams().width = getScreenWidth(getApplicationContext());
                    View root = mVideoView.getRootView();
                    root.setBackgroundColor(getResources().getColor(android.R.color.white));
                } else {
                    hideSystemUI(this);
                    videoInfo.setVisibility(View.GONE);
                    mVideoView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    View root = mVideoView.getRootView();
                    root.setBackgroundColor(getResources().getColor(android.R.color.black));
                }
            }
        } else {
            mVideoView.getLayoutParams().width = (int) (Utils
                    .getScreenWidth(getApplicationContext()) * 0.68);
            mVideoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        screenOrientation();

    }


    @Override
    protected void onResume() {
        super.onResume();
        ArchiveDBHelper helper = new ArchiveDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        mVideoView.start();
        mVideoView.seekTo(loadVideoTimeFromDB(db, archiveItem.getVideoDBID()));
        if(loadVideoTimeFromDB(db, archiveItem.getVideoDBID()) != 0) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.resuming_playback),Toast.LENGTH_LONG).show();
        }

        registerReceiver(noisyAudioReceiver,headphonesListener);
    }

    @Override
    protected void onPause() {
        PowerManager pm =(PowerManager) getSystemService(Context.POWER_SERVICE);
        ArchiveDBHelper helper = new ArchiveDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        saveVideoTimeToDB(db, archiveItem.getVideoDBID(), mVideoView.getCurrentPosition());
        if (pm.isScreenOn()) {
            mVideoView.suspend();
        } else {
            mVideoView.pause();
        }
        unregisterReceiver(noisyAudioReceiver);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_video_player, menu);

        return true;
    }

    public boolean isDownloadingAudio() {
        return App.isDownloadingAudio;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();
                Utils.goBackwardAnimation(this);
                return true;
            case R.id.action_download_audio:
                if (isDownloadingAudio()) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.already_downloading),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(this,ListDownloadedAudio.class);
                    i.putExtra("download",true);
                    i.putExtra(Constants.VIDEO_LINK, archiveItem.getVideoURL());
                    i.putExtra(Constants.VIDEO_DATE, archiveItem.getVideoDate());
                    i.putExtra(Constants.VIDEO_NAME, archiveItem.getTitle());
                    startActivity(i);
                }
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
        finish();
    }
}
