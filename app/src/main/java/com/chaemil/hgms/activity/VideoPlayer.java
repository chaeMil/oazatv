package com.chaemil.hgms.activity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chaemil.hgms.App;
import com.chaemil.hgms.R;
import com.chaemil.hgms.db.ArchiveDBHelper;
import com.chaemil.hgms.factory.RequestFactory;
import com.chaemil.hgms.factory.RequestFactoryListener;
import com.chaemil.hgms.factory.ResponseFactory;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.RequestType;
import com.chaemil.hgms.service.MyRequestService;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.chaemil.hgms.utils.Utils.getScreenWidth;
import static com.chaemil.hgms.utils.Utils.hideSystemUI;
import static com.chaemil.hgms.utils.Utils.showSystemUI;


public class VideoPlayer extends ActionBarActivity implements RequestFactoryListener {

    private VideoView mVideoView;
    private Fragment fragment;
    private LinearLayout videoInfo;
    private MediaController mediaController;
    private ImageButton fullscreenButton;
    private ArchiveItem archiveItem;
    private NoisyAudioStreamReceiver noisyAudioReceiver;
    private ProgressBar progressBar;
    private IntentFilter headphonesListener = new IntentFilter(AudioManager
            .ACTION_AUDIO_BECOMING_NOISY);
    private TextView videoViewsElement;
    private TextView videoDateElement;
    private String idToSubmitViews;
    private Uri video;
    private FlowLayout videoTags;
    private ArchiveDBHelper helper;
    private SQLiteDatabase db;
    private ArrayList<String> videoTagsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        archiveItem = getArchiveItem(getIntent().getExtras());
        noisyAudioReceiver = new NoisyAudioStreamReceiver();
        idToSubmitViews = archiveItem.getVideoURL();
        idToSubmitViews = idToSubmitViews
                .substring(idToSubmitViews.lastIndexOf("/") + 1, idToSubmitViews.lastIndexOf("."));

        helper = new ArchiveDBHelper(getApplicationContext());
        db = helper.getReadableDatabase();

        Utils.sendGet(Constants.MAIN_SERVER + "?page=vp-stats&source=app&video=" + idToSubmitViews,
                getApplicationContext());
        Utils.submitStatistics(getApplicationContext());


        // Lollipop video hack
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= Build.VERSION_CODES.KITKAT){
            video = Uri.parse(archiveItem.getVideoURL());
        } else{
            video = Uri.parse(archiveItem.getVideoURL().replace(Constants.EXTENSION_WEBM, Constants.EXTENSION_MP4));
        }

        getUI();
        setupUI();
        getData();
    }

    private void getData() {
        Request tagsRequest = RequestFactory.getVideoTags(this, archiveItem.getVideoDBID());
        MyRequestService.getRequestQueue().add(tagsRequest);
    }

    private void getUI() {
        videoViewsElement = (TextView) findViewById(R.id.videoViews);
        videoDateElement = (TextView) findViewById(R.id.videoDate);
        videoInfo = (LinearLayout) findViewById(R.id.videoInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        videoTags = (FlowLayout) findViewById(R.id.videoTags);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        fullscreenButton = (ImageButton) findViewById(R.id.fullscreenButton);
    }

    private void setupUI() {
        videoViewsElement.setText(archiveItem.getVideoViews());
        videoDateElement.setText(archiveItem.getDate());
        mediaController = new MediaController(this);
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

                showSystemUI((ActionBarActivity) getParent());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hideSystemUI((ActionBarActivity) getParent());

                        SmartLog.log("screenOrientation", "tappedVideo");

                    }
                }, 1000);
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#000000")));
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(archiveItem.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    getString(R.string.resuming_playback), Toast.LENGTH_LONG).show();
        }

        registerReceiver(noisyAudioReceiver, headphonesListener);
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
                    i.putExtra(Constants.VIDEO_DATE, archiveItem.getDate());
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

    private ArchiveItem getArchiveItem(Bundle b) {
        return b.getParcelable(ArchiveItem.ARCHIVE_ITEM);
    }

    private void pause() {
        mVideoView.pause();
    }

    @Override
    public void onSuccessResponse(JSONObject response, RequestType requestType) {
        SmartLog.log("VideoPlayer response", String.valueOf(response));

        switch (requestType) {
            case VIDEO_TAGS:
                videoTagsArray = ResponseFactory.parseVideoTags(response);

                SmartLog.log("videoTagsArray length", String.valueOf(videoTagsArray.size()));

                for(int i = 0; i < videoTagsArray.size(); i++) {

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view  = inflater.inflate(R.layout.tag, videoTags, false);

                    final TextView tagElement = (TextView) view.findViewById(R.id.tag);
                    tagElement.setText("#" + videoTagsArray.get(i));

                    tagElement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            i.putExtra(Constants.BUNDLE_TAG, tagElement.getText().toString().replace("#", ""));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(i);
                            Utils.goBackwardAnimation(VideoPlayer.this);
                        }
                    });

                    videoTags.addView(view);
                }
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError exception) {
        Toast.makeText(this, getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT).show();
        SmartLog.log("errorResponse", String.valueOf(exception));
    }

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                pause();
            }
        }
    }

    public void screenOrientation() {
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
        ArchiveDBHelper.saveVideoTime(db, videoId, videoTime);
    }
}
