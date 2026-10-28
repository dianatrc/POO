package org.example;

import java.time.LocalDateTime;

public class FillInTheBlankQuestion extends Question<String> {
    private final String correctAnswer;

    public FillInTheBlankQuestion(String text, String name, LocalDateTime dateTime,
                             int difficulty, double maxScore,String correctAnswer) {
        super(text, name, dateTime, difficulty, maxScore);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public double getPercentage(Correctness correctness) {
        if (correctness == null)
            return 0.0;

        return switch (correctness) {
            case CORRECT -> 1.0;
            case PARTIALLY_CORRECT -> 0.5;
            case INCORRECT -> 0.0;
        };
    }

    @Override
    public Correctness checkAnswer(String answer) {
        if (answer.equals(correctAnswer))
            return Correctness.CORRECT;
        if (answer.contains(correctAnswer) || correctAnswer.contains(answer)) {
            int answerLength = answer.length();
            int correctAnswerLength = correctAnswer.length();
            if (answerLength >= (correctAnswerLength - 2) && answerLength <= (correctAnswerLength + 2))
                return Correctness.PARTIALLY_CORRECT;  // lungimea sa fie +/- 2 caractere fata de raspuns corect
        }
        return Correctness.INCORRECT;
    }

    @Override
    public String getCorrectAnswer() { return correctAnswer; }
}
