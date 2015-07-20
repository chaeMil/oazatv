package com.chaemil.hgms.utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.chaemil.hgms.activity.AudioPlayer;

/**
 * Created by chaemil on 8.12.14.
 */
public class NotificationReturnSlot extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = (String) getIntent().getExtras().get("DO");
        if (action.equals("pause")) {
            Log.i("NotificationReturnSlot", "pause");
            AudioPlayer.pause();
        } else if (action.equals("play")) {
            Log.i("NotificationReturnSlot", "play");
            AudioPlayer.play();
        }
        finish();
    }
}
