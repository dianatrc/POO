package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Platform {
    private List<Exam> examList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();

    public void addStudent(String name, String group) {
        studentList.add(new Student(name, group));
    }

    public void addExam(String name, LocalDateTime start, LocalDateTime end) {
        examList.add(new Exam(name, start, end));
    }

    public void addQuestionToExam(String examName, Question<?> question) {
        Exam exam = findExamByName(examName);
        if (exam != null)
            exam.addQuestion(question);
    }

    private Exam findExamByName(String name) {
        // metoda privata pentru a gasi un examen in lista
        if (name == null)
            return null;
        for (Exam e: examList)  {
            if (e.getName().equalsIgnoreCase(name))  // ignora diferentele de litere mari/mici
                return e;
        }
        return null;
    }

    public void handleExam(String examName, String studentName, List<String> answers, LocalDateTime submissionTime)
        throws SubmissionOutsideTimeIntervalException {
        // gestioneaza procesul de trimitere a unui examen de catre un student
        Student student = findStudentByName(studentName);
        Exam exam = findExamByName(examName);

        if (student == null || exam == null)
            return;

        // previne transmiterea multipla
        if (exam.hasStudentParticipated(studentName))
            return;

        // verificare daca trimiterea se incadreaza in intervalul orar al examenului
        if (!exam.isActive(submissionTime)) {
            exam.recordStudent(studentName, 0.0);
            student.addGrade(examName, 0.0);

            throw  new SubmissionOutsideTimeIntervalException(submissionTime, studentName);
        }

        // calculeaza scorul total
        double total = 0;
        List<Question<?>> questions = exam.getQuestionsForEvaluations();
        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            total += calculateScore(questions.get(i), answers.get(i));
        }

        // salveaza rezultatul atat in evidenta examenului, cat si la student
        exam.recordStudent(studentName, total);
        student.addGrade(examName, total);
    }

    public void generateExamReport(String examName, String filename) {
        // processul de generare a fisierului raport pentru un examen
        Exam exam = findExamByName(examName);
        if (exam == null)
            return;
        exam.generateReport(filename);
    }

    private Student findStudentByName(String name) {
        // metoda privata pentru a cauta un student dupa nume
        if (name == null)
            return null;
        for (Student s: studentList)
            if (s.getName().equalsIgnoreCase(name))
                return s;
        return null;
    }

    private double calculateScore(Question<?> q, String input) {
        // calculul scorului pentru o singura intrebare
        Correctness result;
        if (q instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;

            try {
                // converteste string-ul in optiune de tip Enum (A, B, C, D)
                ResponseOption answer = ResponseOption.valueOf(input.toUpperCase());
                result = mcq.checkAnswer(answer);
            } catch (IllegalArgumentException e) {
                result = Correctness.INCORRECT;  // raspuns invalid
            }
        } else {
            // OpenEndedQuestion, FillInTheBlankQuestion
            Question<String> question = (Question<String>) q;
            result = question.checkAnswer(input);
        }

        // aplica procentul corespunzator la punctajul maxim al intrebarii
        double maxScore = q.getMaxScore();
        double percentage = q.getPercentage(result);

        return maxScore * percentage;
    }

    public void printExamDetails(String examName, boolean sortByHistory) {
        // afiseaza detaliile examenului
        Exam exam = findExamByName(examName);
        if (exam == null)
            return;
        if (sortByHistory)
            exam.listQuestionsHistory();
        else exam.printExam();
    }

    public void showScore(String studentName, String examName, String timestamp) {
        // afiseaza in consola nota obtinuta de un student la un anumit examen
        Exam exam = findExamByName(examName);
        if (exam != null && exam.hasStudentParticipated(studentName)) {
            Double score = exam.getScores().get(studentName);
            System.out.println(String.format("%s | The score of %s for exam %s is %.2f", timestamp,
                    studentName, examName, score));
        }
    }
}
