package com.chaemil.hgms;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.MediaController;
import android.widget.VideoView;


/**
 * Created by chaemil on 23.10.14.
 */
public class VideoPlayer extends FragmentActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        //if (!LibsChecker.checkVitamioLibs(this))
        //    return;

        Bundle extras = getIntent().getExtras();
        String videoURL = extras.getString("videoLink");
        Uri vidUri = Uri.parse(videoURL);

        if (findViewById(R.id.similarVideosFrag) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(mVideoView);
        mVideoView.setMediaController(vidControl);
        mVideoView.start();

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
}
