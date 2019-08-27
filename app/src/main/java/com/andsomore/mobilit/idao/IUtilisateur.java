package com.andsomore.mobilit.idao;
public interface IUtilisateur<T> {

    public void seConnecter(T t,IConnected connected);
    public boolean seDeconnecter(T t);
    public boolean modifierInfoCompte(T t);
    public boolean supprimerCompte(T t);
    public boolean creerCompte(T t,IConnected connected);
    public void isConnected(boolean ok);
    public void isDriver(IResult result);



}
