package com.wind.countservice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by zhangcong on 2018/2/3.
 */

public class CounterService extends Service implements IcounterService {
    private boolean stop = false;
    private IcounterCallBack icounterCallBack = null;
    private final static String LOG = "CounterService";

    private final IBinder binder = new CounterBiner();

    public class CounterBiner extends Binder {
        public CounterService getService() {
            return CounterService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void startCounter(int initVal, IcounterCallBack callBack) {
        icounterCallBack = callBack;
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                icounterCallBack.count(integer);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                int val = values[0];
                icounterCallBack.count(val);
            }

            @Override
            protected Integer doInBackground(Integer... integers) {
                Integer initCounter = integers[0];
                stop = false;
                while (!stop) {
                    publishProgress(initCounter);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initCounter++;
                }
                return initCounter;
            }
        };
        task.execute(initVal);
    }

    @Override
    public void stopCounter() {
        stop = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG, "Counter Service Created");
    }

}
