package com.chaemil.hgms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
}
