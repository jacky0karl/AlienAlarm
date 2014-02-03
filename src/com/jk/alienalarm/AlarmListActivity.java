package com.jk.alienalarm;

import com.jk.alienalarm.db.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AlarmListActivity extends Activity implements OnItemClickListener {

	private DBHelper mDBHelper;
	private ListView mListView;
	AlarmListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);

		mDBHelper = new DBHelper(this);
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.list);
		mAdapter = new AlarmListAdapter(this, mDBHelper);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, AlarmEditActivity.class);
		intent.putExtra(AlarmEditActivity.ALARM_ID, mAdapter.getAlarmList()
				.get(position).id);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(R.string.new_alarm);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = new Intent(this, AlarmEditActivity.class);
		startActivity(intent);
		return true;
	}
}
