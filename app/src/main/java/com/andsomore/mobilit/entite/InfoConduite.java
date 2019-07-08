package com.andsomore.mobilit.entite;

import java.util.Date;
import java.util.Timer;

public class InfoConduite {

    private Date DateConduite;
    private double DureeConduite;
    private int IdChauffeur;
    private  int IdVehicule;

    public InfoConduite(){}

    public InfoConduite(Date dateConduite, double dureeConduite, int idChauffeur, int idVehicule) {
        DateConduite = dateConduite;
        DureeConduite = dureeConduite;
        IdChauffeur = idChauffeur;
        IdVehicule = idVehicule;
    }

    public Date getDateConduite() {
        return DateConduite;
    }

    public double getDureeConduite() {
        return DureeConduite;
    }

    public int getIdChauffeur() {
        return IdChauffeur;
    }

    public int getIdVehicule() {
        return IdVehicule;
    }
}
