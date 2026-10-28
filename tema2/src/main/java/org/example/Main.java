package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String RESOURCE_PATH = "src/main/resources/";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    public static void main(String[] args) {
        if (args.length == 0)
            return;

        // calea pentru fisierele de test
        String testName = args[0];
        String basePath = RESOURCE_PATH + testName + "/";
        String inputPath = basePath + "input.in";

        Platform platform = new Platform();
        PrintStream originalOut = System.out;

        // deschidere fisier de intrare si creare fisier consola
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             PrintStream consoleOut = new PrintStream(new FileOutputStream(basePath + "console.out"))) {

            System.setOut(consoleOut);  // redirectionarea output-ului standard
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("exit")) break;

                List<String> parts = Arrays.asList(line.split(" \\| "));  // split folosind operatorul specificat in cerinta
                if (parts.size() < 2) continue;

                String timestampStr = parts.get(0);
                String command = parts.get(1);
                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, FORMATTER);

                try {
                    switch (command) {
                        case "create_exam" ->
                            platform.addExam(parts.get(2).trim(),
                                    LocalDateTime.parse(parts.get(3), FORMATTER),
                                    LocalDateTime.parse(parts.get(4), FORMATTER));

                        case "add_question" ->
                            handleAddQuestion(parts, timestamp, platform);

                        case "print_exam" -> {
                            // creare fisier specific pentru vizualizarea print_exam
                            String filename = "print_exam_" + parts.get(2).trim() + "_" + timestampStr + ".out";
                            try (PrintStream fileOut = new PrintStream(new FileOutputStream(basePath + filename))) {
                                System.setOut(fileOut);
                                platform.printExamDetails(parts.get(2).trim(), false);
                                System.setOut(consoleOut);  // revine la log-ul de consola
                            }
                        }

                        case "list_questions_history" -> {
                            // creare fisier specific pentru istoricul cronologic
                            String hFileName = "questions_history_" + parts.get(2).trim() + "_" + timestampStr + ".out";
                            try (PrintStream fileOut = new PrintStream(new FileOutputStream(basePath + hFileName))) {
                                System.setOut(fileOut);
                                platform.printExamDetails(parts.get(2).trim(), true);
                                System.setOut(consoleOut);
                            }
                        }

                        case "submit_exam" -> {
                            String examName = parts.get(2).trim();
                            String studentName = parts.get(3).trim();
                            // extragerea tuturor raspunsurilor din restul liniei
                            List<String> answers = parts.subList(4, parts.size());
                            try {
                                platform.handleExam(examName, studentName, answers, timestamp);
                            } catch (SubmissionOutsideTimeIntervalException e) {
                                System.out.println(e.toString());
                            }
                        }

                        case "register_student" ->
                            platform.addStudent(parts.get(2).trim(), parts.get(3).trim());

                        case "show_student_score" ->
                            platform.showScore(parts.get(2).trim(), parts.get(3).trim(), timestampStr);

                        case "generate_report" -> {
                            String examName = parts.get(2).trim();
                            // crearea fisierului specific pentru generarea raportului
                            String filename = "exam_report_" + examName + "_" + timestampStr + ".out";
                            platform.generateExamReport(examName, basePath + filename);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            originalOut.println("Error reading input file: " + e.getMessage());
        } finally {
            System.setOut(originalOut); // restaurarea iesirii standard la final
        }
    }

    private static void handleAddQuestion(List<String> parts, LocalDateTime ts, Platform platform) {
        String type = parts.get(2).toUpperCase();
        String examName = parts.get(3).trim();
        String authorName = parts.get(4).trim();
        int diff = Integer.parseInt(parts.get(5));
        double score = Double.parseDouble(parts.get(6));
        String text = parts.get(7);
        String correct = parts.get(8).trim();

        Question<?> q = null;
        // instantierea tipului corect de intrebare
        if (type.equals("MULTIPLE_CHOICE")) {
            q = new MultipleChoiceQuestion(text, authorName, ts, diff, score, ResponseOption.valueOf(correct));
        } else if (type.equals("OPEN_ENDED")) {
            q = new OpenEndedQuestion(text, authorName, ts, diff, score, correct);
        } else if (type.equals("FILL_IN_THE_BLANK")) {
            q = new FillInTheBlankQuestion(text, authorName, ts, diff, score, correct);
        }

        if (q != null)
            platform.addQuestionToExam(examName, q);
    }
}