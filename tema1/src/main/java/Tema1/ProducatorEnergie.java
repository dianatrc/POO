package Tema1;

public abstract class ProducatorEnergie extends ComponentaRetea {
    protected double putere;

    public ProducatorEnergie(String id, boolean status, double putere) {
        super(id, status);
        this.putere = putere;
    }
    // metoda abstracta pentru calcului productiei de energie
    public abstract double calculeazaProductie(double factorExtern);
}
