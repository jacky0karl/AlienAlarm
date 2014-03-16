package com.jk.alienalarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;
import com.jk.alienalarm.service.AlarmService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

    // To do list
    // notification
    // flash light
    // setting: alarm length
    // setting: theme dark or light

    private TextView mNextAlarmName;
    private TextView mNextAlarmTime;
    private DBHelper mDBHelper;
    private AlarmInfo mNextAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNextAlarm();
    }

    private void init() {
        mNextAlarmName = (TextView) findViewById(R.id.alarm_name);
        mNextAlarmTime = (TextView) findViewById(R.id.alarm_time);
        mDBHelper = new DBHelper(this);
        updateNextAlarm();
    }

    private void updateNextAlarm() {
        mNextAlarm = mDBHelper.getNextAlarm();
        if (mNextAlarm != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTimeInMillis(mNextAlarm.nextAlarmDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(calendar.getTime());
            mNextAlarmName.setText(mNextAlarm.name);
            mNextAlarmTime.setText(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_alarm_list) {
            Intent intent = new Intent(this, AlarmListActivity.class);
            Uri data = Uri.parse("jk");
            intent.setData(data);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_settings) {

        } else if (item.getItemId() == R.id.menu_about) {

        }
        return true;
    }
}
