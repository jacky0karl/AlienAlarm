package com.jk.alienalarm.action;

import android.content.Context;
import android.os.Vibrator;

public class VibrationAction implements AlarmAction {

    private static long[] pattern = { 100, 200, 100, 200, 100, 200, 1000 };
    private Context mContext;
    private Vibrator mVibrator = null;

    public VibrationAction(Context context) {
        mContext = context;
        init();
    }

    @Override
    public void init() {
        mVibrator = (Vibrator) mContext
                .getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void start() {
        mVibrator.vibrate(pattern, 0);
    }

    @Override
    public void stop() {
        mVibrator.cancel();
    }

    @Override
    public void release() {
    }

}
