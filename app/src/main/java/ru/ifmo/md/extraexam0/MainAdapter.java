package ru.ifmo.md.extraexam0;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.ifmo.md.extraexam0.provider.build.BuildColumns;

/**
 * Created by Kirill on 21.01.2015.
 */
public class MainAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public MainAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.mainlist_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        String name = cursor.getString(cursor.getColumnIndex(BuildColumns.NAME));
        int status = cursor.getInt(cursor.getColumnIndex(BuildColumns.STATUS));
        int start = cursor.getInt(cursor.getColumnIndex(BuildColumns.START_TIME));
        int end = cursor.getInt(cursor.getColumnIndex(BuildColumns.END_TIME));
        int number  = cursor.getInt(cursor.getColumnIndex(BuildColumns.NUMBER));


        String statusStr = "status";
        if (status == 1) {
            statusStr = "Pending";
        } else if (status == 2) {
            statusStr = "Failed";
        } else if (status == 3) {
            statusStr = "Success";
        } else if (status == 4) {
            statusStr = "Canceled";
        }

        String durStr ="";

        if (status == 1) {
            Date date = new Date(start * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String dateString = sdf.format(date);
            durStr = "started at " + dateString;
        } else {
            Date date = new Date(end * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String dateString = sdf.format(date);
            durStr = "finished at " + dateString;
        }

        TextView titleView = (TextView) view.findViewById(R.id.Build_name);
        titleView.setText(name);
        TextView dateView = (TextView) view.findViewById(R.id.Build_status);
        dateView.setText(statusStr);
        TextView time = (TextView)view.findViewById(R.id.Build_duration);
        time.setText(durStr);
        TextView nu = (TextView)view.findViewById(R.id.Build_number);
        nu.setText("Build number: " + number);

    }
}
