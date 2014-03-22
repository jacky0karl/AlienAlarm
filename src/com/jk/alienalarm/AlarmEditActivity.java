package com.jk.alienalarm;

import java.util.Calendar;
import java.util.TimeZone;

import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;
import com.jk.alienalarm.db.RepeatabilityHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmEditActivity extends Activity {
    public static final String ALARM_ID = "AlarmId";
    public static final int NO_ALARM_ID = -1;

    private DBHelper mDBHelper;
    private TimePicker mTimePicker;
    private EditText mEditName;
    private Spinner mTimes;
    private Spinner mInterval;
    private TextView mRepeatability;
    private TextView mRingtone;
    private Button mOkButton;
    private Button mCancelButton;

    private boolean[] mSelectedItems;
    private AlarmInfo mInfo = null;
    private long mAlarmId = NO_ALARM_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        mAlarmId = getIntent().getLongExtra(ALARM_ID, NO_ALARM_ID);
        mDBHelper = new DBHelper(this);
        initView();
        initSettings();
    }

    private void initView() {
        mOkButton = (Button) findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditName.getText().toString().trim();
                String ringtone = mRingtone.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AlarmEditActivity.this, R.string.pls_enter_name, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ringtone)) {
                    Toast.makeText(AlarmEditActivity.this, R.string.pls_enter_ringtone, Toast.LENGTH_SHORT).show();
                } else {
                    if (mAlarmId == NO_ALARM_ID) {
                        mDBHelper.newAlarm(name, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), mTimes.getSelectedItemPosition(),
                                mInterval.getSelectedItemPosition(), mInfo.repeatability, true, mInfo.ringtone);
                    } else {
                        mDBHelper.modifyAlarm(mAlarmId, name, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(),
                                mTimes.getSelectedItemPosition(), mInterval.getSelectedItemPosition(), mInfo.repeatability, true, mInfo.ringtone);
                    }
                    finish();
                }
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
        mTimes = (Spinner) findViewById(R.id.times);
        mInterval = (Spinner) findViewById(R.id.interval);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        // mTimePicker.setIs24HourView(true);
        mRepeatability = (TextView) findViewById(R.id.repeatability);
        mRepeatability.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatabilityDialog();
            }
        });

        mRingtone = (TextView) findViewById(R.id.ringtone);
        mRingtone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRingtone();
            }
        });
    }

    private void initSettings() {
        if (mAlarmId == NO_ALARM_ID) {
            mInfo = new AlarmInfo();
            mInfo.repeatability = AlarmInfo.NO_REPEAT;
            mRepeatability.setText(R.string.no_repeat);

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        } else {
            mInfo = mDBHelper.getAlarm(mAlarmId);
            if (mInfo != null) {
                mEditName.setText(mInfo.name);
                mTimePicker.setCurrentHour(mInfo.hour);
                mTimePicker.setCurrentMinute(mInfo.minute);
                mTimes.setSelection(mInfo.times);
                mInterval.setSelection(mInfo.interval);

                mSelectedItems = RepeatabilityHelper.parseRepeatability(mInfo.repeatability);
                mRepeatability.setText(RepeatabilityHelper.genRepeatabilityString(getApplicationContext(), mSelectedItems));
                mRingtone.setText(Uri.parse(mInfo.ringtone).getLastPathSegment());
            }
        }
    }

    private void showRepeatabilityDialog() {
        OnMultiChoiceClickListener listener = new OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mSelectedItems[which] = isChecked;
                mInfo.repeatability = RepeatabilityHelper.calcRepeatability(mSelectedItems);
                mRepeatability.setText(RepeatabilityHelper.genRepeatabilityString(getApplicationContext(), mSelectedItems));
            }
        };

        if (mAlarmId == NO_ALARM_ID) {
            mSelectedItems = new boolean[] { false, false, false };
        } else {
            mSelectedItems = RepeatabilityHelper.parseRepeatability(mInfo.repeatability);
        }

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.choose_alarm_repeatability)
                .setMultiChoiceItems(R.array.alarm_repeatability, mSelectedItems, listener).create();
        dialog.show();
    }

    private void setRingtone() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String ringtone = cursor.getString(index);
                    mInfo.ringtone = ringtone;
                    mRingtone.setText(Uri.parse(ringtone).getLastPathSegment());
                }
            }
        }
    }

}
