package Tema1;

public class PanouSolar extends ProducatorEnergie {
    public PanouSolar(String id, boolean status, double putere) {
        super(id, status, putere);
    }

    // factorul extern este factorul soarelui
    @Override
    public double calculeazaProductie(double procentajSoare) {
        if (statusOperational)
            return putere * procentajSoare;
        else
            return 0;
    }

    @Override
    public String getStatusGrid() {
        if (statusOperational)
            return String.format("Producator %s (PanouSolar) - PutereBaza: %.2f - Status: %s%n ",
                    id, putere, "Operational");
        else
            return String.format("Producator %s (PanouSolar) - PutereBaza: %.2f - Status: %s%n ",
                id, putere, "Defect");
    }
}
