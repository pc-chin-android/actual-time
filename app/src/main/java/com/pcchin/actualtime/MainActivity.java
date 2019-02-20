package com.pcchin.actualtime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLoc;
    private LocationRequest locationRequest;
    private boolean doubleBackToExitPressedOnce = false;
    private TimeThread timeThread;
    private LocationCallback locationCallback;
    // CurrentLong must not be valid val (-180 < currentVal < 180)
    double currentLong = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLoc = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    currentLong = locationResult.getLastLocation().getLongitude();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView longVal = findViewById(R.id.long_int);
                            longVal.setText(String.format(Locale.ENGLISH, "%s", currentLong));
                        }
                    });
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();

        fusedLoc.removeLocationUpdates(locationCallback);
        this.timeThread.setRunning(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissionList,100);
        } else {
            this.setLastLong();
        }

        this.timeThread = new TimeThread(this);
        this.timeThread.setRunning(true);
        this.timeThread.start();
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
                    doubleBackToExitPressedOnce = false;
                }
            }, 1500);

        }
    }

    @SuppressLint("MissingPermission")
    private void setLastLong() {
        // Permission checked in this.getLastLong
        if (currentLong < -180 && currentLong > 180) {
            fusedLoc.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLong = location.getLongitude();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView longVal = findViewById(R.id.long_int);
                                        longVal.setText(String.format(Locale.ENGLISH, "%s", currentLong));
                                    }
                                });
                            }
                        }
                    });
        }
        fusedLoc.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch(requestCode) {
            case 100:
                // getLastLong
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.setLastLong();
                } else {
                    Toast.makeText(this, "Location permission not granted. " +
                            "Please enable the permission and try again.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}