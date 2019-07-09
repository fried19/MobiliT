package com.andsomore.mobilit.idao;

import java.util.List;

public interface IClient<T> {
    public boolean acheterBillet(T t);
    public boolean annulerAchatBillet(T t);
    public boolean crediterCompte(T t);
    public boolean debiterCompte(T t);
    public List<T> listClient();

}
