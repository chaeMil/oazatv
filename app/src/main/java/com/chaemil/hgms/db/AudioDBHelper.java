package com.chaemil.hgms.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.chaemil.hgms.db.AudioDBContract.DownloadedAudio;

/**
 * Created by chaemil on 26.11.14.
 */
public class AudioDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Audio.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DownloadedAudio.TABLE_NAME + " (" +
                    DownloadedAudio._ID + " INTEGER PRIMARY KEY," +
                    DownloadedAudio.COLUMN_NAME_AUDIO_FILE + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_THUMB + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_NAME + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_DATE + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DownloadedAudio.TABLE_NAME;


    public AudioDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static boolean audioFileExists(SQLiteDatabase db, String audioFileName) {
        Cursor cursor = db.query(DownloadedAudio.TABLE_NAME,
                new String[] {DownloadedAudio.COLUMN_NAME_AUDIO_FILE},
                DownloadedAudio.COLUMN_NAME_AUDIO_FILE + " = ?",
                new String[] {audioFileName},
                null,
                null,
                DownloadedAudio.COLUMN_NAME_AUDIO_FILE + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            return true;
        }
        return false;
    }
}
