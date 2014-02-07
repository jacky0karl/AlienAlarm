package com.jk.alienalarm.db;

public class AlarmInfo {
    /** for alarm times */
    public static final int ONCE = 0;
    public static final int TWICE = 1;
    public static final int THRICE = 2;

    /** for alarm interval */
    public static final int FIVE_MINUTE = 0;
    public static final int TEN_MINUTE = 1;
    public static final int FIFTEEN_MINUTE = 2;
    public static final int HALF_AN_HOUR = 3;

    public long id;
    public String name;
    public boolean isEnable;
    public int hour;
    public int minute;
    public int times = ONCE;
    public int interval = FIVE_MINUTE;
}
