package com.andsomore.mobilit.entite;

import java.util.Date;

public class Reservation {
    private static  int IdReservation;
    private static int NumPlace;
    private Date DateReservation;
    private int NumVoyage;
    private int IdClient;

    public Reservation(){}

    public Reservation(Date dateReservation, int numVoyage, int idClient) {
        DateReservation = dateReservation;
        NumVoyage = numVoyage;
        IdClient = idClient;
    }

    public static int getIdReservation() {
        return IdReservation;
    }

    public static int getNumPlace() {
        return NumPlace;
    }

    public Date getDateReservation() {
        return DateReservation;
    }

    public int getNumVoyage() {
        return NumVoyage;
    }

    public int getIdClient() {
        return IdClient;
    }
}
