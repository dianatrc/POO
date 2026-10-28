# Proiect 2026 POO
**Tararache Diana Cristina 322CB**

Aplicatia reprezinta un sistem de gestiune a infrastructurii IT, permitand administrarea serverelor, organizarea acestora in grupuri de resurse si monitorizarea activitatii prin alerte. Sistemul proceseaza comenzi din fisierele de intrare si genereaza rapoarte de stare in fisierele de output.

## Design Patterns
Aplicatia utilizeaza `5 design patterns` pentru a asigura un cod modular, extensibil si usor de intretinut:
- **Singleton Pattern (clasa `Database`)**
    - Asigura ca exista o singura instanta a bazei de date in intreaga aplicatie, prevenind inconsistentele si conflictele de acces la date. Singleton previne crearea de copii ale bazei de date care ar putea deveni inconsistente.
    - **Avantaje:**
        - Control centralizat - toate operatiunile cu servere, grupuri si utilizatori trec printr-un singur punct de acces (`Database.getInstance()`).
        - Gestionare de resurse optima - nu se creeaza multiple instante ale bazei de date.
        - Acces global - orice componenta poate accesa baza de date fara a trece prin referinte.
- **Builder Pattern (clasele `Server.Builder` si `Location.Builder`)**
    - Aceast design pattern permite construirea incrementala a obiectelor complexe care au mai multe campuri optionale, fara a avea constructori cu zeci de parametri.
    - **Avantaje:**
        - Flexibilitate - se pot omite campuri optionale fara a avea zeci de constructori.
        - Imutabilitate - obiectele finale sunt imutabile (toate campurile sunt de tip final).
        - Lizibilitate crescuta - se vede clar ce reprezinta fiecare valoare, in special cele care sunt obligatorii, respectiv cele care sunt optionale.
- **Chain of Responsability Pattern (clasele `BaseValidator`, `IpValidator`, `LocationValidator`, `UserValidator`)**
    - Chain of responsability permite procesarea secventiala a unei cereri prin multipli validatori, fiecare avand responsabilitatea de a verifica un aspect specific.
    - Flow-ul validarii:
        - IpValidator -> verifica daca IP-ul exista.
        - LocationValidator -> verifica daca tara este furnizata.
        - UserValidator -> verifica daca numele si rolul sunt furnizate.
    - **Avantaje:**
        - Separarea responsabilitatilor - fiecare validator verifica un singur lucru.
        - Extensibilitate - se pot adauga noi validatori fara sa se modifice codul existent.
        - Error handling - toate validarile arunca exceptii custom.
    - **Justificare:** Comenzile au criterii de validare diferite (ADD SERVER verifica location, ADD MEMBER verifica user etc.). Chain of responsability permite aplicarea validarilor relevante fara logica conditionala complexa (if-else).
- **Observer Pattern (clasele `Observer(interfata)`, `Server(Subiect)`, `ResourceGroup(Observator)`)**
    - Implementeaza un mecanism de notificare automata - cand un server genereaza o alerta, toate grupurile de monitorizare inregistrate sunt notificate.
    - Flow-ul evenimentelor:
        - se executa comanda ADD EVENT cu parametrii alertei;
        - CommandHandler.addEvent() gaseste serverul corespunzator;
        - Server.addAlert() este apelat;
        - serverul salveaza alerta in Database;
        - serverul notifica automat toate grupurile de monitorizare inregistrate;
        - fiecare ResourceGroup executa metoda update() si afiseaza mesajul;
    - **Avantaje:**
        - Sincronizare - nu apelam manual fiecare notificare.
        - Decuplare - serverul nu stie detalii despre grupurile care il monitorizeaza.

- **Factory Pattern (clasa `UserFactory`)**
    - Factory Pattern centralizeaza logica de creare a diferitelor tipuri de utilizatori (User, Operator, Admin) bazat pe rolul specific.
    - Ierarhia claselor:
        - User -> Operator (adauga department) -> Admin (adauga clearance level)
    - **Avantaje:**
        - Encapsulare - logica de decizie pentru crearea obiectelor este intr-un singur loc.
        - Evitarea codului redundant - se apeleaza metoda createUser pentru creare obiectelor.
        - Validare centralizata - toata validarile se fac in factory.

## Principii POO Aplicate
- **1. Encapsulare**
    - Toate campurile sensibile (ipAddress, clearanceLevel etc.) sunt private sau protected, fiind accesibile doar prin metode de tip getter.
    - Constructorii sunt privati pentru Builder Pattern si Singleton. In Builder, constructorii sunt private, fortand utilizatorul sa treaca prin procesul de constructie controlat de Builder. In Singleton, constructorul este private pentru a preveni crearea de instante multiple din exterior.
- **2. Mostenire**
    - Ierarhia Utilizatorilor
        - `User -> Operator (adauga department) -> Admin (adauga clearance level)`: Admin mosteneste de la Operator, care mosteneste de la User. 
    - Ierarhia Validatorilor
        Toti validatorii specifici (IpValidator, UserValidator, LocationValidator) extind clasa abstracta BaseValidator, mostenind mecanismul de inlantuire next.
- **3. Polimorfism**
    - ResourceGroup implementeaza interfata Observer. Astfel, un Server poate notifica orice obiect care este de tip Observer, fara a sti ca acesta este un ResourceGroup.
    - `Overriding`
        - Fiecare validator implementeaza propria logica in metoda validate().
        - Suprascrierea equals() si hashCode() asigura ca obiectele sunt comparate pe baza identitatii lor logice - de exemplu, pentru Server, in functie de ipAddress.
- **4. Abstractie**
    - Clase abstracte - `BaseValidator` defineste un sablon pentru validare, dar lasa detaliile specifice implementarilor concrete.
- **5. Imutabilitate**
    - Campuri final - in clasele `Server`, `Location`, `Alert` etc.
    - Stabilitatea starii - odata ce un obiect a fost creat prin `Builder` sau `Factory`, starea sa nu mai poate fi schimbata.
- **6. Genericitate**
    - Utilizarea colectiilor (`List<User>, Set<Server>, List<String>`) pentru a asigura `Type Safety` la compilare si a evita operatiunile de casting riscante.
- **7. Error Handling**
    - Aplicatia este bazata pe exceptii custom: `MissingIpAddressException, UserException si LocationException`. Erorile sunt interceptate in `CommandHandler` sau in `Main`, oferind mesaje de eroare specifice liniei care a generat problema in fisierul de intrare.

## Structuri folosite
- **LinkedHashSet** (clasa `Database`) - garanteaza unicitatea elementelor (nu putem avea doua servere cu acelasi IP), mentinand in acelasi timp ordinea inserarii. 
- **ArrayList** - folosit pentru liste de membri si observatori, unde accesul indexat si ordinea sunt importante.
- **Collections.unmodifiableSet** - folosit in gettere pentru a proteja integritatea datelor din Database.

## Functionalitate 
Sistemul proceseaza comenzi complexe, asigurand consistenta datelor prin validari multiple si notificari automate.
- **1. Managementul Serverelor (comanda `ADD SERVER`)** - Atunci cand o comanda de adaugare server este primita:
    - Validare in lant: Datele trec prin `IpValidator` (verifica IP-ul), `LocationValidator` (verifica tara) si `UserValidator` (verifica nume si rol).
    - Constructie obiecte: Se utilizeaza `Location.Builder` pentru a crea locatia, apoi `UserFactory` pentru a crea proprietarul. In final, se foloseste `Server.Builder` pentru a asambla serverul cu toate atributele (CPU, RAM etc.).
    - Persistenta: Serverul este salvat in Database intr-un `LinkedHashSet`, garantand ca nu exista duplicate dupa IP, dar pastrand ordinea cronologica.
- **2. Gestiunea Grupurilor de Resurse (comenzile `ADD/FIND/REMOVE GROUP`)**
    - Grupare pe IP: Un ResourceGroup este creat pentru un IP specific.
    - Mecanismul de Monitoring: La crearea unui grup, acesta este inregistrat automat ca Observer in lista serverului corespunzator acelui IP. Astfel, orice eveniment de tip server va fi auzit de grup.
    - Cautare si Stergere: Database permite localizarea rapida a grupurilor pentru audit sau eliminare.
- **3. Administrarea Membrilor (comanda `ADD/REMOVE MEMBER`)**
    - Ierarhie si Roluri: Membrii adaugati intr-un grup pot fi User, Operator sau Admin. UserFactory decide tipul obiectului bazat pe string-ul primit.
    - Atributele Specifice: Daca rolul este Admin, sistemul extrage si clearanceLevel, dar daca este Operator, extrage department.
    - Validare Identitate: Metoda equals() din clasa User asigura ca un membru este unic in cadrul grupul pe baza adresei de email.
- **4. Sistemul de Alerte si Notificari (comanda `ADD EVENT`)**
    - Declansare: Se primeste un eveniment (ex. `ANOMALY, HIGH severity`)
    - Logare: Serverul creeaza un obiect Alert si il trimite catre Database pentru istoricul global
    - Notificare: Serverul itereaza prin lista de observers(obiecte ResourceGroup) si apeleaza metoda update().
    - Output: Fiecare grup notificat afiseaza in fisierul .out detaliile alertei, formatate conform cerintelor.
