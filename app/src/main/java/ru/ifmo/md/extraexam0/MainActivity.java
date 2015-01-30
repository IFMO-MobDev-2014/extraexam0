package ru.ifmo.md.extraexam0;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Random;

import ru.ifmo.md.extraexam0.provider.build.BuildColumns;
import ru.ifmo.md.extraexam0.provider.build.BuildContentValues;
import ru.ifmo.md.extraexam0.provider.build.BuildCursor;
import ru.ifmo.md.extraexam0.provider.build.BuildSelection;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText nameEditText;
    private ListView rootView;
    private MainAdapter adapter;
    private Activity activity;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int type = bundle.getInt(UpdateService.RESULT);
                if (type == UpdateService.OK) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra(UpdateService.REQUEST_TYPE, UpdateService.START_REQUEST);
        startService(intent);
        rootView = (ListView)findViewById(R.id.list);
        adapter = new MainAdapter(this
                , null, 0);
        rootView.setAdapter(adapter);
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, HistoryActivity.class);
                BuildSelection where = new BuildSelection();
                where.id(id);
                BuildCursor builds = where.query(getContentResolver());
                builds.moveToFirst();

                intent.putExtra("id", builds.getName());
                startActivity(intent);
            }
        });
        rootView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BuildSelection where = new BuildSelection();
                where.id(id);
                BuildContentValues val = new BuildContentValues();
                int status = 4;
                val.putStatus(status);
                val.update(getContentResolver(), where);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
        registerReceiver(receiver, new IntentFilter(UpdateService.NOTIFICATION));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add:
                createBuild();
                break;
            default:
                break;
        }

        return true;
    }

    private void createBuild() {
        LayoutInflater inflater = getLayoutInflater();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        View view = inflater.inflate(R.layout.add_build ,null);
        nameEditText = (EditText) view.findViewById(R.id.build_name);
        alert.setView(view);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("onClick", "OK");
                BuildContentValues val = new BuildContentValues();
                val.putName(nameEditText.getText().toString());
                val.putStatus(1);
                int time = (int)(System.currentTimeMillis() / 1000L);
                val.putStartTime(time);
                int dur = 5 + new Random().nextInt(10);
                val.putEndTime(time + dur);
                val.putNumber(1);
                val.putLast(true);
                val.insert(getContentResolver());
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("onClick", "cancel");
                dialog.cancel();
            }
        });
        alert.create().show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this,
                BuildColumns.CONTENT_URI, null, BuildColumns.LAST +" == ?", new String[]{"1"}, null);
        /*CursorLoader cursorLoader = new CursorLoader(this,
                BuildColumns.CONTENT_URI, null, null, null, null);*/
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
