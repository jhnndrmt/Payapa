package com.example.payapa;

public class User {
    private String firstName;
    private String middleName;
    private String lastName;
    private String age;
    private String homeAddress;
    private String contactNumber;
    private String emailAddress;
    private String fbLink;
    private String gender;
    private String course;
    private String year;
    private int studentId;
    private String username;
    private String department;
    private String role;


    public User(String firstName, String middleName, String lastName, String age, String homeAddress,
                String contactNumber, String emailAddress, String fbLink, String gender, String course,
                String year, int studentId, String username, String department, String role) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.homeAddress = homeAddress;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.fbLink = fbLink;
        this.gender = gender;
        this.course = course;
        this.year = year;
        this.studentId = studentId;
        this.username = username;
        this.department = department;
        this.role = role;
    }

    // Getters and setters for all fields including studentId
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.role = department;
    }
}
