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

                    if (mainActivity.currentLong >= -180 && mainActivity.currentLong <= 180) {
                        // Calculate current time in secs based on current coordinates
                        double currentSecs = (calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60) +
                                (calendar.get(Calendar.MINUTE) * 60) + calendar.get(Calendar.SECOND);
                        double diffSecs = (mainActivity.currentLong / 180) * 60 * 60 * 12;
                        currentSecs += diffSecs;
                        if (currentSecs < 0) {
                            currentSecs += 60 * 60 * 24;
                        }

                        // Convert them into hours, mins, secs
                        int[] currentTimeArray = convertTime(currentSecs);
                        int[] diffTimeArray = convertTime(diffSecs);

                        // Set time
                        TextView actTime = mainActivity.findViewById(R.id.act_time_int);
                        actTime.setText(String.format(Locale.ENGLISH, "%02d:%02d:%02d",
                                currentTimeArray[0], currentTimeArray[1], currentTimeArray[2]));
                        TextView diffTime = mainActivity.findViewById(R.id.diff_time_int);
                        diffTime.setText(String.format(Locale.ENGLISH, "%03d:%02d:%02d",
                                diffTimeArray[0], diffTimeArray[1], diffTimeArray[2]));
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

    private static int[] convertTime(double secs) {
        int[] returnInt = new int[3];
        returnInt[0] = (int) Math.floor(secs / 60 / 60);
        returnInt[1] = (int) Math.floor((secs - returnInt[0] * 60 * 60) / 60);
        returnInt[2] = (int) Math.floor(secs - (returnInt[0] * 60 * 60) - (returnInt[1] * 60));
        if (returnInt[0] >= 24) {
            returnInt[0] -= 24;
        }
        return returnInt;
    }
}