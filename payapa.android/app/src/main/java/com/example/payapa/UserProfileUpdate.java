package com.example.payapa;


public class UserProfileUpdate {
    private String email, course, age, gender;

    public UserProfileUpdate(String email, String course, String age, String gender) {
        this.email = email;
        this.course = course;
        this.age = age;
        this.gender = gender;
    }

    // Getter and Setter methods
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}