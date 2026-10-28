package Tema1;

public class Baterii extends ComponentaRetea {
    private double capacitateMaxima;
    private double energieStocata;

    public Baterii(String id, boolean status, double capMax, double energie) {
        super(id, status);
        this.capacitateMaxima = capMax;
        this.energieStocata = energie;
    }

    public double getEnergieStocata() {
        return energieStocata;
    }

    public double incarca(double energieDisponibila) {
        if (!statusOperational)
            return energieDisponibila;  // nu incarca daca e defecta
        if ((energieDisponibila + energieStocata) <= capacitateMaxima) {
            energieStocata += energieDisponibila;
            return 0;
        } else {
            double surplus = (energieDisponibila + energieStocata) - capacitateMaxima;
            energieStocata = capacitateMaxima;
            return surplus;  // intoarce surplusul de energia ce nu poate fi stocat
        }
    }

    public double descarca(double energieCeruta) {
        if (energieCeruta <= energieStocata) {
            energieStocata -= energieCeruta;
            return energieCeruta;
        } else {
            double energieFurnizata = energieStocata;
            energieStocata = 0;
            return energieFurnizata;
        }
    }

    @Override
    public String getStatusGrid() {
        if (statusOperational)
            return String.format("Baterie %s - Stocare: %.2f/%.2f - Status: %s%n",
                    id, energieStocata, capacitateMaxima, "Operational");
        return String.format("Baterie %s - Stocare: %.2f/%.2f - Status: %s%n",
                id, energieStocata, capacitateMaxima, "Defect");
    }
}
