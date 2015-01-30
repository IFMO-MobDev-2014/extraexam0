package ru.ifmo.md.extraexam0;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ru.ifmo.md.extraexam0.provider.build.BuildContentValues;
import ru.ifmo.md.extraexam0.provider.build.BuildCursor;
import ru.ifmo.md.extraexam0.provider.build.BuildSelection;


/**
 * Created by Kirill on 01.12.2014.
 */
public class UpdateService extends IntentService {

    public static final String REQUEST_TYPE = "type";
    public static final int START_REQUEST = 0;

    public static final String RESULT = "result";
    public static final int OK = 0;
    public static final int ERROR = 0;
    public static final String ERROR_MSG = "errorMessage";
    public static final String NOTIFICATION = "ru.ifmo.md.weather";

    Context context = null;

    public UpdateService() {
        super("LoadWeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("service", "started");
        try {
            Log.i("start ", "onHandleIntent");
            int type = intent.getIntExtra(REQUEST_TYPE, -1);
            if (type == START_REQUEST) {
                run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            publishResults(e.getMessage(), 0);
        }
        publishResults("Download finished", 1);
    }

    private void run() throws InterruptedException {
        HashMap<BuildSelection, BuildContentValues> upd = new HashMap<>();
        ArrayList<BuildContentValues> ins = new ArrayList<>();
        int step = 0;
        while (true) {
            upd.clear();
            ins.clear();
            long curTime = System.currentTimeMillis() / 1000L;
            BuildSelection where = new BuildSelection();
            where.status(1);
            BuildCursor builds = where.query(getContentResolver());
            builds.moveToFirst();
            Log.i("run()", builds.getCount()+"");
            while (!builds.isAfterLast()) {
                long endTime = builds.getEndTime();
                if (curTime >= endTime) {
                    BuildContentValues val = new BuildContentValues();
                    int status = new Random().nextInt(2) + 2;
                    val.putStatus(status);
                    Toast.makeText(this, builds.getName() + status, Toast.LENGTH_SHORT).show();
                    val.putLast(false);
                    BuildSelection selection = new BuildSelection();
                    selection.id(builds.getId());
                    upd.put(selection, val);

                    BuildContentValues newVal = new BuildContentValues();
                    newVal.putName(builds.getName());
                    newVal.putNumber(builds.getNumber()+1);
                    int time = (int)(System.currentTimeMillis()/1000L);
                    int dur = 5 + new Random().nextInt(10);
                    newVal.putStatus(1);
                    newVal.putStartTime(time);
                    newVal.putEndTime(time + dur);
                    newVal.putLast(true);
                    ins.add(newVal);
                }
                builds.moveToNext();
            }
            for (Map.Entry<BuildSelection, BuildContentValues> i : upd.entrySet()) {
                i.getValue().update(getContentResolver(), i.getKey());
            }
            for (BuildContentValues i : ins) {
                i.insert(getContentResolver());
            }

            publishResults("Ok", OK);

            Thread.sleep(1000L);
            Log.i("run(): step", step + "");
            step++;
        }
    }

    private void publishResults(String errorMsg, int result) {
        //Toast.makeText(this, "finish downloading", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        intent.putExtra(ERROR_MSG, errorMsg);
        sendBroadcast(intent);
    }
}
