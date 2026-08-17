USE quiz_management;


-- ============================================
-- 1. SAMPLE TEACHER
-- ============================================

INSERT INTO Teacher
(firstName, lastName, email, password)
VALUES
('Juan', 'Teacher', 'juan.teacher@example.com', 'test123');


-- ============================================
-- 2. SAMPLE STUDENTS
-- ============================================

INSERT INTO Student
(firstName, lastName, email, password, StudSect)
VALUES
('Maria', 'Santos', 'maria.santos@example.com', 'test123', 'ICT-1A'),
('Pedro', 'Cruz', 'pedro.cruz@example.com', 'test123', 'ICT-1A'),
('Ana', 'Reyes', 'ana.reyes@example.com', 'test123', 'ICT-1A');


-- ============================================
-- 3. SAMPLE TOPICS
-- ============================================

INSERT INTO Topic
(topicName, description)
VALUES
('Java Basics', 'Basic concepts of Java programming'),
('Variables and Data Types', 'Understanding variables and Java data types'),
('Control Structures', 'If statements, loops, and decision making');


-- ============================================
-- 4. SAMPLE QUESTIONS
-- ============================================

INSERT INTO Question
(topicID, questionText, choiceA, choiceB, choiceC, choiceD, correctAnswer, difficulty)
VALUES
(1,
 'Which keyword is used to create a class in Java?',
 'class',
 'function',
 'define',
 'struct',
 'class',
 'Easy'),

(1,
 'Which method is the entry point of a Java program?',
 'start()',
 'main()',
 'run()',
 'execute()',
 'main()',
 'Easy'),

(2,
 'Which data type is used to store a whole number?',
 'String',
 'double',
 'int',
 'boolean',
 'int',
 'Easy'),

(2,
 'Which data type is used to store true or false?',
 'int',
 'String',
 'boolean',
 'char',
 'boolean',
 'Easy'),

(3,
 'Which statement is used to make a decision in Java?',
 'if',
 'loop',
 'class',
 'import',
 'if',
 'Easy'),

(3,
 'Which loop is commonly used when the number of repetitions is known?',
 'if',
 'for',
 'switch',
 'try',
 'for',
 'Medium');


-- ============================================
-- 5. SAMPLE QUIZ
-- ============================================

INSERT INTO Quiz
(teacherID, quizTitle, description, timeLimit, status)
VALUES
(1,
 'Java Basics Quiz',
 'Basic Java programming assessment',
 30,
 'Published');


-- ============================================
-- 6. ADD QUESTIONS TO THE QUIZ
-- ============================================

INSERT INTO Quiz_Question
(quizID, questionID, questionOrder)
VALUES
(1, 1, 1),
(1, 2, 2),
(1, 3, 3),
(1, 4, 4),
(1, 5, 5),
(1, 6, 6);


-- ============================================
-- 7. SAMPLE STUDENT ATTEMPT
-- ============================================

INSERT INTO Attempt
(studentID, quizID, startTime, endTime, status)
VALUES
(1,
 1,
 '2026-08-08 09:00:00',
 '2026-08-08 09:15:00',
 'Completed');


-- ============================================
-- 8. SAMPLE STUDENT ANSWERS
-- ============================================

INSERT INTO Attempt_Answer
(attemptID, questionID, selectedAnswer, isCorrect)
VALUES
(1, 1, 'class', TRUE),
(1, 2, 'main()', TRUE),
(1, 3, 'int', TRUE),
(1, 4, 'int', FALSE),
(1, 5, 'if', TRUE),
(1, 6, 'for', TRUE);


-- ============================================
-- 9. SAMPLE STUDENT RESULT
-- ============================================

INSERT INTO Student_Result
(attemptID, studentID, quizID, score, totalItems, percentage, status)
VALUES
(1,
 1,
 1,
 5,
 6,
 83.33,
 'Passed');