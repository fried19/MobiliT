package com.andsomore.mobilit.entite;

public class InfoStationnement {
    private int HeureArrivee;
    private int HeureDepart;
    private int IdStationnement;
    private int NumTransport;

    public InfoStationnement(){}

    public InfoStationnement(int heureArrivee, int heureDepart, int idStationnement, int numTransport) {
        HeureArrivee = heureArrivee;
        HeureDepart = heureDepart;
        IdStationnement = idStationnement;
        NumTransport = numTransport;
    }

    public int getHeureArrivee() {
        return HeureArrivee;
    }

    public int getHeureDepart() {
        return HeureDepart;
    }

    public int getIdStationnement() {
        return IdStationnement;
    }

    public int getNumTransport() {
        return NumTransport;
    }
}
