package com.chaemil.hgms.db;

import android.provider.BaseColumns;

/**
 * Created by chaemil on 26.11.14.
 */
public final class AudioDBContract {

    public AudioDBContract () {}

    public static abstract class DownloadedAudio implements BaseColumns {
        public static final String TABLE_NAME = "downloadedAudio";
        public static final String COLUMN_NAME_AUDIO_FILE = "audioFile";
        public static final String COLUMN_NAME_AUDIO_THUMB = "audioThumb";
        public static final String COLUMN_NAME_AUDIO_NAME = "audioName";
        public static final String COLUMN_NAME_AUDIO_DATE = "audioDate";
        public static final String COLUMN_NAME_AUDIO_TIME = "audioTime";
    }
}
