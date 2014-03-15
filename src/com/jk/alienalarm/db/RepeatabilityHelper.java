package com.jk.alienalarm.db;

import com.jk.alienalarm.R;

import android.content.Context;
import android.text.TextUtils;

public class RepeatabilityHelper {

    public static boolean[] parseRepeatability(int repeatability) {
        boolean[] selectedItems = new boolean[] { false, false, false };
        if ((repeatability & AlarmInfo.WEEKDAY_REPEAT) == 1) {
            selectedItems[0] = true;
        }
        if ((repeatability & AlarmInfo.SATURDAY_REPEAT) == 1) {
            selectedItems[1] = true;
        }
        if ((repeatability & AlarmInfo.SUNDAY_REPEAT) == 1) {
            selectedItems[2] = true;
        }
        return selectedItems;
    }

    public static int calcRepeatability(boolean[] selectedItems) {
        int repeatability = 0;
        if (selectedItems[0] == true) {
            repeatability |= AlarmInfo.WEEKDAY_REPEAT;
        }
        if (selectedItems[1] == true) {
            repeatability |= AlarmInfo.SATURDAY_REPEAT;
        }
        if (selectedItems[2] == true) {
            repeatability |= AlarmInfo.SUNDAY_REPEAT;
        }
        return repeatability;
    }

    public static String genRepeatabilityString(Context context,
            boolean[] selectedItems) {
        String text = "";
        String[] array = context.getResources().getStringArray(
                R.array.alarm_repeatability);
        if (selectedItems[0] == true) {
            text += array[0];
        }
        if (selectedItems[1] == true) {
            if (!TextUtils.isEmpty(text)) {
                text += ",";
            }
            text += array[1];
        }
        if (selectedItems[2] == true) {
            if (!TextUtils.isEmpty(text)) {
                text += ",";
            }
            text += array[2];
        }

        int repeatability = calcRepeatability(selectedItems);
        if (repeatability == AlarmInfo.NO_REPEAT) {
            text = context.getString(R.string.no_repeat);
        } else if (repeatability == AlarmInfo.EVERYDAY_REPEAT) {
            text = context.getString(R.string.everyday);
        }
        return text;
    }

    public static boolean contains(int repeatability, int dayOfWeek) {
        boolean[] array = buildCheckingArray(parseRepeatability(repeatability));
        return array[dayOfWeek];
    }

    private static boolean[] buildCheckingArray(boolean[] selectedItems) {
        boolean[] array = new boolean[] { false, false, false, false, false,
                false, false };
        if (selectedItems[0] == true) {
            array[1] = true;
            array[2] = true;
            array[3] = true;
            array[4] = true;
            array[5] = true;
        }
        if (selectedItems[1] == true) {
            array[6] = true;
        }
        if (selectedItems[2] == true) {
            array[0] = true;
        }
        return array;
    }
}
