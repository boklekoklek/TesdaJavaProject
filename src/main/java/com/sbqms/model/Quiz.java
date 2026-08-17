package com.sbqms.model;

import java.sql.Timestamp;

public class Quiz {

    private int quizID;
    private int teacherID;
    private String quizTitle;
    private String description;
    private int timeLimit;
    private Timestamp dateCreated;
    private String status;

    public Quiz(int quizID, int teacherID, String quizTitle, String description,
                int timeLimit, Timestamp dateCreated, String status) {

        this.quizID = quizID;
        this.teacherID = teacherID;
        this.quizTitle = quizTitle;
        this.description = description;
        this.timeLimit = timeLimit;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public int getQuizID() {
        return quizID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public String getDescription() {
        return description;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
