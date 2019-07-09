package com.andsomore.mobilit.dao;

import com.andsomore.mobilit.idao.IVehicule;

import java.util.List;

public class TraitementVehicule implements IVehicule {
    @Override
    public boolean geolocaliser(Object o) {
        return false;
    }

    @Override
    public boolean affecterVehiculeVoyage(Object o) {
        return false;
    }

    @Override
    public List listVehiculer() {
        return null;
    }
}
