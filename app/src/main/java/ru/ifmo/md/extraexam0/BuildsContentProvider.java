package ru.ifmo.md.extraexam0;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Евгения on 30.11.2014.
 */
public class BuildsContentProvider extends ContentProvider {

    // db
    private DBAdapter db;

    // used for the UriMacher
    private static final int BUILDS = 10;

    public static final String AUTHORITY = "ru.ifmo.md.extraexam0.BuildsContentProvider";

    private static final String BASE_BUILDS = "builds";

    public static final Uri BUILDS_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_BUILDS);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_BUILDS, BUILDS);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        if (uriType == BUILDS) {
            int result = db.deleteBuilds(selection);
            getContext().getContentResolver().notifyChange(uri, null);
            return result;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = -1;
        if (uriType == BUILDS) {
            id = db.addBuild(values);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(uri.toString());
    }

    @Override
    public boolean onCreate() {
        db = DBAdapter.getOpenedInstance(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        Cursor result = null;
        if (uriType == BUILDS) {
            result = db.getBuilds(selection);
        }
        if (result != null) {
            result.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int result = -1;
        if (uriType == BUILDS)
            result = db.updateBuild(values);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}

