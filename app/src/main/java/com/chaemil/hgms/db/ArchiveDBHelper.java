package com.chaemil.hgms.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chaemil.hgms.utils.SmartLog;

public class ArchiveDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Archive.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArchiveDBContract.Archive.TABLE_NAME + " (" +
                    ArchiveDBContract.Archive._ID + " INTEGER PRIMARY KEY," +
                    ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + TEXT_TYPE + COMMA_SEP +
                    ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_TIME + INT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ArchiveDBContract.Archive.TABLE_NAME;


    public ArchiveDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static int count (SQLiteDatabase db) {
        Cursor mCount= db.rawQuery("select count(*) from "
                + ArchiveDBContract.Archive.TABLE_NAME , null);
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

    public static void saveVideoTime(SQLiteDatabase db, String videoId, int time) {
        String sql = "insert or replace into " + ArchiveDBContract.Archive.TABLE_NAME +
                " (_id, " + ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + ", "
                + ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_TIME
                + ") values ((select _id from " + ArchiveDBContract.Archive.TABLE_NAME
                + " where " + ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + " = "
                + "'" + videoId + "'), '" + videoId + "', " + time + ");";
        SmartLog.log("saveVideoTime", sql);
        db.execSQL(sql);
        SmartLog.log("saveVideoTime", String.valueOf(time));
    }

    public static int loadVideoTime(SQLiteDatabase db, String fileName) {
        Cursor cursor = db.query(ArchiveDBContract.Archive.TABLE_NAME,
                new String[] { ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_TIME},
                ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + "=?",
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

    public static void deleteVideoDBRecord(SQLiteDatabase db, String fileName) {
        db.delete(ArchiveDBContract.Archive.TABLE_NAME,
                ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + "= '"
                + fileName + "'", null);
    }

    public static boolean videoRecordExists(SQLiteDatabase db, String audioFileName) {
        Cursor cursor = db.query(ArchiveDBContract.Archive.TABLE_NAME,
                new String[] {ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID},
                ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + " = ?",
                new String[] {audioFileName},
                null,
                null,
                ArchiveDBContract.Archive.COLUMN_NAME_VIDEO_ID + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
            return true;
        }
        return false;
    }
}
