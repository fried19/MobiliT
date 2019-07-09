package com.andsomore.mobilit.idao;

import java.util.List;

public interface IChauffeur<T> {
    public boolean programmerChauffeur(T t);
    public List<T>lisChauffeur();
    public boolean demanderConge(T t);
    public boolean recevoirConge(T t);

}
