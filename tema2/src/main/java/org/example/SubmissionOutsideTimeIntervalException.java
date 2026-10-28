package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmissionOutsideTimeIntervalException extends Exception {
    private  final LocalDateTime timestamp;
    private final String studentName;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    public SubmissionOutsideTimeIntervalException(LocalDateTime timestamp, String studentName) {
        this.timestamp = timestamp;
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return timestamp.format(FORMATTER) + " | Submission outside of time interval for student " + studentName;
    }
}
