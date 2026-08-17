package com.sbqms.model;

// Simple read-only row used to display joined result data
// (Student_Result + Quiz + Student + Attempt) without needing
// a separate DAO round trip for each piece of information.
public class ResultReportRow {

    private String studentName;
    private String studentSection;
    private String quizTitle;
    private int score;
    private int totalItems;
    private double percentage;
    private String status;
    private String dateTaken;

    public ResultReportRow(String studentName, String studentSection, String quizTitle,
                            int score, int totalItems, double percentage,
                            String status, String dateTaken) {

        this.studentName = studentName;
        this.studentSection = studentSection;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalItems = totalItems;
        this.percentage = percentage;
        this.status = status;
        this.dateTaken = dateTaken;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSection() {
        return studentSection;
    }

    public String getQuizTitle() {
        return quizTitle;
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

    public String getDateTaken() {
        return dateTaken;
    }
}
