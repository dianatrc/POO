package Tema1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.*;

public class App {
    private Scanner scanner;

    public App(InputStream input) {
        this.scanner = new Scanner(input);
    }

    public void run() {
        // Implementați aici cerințele din enunț
        // Pentru citirea datelor de la tastatura se folosește câmpul scanner.
        GridController grid = new GridController();
        List<String> pendingMessages = new ArrayList<>();

        while(scanner.hasNextLine()) {
            String linie = scanner.nextLine();
            String cuvinte[] = linie.split(" ");

            // verificare globala BLACKOUT
            if (grid.getEsteInBlackout()) {
                // daca e BLACKOUT, comenzile 0-4 sunt blocate
                if (cuvinte[0].equals("0") || cuvinte[0].equals("1") || cuvinte[0].equals("2") ||
                        cuvinte[0].equals("3") || cuvinte[0].equals("4")) {
                    System.out.println("EROARE: Reteaua este in BLACKOUT. Simulare oprita.");
                    // sarim logica switch-ului si citim urmatoarea linie
                    continue;
                }
            }
            switch(cuvinte[0]) {
                case "0":  // adauga producator
                    // verificare componenta cu acelasi id
                    ComponentaRetea existaProducator = grid.cautareComponenta(cuvinte[2]);
                    if (existaProducator != null) {
                        pendingMessages.add(String.format("EROARE: Exista deja o componenta cu id-ul %s%n", cuvinte[2]));
                    } else if (cuvinte.length < 4) {
                        pendingMessages.add("EROARE: Format comanda invalida");  // putere lipsa
                    } else if (Double.parseDouble(cuvinte[3]) <= 0) {
                        pendingMessages.add("EROARE: Putere invalida");
                    } else {
                        // creare si adaugare in functie de tip
                        if (cuvinte[1].equals("solar")) {
                            grid.addProducator(new PanouSolar(cuvinte[2], true, Double.parseDouble(cuvinte[3])));
                        } else if (cuvinte[1].equals("turbina")) {
                            grid.addProducator(new TurbinaEoliana(cuvinte[2], true, Double.parseDouble(cuvinte[3])));
                        } else if (cuvinte[1].equals("reactor")) {
                            grid.addProducator(new ReactorNuclear(cuvinte[2], true, Double.parseDouble(cuvinte[3])));
                        } else {
                            pendingMessages.add("EROARE: Tip producator invalid");
                        }
                        pendingMessages.add(String.format("S-a adaugat producatorul %s de tip %s%n", cuvinte[2], cuvinte[1]));
                    }
                    for (String s: pendingMessages)
                        System.out.print(s);
                    break;
                case "1": // adaugare consumator
                    // verificare daca exista componenta cu acelasi id
                    ComponentaRetea existaConsumator = grid.cautareComponenta(cuvinte[2]);

                    if (existaConsumator != null) {
                        pendingMessages.add(String.format("EROARE: Exista deja o componenta cu id-ul %s%n", cuvinte[2]));
                    } else if (cuvinte.length < 4) {
                        pendingMessages.add("EROARE: Format comanda invalida");  // putere lipsa
                    } else if (Double.parseDouble(cuvinte[3]) <= 0) {
                        pendingMessages.add("EROARE: Cerere putere invalida");
                    } else {
                        // creare si adaugare in functie de tip
                        if (cuvinte[1].equals("suport_viata")) {
                            grid.addConsumator(new SistemSuportViata(cuvinte[2], true, Double.parseDouble(cuvinte[3]), true));
                        } else if (cuvinte[1].equals("laborator")) {
                            grid.addConsumator(new LaboratorStiintific(cuvinte[2], true, Double.parseDouble(cuvinte[3]), true));
                        } else if (cuvinte[1].equals("iluminat")) {
                            grid.addConsumator(new SistemIluminat(cuvinte[2], true, Double.parseDouble(cuvinte[3]), true));
                        } else {
                            pendingMessages.add("EROARE: Tip consumator invalid");
                        }
                        pendingMessages.add(String.format("S-a adaugat consumatorul %s de tip %s%n", cuvinte[2], cuvinte[1]));
                    }
                    for (String s: pendingMessages)
                        System.out.print(s);
                    break;
                case "2":  // adaugare baterie
                    // verificare daca exista deja o componenta cu acelasi id
                    ComponentaRetea existaBaterie = grid.cautareComponenta(cuvinte[1]);

                    if (existaBaterie != null) {
                        pendingMessages.add(String.format("EROARE: Exista deja o componenta cu id-ul %s%n", cuvinte[1]));
                    } else if (cuvinte.length < 3) {
                        pendingMessages.add("EROARE: Format comanda invalida");
                    } else if (Double.parseDouble(cuvinte[2]) <= 0) {
                        pendingMessages.add("EROARE: Capacitate invalida");
                    } else {
                        grid.addBaterie(new Baterii(cuvinte[1], true, Double.parseDouble(cuvinte[2]), 0));
                        pendingMessages.add(String.format("S-a adaugat bateria %s cu capacitatea %s%n", cuvinte[1], cuvinte[2]));
                    }
                    for (String s: pendingMessages)
                        System.out.print(s);
                    break;
                case "3":  // simuleaza tick
                    pendingMessages.clear();
                    if (cuvinte.length < 3) {
                        pendingMessages.add("EROARE: Format comanda invalid");
                    } else {
                        try {
                            double factorSoare = Double.parseDouble(cuvinte[1]);
                            double factorVant = Double.parseDouble(cuvinte[2]);
                            pendingMessages = grid.simuleazaTick(factorSoare, factorVant);
                        } catch (NumberFormatException e) {
                            pendingMessages.add("EROARE: Factori invalizi");  // (nu de tip boolean)
                        }
                    }
                    for (String s: pendingMessages)
                        System.out.print(s);
                    pendingMessages.clear();
                    break;
                case "4":  // modifica status componente
                    pendingMessages.clear();
                    if (cuvinte.length < 3) {
                        pendingMessages.add(String.format("EROARE: Format comanda invalid%n"));
                    } else {
                        String id = cuvinte[1];
                        boolean status;
                        try {
                            if (!cuvinte[2].equalsIgnoreCase("true") && !cuvinte[2].equalsIgnoreCase("false"))
                                throw new IllegalArgumentException("");

                            status = Boolean.parseBoolean(cuvinte[2]);
                        } catch (IllegalArgumentException e) {
                            pendingMessages.add(String.format("EROARE: Status invalid%n"));
                            for (String s: pendingMessages)
                                System.out.print(s);
                            break;
                        }
                        ComponentaRetea componenta = grid.cautareComponenta(id);
                        if (componenta == null) {
                            pendingMessages.add(String.format("EROARE: Nu exista componenta cu id-ul %s%n", id));
                        } else {
                            // seteaza noul status operational
                            componenta.setStatusOperational(status);
                            if (status)
                                pendingMessages.add(String.format("Componenta %s este acum operationala.%n", id));
                            else
                                pendingMessages.add(String.format("Componenta %s este acum defecta.%n", id));
                        }
                    }
                    for (String s: pendingMessages)
                        System.out.print(s);
                    break;
                case "5": // afiseaza status retea
                    pendingMessages.clear();
                    pendingMessages = grid.getStatusMessages();
                    for (String s: pendingMessages)
                        System.out.print(s);
                    pendingMessages.clear();
                    break;
                case "6":  // afiseaza istoric
                    pendingMessages.clear();

                    List<String> istoric = grid.getIstoric();

                    if(istoric.isEmpty()) {
                        pendingMessages.add(String.format("Istoric evenimente gol.%n"));
                    } else {
                        for (String eveniment: istoric)
                            pendingMessages.add(String.format("%s%n", eveniment));
                    }
                    for(String s: pendingMessages)
                        System.out.print(s);
                    pendingMessages.clear();
                    break;
                case "7":  // inchide simulatorul
                    System.out.println("Simulatorul se inchide.");
                    return;
                default:
                    System.out.println("EROARE: Comanda necunoscuta.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}