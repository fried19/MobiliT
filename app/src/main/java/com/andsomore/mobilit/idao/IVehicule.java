package com.andsomore.mobilit.idao;

import java.util.List;

public interface IVehicule<T> {
    public boolean geolocaliser(T t);
    public boolean affecterVehiculeVoyage(T t);
    public List<T>listVehiculer();
}
