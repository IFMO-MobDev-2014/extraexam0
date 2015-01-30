package ru.ifmo.md.extraexam0;

import android.content.ContentValues;
import android.database.Cursor;

public class Build {
    public final long id;
    public final String name;
    public final long status;
    public final long timeCreated;
    public final long timeFinished;
    public final long timeStartBuild;

    public Build(long id, String name, long status, long timeCreated, long timeFinished, long timeStartBuild) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeFinished = timeFinished;
        this.timeStartBuild = timeStartBuild;
    }

    public Build succeed() {
        return new Build(id, name, DBAdapter.BUILD_SUCCESS, timeCreated, System.currentTimeMillis()/1000, timeStartBuild);
    }

    public Build fail() {
        return new Build(id, name, DBAdapter.BUILD_FAILED, timeCreated, System.currentTimeMillis()/1000, timeStartBuild);
    }

    public Build run() {
        return new Build(id, name, DBAdapter.BUILD_RUNNING, timeCreated, timeFinished, System.currentTimeMillis()/1000);
    }

    public ContentValues toContentValues() {
        ContentValues result = new ContentValues();

        result.put(DBAdapter.KEY_BUILDS_NAME, name);
        if (id != -1) {
            result.put(DBAdapter.KEY_ID, id);
        }
        result.put(DBAdapter.KEY_BUILDS_STATUS, status);
        result.put(DBAdapter.KEY_BUILDS_TIME_CREATED, timeCreated);

        if (timeFinished != -1) {
            result.put(DBAdapter.KEY_BUILDS_TIME_FINISHED, timeFinished);
        }

        if (timeStartBuild != -1) {
            result.put(DBAdapter.KEY_BUILDS_TIME_START, timeStartBuild);
        }
        return result;
    }

    public static Build fromCursor(Cursor c) {
        long id = c.getLong(c.getColumnIndex(DBAdapter.KEY_ID));
        String name = c.getString(c.getColumnIndex(DBAdapter.KEY_BUILDS_NAME));
        long status = c.getLong(c.getColumnIndex(DBAdapter.KEY_BUILDS_STATUS));
        long timeCreated = c.getLong(c.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_CREATED));
        long timeFinished = c.getLong(c.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_FINISHED));
        long timeStart = c.getLong(c.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_START));
        return new Build(id, name, status, timeCreated, timeFinished, timeStart);
    }
}
