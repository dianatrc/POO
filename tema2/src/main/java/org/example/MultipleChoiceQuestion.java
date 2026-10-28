package org.example;

import java.time.LocalDateTime;

public class MultipleChoiceQuestion extends Question<ResponseOption> {
    private final ResponseOption correctAnswer;

    public MultipleChoiceQuestion(String text, String name, LocalDateTime dateTime,
                             int difficulty, double maxScore,ResponseOption correctAnswer) {
        super(text, name, dateTime, difficulty, maxScore);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public double getPercentage(Correctness correctness) {
        if (correctness == null)
            return 0.0;

        return switch (correctness) {
            case CORRECT -> 1.0;
            case PARTIALLY_CORRECT, INCORRECT -> 0.0;
        };
    }

    @Override
    public Correctness checkAnswer(ResponseOption answer) {
        if (answer == null)
            return Correctness.INCORRECT;
        if (answer == correctAnswer)
            return Correctness.CORRECT;
        return Correctness.INCORRECT;
    }

    @Override
    public ResponseOption getCorrectAnswer() { return correctAnswer; }
}
