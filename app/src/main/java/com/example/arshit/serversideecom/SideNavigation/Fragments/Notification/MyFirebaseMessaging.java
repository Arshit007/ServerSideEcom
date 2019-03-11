package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.AllOrderFragment;
import com.example.arshit.serversideecom.SideNavigation.Fragments.OrderDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    String currentUserId;;
    FirebaseAuth mAuth;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            sendNotificationAPI26(remoteMessage);

        }

//        else {
//
//            sendNotification(remoteMessage);
//        }

    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        String  title = notification.getTitle();
        String  content = notification.getBody();


        Intent intent = new Intent(this,AllOrderFragment.class);
        intent.putExtra("ID",currentUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri deafultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder = helper.getNotification1(title,content,pendingIntent,deafultSound);

        helper.getManager().notify(new Random().nextInt(),builder.build());



    }


}
