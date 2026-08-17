package com.sbqms;

import java.sql.Connection;

import com.sbqms.dao.StudentDAO;
import com.sbqms.database.DatabaseConnection;
import com.sbqms.model.Student;

public class Main {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return;
        }

        StudentDAO studentDAO = new StudentDAO(connection);

        Student student = studentDAO.login(
                "maria.santos@example.com",
                "test123"
        );

        if (student != null) {

            System.out.println("Student login successful!");
            System.out.println("ID: " + student.getStudentID());

            System.out.println("Name: "
                    + student.getFirstName() + " "
                    + student.getLastName());

            System.out.println("Email: " + student.getEmail());
            System.out.println("Section: " + student.getStudSect());

        } else {

            System.out.println("Student login failed!");
        }
    }
}