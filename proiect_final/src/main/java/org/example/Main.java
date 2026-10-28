package org.example;

import java.io.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("eroare primire argumente");
            return;
        }
        String pathType = args[0];

        // Chain of Responsibility
        BaseValidator ipVal = new IpValidator();
        BaseValidator locationVal = new LocationValidator();
        BaseValidator userVal = new UserValidator();

        ipVal.setNext(locationVal);
        locationVal.setNext(userVal);

        List<String> filesToProcess = new ArrayList<>();
        if (args.length == 2) {
            filesToProcess.add(args[1]);
        } else if (args.length == 4) {
            filesToProcess.add(args[1]); // servere
            filesToProcess.add(args[2]); // grupuri
            filesToProcess.add(args[3]); // alerte
        }

        PrintStream console = System.out;  // referinta catre consola
        CommandHandler handler = new CommandHandler(pathType, ipVal);

        for (String inputPath: filesToProcess) {
            inputPath = inputPath + ".in";
            String outputPath = inputPath.replace(".in", ".out");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
                 PrintStream fileOut = new PrintStream(new FileOutputStream(outputPath))) {
                System.setOut(fileOut);

                String line;
                int indexLine = 1;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("command")) continue;

                    List<String> data = Arrays.stream(line.split("\\|", -1))
                            .map(String::trim)
                            .collect(Collectors.toList());
                    String command = data.get(0);
                    try {
                        switch (command) {
                            case "ADD SERVER" ->
                                handler.addServer(data, indexLine);
                            case "ADD GROUP" ->
                                handler.addGroup(data, indexLine);
                            case "FIND GROUP" ->
                                handler.findGroup(data, indexLine);
                            case "REMOVE GROUP" ->
                                handler.removeGroup(data, indexLine);
                            case "ADD MEMBER" ->
                                handler.addMember(data, indexLine);
                            case "FIND MEMBER" ->
                                handler.findMember(data, indexLine);
                            case "REMOVE MEMBER" ->
                                handler.removeMember(data, indexLine);
                            case "ADD EVENT" ->
                                handler.addEvent(data);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    indexLine++;
                }
            } catch (IOException e) {
                System.err.println("IOException error");
            } finally {
                System.setOut(console);
            }
        }
    }
}