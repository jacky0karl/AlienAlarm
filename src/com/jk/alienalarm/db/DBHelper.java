package com.jk.alienalarm.db;

import java.util.ArrayList;
import java.util.List;

import com.jk.alienalarm.db.DBInfo.AlarmTable;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DBHelper {
	private Context mContext;

	public DBHelper(Context context) {
		mContext = context;
	}

	public void newAlarm(String name, int hour, int minute) {
		ContentValues values = new ContentValues();
		values.put(AlarmTable.NAME, name);
		values.put(AlarmTable.IS_ENABLE, 1);
		values.put(AlarmTable.HOUR, hour);
		values.put(AlarmTable.MINUTE, minute);

		Uri uri = mContext.getContentResolver().insert(AlarmTable.CONTENT_URI,
				values);
		ContentUris.parseId(uri);
	}

	public void modifyAlarm(String name, int hour, int minute) {
		ContentValues values = new ContentValues();
		values.put(AlarmTable.NAME, name);
		values.put(AlarmTable.IS_ENABLE, 1);
		values.put(AlarmTable.HOUR, hour);
		values.put(AlarmTable.MINUTE, minute);

		Uri uri = mContext.getContentResolver().insert(AlarmTable.CONTENT_URI,
				values);
		ContentUris.parseId(uri);
	}

	public int setAlarmEnable(long id, boolean enable) {
		ContentValues values = new ContentValues();
		values.put(AlarmTable.IS_ENABLE, enable ? 1 : 0);
		String selection = AlarmTable._ID + "=" + id;
		return mContext.getContentResolver().update(AlarmTable.CONTENT_URI,
				values, selection, null);
	}

	public AlarmInfo getAlarm(long id) {
		AlarmInfo info = null;
		String selection = AlarmTable._ID + "=" + id;
		Cursor cursor = mContext.getContentResolver().query(
				AlarmTable.CONTENT_URI, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = fillAlarmInfo(cursor);
			}
			cursor.close();
		}
		return info;
	}

	public List<AlarmInfo> getAlarmList(boolean onlyEnable) {
		String selection = null;
		if (onlyEnable) {
			selection = AlarmTable.IS_ENABLE + "=1";
		}

		List<AlarmInfo> alarmList = new ArrayList<AlarmInfo>();
		Cursor cursor = mContext.getContentResolver().query(
				AlarmTable.CONTENT_URI, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					AlarmInfo info = fillAlarmInfo(cursor);
					alarmList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return alarmList;
	}

	private AlarmInfo fillAlarmInfo(Cursor cursor) {
		AlarmInfo info = new AlarmInfo();
		int dxzs = cursor.getColumnIndex(AlarmTable._ID);
		info.id = cursor.getLong(0);
		info.isEnable = (cursor.getInt(cursor
				.getColumnIndex(AlarmTable.IS_ENABLE)) == 1) ? true : false;
		info.name = cursor.getString(cursor.getColumnIndex(AlarmTable.NAME));
		info.hour = cursor.getInt(cursor.getColumnIndex(AlarmTable.HOUR));
		info.minute = cursor.getInt(cursor.getColumnIndex(AlarmTable.MINUTE));
		return info;
	}
}
