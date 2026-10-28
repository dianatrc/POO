package Tema1;

public class SistemIluminat extends ConsumatorEnergie {
    public SistemIluminat(String id, boolean status, double cerere, boolean esteAlimentat) {
        super(id, status, cerere, 3, esteAlimentat);  // prioritate 3
    }

    @Override
    public String getStatusGrid() {
        if (esteAlimentat)
            return String.format("Consumator %s(SistemIluminat) - Cerere: %.2f - Prioritate: %d - Status: %s%n",
                    id, cerereEnergie, prioritate, "Alimentat");
        return String.format("Consumator %s(SistemIluminat) - Cerere: %.2f - Prioritate: %d - Status: %s%n",
                id, cerereEnergie, prioritate, "Decuplat");
    }
}
