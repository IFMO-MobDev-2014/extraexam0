package lyamkin.com.extraexam0;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class MyCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_row, parent, false);
        return v;
    }

    /**
     * @author will
     *
     * @param   v
     *          The view in which the elements we set up here will be displayed.
     *
     * @param   context
     *          The running context where this ListView adapter will be active.
     *
     * @param   c
     *          The Cursor containing the query results we will display.
     */

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow("name"));
        Long date = c.getLong(c.getColumnIndexOrThrow("time"));
        String status = c.getString(c.getColumnIndexOrThrow("status"));
        String count = c.getString(c.getColumnIndexOrThrow("count"));
        /**
         * Next set the title of the entry.
         */

        TextView title_text = (TextView) v.findViewById(R.id.item_text);
        if (title_text != null) {
            title_text.setText(title+" ("+count+")");
        }

        /**
         * Set Date
         */

        TextView date_text = (TextView) v.findViewById(R.id.item_date);
        if (date_text != null) {
            double dt = date / 1000;
            date_text.setText(status + ", " + Double.toString(dt));
        }

    }
}
