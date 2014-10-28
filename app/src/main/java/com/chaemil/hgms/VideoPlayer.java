package com.chaemil.hgms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.chaemil.hgms.Adapters.TagsAdapter;

import org.w3c.dom.Text;

import static com.chaemil.hgms.Utils.Utils.fetchTags;


/**
 * Created by chaemil on 23.10.14.
 */
public class VideoPlayer extends FragmentActivity {

    private VideoView mVideoView;
    private TagsAdapter tagsAdapter;
    private Fragment fragment;

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

        if (findViewById(R.id.rightFrag) != null) {
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

        tagsAdapter = new TagsAdapter(this);

        GridView videoTags = (GridView) findViewById(R.id.videoTags);
        videoTags.setAdapter(tagsAdapter);
        fetchTags(getApplicationContext(),tagsAdapter,videoID);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        Uri video = Uri.parse(videoURL);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(video);
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

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }
}
