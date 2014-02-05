package com.jk.alienalarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;

public class AlarmCalculator {

    private static final int MILLI_SEC_FOR_ONE_DAY = 24 * 60 * 60 * 1000;

    public static long getTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMinute = calendar.get(Calendar.MINUTE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(System.currentTimeMillis());
        try {
            calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = calendar.getTimeInMillis();
        time += (hour * 60 + minute) * 60 * 1000;

        // the given time of today is past
        if (currHour * 60 + currMinute >= hour * 60 + minute) {
            time += MILLI_SEC_FOR_ONE_DAY;
        }
        return time;
    }

    public static void updateAlarms(Context context) {
        DBHelper helper = new DBHelper(context);
        List<AlarmInfo> alarmList = helper.getAlarmList(true);
        AlarmManager alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        alarmMgr.cancel(alarmIntent);
        for (int i = 0; i < alarmList.size(); i++) {
            AlarmInfo info = alarmList.get(i);
            alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    getTime(info.hour, info.minute), alarmIntent);
//            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
//                    getTime(info.hour, info.minute), 1000 * 60 * 20,
//                    alarmIntent);
        }
    }
}
