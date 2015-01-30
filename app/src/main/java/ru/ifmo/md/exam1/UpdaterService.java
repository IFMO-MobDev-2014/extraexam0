package ru.ifmo.md.exam1;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import java.util.Random;

/**
 * Created by daria on 30.01.15.
 */

public class UpdaterService extends IntentService {

    public UpdaterService() {
        super("UpdaterService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String operation = intent.getStringExtra("operation");
        if (operation.equals("insert")) {
            String number = intent.getStringExtra("number");
            int counter = 0;
            String status = "Not executed";
            ContentValues contentValues = new ContentValues();
            contentValues.put(BuilderContentProvider.BUILD_NUMBER, number);
            contentValues.put(BuilderContentProvider.BUILD_COUNTER, counter);
            contentValues.put(BuilderContentProvider.BUILD_STATUS, status);
            getContentResolver().insert(BuilderContentProvider.BUILDS_CONTENT_URI, contentValues);
        }
        if (operation.equals("execute")) {
            String number = intent.getStringExtra("number");
            int counter = intent.getIntExtra("counter", -1);
            getContentResolver().delete(BuilderContentProvider.BUILDS_CONTENT_URI, "number = ?", new String[]{number});
            counter++;
            Random random = new Random();
            boolean stat = random.nextBoolean();
            String status;
            if (!stat) {
                status = "Successful";
            } else status = "Failed";
            ContentValues contentValues = new ContentValues();
            contentValues.put(BuilderContentProvider.BUILD_NUMBER, number);
            contentValues.put(BuilderContentProvider.BUILD_COUNTER, counter);
            contentValues.put(BuilderContentProvider.BUILD_STATUS, status);
            getContentResolver().insert(BuilderContentProvider.BUILDS_CONTENT_URI, contentValues);

        }
    }

}