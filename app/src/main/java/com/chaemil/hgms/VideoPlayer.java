package com.chaemil.hgms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;
import com.wefika.flowlayout.FlowLayout;

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
    private ProgressBar videoSpinner;


    private String getVideoId(Bundle b) {
        String s = b.getString(Basic.BUNDLE_VIDEO_LINK);
        return s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
    }

    private String getVideoName(Bundle b) {
        return b.getString(Basic.VIDEO_NAME);
    }

    /*private String getAudioFileName(Bundle b, boolean fake) {
        if (fake) {
            return getVideoId(b)+Basic.EXTENSION_AUDIO;
        }
        else {
            return getVideoId(b)+Basic.EXTENSION_MP3;
        }

    }

    private String getAudioThumbFileName(Bundle b) {
        return getVideoId(b)+Basic.EXTENSION_JPG;
    }*/

    private void pause() {
        mVideoView.pause();
    }

    private String getVideoUrl(Bundle b) {
        return b.getString(Basic.VIDEO_LINK);
    }

    private String getVideoDate(Bundle b) {
        return b.getString(Basic.VIDEO_DATE);
    }

    private String getVideoViews(Bundle b) {
        return b.getString(Basic.VIDEO_VIEWS);
    }

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                pause();
            }
        }
    }

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);


    private void shareLink() {
        Bundle bundle = getIntent().getExtras();
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getVideoName(bundle));
        share.putExtra(Intent.EXTRA_TEXT, Basic.MAIN_SERVER_VIDEO_LINK_PREFIX + getVideoId(bundle));
        startActivity(Intent.createChooser(share, getResources().getString(R.string.action_share)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        if(getActionBar() != null) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        }

        //if (!LibsChecker.checkVitamioLibs(this))
        //    return;

        registerReceiver(new NoisyAudioStreamReceiver(), intentFilter);

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
            args.putString(Basic.VIDEO_LINK, getVideoUrl(extras));

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
        videoSpinner = (ProgressBar) findViewById(R.id.videoSpinner);

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
            video = Uri.parse(videoURL.replace(Basic.EXTENSION_WEBM, Basic.EXTENSION_MP4));
        }

        //Uri video = Uri.parse("http://oaza.tv/root/db/2014-11-23-lxaAhP.webm");
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(video);
        mVideoView.start();


        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        videoSpinner.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        videoSpinner.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        videoSpinner.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
        }
        else {
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoSpinner.setVisibility(View.GONE);
                    mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            Log.i("percent", String.valueOf(percent));
                            if (percent < 50) {
                                videoSpinner.setVisibility(View.VISIBLE);
                            }
                            else {
                                videoSpinner.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }

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
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (findViewById(R.id.rightFrag) == null) {
            Display getOrient = getWindowManager().getDefaultDisplay();
            if (getOrient.getWidth() == getOrient.getHeight()) {
            } else {
                if (getOrient.getWidth() < getOrient.getHeight()) {
                    showSystemUI(this, getCurrentFocus());
                    videoInfo.setVisibility(View.VISIBLE);
                    mVideoView.getLayoutParams().width = getScreenWidth(getApplicationContext());


                } else {
                    hideSystemUI(this, getCurrentFocus());
                    videoInfo.setVisibility(View.GONE);
                    mVideoView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

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
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.already_downloading),Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle extras = getIntent().getExtras();
                    Intent i = new Intent(this,ListDownloadedAudio.class);
                    i.putExtra("download",true);
                    i.putExtra(Basic.VIDEO_LINK, getVideoUrl(extras));
                    i.putExtra(Basic.VIDEO_DATE, getVideoDate(extras));
                    i.putExtra(Basic.VIDEO_NAME, getVideoName(extras));
                    Log.i(Basic.VIDEO_NAME, getVideoName(extras));
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
    }
}
