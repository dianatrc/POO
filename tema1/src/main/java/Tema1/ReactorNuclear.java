package Tema1;

public class ReactorNuclear extends ProducatorEnergie {
    public ReactorNuclear(String id, boolean status, double putere) {
        super(id, status, putere);
    }
    // factorul extern este ignorat
    @Override
    public double calculeazaProductie(double factorExtern) {
        if (statusOperational == true)
            return putere;
        else return 0;
    }

    // returneaza starea pentru afisarea in retea
    @Override
    public String getStatusGrid() {
        if (statusOperational)
            return String.format("Producator %s (ReactorNuclear) - PutereBaza: %.2f - Status: %s%n ",
                    id, putere, "Operational");
        else
            return String.format("Producator %s (ReactorNuclear) - PutereBaza: %.2f - Status: %s%n ",
                    id, putere, "Defect");
    }
}
