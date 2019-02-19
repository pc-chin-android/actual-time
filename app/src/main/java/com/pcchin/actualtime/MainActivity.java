package com.pcchin.actualtime;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private TimeThread timeThread;
    private LocationThread locationThread;
    // CurrentLong must not be valid val (-180 < currentVal < 180)
    double currentLong = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPause() {
        super.onPause();

        boolean retryTime = true;
        while (retryTime) {
            try {
                this.timeThread.setRunning(false);
                this.timeThread.join();
                retryTime = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean retryLoc = true;
        while (retryLoc) {
            try {
                this.locationThread.setRunning(false);
                this.locationThread.join();
                retryLoc = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.timeThread = new TimeThread(this);
        this.timeThread.setRunning(true);
        this.timeThread.start();

        this.locationThread = new LocationThread(this);
        this.locationThread.setRunning(true);
        this.locationThread.start();
    }

    public void onAbtBtnSelected(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Press back to exit
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1500);

        }
    }

}