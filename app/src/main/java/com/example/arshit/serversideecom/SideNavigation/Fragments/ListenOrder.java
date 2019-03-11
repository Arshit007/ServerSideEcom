package com.example.arshit.serversideecom.SideNavigation.Fragments;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.arshit.serversideecom.Model.Order;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Notification.NotificationHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

    private NotificationManager manager;
    public static final String PRIMARY_CHANNEL_ID = "com.example.arshit.serversideecom";
    public static final String SECONDARY_CHANNEL_NAME = "Ecommerce";

    public ListenOrder() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        Order order = dataSnapshot.getValue(Order.class);
        showNotification(dataSnapshot.getKey(), order);


    }


    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private void showNotification(String key, Order order) {

        NotificationChannel chan1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(PRIMARY_CHANNEL_ID, SECONDARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
Intent intent = new Intent(getBaseContext(),Order.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);

            chan1.setLightColor(Color.GREEN);
            chan1.enableLights(false);
            chan1.enableVibration(true);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"My Notification")
                    .setContentTitle(key)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setAutoCancel(true)
                    .setContentText("Hello Server App")
                    .setContentIntent(contentIntent);

            NotificationHelper helper = new NotificationHelper(this);



            NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            helper.getManager().createNotificationChannel(chan1);

            int randomInt = new Random().nextInt(9999-1)+1;
            notificationManager.notify(randomInt,notificationBuilder.build());


        }

   }
}
