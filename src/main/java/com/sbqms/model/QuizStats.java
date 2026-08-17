package com.sbqms.model;

// Aggregated statistics for a single quiz, used by the Reports feature.
public class QuizStats {

    private String quizTitle;
    private int totalAttempts;
    private double averageScore;
    private double averagePercentage;
    private int highestScore;
    private int lowestScore;
    private int passCount;
    private int failCount;

    public QuizStats(String quizTitle, int totalAttempts, double averageScore,
                      double averagePercentage, int highestScore, int lowestScore,
                      int passCount, int failCount) {

        this.quizTitle = quizTitle;
        this.totalAttempts = totalAttempts;
        this.averageScore = averageScore;
        this.averagePercentage = averagePercentage;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.passCount = passCount;
        this.failCount = failCount;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public double getAveragePercentage() {
        return averagePercentage;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }

    public int getPassCount() {
        return passCount;
    }

    public int getFailCount() {
        return failCount;
    }
}
