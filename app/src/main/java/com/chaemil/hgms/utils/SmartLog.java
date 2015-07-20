package com.chaemil.hgms.utils;

import android.util.Log;

/**
 * Created by chaemil on 20.7.15.
 */
public class SmartLog {
    public static void log(String tag, String sql) {
        if(Constants.DEBUG) {
            Log.i(tag, sql);
        }
    }
}
