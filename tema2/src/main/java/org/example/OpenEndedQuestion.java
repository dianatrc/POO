package org.example;

import java.time.LocalDateTime;

public class OpenEndedQuestion extends Question<String> {
    private final String correctAnswer;

    public OpenEndedQuestion(String text, String name, LocalDateTime dateTime,
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
            case PARTIALLY_CORRECT -> 0.7;
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

            // calcul marja 30% din lungimea raspunsului corect, rotunjit
            int margin = (int) Math.round(correctAnswerLength * 0.3);
            if (answerLength >= (correctAnswerLength - margin) &&
            answerLength <= (correctAnswerLength + margin))
                return Correctness.PARTIALLY_CORRECT;
        }
        return Correctness.INCORRECT;
    }

    @Override
    public String getCorrectAnswer() { return correctAnswer; }
}
