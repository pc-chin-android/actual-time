package com.pcchin.actualtime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
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
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    SimpleDateFormat dtFormat;
                    if (DateFormat.is24HourFormat(mainActivity)) {
                        dtFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    } else {
                        dtFormat = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
                    }
                    Date currentDate = new Date();

                    TextView sysTime = mainActivity.findViewById(R.id.sys_time_int);
                    sysTime.setText(dtFormat.format(currentDate));

                    if (mainActivity.currentLong >= -180 && mainActivity.currentLong <= 180) {
                        // Calculate current time in secs based on current coordinates
                        double diffSecs = mainActivity.currentLong / 180 * 60 * 60 * 12
                                - ((double) TimeZone.getDefault().getOffset(Calendar.ZONE_OFFSET) / 1000);
                        Calendar newCal = Calendar.getInstance();
                        newCal.setTime(currentDate);
                        newCal.add(Calendar.SECOND, (int) diffSecs);

                        double hrs;
                        if (diffSecs > 0) {
                            hrs = Math.abs(Math.floor(diffSecs / 3600));
                        } else {
                            hrs = Math.abs(Math.ceil(diffSecs / 3600));
                        }
                        double mins = Math.floor((Math.abs(diffSecs) - (Math.abs(hrs) * 3600)) / 60);
                        double secs = Math.floor(Math.abs(diffSecs) - (Math.abs(hrs) * 3600) - (mins * 60));

                        // Set time
                        TextView actTime = mainActivity.findViewById(R.id.act_time_int);
                        actTime.setText(dtFormat.format(newCal.getTime()));
                        TextView diffTime = mainActivity.findViewById(R.id.diff_time_int);
                        if (diffSecs >= 0) {
                            diffTime.setText(String.format(Locale.ENGLISH, "%02d:%02d:%02d", (int) hrs, (int) mins, (int) secs));
                        } else {
                            diffTime.setText(String.format(Locale.ENGLISH, "-%02d:%02d:%02d", (int) hrs, (int) mins, (int) secs));
                        }
                    }
                }
            });
            try {
                long sleepTime = 1000 + tempTime - (System.nanoTime() / 1000);
                if (sleepTime > 0) {
                    sleep(sleepTime);
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