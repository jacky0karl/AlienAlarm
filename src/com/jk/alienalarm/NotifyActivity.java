package com.jk.alienalarm;

import java.util.ArrayList;
import java.util.List;

import com.jk.alienalarm.action.AlarmAction;
import com.jk.alienalarm.action.SoundAction;
import com.jk.alienalarm.action.VibrationAction;
import com.jk.alienalarm.db.AlarmInfo;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotifyActivity extends Activity {
    public static final String ALARM_ID = "alarmId";
    public static final String ALARM_TIME = "alarmTime";

    private boolean mLastAlarm = false;
    private int mAlarmTime = 0;
    private AlarmInfo mAlarmInfo;

    private Handler mHandler;
    private KeyguardLock mKeyguardLock;
    private List<AlarmAction> mActionList;

    private Button mLaterButton;
    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        init();
        reset();
    }

    private void init() {
        mName = (TextView) findViewById(R.id.alarm_name);
        mLaterButton = (Button) findViewById(R.id.later_button);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = keyguardManager.newKeyguardLock("jk");
        mHandler = new Handler();
        initActions();
    }

    private void initActions() {
        mActionList = new ArrayList<AlarmAction>();
        SoundAction soundAction = new SoundAction();
        VibrationAction vibrationAction = new VibrationAction(this);
        mActionList.add(soundAction);
        mActionList.add(vibrationAction);
    }

    private void startActions() {
        for (AlarmAction action : mActionList) {
            action.start();
        }
    }

    private void stopActions() {
        for (AlarmAction action : mActionList) {
            action.stop();
        }
    }

    private void releaseActions() {
        for (AlarmAction action : mActionList) {
            action.release();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        reset();
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

    @Override
    protected void onDestroy() {
        stopActions();
        releaseActions();
        super.onDestroy();
    }

    private void reset() {
        resetVariables();
        resetView();
        setAutoClose();

        AlarmImpl.getInstance().wakeScreen();
        if (mLastAlarm) {
            AlarmImpl.getInstance().cancelAlarm(mAlarmInfo.id);
        } else {
            AlarmImpl.getInstance().resetAlarm(mAlarmInfo, mAlarmTime);
        }

        stopActions();
        startActions();
    }

    private void resetVariables() {
        mAlarmTime = getIntent().getIntExtra(ALARM_TIME, 0);
        long id = getIntent().getLongExtra(ALARM_ID, -1);
        mAlarmInfo = AlarmImpl.getInstance().getInfoById(id);

        if (mAlarmTime >= mAlarmInfo.times) {
            mLastAlarm = true;
        }
    }

    private void resetView() {
        mName.setText(mAlarmInfo.name);
        if (mLastAlarm) {
            mLaterButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setAutoClose() {
        mHandler.removeCallbacks(mTask);
        mHandler.postDelayed(mTask, 60 * 1000);
    }

    public void onStopClick(View v) {
        AlarmImpl.getInstance().updateAlarm(mAlarmInfo.id);
        finish();
    }

    public void onLaterClick(View v) {
        finish();
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            AlarmImpl.getInstance().updateAlarm(mAlarmInfo.id);
            finish();
        }
    };
}
