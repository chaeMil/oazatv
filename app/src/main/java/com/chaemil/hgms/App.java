package com.chaemil.hgms;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.chaemil.hgms.service.MyRequestService;

import java.io.File;

/**
 * Created by chaemil on 6.12.14.
 */
public class App extends Application {
    public static boolean isDownloadingAudio = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MyRequestService.init(getApplicationContext());
    }
}
