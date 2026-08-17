package com.sbqms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.sbqms.model.Teacher;

public class TeacherDAO {

    private Connection connection;

    // Constructor
    public TeacherDAO(Connection connection) {
        this.connection = connection;
    }

    // Login teacher using email and password
    public Teacher login(String email, String password) {

        String sql = "SELECT * FROM Teacher WHERE email = ? AND password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Teacher teacher = new Teacher(
                        result.getInt("teacherID"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password")
                );

                return teacher;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}