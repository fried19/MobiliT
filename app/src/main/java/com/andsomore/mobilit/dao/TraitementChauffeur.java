package com.andsomore.mobilit.dao;

import com.andsomore.mobilit.idao.IChauffeur;

import java.util.List;

public class TraitementChauffeur implements IChauffeur {
    @Override
    public boolean programmerChauffeur(Object o) {
        return false;
    }

    @Override
    public List lisChauffeur() {
        return null;
    }

    @Override
    public boolean demanderConge(Object o) {
        return false;
    }

    @Override
    public boolean recevoirConge(Object o) {
        return false;
    }
}
