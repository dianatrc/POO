package Tema1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GridController {
    private List<ProducatorEnergie> producatori = new ArrayList<>();
    private List<ConsumatorEnergie> consumatori = new ArrayList<>();
    private List<Baterii> baterii = new ArrayList<>();
    private boolean esteInBlackout = false;
    private List<String> istoric = new ArrayList<>();
    private int tick = 0;

    public boolean getEsteInBlackout() {
        return esteInBlackout;
    }

    public void addProducator(ProducatorEnergie p) {
        producatori.add(p);
    }
    public List<ProducatorEnergie> getProducatori() {
        return producatori;
    }

    public void addConsumator(ConsumatorEnergie c) {
        consumatori.add(c);
    }
    public List<ConsumatorEnergie> getConsumatori() {
        return consumatori;
    }

    public void addBaterie(Baterii b) {
        baterii.add(b);
    }
    public List<Baterii> getBaterii() {
        return baterii;
    }

    public List<String> getIstoric() {
        return istoric;
    }

    public List<String> simuleazaTick(double factorSoare, double factorVant) {
        List<String> pendingMessages = new ArrayList<>();

        if (esteInBlackout) {
            pendingMessages.add(String.format("EROARE: Reteaua este in BLACKOUT. Simulare oprita.%n"));
            return pendingMessages;
        }
        tick++;

        // cuplam toti consumatorii (daca sunt operationali)
        for (ConsumatorEnergie c: consumatori) {
            if (c.getStatusOperational())
                c.cupleazaLaRetea();
        }
        // calcul productie totala
        double productieTotala = 0;
        for(ProducatorEnergie p: producatori) {
            if (p.getStatusOperational()) {
                if (p instanceof PanouSolar) {
                    productieTotala += p.calculeazaProductie(factorSoare);
                } else if (p instanceof TurbinaEoliana) {
                    productieTotala += p.calculeazaProductie(factorVant);
                } else {
                    // la ReactorNuclear factorul extern nu conteaza
                    productieTotala += p.calculeazaProductie(0);
                }
            }
        }
        // calcul cerere totala
        double cerereTotala = 0;
        for(ConsumatorEnergie c: consumatori) {
            if (c.getStatusOperational()) {
                cerereTotala +=c.getCerereCurenta();
            }
        }

        double delta = productieTotala - cerereTotala;

        // surplus
        if (delta > 0) {
            // incarca bateriile
            for(Baterii b: baterii) {
                if (b.getStatusOperational() && delta > 0)
                    delta = b.incarca(delta);
                if (delta <= 0) {
                    break;
                }
            }
            // calcul baterii
            double putereBaterii = 0;
            for (Baterii b: baterii) {
                putereBaterii += b.getEnergieStocata();
            }
            pendingMessages.add(String.format("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: []%n",
                    productieTotala, cerereTotala, putereBaterii));
        } else if (delta < 0) {  // deficit
            double deficit = -delta;

            // descarca bateriile pentru a acoperi deficitul
            for (Baterii b: baterii) {
                if (b.getStatusOperational() && deficit > 0)
                    deficit -= b.descarca(deficit);
                if (deficit <= 0) {
                    break;
                }
            }
            // Triage - daca deficitul persista
            List<String> decuplati = new ArrayList<>();
            if (deficit > 0) {
                // sorteaza consumatorii in ordinea descrescatoare a prioritatii
                consumatori.sort(new Comparator<ConsumatorEnergie>() {
                    @Override
                    public int compare(ConsumatorEnergie c1, ConsumatorEnergie c2) {
                        int prioritate1 = c1.getPrioritate();
                        int prioritate2 = c2.getPrioritate();
                        if (prioritate2 > prioritate1) {
                            return 1;
                        } else if (prioritate2 < prioritate1) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                // Triage - decuplare
                for (ConsumatorEnergie c: consumatori) {
                    if (deficit <= 0)
                        break;
                    if (c.getPrioritate() >= 2 && c.getEsteAlimentat()) {
                        if (c.getStatusOperational()) {
                            c.decupleazaDeLaRetea();
                            deficit -= c.getCerereEnergie();
                            decuplati.add(c.getID());
                        }
                    }
                }
                if (!decuplati.isEmpty()) {
                    // adauga eveniment in istoric
                    String rez = "";
                    for (int i = 0; i < decuplati.size(); i++) {
                        rez += decuplati.get(i);
                        if (i < decuplati.size() - 1)
                            rez += ", ";
                    }
                    istoric.add("Tick " + tick + ": Deficit - Decuplat " + rez);
                }
            }
            // calcul baterii
            double putereBaterii = 0;
            for (Baterii b: baterii) {
                putereBaterii += b.getEnergieStocata();
            }
            // Blackout
            if (deficit > 0) {
                esteInBlackout = true;
                istoric.add(String.format("Tick %d: BLACKOUT! SIMULARE OPRITA.%n", tick));
                pendingMessages.add(String.format("BLACKOUT! SIMULARE OPRITA.%n"));
            } else {
                pendingMessages.add(String.format("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: %s%n",
                        productieTotala, cerereTotala, putereBaterii, decuplati));
            }
        } else {  // delta = 0 (echilibru)
            // calcul baterii
            double putereBaterii = 0;
            for (Baterii b: baterii) {
                putereBaterii += b.getEnergieStocata();
            }
            pendingMessages.add(String.format("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: []%n",
                    productieTotala, cerereTotala, putereBaterii));
        }
        return pendingMessages;
    }

    public ComponentaRetea cautareComponenta(String id) {
        for (ProducatorEnergie p: producatori) {
            if (p.getID().equals(id))
                return p;
        }
        for (ConsumatorEnergie c: consumatori) {
            if (c.getID().equals(id))
                return c;
        }
        for (Baterii b: baterii) {
            if(b.getID().equals(id))
                return b;
        }
        return null;
    }

    public List<String> getStatusMessages() {
        List<String> pendingMessages = new ArrayList<>();

        if (producatori.isEmpty() && consumatori.isEmpty() && baterii.isEmpty()) {
            pendingMessages.add(String.format("Reteaua este goala.%n"));
            return pendingMessages;
        }

        // status general
        if (esteInBlackout) {
            pendingMessages.add(String.format("Stare Retea: BLACKOUT%n"));
        } else {
            pendingMessages.add(String.format("Stare Retea: STABILA%n"));
        }
        // status componente
        for (ProducatorEnergie p:  producatori)
            pendingMessages.add(p.getStatusGrid());
        for (ConsumatorEnergie c: consumatori)
            pendingMessages.add(c.getStatusGrid());
        for (Baterii b: baterii)
            pendingMessages.add(b.getStatusGrid());
        return pendingMessages;
    }
}
