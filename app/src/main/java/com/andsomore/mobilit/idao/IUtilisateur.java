package com.andsomore.mobilit.idao;

public interface IUtilisateur <T>{
    public boolean seConnecter(T t);
    public boolean seDeconnecter(T t);
    public boolean modifierInfoCompte(T t);
    public boolean supprimerCompte(T t);
    public boolean creerCompte(T t);
}
