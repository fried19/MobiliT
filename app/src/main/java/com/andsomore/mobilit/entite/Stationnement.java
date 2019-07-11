package com.andsomore.mobilit.entite;

public class Stationnement {
    private static int Id=1;
    private String Lieu;

    public Stationnement(){}

    public Stationnement( String lieu) {
        Id ++;
        Lieu = lieu;
    }

    public static  int getId() {
        return Id;
    }

    public String getLieu() {
        return Lieu;
    }
}
