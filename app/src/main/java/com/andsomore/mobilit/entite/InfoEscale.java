package com.andsomore.mobilit.entite;

public class InfoEscale {
    private String Lieuscale;
    private int HeureArrivee;
    private int HeureDepart;
    private int NumVoyage;
    private int IdAgence;

    public InfoEscale(){}

    public InfoEscale(String lieuscale, int heureArrivee, int heureDepart, int numVoyage, int idAgence) {
        Lieuscale = lieuscale;
        HeureArrivee = heureArrivee;
        HeureDepart = heureDepart;
        NumVoyage = numVoyage;
        IdAgence = idAgence;

    }

    public String getLieuscale() {
        return Lieuscale;
    }

    public int getHeureArrivee() {
        return HeureArrivee;
    }

    public int getHeureDepart() {
        return HeureDepart;
    }

    public int getNumVoyage() {
        return NumVoyage;
    }

    public int getIdAgence() {
        return IdAgence;
    }
}
