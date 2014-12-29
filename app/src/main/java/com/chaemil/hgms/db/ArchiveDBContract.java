package com.chaemil.hgms.db;

import android.provider.BaseColumns;

/**
 * Created by chaemil on 29.12.14.
 */
public class ArchiveDBContract {

    public ArchiveDBContract() {}

    public static abstract class Archive implements BaseColumns {
        public static final String TABLE_NAME = "archive";
        public static final String COLUMN_NAME_VIDEO_ID = "videoId";
        public static final String COLUMN_NAME_VIDEO_TIME = "videoTime";
    }
}
