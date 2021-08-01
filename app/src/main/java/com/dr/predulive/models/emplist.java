package com.dr.predulive.models;

public class emplist {

    public emplist() {
    }

    String displayName, email, imageUrl,userType;

    public emplist(String displayName, String email, String imageUrl, String userType) {
        this.displayName = displayName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.userType = userType;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
