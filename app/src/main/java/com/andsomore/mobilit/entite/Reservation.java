package com.andsomore.mobilit.entite;

import java.util.Date;

public class Reservation {
    private Date dateReservation;
    private String villeDepart;
    private String villeArrivee;
    private String nomBus;
    private Date jourVoyage;
    private String etatPaiement;
    private String modePaiement;
    private String idClient;
    private String numTelephone;
    private int amount;
    private int numPlace;
    private String codeRef;


    public Reservation(){}

    public Reservation(Date dateReservation,
                       String villeDepart, String villeArrivee, String nomBus,
                       Date jourVoyage, String etatPaiement, String modePaiement,
                       String idClient, int amount, int numPlace,String numTelephone, String codeRef)
    {
        this.dateReservation = dateReservation;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.nomBus = nomBus;
        this.jourVoyage = jourVoyage;
        this.etatPaiement = etatPaiement;
        this.modePaiement = modePaiement;
        this.idClient = idClient;
        this.amount = amount;
        this.numPlace = numPlace;
        this.numTelephone=numTelephone;
        this.codeRef = codeRef;
    }

    public Date getDateReservation() {
        return dateReservation;

    }

    public String getModePaiement() {
        return modePaiement;
    }

    public Date getJourVoyage() {
        return jourVoyage;
    }

    public String getNomBus() {
        return nomBus;
    }


    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public int getAmount() {
        return amount;
    }

    public String getEtatPaiement() {
        return etatPaiement;
    }

    public int getNumPlace() {
        return numPlace;
    }


    public String getCodeRef() {
        return codeRef;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;

    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public void setNomBus(String nomBus) {
        this.nomBus = nomBus;
    }

    public void setJourVoyage(Date jourVoyage) {
        this.jourVoyage = jourVoyage;
    }

    public void setEtatPaiement(String etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setNumPlace(int numPlace) {
        this.numPlace = numPlace;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }
}
