package com.andsomore.mobilit.entite;

public class Vehicule {
    private static int Id=1;
    private String Immatriculation;
    private String NomBapteme;
    private int NombrePlace;
    private String Etat;
    private double Longitude;
    private double Latitude;

    public Vehicule(){}
    public Vehicule( String immatriculation, String nomBapteme, int nombrePlace, String etat, double longitude, double latitude) {
        Id ++;
        Immatriculation = immatriculation;
        NomBapteme = nomBapteme;
        NombrePlace = nombrePlace;
        Etat = etat;
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
        return Immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        Immatriculation = immatriculation;
    }

    public String getNomBapteme() {
        return NomBapteme;
    }

    public void setNomBapteme(String nomBapteme) {
        NomBapteme = nomBapteme;
    }

    public int getNombrePlace() {
        return NombrePlace;
    }

    public void setNombrePlace(int nombrePlace) {
        NombrePlace = nombrePlace;
    }

    public String getEtat() {
        return Etat;
    }

    public void setEtat(String etat) {
        Etat = etat;
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
