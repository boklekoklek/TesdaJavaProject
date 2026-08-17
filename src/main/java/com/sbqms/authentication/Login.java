package com.sbqms.authentication;

import java.io.Console;
import java.sql.Connection;
import java.util.Scanner;

import com.sbqms.dao.StudentDAO;
import com.sbqms.dao.TeacherDAO;
import com.sbqms.database.DatabaseConnection;
import com.sbqms.model.Student;
import com.sbqms.model.Teacher;
import com.sbqms.student.StudentDashboard;
import com.sbqms.teacher.TeacherDashboard;

public class Login {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        showLoginScreen();

        System.out.print("||  Email:    ");
        String email = scanner.nextLine();

        String password;

        Console console = System.console();

        if (console != null) {

            password = new String(
                    console.readPassword("||  Password: ")
            );

        } else {

            // Fallback if System.console() is unavailable
            System.out.print("||  Password: ");
            password = scanner.nextLine();
        }

        System.out.println("||");
        System.out.println("||  Checking login...");
        System.out.println("||");

        Connection connection =
                DatabaseConnection.getConnection();

        if (connection == null) {

            System.out.println("||  Database connection failed.");
            System.out.println("||");
            System.out.println("==================================================");

            scanner.close();
            return;
        }


        // CHECK TEACHER


        TeacherDAO teacherDAO =
                new TeacherDAO(connection);

        Teacher teacher =
                teacherDAO.login(email, password);

        if (teacher != null) {

            System.out.println("||  Teacher login successful!");
            System.out.println("||");
            System.out.println(
                    "||  Welcome, "
                            + teacher.getFirstName()
                            + " "
                            + teacher.getLastName()
                            + "!"
            );

            System.out.println();
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();

            TeacherDashboard.showDashboard(
                    teacher,
                    scanner,
                    connection
            );

            scanner.close();
            return;
        }

        // CHECK STUDENT


        StudentDAO studentDAO =
                new StudentDAO(connection);

        Student student =
                studentDAO.login(email, password);

        if (student != null) {

            System.out.println("||  Student login successful!");
            System.out.println("||");
            System.out.println(
                    "||  Welcome, "
                            + student.getFirstName()
                            + " "
                            + student.getLastName()
                            + "!"
            );

            System.out.println();
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();

            StudentDashboard.showDashboard(
                    student,
                    scanner,
                    connection
            );

            scanner.close();
            return;
        }


        // INVALID LOGIN


        System.out.println("||  Invalid email or password.");
        System.out.println("||");
        System.out.println("==================================================");

        scanner.close();
    }

    private static void showLoginScreen() {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 L O G I N                    ||");
        System.out.println("||                                              ||");
        System.out.println("||  Please enter your account information.     ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println();
    }
}