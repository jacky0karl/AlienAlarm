package com.jk.alienalarm;

import com.jk.alienalarm.db.DBHelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DigitalClock;

public class MainActivity extends Activity {

    private DigitalClock mClock;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);
        mClock = (DigitalClock) findViewById(R.id.clock);
        
        Intent intent = new Intent(this, AlarmService.class);      
        startService(intent);
        
        
//        Intent intent2 = new Intent(this, NotifyActivity.class);
//        Uri data = Uri.parse("alarm/" + 5);
//        intent2.setData(data);
//        PendingIntent in = PendingIntent.getActivity(this, 0, intent2,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000,
//                in);
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
