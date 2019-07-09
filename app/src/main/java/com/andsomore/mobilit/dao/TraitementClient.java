package com.andsomore.mobilit.dao;

import com.andsomore.mobilit.idao.IClient;

import java.util.List;

public class TraitementClient implements IClient {
    @Override
    public boolean acheterBillet(Object o) {
        return false;
    }

    @Override
    public boolean annulerAchatBillet(Object o) {
        return false;
    }

    @Override
    public boolean crediterCompte(Object o) {
        return false;
    }

    @Override
    public boolean debiterCompte(Object o) {
        return false;
    }

    @Override
    public List listClient() {
        return null;
    }
}
