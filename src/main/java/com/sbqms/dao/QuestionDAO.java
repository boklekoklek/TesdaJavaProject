package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sbqms.model.Question;

public class QuestionDAO {

    private Connection connection;

    public QuestionDAO(Connection connection) {
        this.connection = connection;
    }

    // Adds a new question and returns the generated questionID, or -1 on failure.
    public int addQuestion(int topicID, String questionText, String choiceA, String choiceB,
                            String choiceC, String choiceD, String correctAnswer,
                            String difficulty) {

        String sql = "INSERT INTO Question "
                + "(topicID, questionText, choiceA, choiceB, choiceC, choiceD, correctAnswer, difficulty) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, topicID);
            statement.setString(2, questionText);
            statement.setString(3, choiceA);
            statement.setString(4, choiceB);
            statement.setString(5, choiceC);
            statement.setString(6, choiceD);
            statement.setString(7, correctAnswer);
            statement.setString(8, difficulty);

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

    public List<Question> getAllQuestions() {

        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM Question ORDER BY questionID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                questions.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public Question getQuestionById(int questionID) {

        String sql = "SELECT * FROM Question WHERE questionID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, questionID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapRow(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Returns the questions that belong to a quiz, in questionOrder.
    public List<Question> getQuestionsByQuiz(int quizID) {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT q.* FROM Question q "
                + "JOIN Quiz_Question qq ON q.questionID = qq.questionID "
                + "WHERE qq.quizID = ? "
                + "ORDER BY qq.questionOrder";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                questions.add(mapRow(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public boolean updateQuestion(int questionID, int topicID, String questionText,
                                   String choiceA, String choiceB, String choiceC,
                                   String choiceD, String correctAnswer, String difficulty) {

        String sql = "UPDATE Question SET topicID = ?, questionText = ?, choiceA = ?, "
                + "choiceB = ?, choiceC = ?, choiceD = ?, correctAnswer = ?, difficulty = ? "
                + "WHERE questionID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, topicID);
            statement.setString(2, questionText);
            statement.setString(3, choiceA);
            statement.setString(4, choiceB);
            statement.setString(5, choiceC);
            statement.setString(6, choiceD);
            statement.setString(7, correctAnswer);
            statement.setString(8, difficulty);
            statement.setInt(9, questionID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteQuestion(int questionID) {

        String sql = "DELETE FROM Question WHERE questionID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, questionID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("||  Cannot delete this question. It may already be");
            System.out.println("||  attached to a quiz or answered by a student.");
            return false;
        }
    }

    // Attaches a question to a quiz at the next available order position.
    public boolean attachQuestionToQuiz(int quizID, int questionID) {

        int nextOrder = getNextQuestionOrder(quizID);

        String sql = "INSERT INTO Quiz_Question (quizID, questionID, questionOrder) "
                + "VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, quizID);
            statement.setInt(2, questionID);
            statement.setInt(3, nextOrder);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("||  Could not attach question. It may already be in this quiz.");
            return false;
        }
    }

    public boolean removeQuestionFromQuiz(int quizID, int questionID) {

        String sql = "DELETE FROM Quiz_Question WHERE quizID = ? AND questionID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, quizID);
            statement.setInt(2, questionID);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private int getNextQuestionOrder(int quizID) {

        String sql = "SELECT COALESCE(MAX(questionOrder), 0) + 1 AS nextOrder "
                + "FROM Quiz_Question WHERE quizID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt("nextOrder");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    private Question mapRow(ResultSet result) throws Exception {

        return new Question(
                result.getInt("questionID"),
                result.getInt("topicID"),
                result.getString("questionText"),
                result.getString("choiceA"),
                result.getString("choiceB"),
                result.getString("choiceC"),
                result.getString("choiceD"),
                result.getString("correctAnswer"),
                result.getString("difficulty")
        );
    }
}
