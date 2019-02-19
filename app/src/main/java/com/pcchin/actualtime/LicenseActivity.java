package com.pcchin.actualtime;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        // Set text
        TextView licenseView = findViewById(R.id.license_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            licenseView.setText(Html.fromHtml(getString(R.string.license_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            licenseView.setText(Html.fromHtml(getString(R.string.license_text)));
        }
        licenseView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
