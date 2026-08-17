package com.sbqms.teacher;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.sbqms.dao.QuestionDAO;
import com.sbqms.dao.QuizDAO;
import com.sbqms.dao.StudentDAO;
import com.sbqms.dao.StudentResultDAO;
import com.sbqms.dao.TopicDAO;
import com.sbqms.model.Question;
import com.sbqms.model.Quiz;
import com.sbqms.model.QuizStats;
import com.sbqms.model.ResultReportRow;
import com.sbqms.model.Student;
import com.sbqms.model.Teacher;
import com.sbqms.model.Topic;

public class TeacherDashboard {

    public static void showDashboard(
            Teacher teacher,
            Scanner scanner,
            Connection connection) {

        boolean running = true;

        while (running) {

            clearScreen();

            showHeader(teacher);

            System.out.println("||                                              ||");
            System.out.println("||  [1] Create Quiz                             ||");
            System.out.println("||  [2] Manage Quizzes                          ||");
            System.out.println("||  [3] Manage Questions                        ||");
            System.out.println("||  [4] Student Results                         ||");
            System.out.println("||  [5] Reports                                 ||");
            System.out.println("||  [6] Manage Students                         ||");
            System.out.println("||  [7] My Profile                              ||");
            System.out.println("||  [8] Logout                                  ||");
            System.out.println("||                                              ||");
            System.out.println("==================================================");

            System.out.print("||  Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    createQuiz(scanner, connection, teacher);
                    break;

                case "2":
                    manageQuizzes(scanner, connection, teacher);
                    break;

                case "3":
                    manageQuestions(scanner, connection);
                    break;

                case "4":
                    studentResults(scanner, connection, teacher);
                    break;

                case "5":
                    reports(scanner, connection, teacher);
                    break;

                case "6":
                    manageStudents(scanner, connection);
                    break;

                case "7":
                    myProfile(teacher, scanner);
                    break;

                case "8":
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

    private static void showHeader(Teacher teacher) {

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||              T E A C H E R                   ||");
        System.out.println("||                D A S H B O A R D             ||");
        System.out.println("||                                              ||");

        System.out.println(
                "||  Teacher: "
                        + teacher.getFirstName()
                        + " "
                        + teacher.getLastName()
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
    // [1] CREATE QUIZ
    // ============================================

    private static void createQuiz(Scanner scanner, Connection connection, Teacher teacher) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 C R E A T E                  ||");
        System.out.println("||                    Q U I Z                   ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("Quiz Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        int timeLimit = -1;
        while (timeLimit <= 0) {
            System.out.print("Time Limit (minutes): ");
            timeLimit = readInt(scanner);
            if (timeLimit <= 0) {
                System.out.println("Please enter a valid number of minutes.");
            }
        }

        QuizDAO quizDAO = new QuizDAO(connection);

        int quizID = quizDAO.createQuiz(
                teacher.getTeacherID(),
                title,
                description,
                timeLimit,
                "Draft"
        );

        System.out.println();

        if (quizID == -1) {

            System.out.println("||  Could not create the quiz. Please try again.");
            pause(scanner);
            return;
        }

        System.out.println("||  Quiz created successfully! (Quiz ID: " + quizID + ")");
        System.out.println("||  Status: Draft. Use Manage Quizzes to publish it");
        System.out.println("||  once you've added questions.");
        System.out.println();
        System.out.print("Add questions to this quiz now? (Y/N): ");

        String answer = scanner.nextLine().trim().toUpperCase();

        if (answer.equals("Y")) {
            attachQuestionsLoop(scanner, connection, quizID);
        }

        pause(scanner);
    }

    // ============================================
    // [2] MANAGE QUIZZES
    // ============================================

    private static void manageQuizzes(Scanner scanner, Connection connection, Teacher teacher) {

        QuizDAO quizDAO = new QuizDAO(connection);

        while (true) {

            clearScreen();

            System.out.println("==================================================");
            System.out.println("||                  S B Q M S                   ||");
            System.out.println("||                                              ||");
            System.out.println("||              M A N A G E                     ||");
            System.out.println("||                 Q U I Z Z E S                ||");
            System.out.println("||                                              ||");
            System.out.println("==================================================");
            System.out.println();

            List<Quiz> quizzes = quizDAO.getQuizzesByTeacher(teacher.getTeacherID());

            if (quizzes.isEmpty()) {

                System.out.println("You have not created any quizzes yet.");
                pause(scanner);
                return;
            }

            for (int i = 0; i < quizzes.size(); i++) {

                Quiz quiz = quizzes.get(i);

                System.out.println(
                        "[" + (i + 1) + "] " + quiz.getQuizTitle()
                                + "  |  Status: " + quiz.getStatus()
                                + "  |  Time Limit: " + quiz.getTimeLimit() + " min"
                );
            }

            System.out.println();
            System.out.println("[0] Back");
            System.out.print("Enter quiz number to manage: ");

            int selection = readInt(scanner);

            if (selection == 0) {
                return;
            }

            if (selection < 1 || selection > quizzes.size()) {
                System.out.println("Invalid selection.");
                pause(scanner);
                continue;
            }

            manageOneQuiz(scanner, connection, quizzes.get(selection - 1));
        }
    }

    private static void manageOneQuiz(Scanner scanner, Connection connection, Quiz quiz) {

        QuizDAO quizDAO = new QuizDAO(connection);

        boolean editing = true;

        while (editing) {

            // Refresh the quiz in case it was edited/published in a previous loop.
            Quiz current = quizDAO.getQuizById(quiz.getQuizID());

            if (current == null) {
                // Quiz was deleted.
                return;
            }

            clearScreen();

            System.out.println("==================================================");
            System.out.println("||  Quiz:        " + current.getQuizTitle());
            System.out.println("||  Description:  " + current.getDescription());
            System.out.println("||  Time Limit:   " + current.getTimeLimit() + " minutes");
            System.out.println("||  Status:       " + current.getStatus());
            System.out.println("==================================================");
            System.out.println();
            System.out.println("[1] Edit Title / Description / Time Limit");
            System.out.println("[2] Toggle Publish / Draft");
            System.out.println("[3] Delete Quiz");
            System.out.println("[4] Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1": {

                    System.out.print("New Title (leave blank to keep current): ");
                    String newTitle = readLineOrKeep(scanner, current.getQuizTitle());

                    System.out.print("New Description (leave blank to keep current): ");
                    String newDescription = readLineOrKeep(scanner, current.getDescription());

                    System.out.print("New Time Limit in minutes (leave blank to keep current): ");
                    String timeInput = scanner.nextLine().trim();
                    int newTimeLimit = current.getTimeLimit();

                    if (!timeInput.isEmpty()) {
                        try {
                            newTimeLimit = Integer.parseInt(timeInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Keeping previous time limit.");
                        }
                    }

                    boolean updated = quizDAO.updateQuiz(
                            current.getQuizID(), newTitle, newDescription, newTimeLimit
                    );

                    System.out.println(updated ? "Quiz updated." : "Could not update quiz.");
                    pause(scanner);
                    break;
                }

                case "2": {

                    String newStatus = current.getStatus().equalsIgnoreCase("Published")
                            ? "Draft" : "Published";

                    boolean updated = quizDAO.updateQuizStatus(current.getQuizID(), newStatus);

                    System.out.println(updated
                            ? "Quiz is now: " + newStatus
                            : "Could not update quiz status.");

                    pause(scanner);
                    break;
                }

                case "3": {

                    System.out.print("Are you sure you want to delete this quiz? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toUpperCase();

                    if (confirm.equals("Y")) {

                        boolean deleted = quizDAO.deleteQuiz(current.getQuizID());

                        if (deleted) {
                            System.out.println("Quiz deleted.");
                            pause(scanner);
                            editing = false;
                        } else {
                            pause(scanner);
                        }

                    } else {
                        System.out.println("Cancelled.");
                        pause(scanner);
                    }

                    break;
                }

                case "4":
                    editing = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    pause(scanner);
            }
        }
    }

    // ============================================
    // [3] MANAGE QUESTIONS
    // ============================================

    private static void manageQuestions(Scanner scanner, Connection connection) {

        boolean running = true;

        while (running) {

            clearScreen();

            System.out.println("==================================================");
            System.out.println("||                  S B Q M S                   ||");
            System.out.println("||                                              ||");
            System.out.println("||              M A N A G E                     ||");
            System.out.println("||               Q U E S T I O N S              ||");
            System.out.println("||                                              ||");
            System.out.println("==================================================");
            System.out.println();
            System.out.println("[1] Add Question");
            System.out.println("[2] View All Questions");
            System.out.println("[3] Edit Question");
            System.out.println("[4] Delete Question");
            System.out.println("[5] Attach Question to Quiz");
            System.out.println("[6] Remove Question from Quiz");
            System.out.println("[7] Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addQuestion(scanner, connection);
                    break;

                case "2":
                    viewAllQuestions(scanner, connection);
                    break;

                case "3":
                    editQuestion(scanner, connection);
                    break;

                case "4":
                    deleteQuestion(scanner, connection);
                    break;

                case "5":
                    attachQuestionToQuizMenu(scanner, connection);
                    break;

                case "6":
                    removeQuestionFromQuizMenu(scanner, connection);
                    break;

                case "7":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    pause(scanner);
            }
        }
    }

    private static void addQuestion(Scanner scanner, Connection connection) {

        TopicDAO topicDAO = new TopicDAO(connection);
        QuestionDAO questionDAO = new QuestionDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                A D D  Q U E S T I O N        ||");
        System.out.println("==================================================");
        System.out.println();

        List<Topic> topics = topicDAO.getAllTopics();

        if (topics.isEmpty()) {
            System.out.println("No topics exist yet. Please add a Topic to the");
            System.out.println("database before creating questions.");
            pause(scanner);
            return;
        }

        System.out.println("Topics:");
        for (int i = 0; i < topics.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + topics.get(i).getTopicName());
        }

        System.out.print("Select topic number: ");
        int topicChoice = readInt(scanner);

        if (topicChoice < 1 || topicChoice > topics.size()) {
            System.out.println("Invalid topic selection.");
            pause(scanner);
            return;
        }

        int topicID = topics.get(topicChoice - 1).getTopicID();

        System.out.print("Question Text: ");
        String questionText = scanner.nextLine().trim();

        System.out.print("Choice A: ");
        String choiceA = scanner.nextLine().trim();

        System.out.print("Choice B: ");
        String choiceB = scanner.nextLine().trim();

        System.out.print("Choice C: ");
        String choiceC = scanner.nextLine().trim();

        System.out.print("Choice D: ");
        String choiceD = scanner.nextLine().trim();

        String correctLetter = null;
        String correctAnswer = null;

        while (correctAnswer == null) {

            System.out.print("Correct Choice (A/B/C/D): ");
            correctLetter = scanner.nextLine().trim().toUpperCase();

            switch (correctLetter) {
                case "A": correctAnswer = choiceA; break;
                case "B": correctAnswer = choiceB; break;
                case "C": correctAnswer = choiceC; break;
                case "D": correctAnswer = choiceD; break;
                default:
                    System.out.println("Please enter A, B, C, or D.");
            }
        }

        String difficulty = null;

        while (difficulty == null) {

            System.out.print("Difficulty (Easy/Medium/Hard): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Easy")
                    || input.equalsIgnoreCase("Medium")
                    || input.equalsIgnoreCase("Hard")) {

                difficulty = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();

            } else {
                System.out.println("Please enter Easy, Medium, or Hard.");
            }
        }

        int questionID = questionDAO.addQuestion(
                topicID, questionText, choiceA, choiceB, choiceC, choiceD, correctAnswer, difficulty
        );

        System.out.println();
        System.out.println(questionID == -1
                ? "Could not add the question."
                : "Question added successfully! (Question ID: " + questionID + ")");

        pause(scanner);
    }

    private static void viewAllQuestions(Scanner scanner, Connection connection) {

        QuestionDAO questionDAO = new QuestionDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||             A L L  Q U E S T I O N S         ||");
        System.out.println("==================================================");
        System.out.println();

        List<Question> questions = questionDAO.getAllQuestions();

        if (questions.isEmpty()) {

            System.out.println("No questions exist yet.");

        } else {

            for (Question q : questions) {

                System.out.println("ID " + q.getQuestionID() + " [" + q.getDifficulty() + "]: "
                        + q.getQuestionText());
                System.out.println("   A. " + q.getChoiceA() + "   B. " + q.getChoiceB());
                System.out.println("   C. " + q.getChoiceC() + "   D. " + q.getChoiceD());
                System.out.println("   Correct: " + q.getCorrectAnswer());
                System.out.println();
            }
        }

        pause(scanner);
    }

    private static void editQuestion(Scanner scanner, Connection connection) {

        QuestionDAO questionDAO = new QuestionDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||               E D I T  Q U E S T I O N       ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("Enter the Question ID to edit: ");
        int questionID = readInt(scanner);

        Question question = questionDAO.getQuestionById(questionID);

        if (question == null) {
            System.out.println("Question not found.");
            pause(scanner);
            return;
        }

        System.out.println("Leave a field blank to keep its current value.");
        System.out.println();

        System.out.println("Current text: " + question.getQuestionText());
        System.out.print("New text: ");
        String questionText = readLineOrKeep(scanner, question.getQuestionText());

        System.out.println("Current Choice A: " + question.getChoiceA());
        System.out.print("New Choice A: ");
        String choiceA = readLineOrKeep(scanner, question.getChoiceA());

        System.out.println("Current Choice B: " + question.getChoiceB());
        System.out.print("New Choice B: ");
        String choiceB = readLineOrKeep(scanner, question.getChoiceB());

        System.out.println("Current Choice C: " + question.getChoiceC());
        System.out.print("New Choice C: ");
        String choiceC = readLineOrKeep(scanner, question.getChoiceC());

        System.out.println("Current Choice D: " + question.getChoiceD());
        System.out.print("New Choice D: ");
        String choiceD = readLineOrKeep(scanner, question.getChoiceD());

        System.out.println("Current correct answer: " + question.getCorrectAnswer());
        System.out.print("New correct choice (A/B/C/D, blank to keep current): ");
        String letter = scanner.nextLine().trim().toUpperCase();

        String correctAnswer = question.getCorrectAnswer();

        switch (letter) {
            case "A": correctAnswer = choiceA; break;
            case "B": correctAnswer = choiceB; break;
            case "C": correctAnswer = choiceC; break;
            case "D": correctAnswer = choiceD; break;
            case "": break;
            default:
                System.out.println("Not a valid choice letter. Keeping previous correct answer.");
        }

        System.out.println("Current difficulty: " + question.getDifficulty());
        System.out.print("New difficulty (Easy/Medium/Hard, blank to keep current): ");
        String difficulty = readLineOrKeep(scanner, question.getDifficulty());

        boolean updated = questionDAO.updateQuestion(
                question.getQuestionID(),
                question.getTopicID(),
                questionText,
                choiceA,
                choiceB,
                choiceC,
                choiceD,
                correctAnswer,
                difficulty
        );

        System.out.println();
        System.out.println(updated ? "Question updated." : "Could not update question.");

        pause(scanner);
    }

    private static void deleteQuestion(Scanner scanner, Connection connection) {

        QuestionDAO questionDAO = new QuestionDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||             D E L E T E  Q U E S T I O N     ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("Enter the Question ID to delete: ");
        int questionID = readInt(scanner);

        System.out.print("Are you sure you want to delete Question " + questionID + "? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {

            boolean deleted = questionDAO.deleteQuestion(questionID);
            System.out.println(deleted ? "Question deleted." : "Could not delete question.");

        } else {
            System.out.println("Cancelled.");
        }

        pause(scanner);
    }

    private static void attachQuestionToQuizMenu(Scanner scanner, Connection connection) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||        A T T A C H  T O  Q U I Z             ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("Enter the Quiz ID: ");
        int quizID = readInt(scanner);

        attachQuestionsLoop(scanner, connection, quizID);

        pause(scanner);
    }

    // Repeatedly attaches questions to a quiz until the teacher is done.
    // Used both right after Create Quiz and from the Manage Questions menu.
    private static void attachQuestionsLoop(Scanner scanner, Connection connection, int quizID) {

        QuestionDAO questionDAO = new QuestionDAO(connection);
        QuizDAO quizDAO = new QuizDAO(connection);

        Quiz quiz = quizDAO.getQuizById(quizID);

        if (quiz == null) {
            System.out.println("Quiz not found.");
            return;
        }

        boolean addingMore = true;

        while (addingMore) {

            List<Question> allQuestions = questionDAO.getAllQuestions();

            if (allQuestions.isEmpty()) {
                System.out.println("No questions exist yet. Add questions first.");
                return;
            }

            System.out.println();
            System.out.println("Available Questions:");

            for (Question q : allQuestions) {
                System.out.println("  ID " + q.getQuestionID() + ": " + q.getQuestionText());
            }

            System.out.print("Enter Question ID to attach (0 to stop): ");
            int questionID = readInt(scanner);

            if (questionID == 0) {
                addingMore = false;
                continue;
            }

            boolean attached = questionDAO.attachQuestionToQuiz(quizID, questionID);
            System.out.println(attached ? "Attached." : "Could not attach that question.");

            System.out.print("Attach another question? (Y/N): ");
            String more = scanner.nextLine().trim().toUpperCase();

            if (!more.equals("Y")) {
                addingMore = false;
            }
        }
    }

    private static void removeQuestionFromQuizMenu(Scanner scanner, Connection connection) {

        QuestionDAO questionDAO = new QuestionDAO(connection);
        QuizDAO quizDAO = new QuizDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||      R E M O V E  F R O M  Q U I Z           ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("Enter the Quiz ID: ");
        int quizID = readInt(scanner);

        Quiz quiz = quizDAO.getQuizById(quizID);

        if (quiz == null) {
            System.out.println("Quiz not found.");
            pause(scanner);
            return;
        }

        List<Question> questions = questionDAO.getQuestionsByQuiz(quizID);

        if (questions.isEmpty()) {
            System.out.println("This quiz has no questions attached.");
            pause(scanner);
            return;
        }

        System.out.println("Questions in \"" + quiz.getQuizTitle() + "\":");

        for (Question q : questions) {
            System.out.println("  ID " + q.getQuestionID() + ": " + q.getQuestionText());
        }

        System.out.print("Enter Question ID to remove: ");
        int questionID = readInt(scanner);

        boolean removed = questionDAO.removeQuestionFromQuiz(quizID, questionID);
        System.out.println(removed ? "Removed." : "Could not remove that question.");

        pause(scanner);
    }

    // ============================================
    // [4] STUDENT RESULTS
    // ============================================

    private static void studentResults(Scanner scanner, Connection connection, Teacher teacher) {

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||             S T U D E N T                    ||");
        System.out.println("||                R E S U L T S                ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println();

        StudentResultDAO studentResultDAO = new StudentResultDAO(connection);
        List<ResultReportRow> results = studentResultDAO.getResultsByTeacher(teacher.getTeacherID());

        if (results.isEmpty()) {

            System.out.println("No students have taken any of your quizzes yet.");

        } else {

            for (ResultReportRow row : results) {

                System.out.println(
                        row.getStudentName() + " (" + row.getStudentSection() + ")  |  "
                                + row.getQuizTitle() + "  |  "
                                + row.getScore() + "/" + row.getTotalItems()
                                + " (" + String.format("%.2f", row.getPercentage()) + "%)  |  "
                                + row.getStatus() + "  |  " + row.getDateTaken()
                );
            }
        }

        pause(scanner);
    }

    // ============================================
    // [5] REPORTS
    // ============================================

    private static void reports(Scanner scanner, Connection connection, Teacher teacher) {

        QuizDAO quizDAO = new QuizDAO(connection);
        StudentResultDAO studentResultDAO = new StudentResultDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                  S B Q M S                   ||");
        System.out.println("||                                              ||");
        System.out.println("||                 R E P O R T S                ||");
        System.out.println("||                                              ||");
        System.out.println("==================================================");
        System.out.println();

        List<Quiz> quizzes = quizDAO.getQuizzesByTeacher(teacher.getTeacherID());

        if (quizzes.isEmpty()) {
            System.out.println("You have not created any quizzes yet.");
            pause(scanner);
            return;
        }

        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + quizzes.get(i).getQuizTitle());
        }

        System.out.print("Select a quiz for its report (0 to cancel): ");
        int selection = readInt(scanner);

        if (selection < 1 || selection > quizzes.size()) {
            pause(scanner);
            return;
        }

        Quiz quiz = quizzes.get(selection - 1);

        QuizStats stats = studentResultDAO.getQuizStats(quiz.getQuizID(), quiz.getQuizTitle());

        System.out.println();
        System.out.println("Report: " + stats.getQuizTitle());
        System.out.println("  Total Attempts:     " + stats.getTotalAttempts());
        System.out.println("  Average Score:      " + String.format("%.2f", stats.getAverageScore()));
        System.out.println("  Average Percentage: " + String.format("%.2f", stats.getAveragePercentage()) + "%");
        System.out.println("  Highest Score:      " + stats.getHighestScore());
        System.out.println("  Lowest Score:       " + stats.getLowestScore());
        System.out.println("  Passed:             " + stats.getPassCount());
        System.out.println("  Failed:             " + stats.getFailCount());

        pause(scanner);
    }

    // ============================================
    // [6] MANAGE STUDENTS
    // ============================================

    private static void manageStudents(Scanner scanner, Connection connection) {

        boolean running = true;

        while (running) {

            clearScreen();

            System.out.println("==================================================");
            System.out.println("||                  S B Q M S                   ||");
            System.out.println("||                                              ||");
            System.out.println("||              M A N A G E                     ||");
            System.out.println("||                S T U D E N T S               ||");
            System.out.println("||                                              ||");
            System.out.println("==================================================");
            System.out.println();
            System.out.println("[1] Add Student");
            System.out.println("[2] View Students");
            System.out.println("[3] Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addStudent(scanner, connection);
                    break;

                case "2":
                    viewStudents(scanner, connection);
                    break;

                case "3":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    pause(scanner);
            }
        }
    }

    private static void addStudent(Scanner scanner, Connection connection) {

        StudentDAO studentDAO = new StudentDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||                A D D  S T U D E N T          ||");
        System.out.println("==================================================");
        System.out.println();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Section: ");
        String section = scanner.nextLine().trim();

        int studentID = studentDAO.createStudent(
                firstName, lastName, email, password, section
        );

        System.out.println();

        if (studentID == -2) {
            System.out.println("Could not add student: that email is already in use.");
        } else if (studentID == -1) {
            System.out.println("Could not add student. Please try again.");
        } else {
            System.out.println("Student added successfully! (Student ID: " + studentID + ")");
        }

        pause(scanner);
    }

    private static void viewStudents(Scanner scanner, Connection connection) {

        StudentDAO studentDAO = new StudentDAO(connection);

        clearScreen();

        System.out.println("==================================================");
        System.out.println("||               A L L  S T U D E N T S         ||");
        System.out.println("==================================================");
        System.out.println();

        List<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {

            System.out.println("No students found.");

        } else {

            for (Student student : students) {

                System.out.println(
                        "ID " + student.getStudentID() + ": "
                                + student.getFirstName() + " " + student.getLastName()
                                + "  |  Section: " + student.getStudSect()
                                + "  |  Email: " + student.getEmail()
                );
            }
        }

        pause(scanner);
    }

    private static void myProfile(
            Teacher teacher,
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
                        + teacher.getTeacherID()
        );

        System.out.println(
                "||  Name:     "
                        + teacher.getFirstName()
                        + " "
                        + teacher.getLastName()
        );

        System.out.println(
                "||  Email:    "
                        + teacher.getEmail()
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

    // Reads a line; if blank, returns the given current value instead.
    private static String readLineOrKeep(Scanner scanner, String currentValue) {

        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return currentValue;
        }

        return input;
    }

    private static void pause(Scanner scanner) {

        System.out.println();
        System.out.print("||  Press ENTER to return...");

        scanner.nextLine();
    }

    private static void clearScreen() {

        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }
}
