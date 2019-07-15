package com.andsomore.mobilit.entite;



public class Utilisateur {
    protected String nom;
    protected String prenom;
    protected String telephone;
    protected String email;
    protected String password;
    protected String typeUtilisateur;


    public Utilisateur(){

    }

    public Utilisateur(String email,String password){
        this.email = email;
        this.password = password;
    }
    public Utilisateur(String nom, String prenom, String telephone, String email, String password, String typeUtilisateur) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.typeUtilisateur = typeUtilisateur;
    }


    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeUtilisateur() {
        return typeUtilisateur;
    }
}
