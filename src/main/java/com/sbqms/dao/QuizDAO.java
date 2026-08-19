package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sbqms.model.Quiz;

public class QuizDAO {

    private Connection connection;

    public QuizDAO(Connection connection) {
        this.connection = connection;
    }

    // Creates a new quiz and returns the generated quizID, or -1 on failure.
    public int createQuiz(int teacherID, String quizTitle, String description,
                           int timeLimit, String status) {

        String sql = "INSERT INTO Quiz (teacherID, quizTitle, description, timeLimit, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, teacherID);
            statement.setString(2, quizTitle);
            statement.setString(3, description);
            statement.setInt(4, timeLimit);
            statement.setString(5, status);

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

    public List<Quiz> getQuizzesByTeacher(int teacherID) {

        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM Quiz WHERE teacherID = ? ORDER BY quizID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, teacherID);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                quizzes.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public List<Quiz> getAvailableQuizzes() {

        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM Quiz WHERE status = 'Published' ORDER BY quizID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                quizzes.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public Quiz getQuizById(int quizID) {

        String sql = "SELECT * FROM Quiz WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapRow(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateQuiz(int quizID, String quizTitle, String description, int timeLimit) {

        String sql = "UPDATE Quiz SET quizTitle = ?, description = ?, timeLimit = ? "
                + "WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, quizTitle);
            statement.setString(2, description);
            statement.setInt(3, timeLimit);
            statement.setInt(4, quizID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateQuizStatus(int quizID, String status) {

        String sql = "UPDATE Quiz SET status = ? WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, status);
            statement.setInt(2, quizID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean deleteQuiz(int quizID) {

        String sql = "DELETE FROM Quiz WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("||  Cannot delete this quiz. It may already have");
            System.out.println("||  questions attached or student attempts recorded.");
            return false;
        }
    }

    private Quiz mapRow(ResultSet result) throws Exception {

        return new Quiz(
                result.getInt("quizID"),
                result.getInt("teacherID"),
                result.getString("quizTitle"),
                result.getString("description"),
                result.getInt("timeLimit"),
                result.getTimestamp("dateCreated"),
                result.getString("status")
        );
    }
}
