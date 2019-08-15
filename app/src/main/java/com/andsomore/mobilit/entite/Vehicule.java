package com.andsomore.mobilit.entite;

public class Vehicule {
    private static int Id=1;
    private String immatriculation;
    private String nomBapteme;
    private int placeTotale;
    private int placeDisponible;
    private String etat;
    private double Longitude;
    private double Latitude;

    public Vehicule(){}
    public Vehicule( String immatriculation, String nomBapteme, int nombrePlace, String etat, double longitude, double latitude) {
        Id ++;
        this.immatriculation = immatriculation;
        this.nomBapteme = nomBapteme;
        this.placeTotale = nombrePlace;
        this.etat = etat;
        Longitude = longitude;
        Latitude = latitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public int getPlaceDisponible() {
        return placeDisponible;
    }

    public void setPlaceDisponible(int placeDisponible) {
        this.placeDisponible = placeDisponible;
    }

    public String getNomBapteme() {
        return nomBapteme;
    }

    public void setNomBapteme(String nomBapteme) {
        this.nomBapteme = nomBapteme;
    }

    public int getPlaceTotale() {
        return placeTotale;
    }

    public void setPlaceTotale(int placeTotale) {
        this.placeTotale = placeTotale;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
