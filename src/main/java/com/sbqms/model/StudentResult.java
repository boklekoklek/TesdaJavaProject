package com.sbqms.model;

public class StudentResult {

    private int resultID;
    private int attemptID;
    private int studentID;
    private int quizID;
    private int score;
    private int totalItems;
    private double percentage;
    private String status;

    public StudentResult(int resultID, int attemptID, int studentID, int quizID,
                          int score, int totalItems, double percentage, String status) {

        this.resultID = resultID;
        this.attemptID = attemptID;
        this.studentID = studentID;
        this.quizID = quizID;
        this.score = score;
        this.totalItems = totalItems;
        this.percentage = percentage;
        this.status = status;
    }

    public int getResultID() {
        return resultID;
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

    public int getScore() {
        return score;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
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

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
