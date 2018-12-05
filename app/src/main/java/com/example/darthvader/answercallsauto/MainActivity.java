package com.example.darthvader.answercallsauto;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentResolver resolver=getApplicationContext().getContentResolver();
        final String enabledNotificationsListener = Settings.Secure.getString(resolver,"enabled_notification_listeners");
        final String packageName = getApplicationContext().getPackageName();
        if (enabledNotificationsListener == null || !enabledNotificationsListener.contains(packageName)) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

        }
        else {
            Intent intent1 = new Intent(this, NLService.class);
            startService(intent1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
