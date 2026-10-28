package Tema1;

public class TurbinaEoliana extends ProducatorEnergie {
    public TurbinaEoliana(String id, boolean status, double putere) {
        super(id, status, putere);
    }
    // factorul extern este cel al vantului
    @Override
    public double calculeazaProductie(double vitezaVant) {
        if (statusOperational)
            return putere * vitezaVant;
        else
            return 0;
    }

    @Override
    public String getStatusGrid() {
        if (statusOperational)
            return String.format("Producator %s(TurbinaEoliana) - Putere: %.2f - Status: %s%n",
                    id, putere, "Operational");
        return String.format("Producator %s(TurbinaEoliana) - Putere: %.2f - Status: %s%n",
                id, putere, "Defect");
    }
}
