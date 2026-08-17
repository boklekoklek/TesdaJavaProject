package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.sbqms.model.Student;

public class StudentDAO {

    private Connection connection;

    // Constructor
    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    // Login teacher using email and password
    public Student login(String email, String password) {

        String sql = "SELECT * FROM Student WHERE email = ? AND password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Student student = new Student(
                        result.getInt("studentID"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("StudSect")
                );

                return student;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Creates a new student and returns the generated studentID, or -1 on failure.
    // Returns -2 specifically when the email is already taken (StudSect column
    // has a UNIQUE constraint on email in the schema).
    public int createStudent(String firstName, String lastName, String email,
                              String password, String studSect) {

        String sql = "INSERT INTO Student (firstName, lastName, email, password, StudSect) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    sql, java.sql.Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, studSect);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // Email already exists (UNIQUE constraint).
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public java.util.List<Student> getAllStudents() {

        java.util.List<Student> students = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Student ORDER BY studentID";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {

                students.add(new Student(
                        result.getInt("studentID"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("StudSect")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}