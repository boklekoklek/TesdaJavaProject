package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.sbqms.model.QuizStats;
import com.sbqms.model.ResultReportRow;

public class StudentResultDAO {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

    private Connection connection;

    public StudentResultDAO(Connection connection) {
        this.connection = connection;
    }

    // Saves the final result for a completed attempt.
    // Returns the generated resultID, or -1 on failure.
    public int saveResult(int attemptID, int studentID, int quizID, int score,
                           int totalItems, double percentage, String status) {

        String sql = "INSERT INTO Student_Result "
                + "(attemptID, studentID, quizID, score, totalItems, percentage, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, attemptID);
            statement.setInt(2, studentID);
            statement.setInt(3, quizID);
            statement.setInt(4, score);
            statement.setInt(5, totalItems);
            statement.setDouble(6, percentage);
            statement.setString(7, status);

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

    // All results belonging to one student (for "My Results").
    public List<ResultReportRow> getResultsByStudent(int studentID) {

        List<ResultReportRow> rows = new ArrayList<>();

        String sql = "SELECT s.firstName, s.lastName, s.StudSect, q.quizTitle, "
                + "sr.score, sr.totalItems, sr.percentage, sr.status, a.endTime "
                + "FROM Student_Result sr "
                + "JOIN Quiz q ON sr.quizID = q.quizID "
                + "JOIN Student s ON sr.studentID = s.studentID "
                + "JOIN Attempt a ON sr.attemptID = a.attemptID "
                + "WHERE sr.studentID = ? "
                + "ORDER BY sr.resultID DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                rows.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    // All results belonging to quizzes created by one teacher (for "Student Results").
    public List<ResultReportRow> getResultsByTeacher(int teacherID) {

        List<ResultReportRow> rows = new ArrayList<>();

        String sql = "SELECT s.firstName, s.lastName, s.StudSect, q.quizTitle, "
                + "sr.score, sr.totalItems, sr.percentage, sr.status, a.endTime "
                + "FROM Student_Result sr "
                + "JOIN Quiz q ON sr.quizID = q.quizID "
                + "JOIN Student s ON sr.studentID = s.studentID "
                + "JOIN Attempt a ON sr.attemptID = a.attemptID "
                + "WHERE q.teacherID = ? "
                + "ORDER BY sr.resultID DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, teacherID);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                rows.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    // All results for one specific quiz (used to filter "Student Results").
    public List<ResultReportRow> getResultsByQuiz(int quizID) {

        List<ResultReportRow> rows = new ArrayList<>();

        String sql = "SELECT s.firstName, s.lastName, s.StudSect, q.quizTitle, "
                + "sr.score, sr.totalItems, sr.percentage, sr.status, a.endTime "
                + "FROM Student_Result sr "
                + "JOIN Quiz q ON sr.quizID = q.quizID "
                + "JOIN Student s ON sr.studentID = s.studentID "
                + "JOIN Attempt a ON sr.attemptID = a.attemptID "
                + "WHERE sr.quizID = ? "
                + "ORDER BY sr.resultID DESC";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                rows.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    // Aggregated statistics for one quiz, used by the Reports feature.
    public QuizStats getQuizStats(int quizID, String quizTitle) {

        String sql = "SELECT COUNT(*) AS totalAttempts, "
                + "AVG(score) AS avgScore, "
                + "AVG(percentage) AS avgPercentage, "
                + "MAX(score) AS highScore, "
                + "MIN(score) AS lowScore, "
                + "SUM(CASE WHEN status = 'Passed' THEN 1 ELSE 0 END) AS passCount, "
                + "SUM(CASE WHEN status = 'Failed' THEN 1 ELSE 0 END) AS failCount "
                + "FROM Student_Result WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return new QuizStats(
                        quizTitle,
                        result.getInt("totalAttempts"),
                        result.getDouble("avgScore"),
                        result.getDouble("avgPercentage"),
                        result.getInt("highScore"),
                        result.getInt("lowScore"),
                        result.getInt("passCount"),
                        result.getInt("failCount")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QuizStats(quizTitle, 0, 0, 0, 0, 0, 0, 0);
    }

    private ResultReportRow mapRow(ResultSet result) throws Exception {

        String studentName = result.getString("firstName") + " " + result.getString("lastName");

        String dateTaken = "N/A";
        if (result.getTimestamp("endTime") != null) {
            dateTaken = result.getTimestamp("endTime").toLocalDateTime().format(DATE_FORMAT);
        }

        return new ResultReportRow(
                studentName,
                result.getString("StudSect"),
                result.getString("quizTitle"),
                result.getInt("score"),
                result.getInt("totalItems"),
                result.getDouble("percentage"),
                result.getString("status"),
                dateTaken
        );
    }
}
