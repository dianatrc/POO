package Tema1;

public abstract class ConsumatorEnergie extends ComponentaRetea {
    protected double cerereEnergie;
    protected int prioritate;
    protected boolean esteAlimentat;

    public ConsumatorEnergie(String id, boolean status, double cerere, int prioritate, boolean esteAlimentat) {
        super(id, status);
        this.cerereEnergie = cerere;
        this.prioritate = prioritate;
        this.esteAlimentat = esteAlimentat;
    }

    public double getCerereEnergie() {
        return  cerereEnergie;
    }

    public int getPrioritate() {
        return prioritate;
    }

    public boolean getEsteAlimentat() {
        return esteAlimentat;
    }

    public double getCerereCurenta() {
        if (esteAlimentat)
            return cerereEnergie;
        else
            return 0;
    }

    public void cupleazaLaRetea() {
        this.esteAlimentat = true;
    }
    public void decupleazaDeLaRetea() {
        this.esteAlimentat = false;
    }
}
