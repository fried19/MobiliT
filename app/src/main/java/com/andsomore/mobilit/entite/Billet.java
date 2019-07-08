package com.andsomore.mobilit.entite;

public class Billet {
    private static int NumBillet;
    private static int NumPlace;
    private double Montant;
    private String ModePaiement;
    private String EtatBillet;

    public Billet(){}

    public Billet(double montant, String modePaiement) {
        Montant = montant;
        ModePaiement = modePaiement;
    }

    public static int getNumBillet() {
        return NumBillet;
    }

    public static int getNumPlace() {
        return NumPlace;
    }

    public double getMontant() {
        return Montant;
    }

    public String getModePaiement() {
        return ModePaiement;
    }

    public String getEtatBillet() {
        return EtatBillet;
    }

    public void setEtatBillet(String etatBillet) {
        EtatBillet = etatBillet;
    }
}
