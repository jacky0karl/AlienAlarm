package com.jk.alienalarm;

import com.jk.alienalarm.db.DBHelper;

import android.app.Activity;
import android.content.Intent;
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
			startActivity(intent);
		} else if (item.getItemId() == R.id.menu_settings) {

		} else if (item.getItemId() == R.id.menu_about) {

		}
		return true;
	}

	// Date getTime() {
	// int hour = mTimePicker.getCurrentHour();
	// int minute = mTimePicker.getCurrentMinute();
	//
	// Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	// String date = sdf.format(System.currentTimeMillis());
	// try {
	// calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse(date));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// long time = calendar.getTimeInMillis();
	// time += (hour * 60 + minute) * 60 * 1000;
	//
	// Date dateTime = new Date();
	// dateTime.setTime(time);
	// return dateTime;
	// }

}
