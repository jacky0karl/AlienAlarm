package com.jk.alienalarm.service;

import com.jk.alienalarm.AlarmImpl;
import com.jk.alienalarm.db.DBInfo.AlarmTable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class AlarmService extends Service {

    private ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            AlarmImpl.getInstance().updateAlarmInfo();
            AlarmImpl.getInstance().updateAllAlarms();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("AlarmService", "onStartCommand");
        getContentResolver().registerContentObserver(AlarmTable.CONTENT_URI,
                false, mObserver);

        this.getApplicationContext();
        AlarmImpl.getInstance().setContext(getApplication());
        // AlarmImpl.getInstance().updateAllAlarms();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("AlarmService", "onDestroy");
        getContentResolver().unregisterContentObserver(mObserver);
        super.onDestroy();
    }

}
