package com.andsomore.mobilit.entite;



public class Utilisateur {
    protected static int Id;
    protected String Nom;
    protected String Prenom;
    protected String Telephone;
    protected String Email;
    protected String Password;
    protected String TypeUtilisateur;

    public Utilisateur(){

    }
    public Utilisateur(String nom, String prenom, String telephone, String email, String password, String typeUtilisateur) {
        Nom = nom;
        Prenom = prenom;
        Telephone = telephone;
        Email = email;
        Password = password;
        TypeUtilisateur = typeUtilisateur;
        Id++;
    }

    public static int getId() {
        return Id;
    }

    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getTelephone() {
        return Telephone;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getTypeUtilisateur() {
        return TypeUtilisateur;
    }
}
