package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
         {

             "Content-Type:application/json",
             "Authorization:key=AAAAhipE5RU:APA91bEzxIOkuQdVAiSTDE8qTs2ipw6aHE-G8LndKe5My_9gn27dIIxYiVerV70D413re-DDTqh2uwGfuX4eXc0qW82WV7QjMYzEjvcbnnkI-SmQndqSsa5lmSVOsuQmKOJ25myPyYD9"



         }
     )


     @POST("fcm/send")
     Call<MyResponse> sendNotification(@Body Sender body);
}