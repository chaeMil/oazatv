package com.chaemil.hgms.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chaemil.hgms.R;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;
import com.chaemil.hgms.youtube.DeveloperKey;
import com.chaemil.hgms.youtube.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LivePlayer extends YouTubeFailureRecoveryActivity implements
        View.OnClickListener,
        YouTubePlayer.OnFullscreenListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private Button fullscreenButton;
    private View otherViews;
    private String videoId;
    private YouTubePlayer.PlayerStyle playerStyleMinimal = YouTubePlayer.PlayerStyle.MINIMAL;
    private YouTubePlayer.PlayerStyle playerStyleDefault = YouTubePlayer.PlayerStyle.DEFAULT;

    private boolean fullscreen = true;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.live_player);
        baseLayout = (LinearLayout) findViewById(R.id.layout);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        fullscreenButton = (Button) findViewById(R.id.fullscreen_button);
        otherViews = findViewById(R.id.other_views);

        fullscreenButton.setOnClickListener(this);

        videoId = getIntent().getExtras().getString(Basic.YOUTUBE_VIDEO_ID);


        playerView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        doLayout();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {

        this.player = player;
        setControlsEnabled();
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        player.setPlayerStyle(playerStyleMinimal);
        player.setShowFullscreenButton(false);
        if (!wasRestored) {
            //videoId = videoId + "";
            player.cueVideo(String.valueOf(videoId));
        }

        int controlFlags = player.getFullscreenControlFlags();
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onClick(View v) {
        player.setFullscreen(!fullscreen);
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) playerView.getLayoutParams();
        if (fullscreen) {
            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            otherViews.setVisibility(View.GONE);
        } else {
            // This layout is up to you - this is just a simple example (vertically stacked boxes in
            // portrait, horizontally stacked in landscape).
            otherViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = otherViewsParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                otherViewsParams.height = MATCH_PARENT;
                playerParams.weight = 1;
                baseLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                playerParams.width = otherViewsParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
                otherViewsParams.height = 0;
                baseLayout.setOrientation(LinearLayout.VERTICAL);
            }
            setControlsEnabled();
        }
    }

    public void screenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() == getOrient.getHeight()) {
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                player.setPlayerStyle(playerStyleMinimal);

            } else {
                player.setPlayerStyle(playerStyleDefault);
            }
        }
    }

    private void setControlsEnabled() {
        fullscreenButton.setEnabled(player != null);
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        screenOrientation();
        doLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.live_player_menu, menu);

        return true;
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
            case R.id.action_share_link:
                shareLink();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareLink() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_live_player));
        share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v="
                + String.valueOf(videoId)
                + " " + getResources().getString(R.string.share_live_player_message));
        startActivity(Intent.createChooser(share, getResources().getString(R.string.action_share)));
    }

    @Override
    public void onBackPressed() {
        finish();
        Utils.goBackwardAnimation(this);
    }
}