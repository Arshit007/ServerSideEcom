package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.example.arshit.serversideecom.R;

/**
 * Helper class to manage notification channels, and create notifications.
 */
public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String PRIMARY_CHANNEL_ID = "com.example.arshit.serversideecom";
    public static final String SECONDARY_CHANNEL_NAME = "Ecommerce";


    public NotificationHelper(Context ctx) {
        super(ctx);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            createchannel();
        }

    }

    private void createchannel(){


        NotificationChannel chan1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(PRIMARY_CHANNEL_ID, SECONDARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            chan1.setLightColor(Color.GREEN);
            chan1.enableLights(false);
            chan1.enableVibration(true);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(chan1);


        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getNotification1(String title, String body, PendingIntent pendingIntent, Uri urisound) {
        return new Notification.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(urisound)
                .setContentIntent(pendingIntent)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(false);
    }




    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
