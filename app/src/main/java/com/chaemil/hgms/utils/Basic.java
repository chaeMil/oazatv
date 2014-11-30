package com.chaemil.hgms.utils;

import android.content.Intent;
import android.view.View;

import com.chaemil.hgms.PhotoalbumActivity;
import com.chaemil.hgms.VideoPlayer;

/**
 * Created by chaemil on 27.10.14.
 */
public class Basic {
    public static void startVideoPlayer(View v, String videoURL, String videoName, String videoDate, String videoViews) {
        Intent a = new Intent(v.getContext(), VideoPlayer.class);
        a.putExtra("videoLink", videoURL);
        a.putExtra("videoName", videoName);
        a.putExtra("videoDate", videoDate);
        a.putExtra("videoViews", videoViews);
        v.getContext().startActivity(a);
    }

    public static void startPhotoalbumViewer(View v, String albumId) {
        Intent a = new Intent(v.getContext(), PhotoalbumActivity.class);
        a.putExtra("albumId", albumId);
        v.getContext().startActivity(a);
    }
}
