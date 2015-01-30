package ru.ifmo.md.extraexam0;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import ru.ifmo.md.extraexam0.provider.build.BuildColumns;
import ru.ifmo.md.extraexam0.provider.build.BuildSelection;

/**
 * Created by Kirill on 30.01.2015.
 */
public class HistoryActivity extends Activity {

    private EditText nameEditText;
    private ListView rootView;
    private HistoryAdapter adapter;
    private Activity activity;
    private String curId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_history);
        curId = getIntent().getStringExtra("id");
        rootView = (ListView)findViewById(R.id.history_list);

        BuildSelection where = new BuildSelection();
        where.name(curId);
        Cursor c = this.getContentResolver().query(BuildColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);

        c.moveToFirst();


        adapter = new HistoryAdapter(this
                , c, 0);
        rootView.setAdapter(adapter);

        //c.close();


        adapter.notifyDataSetChanged();
    }


}
