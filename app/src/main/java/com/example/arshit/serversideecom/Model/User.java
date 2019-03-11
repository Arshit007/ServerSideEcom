package com.example.arshit.serversideecom.Model;

public class User {

    private String id;
    private String username;
    private String profilePic;
    private String Contact;

    public User(String id, String username, String profilePic, String contact) {
        this.id = id;
        this.username = username;
        this.profilePic = profilePic;
        Contact = contact;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public User()
    {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() { return profilePic; }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
