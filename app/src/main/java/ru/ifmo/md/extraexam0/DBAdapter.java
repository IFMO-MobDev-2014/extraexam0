package ru.ifmo.md.extraexam0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    public static final String KEY_ID = "_id";

    public static final int BUILD_NOT_STARTED = 0;
    public static final int BUILD_SUCCESS = 1;
    public static final int BUILD_FAILED = 2;
    public static final int BUILD_RUNNING = 3;

    //Tracks
    public static final String TABLE_NAME_BUILDS = "builds";
    public static final String KEY_BUILDS_NAME = "name";
    public static final String KEY_BUILDS_NUMBER = "number";
    public static final String KEY_BUILDS_STATUS = "build_status";
    public static final String KEY_BUILDS_TIME_CREATED = "time_created";
    public static final String KEY_BUILDS_TIME_START = "time_start";
    public static final String KEY_BUILDS_TIME_FINISHED = "time_finished";

    public static final String CREATE_TABLE_TRACKS = "CREATE TABLE " + TABLE_NAME_BUILDS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //will also be a build number
            + KEY_BUILDS_NAME + " STRING, "
            + KEY_BUILDS_NUMBER + " INTEGER, "
            + KEY_BUILDS_STATUS + " STRING NOT NULL, "
            + KEY_BUILDS_TIME_CREATED + " INTEGER NOT NULL, "
            + KEY_BUILDS_TIME_START + " INTEGER, "
            + KEY_BUILDS_TIME_FINISHED + " INTEGER)";

    private static DBAdapter mInstance = null;
    private Context context;
    private SQLiteDatabase db;

    private DBAdapter(Context context) {
        this.context = context;
    }

    public static DBAdapter getOpenedInstance(Context context) {
        if (mInstance == null)
            mInstance = new DBAdapter(context.getApplicationContext()).open();
        return mInstance;
    }

    private DBAdapter open() {
        DBHelper mDbHelper = new DBHelper(context);
        db = mDbHelper.getWritableDatabase();
        return this;
    }

    public static final String DB_NAME = "database.db";
    public static final Integer VERSION = 1;

    private static class DBHelper extends SQLiteOpenHelper {
        Context context;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
            this.context = context;
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_TRACKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        }
    }

    public Cursor getAllBuilds() {
        return getBuilds(null);
    }

    public long addBuild(Build b) {
        return addBuild(b.toContentValues());
    }

    public long addBuild(ContentValues contentValues) {
        String name = contentValues.getAsString(DBAdapter.KEY_BUILDS_NAME);
        Cursor c = getBuilds(DBAdapter.KEY_BUILDS_NAME + "='"+name+"'");
        c.moveToFirst();
        int number = c.getCount() + 1;
        contentValues.put(DBAdapter.KEY_BUILDS_NUMBER, number);
        return db.insert(TABLE_NAME_BUILDS, null, contentValues);
    }

    public int updateBuild(Build b) {
        return updateBuild(b.toContentValues());
    }

    public int updateBuild(ContentValues contentValues) {
        return db.update(TABLE_NAME_BUILDS, contentValues, KEY_ID + "=" + contentValues.getAsLong(KEY_ID), null);
    }

    public Cursor getBuilds(String where) {
        return db.query(TABLE_NAME_BUILDS,
                new String[]{KEY_ID, KEY_BUILDS_STATUS, KEY_BUILDS_TIME_CREATED, KEY_BUILDS_TIME_FINISHED, KEY_BUILDS_TIME_START, KEY_BUILDS_NAME, KEY_BUILDS_NUMBER},
                where, null, null, null, KEY_BUILDS_TIME_CREATED + " DESC");
    }

    public boolean deleteBuildById(long buildId) {
        return deleteBuilds(KEY_ID + "='" + buildId + "'") == 1;
    }

    public int deleteBuilds(String where) {
        return db.delete(TABLE_NAME_BUILDS, where, null);
    }
}

