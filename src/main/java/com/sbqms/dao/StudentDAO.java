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
}