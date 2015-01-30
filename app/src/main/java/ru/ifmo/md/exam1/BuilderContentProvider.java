package ru.ifmo.md.exam1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;



public class BuilderContentProvider extends ContentProvider {


    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 1;

    private static final int BUILDS = 0;
    private static final int BUILDS_ID = 1;


    static final String BUILDS_TABLE = "builds";

    static final String BUILD_ID ="id";
    static final String BUILD_NUMBER = "number";
    static final String BUILD_STATUS = "status";
    static final String BUILD_COUNTER = "counter";


    static final String AUTHORITY = "ru.ifmo.md.exam1";

    static final String BUILDS_PATH = BUILDS_TABLE;



    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, BUILDS_PATH, BUILDS);
        matcher.addURI(AUTHORITY, BUILDS_PATH+"/#", BUILDS_ID);
    }


    public static final Uri BUILDS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + BUILDS_PATH);



    static final String BUILDS_TABLE_CREATE = "create table " + BUILDS_TABLE + "("
            + BUILD_ID + " integer primary key autoincrement, "
            + BUILD_NUMBER + " text, " + BUILD_STATUS + " text, " + BUILD_COUNTER + " integer" + ");";

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (matcher.match(uri)) {
            case BUILDS:
                queryBuilder.setTables(BUILDS_TABLE);
                break;
            case BUILDS_ID:
                queryBuilder.setTables(BUILDS_TABLE);
                queryBuilder.appendWhere("id=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long rowID;
        switch (matcher.match(uri)) {
            case BUILDS:
                rowID = db.insert(BUILDS_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        Uri resultUri = Uri.withAppendedPath(uri, ""+rowID);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int deleted = 0;
        String id;
        switch (matcher.match(uri)) {
            case BUILDS:
                deleted = db.delete(BUILDS_TABLE, selection, selectionArgs);
                break;
            case BUILDS_ID:
                id = uri.getLastPathSegment();
                deleted = db.delete(BUILDS_TABLE, BUILD_ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int updated = 0;
        String id;
        switch (matcher.match(uri)) {
            case BUILDS:
                updated = db.update(BUILDS_TABLE, values, selection, selectionArgs);
                break;
            case BUILDS_ID:
                id = uri.getLastPathSegment();
                updated = db.update(BUILDS_TABLE, values, BUILD_ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }

    public String getType(Uri uri) {
        return Integer.toString(matcher.match(uri));
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(BUILDS_TABLE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists builds");
            onCreate(db);
        }
    }
}