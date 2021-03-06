package com.example.darthvader.answercallsauto;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class NLService extends NotificationListenerService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneStateListener listener;
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ContentResolver resolver=getApplicationContext().getContentResolver();
        final String enabledNotificationsListener = Settings.Secure.getString(resolver,"enabled_notification_listeners");
        final String packageName = getApplicationContext().getPackageName();

        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {

                    Toast.makeText(NLService.this, "Ringing", Toast.LENGTH_SHORT).show();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            StatusBarNotification[] sbn = new StatusBarNotification[0];
                            if (notificationManager != null) {
                                sbn = notificationManager.getActiveNotifications();
                                Log.i("Htey", sbn.toString());
                            } else {
                                Log.i("Great", "Great");
                            }
                            for (StatusBarNotification statusBarNotification : sbn) {
                                Log.i("Lol", statusBarNotification.toString());
                                if (statusBarNotification.getNotification().actions != null) {
                                    for (Notification.Action action : statusBarNotification.getNotification().actions) {
                                        Log.i("Hello", action.title.toString());
                                        if (action.title.toString().equalsIgnoreCase("Answer")) {
                                            PendingIntent pendingIntent = action.actionIntent;
                                            try {
                                                pendingIntent.send();
                                            } catch (PendingIntent.CanceledException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }

        };
        if (manager != null) {
            manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i("Hello", "Notification service runnung");
        if (sbn.getNotification().actions != null) {
            for (Notification.Action action : sbn.getNotification().actions) {
                Log.i("Hello", action.title.toString());
                if (action.title.toString().equalsIgnoreCase("Answer")) {
                    PendingIntent pendingIntent = action.actionIntent;
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
