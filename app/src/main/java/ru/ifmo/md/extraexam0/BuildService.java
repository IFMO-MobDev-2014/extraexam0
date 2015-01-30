package ru.ifmo.md.extraexam0;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class BuildService extends Service {
    public BuildService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; //binding is not allowed
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new AsyncTask<Void, Void, Void>() {

            volatile boolean requiresReload = false;

            @Override
            protected Void doInBackground(Void... params) {
                if (Looper.myLooper() == null)
                    Looper.prepare();

                getContentResolver().registerContentObserver(BuildsContentProvider.BUILDS_URI,
                        true, new ContentObserver(new Handler()) {
                            @Override
                            public void onChange(boolean selfChange) {
                                requiresReload = true;
                            }
                        });

                final ArrayList<Build> necessary = new ArrayList<>();
                final ArrayList<Build> allBuilds = new ArrayList<>();

                while (true) {

                    final Cursor c =
                            getContentResolver().query(BuildsContentProvider.BUILDS_URI
                                    , null, null, null, null);

                    if (c.moveToFirst()) {
                        do {
                            Build b = Build.fromCursor(c);
                            if (b.status == DBAdapter.BUILD_NOT_STARTED)
                                necessary.add(b);
                            allBuilds.add(b);
                        } while (c.moveToNext());
                    }

                    if (allBuilds.size() == 0)
                        break;

                    if (necessary.size() > 0) {
                        Build b = necessary.get(necessary.size() - 1);
                        necessary.remove(necessary.size() - 1);
                        runBuild(b);
                        continue;
                    }

                    int index = r.nextInt(allBuilds.size());
                    Build b = allBuilds.get(index);
                    runBuild(b);
                }

                stopSelf();
                return null;
            }

        }.execute();
    }

    Random r = new Random();

    private void runBuild(Build b) {
        int time = Math.abs(r.nextInt() % 5000 + 1000);
        boolean success = r.nextBoolean();
        Build running = b.run();
        getContentResolver().update(BuildsContentProvider.BUILDS_URI, running.toContentValues(), null, null);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
        if (success) {
            Build s = b.succeed();
            getContentResolver().update(BuildsContentProvider.BUILDS_URI, s.toContentValues(), null, null);
        } else {
            Build f = b.fail();
            getContentResolver().update(BuildsContentProvider.BUILDS_URI, f.toContentValues(), null, null);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
