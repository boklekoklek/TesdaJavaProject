CREATE DATABASE quiz_management;

USE quiz_management;


-- 1. Teacher
CREATE TABLE Teacher (
    teacherID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
     email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


-- 2. Student
CREATE TABLE Student (
    studentID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
     email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    StudSect VARCHAR(50) NOT NULL
);


-- 3. Topic
CREATE TABLE Topic (
    topicID INT AUTO_INCREMENT PRIMARY KEY,
    topicName VARCHAR(50) NOT NULL,
    description VARCHAR(250) NOT NULL
);


-- 4. Question
CREATE TABLE Question (
    questionID INT AUTO_INCREMENT PRIMARY KEY,
    topicID INT NOT NULL,
    questionText VARCHAR(250) NOT NULL,
    choiceA VARCHAR(250) NOT NULL,
    choiceB VARCHAR(250) NOT NULL,
    choiceC VARCHAR(250) NOT NULL,
    choiceD VARCHAR(250) NOT NULL,
    correctAnswer VARCHAR(250) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,

    FOREIGN KEY (topicID) REFERENCES Topic(topicID)
);


-- 5. Quiz
CREATE TABLE Quiz (
    quizID INT AUTO_INCREMENT PRIMARY KEY,
    teacherID INT NOT NULL,
    quizTitle VARCHAR(250) NOT NULL,
    description VARCHAR(250) NOT NULL,
    timeLimit INT NOT NULL,
    dateCreated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (teacherID) REFERENCES Teacher(teacherID)
);


-- 6. Quiz_Question
CREATE TABLE Quiz_Question (
    quizID INT NOT NULL,
    questionID INT NOT NULL,
    questionOrder INT NOT NULL,

    PRIMARY KEY (quizID, questionID),

    FOREIGN KEY (quizID) REFERENCES Quiz(quizID),
    FOREIGN KEY (questionID) REFERENCES Question(questionID)
);


-- 7. Attempt
CREATE TABLE Attempt (
    attemptID INT AUTO_INCREMENT PRIMARY KEY,
    studentID INT NOT NULL,
    quizID INT NOT NULL,
    startTime DATETIME NOT NULL,
    endTime DATETIME,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (studentID) REFERENCES Student(studentID),
    FOREIGN KEY (quizID) REFERENCES Quiz(quizID)
);


-- 8. Attempt_Answer
CREATE TABLE Attempt_Answer (
    attemptAnswerID INT AUTO_INCREMENT PRIMARY KEY,
    attemptID INT NOT NULL,
    questionID INT NOT NULL,
    selectedAnswer VARCHAR(250) NOT NULL,
    isCorrect BOOLEAN NOT NULL,

    FOREIGN KEY (attemptID) REFERENCES Attempt(attemptID),
    FOREIGN KEY (questionID) REFERENCES Question(questionID)
);


-- 9. Student_Result
CREATE TABLE Student_Result (
    resultID INT AUTO_INCREMENT PRIMARY KEY,
    attemptID INT NOT NULL,
    studentID INT NOT NULL,
    quizID INT NOT NULL,
    score INT NOT NULL,
    totalItems INT NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (attemptID) REFERENCES Attempt(attemptID),
    FOREIGN KEY (studentID) REFERENCES Student(studentID),
    FOREIGN KEY (quizID) REFERENCES Quiz(quizID)
);