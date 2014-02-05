package com.jk.alienalarm.db;

public class AlarmInfo {
    public static final int ONCE = 0;
    public static final int TWICE = 1;
    public static final int THRICE = 2;

    public long id;
    public String name;
    public boolean isEnable;
    public int hour;
    public int minute;
    public int times = ONCE;
}
