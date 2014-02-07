package com.jk.alienalarm;

import com.jk.alienalarm.db.AlarmInfo;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class NotifyActivity extends Activity {
    public static final String ALARM_ID = "alarmId";
    public static final String ALARM_TIME = "alarmTime";

    private Handler mHandler;
    KeyguardLock mKeyguardLock;
    private AlarmInfo mAlarmInfo;
    private int mAlarmTime = 0;
    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        init();
        autoClose();
        AlarmImpl.getInstance().resetAlarm(mAlarmInfo, mAlarmTime);
        AlarmImpl.getInstance().wakeScreen();
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = keyguardManager.newKeyguardLock("jk");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mKeyguardLock.disableKeyguard();
    }

    @Override
    protected void onPause() {
        mKeyguardLock.reenableKeyguard();
        super.onPause();
    }

    private void init() {
        mAlarmTime = getIntent().getIntExtra(ALARM_TIME, 0);
        long id = getIntent().getLongExtra(ALARM_ID, -1);
        mAlarmInfo = AlarmImpl.getInstance().getInfoById(id);
        mName = (TextView) findViewById(R.id.alarm_name);
        mName.setText(mAlarmInfo.name);
    }

    private void autoClose() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 60 * 1000);
    }

    public void onStopClick(View v) {
        AlarmImpl.getInstance().cancelAlarm(mAlarmInfo.id);
        finish();
    }

    public void onLaterClick(View v) {
        finish();
    }
}
