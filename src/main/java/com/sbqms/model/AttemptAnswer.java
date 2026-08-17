package com.sbqms.model;

public class AttemptAnswer {

    private int attemptAnswerID;
    private int attemptID;
    private int questionID;
    private String selectedAnswer;
    private boolean isCorrect;

    public AttemptAnswer(int attemptAnswerID, int attemptID, int questionID,
                          String selectedAnswer, boolean isCorrect) {

        this.attemptAnswerID = attemptAnswerID;
        this.attemptID = attemptID;
        this.questionID = questionID;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    public int getAttemptAnswerID() {
        return attemptAnswerID;
    }

    public int getAttemptID() {
        return attemptID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setAttemptAnswerID(int attemptAnswerID) {
        this.attemptAnswerID = attemptAnswerID;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
