package com.sbqms.model;

import java.sql.Timestamp;

public class Attempt {

    private int attemptID;
    private int studentID;
    private int quizID;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    public Attempt(int attemptID, int studentID, int quizID,
                    Timestamp startTime, Timestamp endTime, String status) {

        this.attemptID = attemptID;
        this.studentID = studentID;
        this.quizID = quizID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getAttemptID() {
        return attemptID;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getQuizID() {
        return quizID;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
