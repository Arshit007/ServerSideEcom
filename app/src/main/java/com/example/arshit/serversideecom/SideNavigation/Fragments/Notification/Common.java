package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;

public class Common {

   private static final String BASE_URL = "https://fcm.googleapis.com/";

   public static APIService getFCMService(){

       return FCMRetrofitClient.getClient(BASE_URL).create(APIService.class);
   }

}
