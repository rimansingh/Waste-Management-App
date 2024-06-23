package com.example.projecta.model;

public class FirebaseDatabase {
    private String userID;
    private String firstName;
    private String lastName;
    private String userPhone;
    private String imgProfile;
    private String status;
    private Long earnedPoints;

    public FirebaseDatabase() {
    }

    public FirebaseDatabase(String userID, String firstName, String lastName, String userPhone, String imgProfile, String status, Long earnedPoints) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.imgProfile = imgProfile;
        this.status = status;
        this.earnedPoints = earnedPoints;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Long earnedPoints) {
        this.earnedPoints = earnedPoints;
    }
}