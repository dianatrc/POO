package Tema1;

public abstract class ComponentaRetea {
    protected String id;
    protected boolean statusOperational;

    protected ComponentaRetea(String id, boolean status) {
        this.id = id;
        this.statusOperational = status;
    }

    public String getID() {
        return id;
    }
    public boolean getStatusOperational() {
        return statusOperational;
    }

    public void setStatusOperational(boolean status) {
        this.statusOperational = status;
    }

    // metoda abstracta pentru a obtine statusul formatat al fiecarei componente din retea
    public abstract String getStatusGrid();
}
