package ru.ifmo.md.extraexam0.provider.build;

import android.net.Uri;
import android.provider.BaseColumns;

import ru.ifmo.md.extraexam0.provider.BuildProvider;
import ru.ifmo.md.extraexam0.provider.build.BuildColumns;

/**
 * Columns for the {@code build} table.
 */
public class BuildColumns implements BaseColumns {
    public static final String TABLE_NAME = "build";
    public static final Uri CONTENT_URI = Uri.parse(BuildProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = new String(BaseColumns._ID);

    public static final String NAME = "name";

    public static final String STATUS = "status";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String LAST = "last";

    public static final String NUMBER = "number";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            NAME,
            STATUS,
            START_TIME,
            END_TIME,
            LAST,
            NUMBER
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c == NAME || c.contains("." + NAME)) return true;
            if (c == STATUS || c.contains("." + STATUS)) return true;
            if (c == START_TIME || c.contains("." + START_TIME)) return true;
            if (c == END_TIME || c.contains("." + END_TIME)) return true;
            if (c == LAST || c.contains("." + LAST)) return true;
            if (c == NUMBER || c.contains("." + NUMBER)) return true;
        }
        return false;
    }

}
