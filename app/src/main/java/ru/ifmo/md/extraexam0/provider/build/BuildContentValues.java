package ru.ifmo.md.extraexam0.provider.build;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import ru.ifmo.md.extraexam0.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code build} table.
 */
public class BuildContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return BuildColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, BuildSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public BuildContentValues putName(String value) {
        if (value == null) throw new IllegalArgumentException("value for name must not be null");
        mContentValues.put(BuildColumns.NAME, value);
        return this;
    }



    public BuildContentValues putStatus(long value) {
        mContentValues.put(BuildColumns.STATUS, value);
        return this;
    }



    public BuildContentValues putStartTime(long value) {
        mContentValues.put(BuildColumns.START_TIME, value);
        return this;
    }



    public BuildContentValues putEndTime(long value) {
        mContentValues.put(BuildColumns.END_TIME, value);
        return this;
    }



    public BuildContentValues putLast(boolean value) {
        mContentValues.put(BuildColumns.LAST, value);
        return this;
    }



    public BuildContentValues putNumber(int value) {
        mContentValues.put(BuildColumns.NUMBER, value);
        return this;
    }


}
