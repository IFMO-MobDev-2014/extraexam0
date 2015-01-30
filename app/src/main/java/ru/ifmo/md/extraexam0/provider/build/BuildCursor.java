package ru.ifmo.md.extraexam0.provider.build;

import java.util.Date;

import android.database.Cursor;

import ru.ifmo.md.extraexam0.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code build} table.
 */
public class BuildCursor extends AbstractCursor {
    public BuildCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(BuildColumns.NAME);
        return getString(index);
    }

    /**
     * Get the {@code status} value.
     */
    public long getStatus() {
        return getLongOrNull(BuildColumns.STATUS);
    }

    /**
     * Get the {@code start_time} value.
     */
    public long getStartTime() {
        return getLongOrNull(BuildColumns.START_TIME);
    }

    /**
     * Get the {@code end_time} value.
     */
    public long getEndTime() {
        return getLongOrNull(BuildColumns.END_TIME);
    }

    /**
     * Get the {@code last} value.
     */
    public boolean getLast() {
        return getBoolean(BuildColumns.LAST);
    }

    /**
     * Get the {@code number} value.
     */
    public int getNumber() {
        return getIntegerOrNull(BuildColumns.NUMBER);
    }
}
