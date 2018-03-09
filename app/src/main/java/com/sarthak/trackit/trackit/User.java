package com.sarthak.trackit.trackit;

public class User {

    private String username;
    private String displayName;
    private String email;
    private String deviceToken;
    private String profileImage;
    private String thumbImage;

    public User() {
        //empty constructor
    }

    public User(String username, String displayName, String email, String deviceToken, String profileImage, String thumbImage) {

        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.deviceToken = deviceToken;
        this.profileImage = profileImage;
        this.thumbImage = thumbImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }
}
