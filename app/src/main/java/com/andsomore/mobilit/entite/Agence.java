package com.andsomore.mobilit.entite;

public class Agence {
       private static int Id=1;
       private String Nom;
       private String Lieu;
       private String Responsable;

       public Agence(){}
       public Agence(String nom, String lieu) {
            Nom = nom;
            Lieu = lieu;
            Id++;
        }

    public String getResponsable() {
        return Responsable;
    }

    public void setResponsable(String responsable) {
        Responsable = responsable;
    }

    public static int getId() {
        return Id;
    }

    public String getNom() {
        return Nom;
    }

    public String getLieu() {
        return Lieu;
    }
}
