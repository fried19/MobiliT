package com.andsomore.mobilit.idao;

import java.util.List;

public interface IClient<T> {
    public void acheterBillet(T t,IResult result);
    public boolean annulerAchatBillet(T t);
    public boolean crediterCompte(T t);
    public boolean debiterCompte(T t);

    public List<T> listClient();


}
