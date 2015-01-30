package ru.ifmo.md.extraexam0;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setOnItemLongClickListener(this);
        startService(new Intent(this, BuildService.class));
        loadList();
    }

    private void loadList() {
        getLoaderManager().initLoader(LOADER_ID_BUILDS, null, this);
        setListAdapter(new CursorAdapter(this, null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View v = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
                bindView(v, context, cursor);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                int status = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_STATUS));
                view.setBackgroundColor(
                        status == DBAdapter.BUILD_FAILED ? Color.rgb(255, 200, 200) :
                                status == DBAdapter.BUILD_SUCCESS ? Color.rgb(200, 255, 200) :
                                        status == DBAdapter.BUILD_RUNNING ? Color.rgb(255, 255, 200)
                                                : Color.WHITE
                );
                ((TextView) view.findViewById(android.R.id.text1)).setText(getString(R.string.buildPrefix)
                        + cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_NUMBER)) + " " + cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_NAME)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                if (status == DBAdapter.BUILD_NOT_STARTED) {
                    ((TextView) view.findViewById(android.R.id.text2)).setText(getString(R.string.created_at)
                            + sdf.format(new Date(cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_CREATED)) * 1000)));
                } else if (status == DBAdapter.BUILD_RUNNING) {
                    long delta = System.currentTimeMillis() / 1000
                            - cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_START));
                    ((TextView) view.findViewById(android.R.id.text2)).setText(getString(R.string.running)
                            + " " + delta + "s");
                } else if (status == DBAdapter.BUILD_SUCCESS) {
                    long delta = cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_FINISHED))
                            - cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_START));
                    ((TextView) view.findViewById(android.R.id.text2)).setText(getString(R.string.succeeded_in)
                            + delta + "s");
                } else if (status == DBAdapter.BUILD_FAILED) {
                    long delta = cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_FINISHED))
                            - cursor.getLong(cursor.getColumnIndex(DBAdapter.KEY_BUILDS_TIME_START));
                    ((TextView) view.findViewById(android.R.id.text2)).setText(getString(R.string.failed_in) + " " +
                            +delta + "s");
                }
            }
        });
    }

    private static final int LOADER_ID_BUILDS = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Build b = new Build(-1, data.getStringExtra(AddBuildActivity.EXTRA_NAME), DBAdapter.BUILD_NOT_STARTED, System.currentTimeMillis() / 1000, -1, -1);
            getContentResolver().insert(BuildsContentProvider.BUILDS_URI, b.toContentValues());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_build) {
            startActivityForResult(new Intent(this, AddBuildActivity.class), 0);
            startService(new Intent(this, BuildService.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(this, "Long tap to delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_BUILDS) {
            return new CursorLoader(this, BuildsContentProvider.BUILDS_URI, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).changeCursor(null);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        getContentResolver().delete(BuildsContentProvider.BUILDS_URI, DBAdapter.KEY_ID + "=(" + id + ")", null);
        return true;
    }
}

