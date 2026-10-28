# Tema 2 - POO 2025
**Tararache Diana Cristina 322CB**

## Descrierea Metodelor Principale
- **Clasa Platform -> Controller-ul Sistemului**
    - metoda `handleExam`: este metoda principala care proceseaza trimiterea unui examen. Aceasta verifica validitatea studentului si a examenului, apeleaza logica de verificare a timpului (aruncand `SubmissionOutsideTimeInterval` la nevoie) si utilizeaza metoda `getQuestionsForEvaluation()` din clasa Exam pentru a asigura corectarea intrebarilor in ordinea corecta.
    - metoda `calculateScore`: utilizeaza polimorfismul pentru a evalua raspunsul primit. Identifica tipul intrebarii (Multiple Choice sau Text), apeleaza metoda `checkAnswer` specifica si aplica ponderea punctajului returnata de getPercentage.
- **Clasa Exam -> Gestionarea Testului**
    - metoda `addQuestion`: adauga o intrebare in lista. Pentru a pastra integritatea ordinii cronologice (necesare pentru istoric), intrebarile sunt stocate in ordinea inserarii, fara sortare imediata.
    - metoda `isActive`: este o metoda de tip boolean care compara obiectul `LocalDateTime` al trimiterii cu intervalul stabilit (startTime si endTime), asigurand respectarea perioadei de examinare.
    - metoda `getQuestionsForEvaluations()`: creeaza o copie auxiliara a listei de intrebari, o sorteaza dupa dificultate si text, apoi o returneaza ca `Collections.unmodifiableList`, pentru a nu putea fi modificata. Aceasta metoda garanteaza sincronizarea intre ce vede studentul (printExam) si ce corecteaza platforma. Folosind liste auxiliare, este protejata lista originala.
- **Ierarhia Question -> Logica de Evaluare**
    - metoda `checkAnswer` (abstracta): forteaza fiecare tip de intrebare sa isi implementeze propria metoda de comparare a raspunsului primit cu cel corect, returnand un obiect din enum-ul Correctness (CORRECT, PARTIALLY_CORRECT, INCORRECT).
    - metoda `getPercentage` (interfata Gradable): determina cat la suta din punctajul maxim al intrebarii primeste studentul. De exemplu, in OpenEndedQuestion, returneaza 0.7 (70%) pentru un raspuns partial corect.
- **Clasa Main -> Interfata cu Utilizatorul**
    - `main`: contine bucla principala de citire din fisierul input.in. Aceasta parseaza fiecare linie, identifica comanda si redirectioneaza executia catre metodele corespunzatoare din Platform.
    - `System.setOut`: metoda utilizata pentru redirectionarea fluxului de iesire catre fisierele de tip .out (console sau rapoarte de examen).
- **Clasa Student**
    - metoda `addGrade`: inregistreaza o nota noua in `HashMap-ul` intern al studentului. Aceasta metoda asigura legatura bidirectionala, permitand interogarea notelor atat dinspre examen cat si dinspre student.

## Justificarea colectiilor
Am ales structuri de date din pachetul `java.util` in functie de complexitatea operatiilor necesare:
- **ArrayList** (in Exam si Platform -> pentru lista de intrebari si pentru lista globala de studenti) - Intrebarile sunt accesate frecvent in timpul procesului de corectare (met. handleExam). ArrayList ofera acces in timp constant, O(1), fiind eficient.
- **TreeMap** (in Exam -> maparea numelui studentului la nota obtinuta) - Cerinta impune ca rapoartele de examen sa fie ordonate alfabetic dupa numele studentului. Utilizarea unui TreeMap garanteaza sortarea automata a cheilor la fiecare inserare, optimizand generarea raportului final fara a mai necesita pasi suplimentari de sortare.
- **HashMap**(in Student -> stocarea istoricului de note per student) - Deoarece nu este necesara o ordine anume in istoricul personal al unui student si cautarea unei note se face dupa cheie, HashMap ofera cea mai buna performanta de timp (O(1) in medie).

## Genericitate si Ierarhia de clase
Am implementat clasa abstracta `Question<T>`, unde parametrul generic T dicteaza tipul raspunsului asteptat:
- MultipleChoiceQuestion foloseste ResponseOption (Enum).
- OpenEndedQuestion si FillInTheBlankQuestion folosesc String.
Metoda abstracta `checkAnswer(T answer)` utilizeaza acest tip generic pentru a asigura type-safety, eliminand riscul de a compara tipuri de date incompatibile in timpul evaluarii.

## Gestionarea exceptiilor
Pentru a asigura integritatea procesului de examinare, am definit exceptia de tip checked `SubmissionOutsideTimeIntervalException`.
- **Aruncare**: In `Platform.handleExam`, daca metoda `exam.isActive(submissionTime)` returneaza false, se inregistreaza nota 0 si se arunca exceptia folosind `throw new SubmissionOutsideTimeIntervalException()`.
- **Tratare**: In `Main.java`, apelul este inconjurat de un bloc `try-catch`. Acest lucru permite afisarea mesajului de eroare exact in formatul cerut, fara a opri executia programului pentru restul comenzilor.

## Concepte POO Aplicate
- **Encapsulare**: Toate campurile sunt private, accesul fiind realizat prin metode de tip getter. Metodele returneaza colectii de tip `Collections.unmodifiableList` pentru a preveni modificari externe neautorizate.
- **Polimorfism**: Metoda calculateScore din Platform trateaza unitar toate tipurile de intrebari prin intermediul referintei de tip `Question<?>`.
- **Abstractizare**: Interfata Gradable defineste contractul pentru calculul procentual al punctajului (100%, 70%, 50% sau 0%), ascunzand logica specifica fiecarui tip de intrebare.

## Mentiuni
- **Formatul datei**: toate interactiunile cu timpul utilizeaza formatul `dd-MM-yyyy-HH-mm`.
- **Rulare**: programul primeste ca argument in linia de comanda numele folderului de test. Acesta cauta fisierele in src/main/resources/<testName>/input.in si genereaza output-ul in acelasi folder.