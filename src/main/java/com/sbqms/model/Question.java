package com.sbqms.model;

public class Question {

    private int questionID;
    private int topicID;
    private String questionText;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String correctAnswer;
    private String difficulty;

    public Question(int questionID, int topicID, String questionText,
                     String choiceA, String choiceB, String choiceC, String choiceD,
                     String correctAnswer, String difficulty) {

        this.questionID = questionID;
        this.topicID = topicID;
        this.questionText = questionText;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
    }

    public int getQuestionID() {
        return questionID;
    }

    public int getTopicID() {
        return topicID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // Returns the choice text for a given letter (A, B, C, D), or null if invalid.
    public String getChoiceByLetter(String letter) {

        if (letter == null) {
            return null;
        }

        switch (letter.trim().toUpperCase()) {
            case "A":
                return choiceA;
            case "B":
                return choiceB;
            case "C":
                return choiceC;
            case "D":
                return choiceD;
            default:
                return null;
        }
    }
}
