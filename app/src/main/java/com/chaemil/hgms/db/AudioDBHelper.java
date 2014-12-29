package com.chaemil.hgms.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chaemil.hgms.db.AudioDBContract.DownloadedAudio;
import com.chaemil.hgms.utils.Utils;


public class AudioDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Audio.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DownloadedAudio.TABLE_NAME + " (" +
                    DownloadedAudio._ID + " INTEGER PRIMARY KEY," +
                    DownloadedAudio.COLUMN_NAME_AUDIO_FILE + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_THUMB + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_NAME + TEXT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_TIME + INT_TYPE + COMMA_SEP +
                    DownloadedAudio.COLUMN_NAME_AUDIO_DATE + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DownloadedAudio.TABLE_NAME;


    public AudioDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static int count (SQLiteDatabase db) {
        Cursor mCount= db.rawQuery("select count(*) from " + DownloadedAudio.TABLE_NAME , null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void saveAudioTime(SQLiteDatabase db, String fileName, int time) {
        db.execSQL("update " + DownloadedAudio.TABLE_NAME +
                " set " + DownloadedAudio.COLUMN_NAME_AUDIO_TIME + "=" +
                time + " where " + DownloadedAudio.COLUMN_NAME_AUDIO_FILE +
                " == '" + fileName + "';");
        Utils.log("saveAudioTime", String.valueOf(time));
    }

    public static int loadAudioTime(SQLiteDatabase db, String fileName) {
        Cursor cursor = db.query(DownloadedAudio.TABLE_NAME,
                new String[] { DownloadedAudio.COLUMN_NAME_AUDIO_TIME},
                DownloadedAudio.COLUMN_NAME_AUDIO_FILE + "=?",
                new String[] { fileName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor != null) {
            return cursor.getInt(0);
        }
        else {
            return 0;
        }
    }

    public static void deleteAudioDBRecord(SQLiteDatabase db, String fileName) {
        db.delete(DownloadedAudio.TABLE_NAME,DownloadedAudio.COLUMN_NAME_AUDIO_FILE + "= '"
                + fileName + "'", null);
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
