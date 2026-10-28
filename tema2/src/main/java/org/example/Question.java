package org.example;

import java.time.LocalDateTime;

public abstract class Question<T> implements Gradable {
    private String questionText;
    private String authorName;
    private LocalDateTime addedDate;
    private int difficulty;
    private double maxScore;

    public Question(String text, String name, LocalDateTime dateTime,
                    int difficulty, double maxScore) {
        this.questionText = text;
        this.authorName = name;
        this.addedDate = dateTime;
        this.difficulty = difficulty;
        this.maxScore = maxScore;
    }

    public String getQuestionText() { return questionText; };
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAuthorName() { return authorName; };
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public LocalDateTime getAddedDate() { return addedDate; };
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }

    public int getDifficulty() { return difficulty; };
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public double getMaxScore() { return maxScore; };
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public abstract Correctness checkAnswer(T answer);

    public abstract T getCorrectAnswer();
}
