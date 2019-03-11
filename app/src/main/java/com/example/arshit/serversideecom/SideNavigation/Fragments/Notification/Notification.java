package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;

public class Notification {

      public String body;
      public  String Title;

    public Notification() {
    }


    public Notification(String body, String title) {
        this.body = body;
        Title = title;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
