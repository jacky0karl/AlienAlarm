package com.jk.alienalarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;
import com.jk.alienalarm.db.RepeatabilityHelper;

public class AlarmImpl {
    private static final int DEAD_ALARM = -1;

    private static AlarmImpl mSelf = null;
    private DBHelper mDBhelper = null;
    private Context mContext = null;
    private AlarmManager mAlarmMgr = null;
    private HashMap<Long, PendingIntent> mIntentMap = null;
    private List<AlarmInfo> mAlarmList = null;

    public synchronized static AlarmImpl getInstance() {
        if (mSelf == null) {
            mSelf = new AlarmImpl();
        }
        return mSelf;
    }

    private AlarmImpl() {
        mIntentMap = new HashMap<Long, PendingIntent>();
        mAlarmList = new ArrayList<AlarmInfo>();
    }

    public void setContext(Context context) {
        mContext = context;
        mDBhelper = new DBHelper(context);
        mAlarmMgr = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
    }

    public void updatePendingIntent(long id, PendingIntent intent) {
        mIntentMap.remove(id);
        mIntentMap.put(id, intent);
    }

    public void updateAlarm(long id) {
        cancelAlarm(id);

        AlarmInfo info = getInfoById(id);
        long date = getDateAndTime(info, false);
        if (date != DEAD_ALARM) {
            PendingIntent intent = getPendingIntent(info, 0);
            mIntentMap.put(info.id, intent);
            mAlarmMgr.set(AlarmManager.RTC_WAKEUP, date, intent);
        }
    }

    public void cancelAlarm(long id) {
        mAlarmMgr.cancel(mIntentMap.get(id));
        mIntentMap.remove(id);
    }

    public void resetAlarm(AlarmInfo info, int alarmTime) {
        long date = getDateAndTime(info, true);
        if (date == DEAD_ALARM) {
            return;
        }

        PendingIntent alarmIntent = AlarmImpl.getInstance().getPendingIntent(
                info, alarmTime + 1);
        AlarmImpl.getInstance().updatePendingIntent(info.id, alarmIntent);

        // FIXME
        long plusTime = 1000;
        switch (info.interval) {
        case AlarmInfo.FIVE_MINUTE:
            plusTime *= 5;
            break;
        case AlarmInfo.TEN_MINUTE:
            plusTime *= 10;
            break;
        case AlarmInfo.FIFTEEN_MINUTE:
            plusTime *= 15;
            break;
        case AlarmInfo.HALF_AN_HOUR:
            plusTime *= 30;
            break;
        default:
            break;
        }

        long time = date + plusTime * (alarmTime + 1);
        mAlarmMgr.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);
    }

    public PendingIntent getPendingIntent(AlarmInfo info, int alarmTime) {
        Intent intent = new Intent(mContext, NotifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri data = Uri.parse("alarm/" + info.id);
        intent.setData(data);
        intent.putExtra(NotifyActivity.ALARM_ID, info.id);
        intent.putExtra(NotifyActivity.ALARM_TIME, alarmTime);
        return PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public AlarmInfo getInfoById(long id) {
        for (int i = 0; i < mAlarmList.size(); i++) {
            if (mAlarmList.get(i).id == id) {
                return mAlarmList.get(i);
            }
        }
        return null;
    }

    public void updateAllAlarms() {
        mAlarmList = mDBhelper.getAlarmList(true);
        Iterator iter = mIntentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            PendingIntent val = (PendingIntent) entry.getValue();
            mAlarmMgr.cancel(val);
        }
        mIntentMap.clear();

        for (int i = 0; i < mAlarmList.size(); i++) {
            AlarmInfo info = mAlarmList.get(i);
            long date = getDateAndTime(info, false);
            if (date != DEAD_ALARM) {
                PendingIntent intent = getPendingIntent(info, 0);
                mIntentMap.put(info.id, intent);
                mAlarmMgr.set(AlarmManager.RTC_WAKEUP, date, intent);
            }
        }
    }

    public void wakeScreen() {
        PowerManager pm = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        WakeLock lock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "jk");
        lock.acquire(5000);
    }

    public long getDateAndTime(AlarmInfo info, boolean isRealarm) {
        Calendar calendar = null;
        if (isRealarm) {
            calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            calendar = getAlarmDate(info);
            if (calendar == null) {
                return DEAD_ALARM;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(calendar.getTime());
        try {
            calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = calendar.getTimeInMillis();
        time += (info.hour * 60 + info.minute) * 60 * 1000;
        return time;
    }

    public Calendar getAlarmDate(AlarmInfo info) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (info.repeatability == AlarmInfo.NO_REPEAT) {
            if (needAlarmToday(info)) {
                return calendar;
            } else {
                return null;
            }
        } else {
            // plus one day every time till it matches the repeatability
            while (true) {
                int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                boolean contains = RepeatabilityHelper.contains(
                        info.repeatability, week);
                if (contains) {
                    return calendar;
                } else {
                    calendar.set(Calendar.DAY_OF_YEAR,
                            calendar.get(Calendar.DAY_OF_YEAR) + 1);
                }
            }
        }
    }

    private boolean needAlarmToday(AlarmInfo info) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMin = calendar.get(Calendar.MINUTE) + (currHour * 60);
        int min = info.minute + (info.hour * 60);
        return currMin < min ? true : false;
    }
}
