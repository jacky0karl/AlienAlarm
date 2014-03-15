package com.jk.alienalarm.action;

import android.media.MediaPlayer;

public class SoundAction implements AlarmAction {
    private MediaPlayer mMediaPlayer = null;

    public SoundAction() {
        init();
    }

    @Override
    public void init() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(true);
        mMediaPlayer.reset();
        try {
            mMediaPlayer
                    .setDataSource("/storage/sdcard1/music/Adele/Set Fire to the Rain.mp3");
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}