package com.chaemil.hgms.utils;

import android.content.Intent;
import android.view.View;

import com.chaemil.hgms.activity.PhotoalbumActivity;
import com.chaemil.hgms.activity.VideoPlayer;

/**
 * Created by chaemil on 20.7.15.
 */
public class IntentUtils {

    public static void startVideoPlayer(View v, String videoURL, String videoName,
                                        String videoDate, String videoViews) {
        Intent a = new Intent(v.getContext(), VideoPlayer.class);
        a.putExtra(Constants.VIDEO_LINK, videoURL);
        a.putExtra(Constants.VIDEO_NAME, videoName);
        a.putExtra(Constants.VIDEO_DATE, videoDate);
        a.putExtra(Constants.VIDEO_VIEWS, videoViews);
        v.getContext().startActivity(a);
    }

    public static void startPhotoalbumViewer(View v, String albumId,
                                             String albumName, String albumDate) {
        Intent a = new Intent(v.getContext(), PhotoalbumActivity.class);
        a.putExtra(Constants.ALBUM_ID, albumId);
        a.putExtra(Constants.ALBUM_NAME, albumName);
        a.putExtra(Constants.ALBUM_DATE, albumDate);
        v.getContext().startActivity(a);
    }
}
