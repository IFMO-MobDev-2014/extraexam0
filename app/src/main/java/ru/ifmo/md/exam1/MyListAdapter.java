package ru.ifmo.md.exam1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daria on 30.01.15.
 */
public class MyListAdapter extends ArrayAdapter<Build> {

    private Context context;

    private List<Build> objects = new ArrayList<>();

    public MyListAdapter(Context context, int resource, List<Build> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.list_item, parent, false);

        TextView number = (TextView) rowView.findViewById(R.id.number);
        number.setText("#" + objects.get(position).getNumber());

        TextView status = (TextView) rowView.findViewById(R.id.status);
        status.setText(objects.get(position).getLastVerdict());

        TextView counter = (TextView) rowView.findViewById(R.id.counter);
        counter.setText(objects.get(position).getCounter());

        Button button = (Button) rowView.findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(position);
                notifyDataSetChanged();

            }
        });

        return rowView;
    }
}
