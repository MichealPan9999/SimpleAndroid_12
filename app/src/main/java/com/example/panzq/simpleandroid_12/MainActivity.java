package com.example.panzq.simpleandroid_12;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleThreads();
    }

    private void scheduleThreads() {
        //runAsyncTask();
        //runIntentService();
        //runThreadPool();
        //test();
        //handlerThread.start();
        //test2();
        //sendLocalIntent();
        RunThreadPool();
    }

    private void runAsyncTask() {
        try {
            new DownloadFilesTask().execute(new URL("http://www.baidu.com"),
                    new URL("http://name.renren.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

        @Override
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                //totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) (((i + 1) / (float) count) * 100));
                if (isCancelled()) {
                    Log.d("panzqw", "------- cancelled ~ ");
                    break;
                }
            }
            return totalSize;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
            Log.d("panzqw", "------- progress " + progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");
            Log.d("panzqw", "------- done! ");
        }

        @Override
        protected void onCancelled(Long aLong) {
            super.onCancelled(aLong);
            Log.d("panzqw", "------- Cancelled! ");
        }
    }

    public void test() {
        new MyAsyncTask("AsyncTask#1").execute("");
        new MyAsyncTask("AsyncTask#2").execute("");
        new MyAsyncTask("AsyncTask#3").execute("");
        new MyAsyncTask("AsyncTask#4").execute("");
        new MyAsyncTask("AsyncTask#5").execute("");
    }
    public void test2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new MyAsyncTask("AsyncTask#1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            new MyAsyncTask("AsyncTask#2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            new MyAsyncTask("AsyncTask#3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            new MyAsyncTask("AsyncTask#4").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            new MyAsyncTask("AsyncTask#5").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        }
    }
    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private String mName = "AsyncTask";

        public MyAsyncTask(String name){
            super();
            mName = name;
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return mName;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.d("panzqww ", result + " execute finish at " + df.format(new Date()));
        }
    }
    int i = 0;
    HandlerThread handlerThread = new HandlerThread("HandlerThread#1")
    {
        @Override
        public void run() {
            super.run();
            Log.d("panzqww","handlerThread start");
            int mTid = Process.myTid();
            Looper.prepare();
            synchronized (this) {
                Looper mLooper = Looper.myLooper();
                notifyAll();
            }
            Process.setThreadPriority(i++);
            onLooperPrepared();
            Log.d("panzqww","mTid = "+mTid);
            Looper.loop();
            mTid = -1;
        }
    };

    private void sendLocalIntent()
    {
        Intent service = new Intent(this, LocalIntentService.class);
        service.putExtra("task_action", "study.pan.TASK1");
        startService(service);
        service.putExtra("task_action", "study.pan.TASK2");
        startService(service);
        service.putExtra("task_action", "study.pan.TASK3");
        startService(service);
    }

    private void RunThreadPool()
    {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.d("panzqww ",  Thread.currentThread().getName()+"#"+Thread.currentThread().getId()+" --------- " + df.format(new Date()));
                SystemClock.sleep(2000);
            }
        };
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("panzqww","-----start ==="+df.format(new Date()));
        ExecutorService fixThreadPool = Executors.newFixedThreadPool(4);
        fixThreadPool.execute(command);

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(command);

        ScheduledExecutorService scheduleThreadPool = Executors.newScheduledThreadPool(4);
        //5000ms后执行command
        //scheduleThreadPool.schedule(command, 5000, TimeUnit.MILLISECONDS);
        //延迟1000ms后，每隔3000ms执行一次command
        scheduleThreadPool.scheduleAtFixedRate(command,1000,3000,TimeUnit.MILLISECONDS);

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(command);
    }

}
