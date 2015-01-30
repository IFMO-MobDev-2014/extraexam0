package ru.ifmo.md.exam1;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;




public class MainActivity extends ActionBarActivity implements AppResultReceiver.Receiver {
    BuilderContentProvider contentProvider;
    List<Build> builds;

    Random random = new Random();

    private AppResultReceiver mReceiver;

    int currentPos;


    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentProvider = new BuilderContentProvider();
        contentProvider.onCreate();
        mReceiver = new AppResultReceiver(new Handler());
        mReceiver.setReceiver(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    void choosePosition() {
        currentPos = random.nextInt(builds.size());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            EditText name = (EditText) findViewById(R.id.new_name);
            String newName= name.getText().toString();

            Intent intent = new Intent(this, CreateService.class);
            intent.putExtra("number", newName);
            startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case AppResultReceiver.OK:
                choosePosition();
                Intent intent = new Intent(this, UpdaterService.class);
                intent.putExtra("number", builds.get(currentPos).getNumber());
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("counter", builds.get(currentPos).getCounter());
                startService(intent);

                break;
            case AppResultReceiver.ERROR:
                choosePosition();
                intent = new Intent(this, UpdaterService.class);
                intent.putExtra("number", builds.get(currentPos).getNumber());
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("counter", builds.get(currentPos).getCounter());
                startService(intent);

                break;
            case AppResultReceiver.UPDATE:
                builds.get(currentPos).setLastVerdict("Running");
                break;
        }
        update();
    }

    void update() {
        builds = new ArrayList<>();
        Cursor cursor = getContentResolver().query(BuilderContentProvider.BUILDS_CONTENT_URI, null, null ,null ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Build build = new Build(name);

            build.setLastVerdict(cursor.getString(2));

            build.setCounter(cursor.getInt(3));

            builds.add(build);
            cursor.moveToNext();
        }
        cursor.close();

        adapter = new MyListAdapter(this, builds.size(), builds);
    }

}
