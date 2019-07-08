package com.andsomore.mobilit.entite;

public class Client extends  Utilisateur{
    public int NumBillet;


    public Client() {
    }

    public Client(String nom, String prenom, String telephone, String email, String password, String typeUtilisateur) {
        super(nom, prenom, telephone, email, password, typeUtilisateur);
    }

    public int getNumBillet() {
        return NumBillet;
    }

    public void setNumBillet(int numBillet) {
        NumBillet = numBillet;
    }
}
