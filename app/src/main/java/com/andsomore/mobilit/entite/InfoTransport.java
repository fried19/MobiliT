package com.andsomore.mobilit.entite;

public class InfoTransport {
    private int NombreTransport;
    private String Jour;
    private int IdClient;
    private int IdTransportCommun;

    public InfoTransport(){}

    public InfoTransport(int nombreTransport, String jour, int idClient, int idTransportCommun) {
        NombreTransport = nombreTransport;
        Jour = jour;
        IdClient = idClient;
        IdTransportCommun = idTransportCommun;
    }

    public int getNombreTransport() {
        return NombreTransport;
    }

    public String getJour() {
        return Jour;
    }

    public int getIdClient() {
        return IdClient;
    }

    public int getIdTransportCommun() {
        return IdTransportCommun;
    }
}
