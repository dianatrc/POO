package Tema1;

public class LaboratorStiintific extends  ConsumatorEnergie {
    public LaboratorStiintific(String id, boolean status, double cerere, boolean esteAlimentat) {
        super(id, status, cerere, 2, esteAlimentat);
    }

    @Override
    public String getStatusGrid() {
        if (esteAlimentat)
            return String.format("Consumator %s (LaboratorStiintific) - Cerere: %.2f - Prioritate: %d - Status: %s%n ",
                    id, cerereEnergie, prioritate, "Alimentat");
        return String.format("Consumator %s (LaboratorStiintific) - Cerere: %.2f - Prioritate: %d - Status: %s%n ",
                id, cerereEnergie, prioritate, "Decuplat");
    }
}
