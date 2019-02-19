package com.pcchin.actualtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set version
        TextView versionView = findViewById(R.id.app_version_text);
        versionView.setText(String.format(Locale.ENGLISH, "Version %s", BuildConfig.VERSION_NAME));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onLicenseBtnPressed(View view) {
        Intent intent = new Intent(this, LicenseActivity.class);
        startActivity(intent);
    }
}
