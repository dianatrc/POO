# TEMA 1 - Programare orientate pe obiecte
**Tararache Diana Cristina 322CB**

Acest proiect implementeaza simularea si gestionarea unei retele energetice a unei colonii autonome pe Marte.

## Ierarhie de clase
- Nume Clasa: `ComponentaRetea`; Clase care o mostenesc: `ProducatorEnergie, ConsumatorEnergie si Baterii`; Tipul clasei: `abstract`
- Nume Clasa: `ProducatorEnergie`; Clase care o mostenesc: `PanouSolar, TurbinaEoliana si ReactorNuclear`; Tipul clasei: `abstract`
- Nume Clasa: `ConsumatorEnergie`; Clase care o mostenesc: `LaboratorStiintific, SistemIluminat si SistemSuportViata`; Tipul clasei: `abstract`
- Nume Clasa: `Baterii`; Clase care o mostenesc: `-` ; Tipul clasei: `abstract`

**Restul claselor sunt de tipul public si nu sunt mostenite.**

## Descrierea metodei simulareTick(double factorSoare, double factorVant)
- Se initializeaza un contor tick care se va incrementa de fiecare data.
- Toti consumatorii operationali sunt cuplati, cu ajutorul metodei `cupleazaLaRetea()`, la retea.
- Se calculeaza productia totala, prin metoda `calculeazaProductie()` (difera in functie de tipul consumatorului, ReactorNuclear ignora factorul nuclear, PanouSolar foloseste factorSoare, iar TurbinaEoliana factorVant) pe toti producatorii operationali si cererea totala, prin getCerereCurenta pe toti consumatorii cuplati. Apoi, se va calcula delta, diferenta dintre cele doua.
- Daca `delta > 0`, va exista un `surplus`. Excesul de energie este trimis la baterii. Vom incarca bateriile operationale prin metoda `incarca()`, care asigura ca se stocheaza doar pana la capacitatea maxima, returnand orice surplus care nu incape.
- Daca `delta < 0`, va exista un deficit. Se vor descarca bateriile, cu ajutorul metodei `descarca()`, pentru a acoperi diferenta ramasa. Mai apoi, se activeaza logica de `Triage`: consumatorii vor fi sortati descrescator dupa prioritate, urmand sa fie decuplati progresiv, incepand cu prioritatea 3, pana cand deficitul scade. Consumatorii decuplati vor fi adaugati in lista decuplati, urmand sa fie afisati. 
- Daca deficitul ramane pozitiv chiar si dupa pasii de Triage si descarcarea bateriilor, atunci reteaua intra in `BLACKOUT`.

## Descriere App
- `App` este clasa principala si punctul de intrare al aplicatiei, gestionand citirea comenzilor (prin Scanner) si coordonarea executiei acestora. Implementeaza structura switch pentru a delega sarcinile catre GridController.
Se utilizeaza o lista temporara pendingMessages pentru a colecta si afisa mesaje si erori, asigurandu-se consistenta formatarii randurilor. La inceputul comenzilor, se goleste lista, evitandu-se afisarea mesajelor ramase dintr-o executie anterioara. Formatul mesajelor afisate respecta formatul cerut in documentatie. 

## Descriere GridController
- `GridController` este o clasa publica care se ocupa de stocarea si gestionarea retelei (producatori, consumatori si baterii). Este unde se implementeaza metoda `simulareTick()`.  De asemenea, metoda `getStatusMessage()` foloseste apeluri polimorfice pentru a colecta statusul detaliat al fiecarei componente prin apelarea `getStatusGrid()` pe fiecare obiect, asigurand formatul corect indiferent de tipul acestuia (comanda 5).

## Mentiuni
- La adaugarea oricarei componente, se verifica exista id-ului in retea pentru a preveni duplicarea.
- Odata ce reteaua intra in starea de BLACKOUT, comenzile ce modifica reteaua (0 - adaugare producator, 1 – adaugare consumator, 2 - adaugare baterie, 3 – simulare tick si 4 – setare defectiune componenta) sunt blocate, afisandu-se mesajul de eroare. Comenzile de interogare (5 – status retea, 6 – istoric evenimente) functioneaza si in timpul Blackout-ului.
- Consumatorii sunt initializati in starea Alimentat – true la adaugare (comanda 1).
- Encapsulare: metodele specifice starii, cupleazaLaRetea() si decupleazaDeLaRetea(), au fost preferate in detrimentul unor metode publice setEsteAlimentat pentru a imbunatati incapsularea si claritatea.
