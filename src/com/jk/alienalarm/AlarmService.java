package com.jk.alienalarm;

import com.jk.alienalarm.db.DBInfo.AlarmTable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class AlarmService extends Service {

    private ContentObserver mObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            Log.e("AlarmService", "ContentObserver");
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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("AlarmService", "onDestroy");
        getContentResolver().unregisterContentObserver(mObserver);
        super.onDestroy();
    }

    void wakeScreen() {
        Log.e("wakeScreen", "wakeScreen");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        WakeLock lock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "test");
        lock.acquire(5000);
    }

}
