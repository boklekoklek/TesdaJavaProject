package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class AttemptDAO {

    private Connection connection;

    public AttemptDAO(Connection connection) {
        this.connection = connection;
    }

    // Starts a new attempt (startTime = now, status = "In Progress")
    // and returns the generated attemptID, or -1 on failure.
    public int startAttempt(int studentID, int quizID) {

        String sql = "INSERT INTO Attempt (studentID, quizID, startTime, status) "
                + "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, studentID);
            statement.setInt(2, quizID);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, "In Progress");

            int rows = statement.executeUpdate();

            if (rows > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Marks an attempt as finished (endTime = now, status = "Completed").
    public boolean completeAttempt(int attemptID) {

        String sql = "UPDATE Attempt SET endTime = ?, status = ? WHERE attemptID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, "Completed");
            statement.setInt(3, attemptID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
