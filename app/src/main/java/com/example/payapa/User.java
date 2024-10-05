package com.example.payapa;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String nickname;
    private int studentID;  // Changed from String to int
    private String photoUrl;

    public User(String email, String firstName, String lastName, String nickname, int studentID, String photoUrl) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.studentID = studentID;  // Changed to int
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public int getStudentID() {
        return studentID;  // Changed to int
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStudentID(int studentID) {  // Changed to int
        this.studentID = studentID;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}