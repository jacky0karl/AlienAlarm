package com.jk.alienalarm;

import com.jk.alienalarm.db.AlarmInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NotifyActivity extends Activity {
    public static final String ALARM_ID = "alarmId";
    public static final String ALARM_TIME = "alarmTime";

    private AlarmInfo mAlarmInfo;
    private int mAlarmTime = 0;
    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        mAlarmTime = getIntent().getIntExtra(ALARM_TIME, 0);
        long id = getIntent().getLongExtra(ALARM_ID, -1);
        mAlarmInfo = AlarmImpl.getInstance().getInfoById(id);
        mName = (TextView) findViewById(R.id.alarm_name);
        mName.setText(mAlarmInfo.name);

        AlarmImpl.getInstance().resetAlarm(mAlarmInfo, mAlarmTime);
        AlarmImpl.getInstance().wakeScreen();
    }

    public void onStopClick(View v) {
        AlarmImpl.getInstance().cancelAlarm(mAlarmInfo.id);
        finish();
    }

    public void onLaterClick(View v) {
        finish();
    }
}
