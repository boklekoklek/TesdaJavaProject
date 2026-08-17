package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AttemptAnswerDAO {

    private Connection connection;

    public AttemptAnswerDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean saveAnswer(int attemptID, int questionID, String selectedAnswer,
                               boolean isCorrect) {

        String sql = "INSERT INTO Attempt_Answer (attemptID, questionID, selectedAnswer, isCorrect) "
                + "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, attemptID);
            statement.setInt(2, questionID);
            statement.setString(3, selectedAnswer);
            statement.setBoolean(4, isCorrect);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
