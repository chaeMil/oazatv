package com.chaemil.hgms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by chaemil on 8.12.14.
 */
public class NotificationReturnSlot extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
