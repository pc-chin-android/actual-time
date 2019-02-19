package com.pcchin.actualtime;

import java.util.Calendar;
import java.util.Locale;

import android.widget.TextView;

class TimeThread extends Thread {
    private boolean running;
    private MainActivity mainActivity;

    TimeThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (this.running) {
            long tempTime = System.nanoTime()/1000;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();

                    TextView sysTime = mainActivity.findViewById(R.id.sys_time_int);
                    sysTime.setText(String.format(Locale.ENGLISH, "%02d:%02d:%02d",
                            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));

                    if (mainActivity.currentLong >= -180 && mainActivity.currentLong < 180) {
                        // TODO: Update coordinates
                    }
                }
            });
            try {
                long sleepTime = 1000 + tempTime - (System.nanoTime() / 1000);
                if (sleepTime > 0) {
                    sleep(1000 - (System.nanoTime() / 1000) + tempTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}