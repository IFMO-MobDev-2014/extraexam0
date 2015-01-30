package ru.ifmo.md.extraexam0.provider.build;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import ru.ifmo.md.extraexam0.provider.base.AbstractSelection;

/**
 * Selection for the {@code build} table.
 */
public class BuildSelection extends AbstractSelection<BuildSelection> {
    @Override
    public Uri uri() {
        return BuildColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code BuildCursor} object, which is positioned before the first entry, or null.
     */
    public BuildCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new BuildCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public BuildCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public BuildCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public BuildSelection id(long... value) {
        addEquals("build." + BuildColumns._ID, toObjectArray(value));
        return this;
    }


    public BuildSelection name(String... value) {
        addEquals(BuildColumns.NAME, value);
        return this;
    }

    public BuildSelection nameNot(String... value) {
        addNotEquals(BuildColumns.NAME, value);
        return this;
    }

    public BuildSelection nameLike(String... value) {
        addLike(BuildColumns.NAME, value);
        return this;
    }

    public BuildSelection status(long... value) {
        addEquals(BuildColumns.STATUS, toObjectArray(value));
        return this;
    }

    public BuildSelection statusNot(long... value) {
        addNotEquals(BuildColumns.STATUS, toObjectArray(value));
        return this;
    }

    public BuildSelection statusGt(long value) {
        addGreaterThan(BuildColumns.STATUS, value);
        return this;
    }

    public BuildSelection statusGtEq(long value) {
        addGreaterThanOrEquals(BuildColumns.STATUS, value);
        return this;
    }

    public BuildSelection statusLt(long value) {
        addLessThan(BuildColumns.STATUS, value);
        return this;
    }

    public BuildSelection statusLtEq(long value) {
        addLessThanOrEquals(BuildColumns.STATUS, value);
        return this;
    }

    public BuildSelection startTime(long... value) {
        addEquals(BuildColumns.START_TIME, toObjectArray(value));
        return this;
    }

    public BuildSelection startTimeNot(long... value) {
        addNotEquals(BuildColumns.START_TIME, toObjectArray(value));
        return this;
    }

    public BuildSelection startTimeGt(long value) {
        addGreaterThan(BuildColumns.START_TIME, value);
        return this;
    }

    public BuildSelection startTimeGtEq(long value) {
        addGreaterThanOrEquals(BuildColumns.START_TIME, value);
        return this;
    }

    public BuildSelection startTimeLt(long value) {
        addLessThan(BuildColumns.START_TIME, value);
        return this;
    }

    public BuildSelection startTimeLtEq(long value) {
        addLessThanOrEquals(BuildColumns.START_TIME, value);
        return this;
    }

    public BuildSelection endTime(long... value) {
        addEquals(BuildColumns.END_TIME, toObjectArray(value));
        return this;
    }

    public BuildSelection endTimeNot(long... value) {
        addNotEquals(BuildColumns.END_TIME, toObjectArray(value));
        return this;
    }

    public BuildSelection endTimeGt(long value) {
        addGreaterThan(BuildColumns.END_TIME, value);
        return this;
    }

    public BuildSelection endTimeGtEq(long value) {
        addGreaterThanOrEquals(BuildColumns.END_TIME, value);
        return this;
    }

    public BuildSelection endTimeLt(long value) {
        addLessThan(BuildColumns.END_TIME, value);
        return this;
    }

    public BuildSelection endTimeLtEq(long value) {
        addLessThanOrEquals(BuildColumns.END_TIME, value);
        return this;
    }

    public BuildSelection last(boolean value) {
        addEquals(BuildColumns.LAST, toObjectArray(value));
        return this;
    }

    public BuildSelection number(int... value) {
        addEquals(BuildColumns.NUMBER, toObjectArray(value));
        return this;
    }

    public BuildSelection numberNot(int... value) {
        addNotEquals(BuildColumns.NUMBER, toObjectArray(value));
        return this;
    }

    public BuildSelection numberGt(int value) {
        addGreaterThan(BuildColumns.NUMBER, value);
        return this;
    }

    public BuildSelection numberGtEq(int value) {
        addGreaterThanOrEquals(BuildColumns.NUMBER, value);
        return this;
    }

    public BuildSelection numberLt(int value) {
        addLessThan(BuildColumns.NUMBER, value);
        return this;
    }

    public BuildSelection numberLtEq(int value) {
        addLessThanOrEquals(BuildColumns.NUMBER, value);
        return this;
    }
}
