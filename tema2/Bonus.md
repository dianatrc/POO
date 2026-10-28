## Bonus.md

## Descriere
Am extins clasa `Exam` pentru a oferi un sistem de analiza a performantei studentilor. Noua functionalitate permite obtinerea unei imagini de ansamblu asupra dificultatii examenului si a succesului colectiv.

## Metode adaugate in clasa Exam
Pentru implementarea acestei functionalitati, am adaugat trei metode cheie care contribuie la realizarea unor statistici precise:
- metoda `getTotalPoints`: calculeaza suma punctajelor maxime ale tuturor intrebarior din examen. Aceasta reprezinta nota maxima teoretica si este esentiala pentru a raporta rezultatele la un punct de referinta.
- metoda `calculateAverageScore`: calculeaza media aritmetica a tuturor punctajelor obtinute de studentii participanti. 
- metoda `getPassRate`: calculeaza procentul de promovabilitate. Un student este considerat "promovat" daca a obtinut cel putin 50% din punctajul maxim. Rezultatul este returnat sub forma de procent: 0-100%.

## Justificare si utilitate
Prin utilizarea metodei `getTotalPossiblePoints`, rata de promovare nu este fixa, ci se adapteaza la configuratia examenului - in functie de punctajul maxim.
Este utila aceasta functionalitate, deoarece se poate identifica daca un examen a fost prea greu (medie scazuta, rata de promovare mica), sau daca intrebarile au fost prea usoare.