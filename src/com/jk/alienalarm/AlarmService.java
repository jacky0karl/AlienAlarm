package com.jk.alienalarm;

import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("AlarmService", "onStartCommand");
        return START_STICKY;
    }

    void wakeScreen() {
        Log.e("wakeScreen", "wakeScreen");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        WakeLock lock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "test");
        lock.acquire(5000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            wakeScreen();
        }
    };
}
