package Tema1;

public class SistemSuportViata extends ConsumatorEnergie {
    public SistemSuportViata(String id, boolean status, double cerere, boolean esteAlimentat) {
        super(id, status, cerere, 1, esteAlimentat); // prioritate 1
    }

    @Override
    public String getStatusGrid() {
        if (esteAlimentat)
            return String.format("Consumator %s(SistemSuportViata) - Cerere: %.2f - Prioritate: %d - Status: %s%n",
                    id, cerereEnergie, prioritate, "Alimentat");
        return String.format("Consumator %s(SistemSuportViata) - Cerere: %.2f - Prioritate: %d - Status: %s%n",
                id, cerereEnergie, prioritate, "Decuplat");
    }
}
