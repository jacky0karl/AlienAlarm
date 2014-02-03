package com.jk.alienalarm;

import java.util.Calendar;
import java.util.TimeZone;

import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmEditActivity extends Activity {
    public static final String ALARM_ID = "AlarmId";

    private DBHelper mDBHelper;
    private TimePicker mTimePicker;
    private EditText mEditName;
    private Button mOkButton;
    private Button mCancelButton;
    private long mAlarmId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        mAlarmId = getIntent().getLongExtra(ALARM_ID, -1);
        mDBHelper = new DBHelper(this);
        initView();
    }

    private void initView() {
        mOkButton = (Button) findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AlarmEditActivity.this, R.string.pls_enter_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                mDBHelper.newAlarm(name, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());

                finish();
                Intent intent = new Intent(AlarmEditActivity.this, AlarmListActivity.class);
                startActivity(intent);
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditName = (EditText) findViewById(R.id.name);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        if (mAlarmId != -1) {
            AlarmInfo info = mDBHelper.getAlarm(mAlarmId);
            if (info != null) {
                mEditName.setText(info.name);
                mTimePicker.setCurrentHour(info.hour);
                mTimePicker.setCurrentMinute(info.minute);
            }
        }
    }
}
