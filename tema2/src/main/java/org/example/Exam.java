package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Exam {
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private List<Question<?>> questions = new ArrayList<>();
    private Map<String, Double> scores = new TreeMap<>();

    public Exam(String name, LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime))
            throw new IllegalArgumentException("endTime before startTime");
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void addQuestion(Question<?> question) {
        // adauga o intrebare
        if (question != null) {
            questions.add(question);
        }
    }

    public void listQuestionsHistory() {
        if (questions == null || questions.isEmpty())
            return;

        List<Question<?>> sortedQuestions = new ArrayList<>(questions);

        // sortare specifica pentru istoric: cronologic, apoi alfabetic
        sortedQuestions.sort((q1, q2) -> {
                    int dateComparison = q1.getAddedDate().compareTo(q2.getAddedDate());
                    if (dateComparison == 0)
                        return q1.getAuthorName().compareTo(q2.getAuthorName());
                    return dateComparison;
                });
        for (Question<?> q: sortedQuestions) {
            String formattedDate = q.getAddedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm"));
            System.out.println(String.format("%s | %s | %s | %d | %.1f | %s", formattedDate, q.getQuestionText(),
                    q.getCorrectAnswer().toString(), q.getDifficulty(), q.getMaxScore(), q.getAuthorName()));
        }
    }

    public void printExam() {
        // afiseaza intrebarile examenului in ordine (dificulate/cerinta)
        if (questions == null || questions.isEmpty())
            return;

        List<Question<?>> sortedQuestions = getQuestionsForEvaluations();

        for (Question<?> q: sortedQuestions)
            System.out.println(String.format("%.1f | %s | %d | %s", q.getMaxScore(), q.getQuestionText(), q.getDifficulty(), q.getCorrectAnswer().toString()));
    }

    public List<Question<?>> getQuestionsForEvaluations() {
        List<Question<?>> sortedQuestions = new ArrayList<>(questions);

        sortedQuestions.sort((q1, q2) -> {
            int diffComparison = Integer.compare(q1.getDifficulty(), q2.getDifficulty());
            if (diffComparison == 0)
                return q1.getQuestionText().compareTo(q2.getQuestionText());
            return diffComparison;
        });

        return  Collections.unmodifiableList(sortedQuestions);
    }

    public void generateReport(String filename) {
        // creeaza un fisier .out cu notele tuturor studentilor care au sustinut examenul
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writeScores(writer);
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    private void writeScores(PrintWriter writer) {
        for (Map.Entry<String, Double> entry: scores.entrySet())
            writer.println(String.format("%s | %.2f", entry.getKey(), entry.getValue()));
    }

    public void recordStudent(String name, double score) { this.scores.put(name, score); }

    public double getTotalPossiblePoints() {
        // calculeaza suma punctajelor maxime a tuturor intrebarilor - nota maxima pe examen
        double totalPoints = 0.0;
        for (Question<?> q: questions)
            totalPoints += q.getMaxScore();
        return totalPoints;
    }

    public boolean isActive(LocalDateTime submission) {
        // verifica daca ora trimiterii este in intervalul examenului
        return !submission.isBefore(startTime) && !submission.isAfter(endTime);
    }

    public boolean hasStudentParticipated(String name) {
        return scores.containsKey(name);
    }

    public double calculateAverageScore() {
        // media scorurilor
        if (scores.isEmpty())
            return 0.0;

        double total = 0.0;
        for (Double score: scores.values())
            total += score;
        return total / scores.size();
    }

    public double getPassRate() {
        // rata de promovabilitate (procent)
        if (scores.isEmpty())
            return 0.0;

        double maxPossibleScore = getTotalPossiblePoints();
        if (maxPossibleScore == 0) return 0.0;

        int passed = 0;
        for (Double score: scores.values())
            if (score >= (maxPossibleScore/2.0))
                passed++;
        return (double) passed * 100 / scores.size();
    }

    public String getName() { return name; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() {return endTime; }
    public List<Question<?>> getQuestions() { return Collections.unmodifiableList(questions); }
    public Map<String, Double> getScores() { return Collections.unmodifiableMap(scores); }
}
