package com.example.panzq.simpleandroid_12;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class LocalIntentService extends IntentService {
    private static final String TAG = "panzqww";

    public LocalIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getStringExtra("task_action");
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        Log.d(TAG, "receive task : " + action +" CPU_COUNT = "+CPU_COUNT);
        SystemClock.sleep(3000);
        if ("study.pan.TASK1".equals(action)) {
            Log.d(TAG, "handle task : " + action);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "service onDestroy");
        super.onDestroy();
    }
}
