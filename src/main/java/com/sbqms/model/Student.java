package com.sbqms.model;
public class Student {

    private int studentID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String studSect;

    // Constructor
    public Student(int studentID, String firstName, String lastName,
                   String email, String password, String studSect) {

        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.studSect = studSect;
    }

    // Getters
    public int getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStudSect() {
        return studSect;
    }

    // Setters
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStudSect(String studSect) {
        this.studSect = studSect;
    }
}