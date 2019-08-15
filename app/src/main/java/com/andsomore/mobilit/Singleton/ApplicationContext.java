package com.andsomore.mobilit.Singleton;

import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {

    private String Nom;
    private String Prenom;
    private String Telephone;
    private String TypeUtilistaur;
    public static final String Token="3b540bf9-09a1-41cd-a854-bc73efbbd305" ;


    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getTypeUtilistaur() {
        return TypeUtilistaur;
    }

    public void setTypeUtilistaur(String typeUtilistaur) {
        TypeUtilistaur = typeUtilistaur;
    }

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

    }

    public static Context getAppContext() {
        return appContext;
    }

}
