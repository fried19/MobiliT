package com.andsomore.mobilit.dao;

import com.andsomore.mobilit.idao.IUtilisateur;

public class TraitementUtilisateur implements IUtilisateur {
    @Override
    public boolean seConnecter(Object o) {
        return false;
    }

    @Override
    public boolean seDeconnecter(Object o) {
        return false;
    }

    @Override
    public boolean modifierInfoCompte(Object o) {
        return false;
    }

    @Override
    public boolean supprimerCompte(Object o) {
        return false;
    }
}
