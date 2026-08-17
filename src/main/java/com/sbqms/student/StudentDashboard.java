package com.sbqms.student;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.sbqms.dao.AttemptAnswerDAO;
import com.sbqms.dao.AttemptDAO;
import com.sbqms.dao.QuestionDAO;
import com.sbqms.dao.QuizDAO;
import com.sbqms.dao.StudentResultDAO;
import com.sbqms.model.Question;
import com.sbqms.model.Quiz;
import com.sbqms.model.ResultReportRow;
import com.sbqms.model.Student;

public class StudentDashboard {

    private static final double PASSING_PERCENTAGE = 60.0;

    public static void showDashboard(
            Student student,
            Scanner scanner,
            Connection connection) {

        boolean running = true;

        while (running) {

            clearScreen();

            showHeader(student);

            System.out.println("||                                              ||");
            System.out.println("||  [1] Available Quizzes                       ||");
            System.out.println("||  [2] Take Quiz                               ||");
            System.out.println("||  [3] My Results                              ||");
            System.out.println("||  [4] My Profile                              ||");
            System.out.println("||  [5] Logout                                  ||");
            System.out.println("||                                              ||");
            System.out.println("==================================================");

            System.out.print("||  Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    availableQuizzes(scanner, connection);
                    break;

                case "2":
                    takeQuiz(scanner, connection, student);
                    break;

                case "3":
                    myResults(scanner, connection, student);
                    break;

                case "4":
                    myProfile(student, scanner);
                    break;

                case "5":
                    running = false;
                    logout(scanner);
                    break;

                default:
                    System.out.println();
                    System.out.println("||  Invalid choice.");
                    pause(scanner);
            }
        }
    }

    private static void showHeader(Student student) {

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||             S T U D E N T                    ||");
        System.out.println("||                D A S H B O A R D             ||");
        System.out.println("||                                              ||");
        System.out.println(
                "||  Student: "
                        + student.getFirstName()
                        + " "
                        + student.getLastName()
        );

        System.out.println(
                "||  Section: "
                        + student.getStudSect()
        );

        System.out.println(
                "||  Date:    "
                        + getCurrentDate()
        );

        System.out.println(
                "||  Time:    "
                        + getCurrentTime()
        );

        System.out.println("||                                              ||");
        System.out.println("==================================================");
    }

    // ============================================
    // [1] AVAILABLE QUIZZES
    // ============================================

    private static void availableQuizzes(Scanner scanner, Connection connection) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||             AVAILABLE QUIZZES                ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println("||                                              ||");

        QuizDAO quizDAO = new QuizDAO(connection);
        List<Quiz> quizzes = quizDAO.getAvailableQuizzes();

        if (quizzes.isEmpty()) {

            System.out.println("||  No quizzes are available right now.         ||");

        } else {

            for (int i = 0; i < quizzes.size(); i++) {

                Quiz quiz = quizzes.get(i);

                System.out.println(
                        "||  [" + (i + 1) + "] " + quiz.getQuizTitle()
                );

                System.out.println(
                        "||      Time Limit: " + quiz.getTimeLimit() + " minutes"
                );

                System.out.println(
                        "||      Description: " + quiz.getDescription()
                );

                System.out.println("||                                              ||");
            }
        }

        System.out.println("==================================================");

        pause(scanner);
    }

    // ============================================
    // [2] TAKE QUIZ
    // ============================================

    private static void takeQuiz(Scanner scanner, Connection connection, Student student) {

        QuizDAO quizDAO = new QuizDAO(connection);
        QuestionDAO questionDAO = new QuestionDAO(connection);
        AttemptDAO attemptDAO = new AttemptDAO(connection);
        AttemptAnswerDAO attemptAnswerDAO = new AttemptAnswerDAO(connection);
        StudentResultDAO studentResultDAO = new StudentResultDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 T A K E  Q U I Z             ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println("||                                              ||");

        List<Quiz> quizzes = quizDAO.getAvailableQuizzes();

        if (quizzes.isEmpty()) {

            System.out.println("||  No quizzes are available right now.         ||");
            System.out.println("==================================================");
            pause(scanner);
            return;
        }

        for (int i = 0; i < quizzes.size(); i++) {

            Quiz quiz = quizzes.get(i);

            System.out.println(
                    "||  [" + (i + 1) + "] " + quiz.getQuizTitle()
                            + " (" + quiz.getTimeLimit() + " min)"
            );
        }

        System.out.println("||                                              ||");
        System.out.println("||  [0] Cancel                                  ||");
        System.out.println("==================================================");
        System.out.print("||  Enter quiz number: ");

        int selection = readInt(scanner);

        if (selection <= 0 || selection > quizzes.size()) {
            System.out.println("||  Cancelled.");
            pause(scanner);
            return;
        }

        Quiz selectedQuiz = quizzes.get(selection - 1);

        List<Question> questions = questionDAO.getQuestionsByQuiz(selectedQuiz.getQuizID());

        if (questions.isEmpty()) {

            System.out.println();
            System.out.println("||  This quiz has no questions yet. Please try again later.");
            pause(scanner);
            return;
        }

        System.out.println();
        System.out.println("||  Starting: " + selectedQuiz.getQuizTitle());
        System.out.println("||  Time Limit: " + selectedQuiz.getTimeLimit() + " minutes");
        System.out.println("||  Total Questions: " + questions.size());
        System.out.println();
        System.out.print("Press ENTER to begin...");
        scanner.nextLine();

        int attemptID = attemptDAO.startAttempt(student.getStudentID(), selectedQuiz.getQuizID());

        if (attemptID == -1) {
            System.out.println("||  Could not start the quiz attempt. Please try again.");
            pause(scanner);
            return;
        }

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);

            clearScreen();

            System.out.println("==================================================");
            System.out.println("||                  S B Q M S                   ||");
            System.out.println("||                                              ||");
            System.out.println("||  " + selectedQuiz.getQuizTitle());
            System.out.println("||  Question " + (i + 1) + " of " + questions.size());
            System.out.println("||                                              ||");
            System.out.println("==================================================");
            System.out.println();
            System.out.println(question.getQuestionText());
            System.out.println();
            System.out.println("  A. " + question.getChoiceA());
            System.out.println("  B. " + question.getChoiceB());
            System.out.println("  C. " + question.getChoiceC());
            System.out.println("  D. " + question.getChoiceD());
            System.out.println();

            String letter = null;
            String selectedText = null;

            while (selectedText == null) {

                System.out.print("Your answer (A/B/C/D): ");
                letter = scanner.nextLine().trim().toUpperCase();

                selectedText = question.getChoiceByLetter(letter);

                if (selectedText == null) {
                    System.out.println("Invalid choice. Please enter A, B, C, or D.");
                }
            }

            boolean isCorrect = selectedText.equalsIgnoreCase(question.getCorrectAnswer());

            attemptAnswerDAO.saveAnswer(
                    attemptID,
                    question.getQuestionID(),
                    selectedText,
                    isCorrect
            );

            if (isCorrect) {
                score++;
            }
        }

        attemptDAO.completeAttempt(attemptID);

        double percentage = (score * 100.0) / questions.size();
        String status = percentage >= PASSING_PERCENTAGE ? "Passed" : "Failed";

        studentResultDAO.saveResult(
                attemptID,
                student.getStudentID(),
                selectedQuiz.getQuizID(),
                score,
                questions.size(),
                percentage,
                status
        );

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                Q U I Z  R E S U L T         ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println("||                                              ||");
        System.out.println("||  Quiz:       " + selectedQuiz.getQuizTitle());
        System.out.println("||  Score:      " + score + " / " + questions.size());
        System.out.println("||  Percentage: " + String.format("%.2f", percentage) + "%");
        System.out.println("||  Status:     " + status);
        System.out.println("||                                              ||");
        System.out.println("==================================================");

        pause(scanner);
    }

    // ============================================
    // [3] MY RESULTS
    // ============================================

    private static void myResults(Scanner scanner, Connection connection, Student student) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 M Y  R E S U L T S            ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println("||                                              ||");

        StudentResultDAO studentResultDAO = new StudentResultDAO(connection);
        List<ResultReportRow> results = studentResultDAO.getResultsByStudent(student.getStudentID());

        if (results.isEmpty()) {

            System.out.println("||  You have not taken any quizzes yet.         ||");

        } else {

            for (ResultReportRow row : results) {

                System.out.println("||  Quiz:       " + row.getQuizTitle());
                System.out.println("||  Score:      " + row.getScore() + " / " + row.getTotalItems());
                System.out.println("||  Percentage: " + String.format("%.2f", row.getPercentage()) + "%");
                System.out.println("||  Status:     " + row.getStatus());
                System.out.println("||  Date:       " + row.getDateTaken());
                System.out.println("||                                              ||");
            }
        }

        System.out.println("==================================================");

        pause(scanner);
    }

    private static void myProfile(
            Student student,
            Scanner scanner) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 M Y  P R O F I L E           ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println("||                                              ||");

        System.out.println(
                "||  ID:       "
                        + student.getStudentID()
        );

        System.out.println(
                "||  Name:     "
                        + student.getFirstName()
                        + " "
                        + student.getLastName()
        );

        System.out.println(
                "||  Email:    "
                        + student.getEmail()
        );

        System.out.println(
                "||  Section:  "
                        + student.getStudSect()
        );

        System.out.println("||                                              ||");
        System.out.println("==================================================");

        pause(scanner);
    }

    private static void logout(Scanner scanner) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||             LOGGING OUT...                   ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");

        System.out.println();
        System.out.println("Goodbye!");

        // Do not start another Login here yet.
        // The application will end.
    }

    private static String getCurrentDate() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "MMMM dd, yyyy"
                );

        return LocalDateTime.now().format(formatter);
    }

    private static String getCurrentTime() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "hh:mm:ss a"
                );

        return LocalDateTime.now().format(formatter);
    }

    private static int readInt(Scanner scanner) {

        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void pause(Scanner scanner) {

        System.out.println();
        System.out.print("||  Press ENTER to return...");

        scanner.nextLine();
    }

    private static void clearScreen() {

        // Simple console spacing.
        // Works reliably in Terminal/VS Code.
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }
}
