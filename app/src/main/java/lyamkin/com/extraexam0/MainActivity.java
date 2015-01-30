package lyamkin.com.extraexam0;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView listView;
    Cursor mCursor;
    MyCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        getLoaderManager().initLoader(0, null, this);
        //listView.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[]{"name", "status"}, new int[]{android.R.id.text1, android.R.id.text2}, 0));
        Intent intent = new Intent(this, RandomService.class);
        startService(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AddDialog addDialog = new AddDialog();
            getFragmentManager().beginTransaction().add(addDialog, "").commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, TaskContentProvider.CHANNELS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        adapter = new MyCursorAdapter(this, data);
        adapter.swapCursor(data);
        listView.setAdapter(adapter);
       //((CursorAdapter)listView.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter)listView.getAdapter()).swapCursor(null);
    }
}
