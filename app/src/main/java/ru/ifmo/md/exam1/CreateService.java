package ru.ifmo.md.exam1;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;

/**
 * Created by daria on 30.01.15.
 */
public class CreateService extends IntentService {

    public CreateService() {
        super(".CreateService");
    }

    @Override
    public void onHandleIntent(Intent intent) {


        String number = intent.getStringExtra("number");
        int counter = 0;
        String status = "Not executed";
        ContentValues contentValues = new ContentValues();
        contentValues.put(BuilderContentProvider.BUILD_NUMBER, number);
        contentValues.put(BuilderContentProvider.BUILD_COUNTER, counter);
        contentValues.put(BuilderContentProvider.BUILD_STATUS, status);
        getContentResolver().insert(BuilderContentProvider.BUILDS_CONTENT_URI, contentValues);
    }
}

