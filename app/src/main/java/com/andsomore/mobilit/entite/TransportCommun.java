package com.andsomore.mobilit.entite;

import java.util.Date;

public class TransportCommun {

    private static int Num;
    private Date DateTransport;
    public TransportCommun(){}
    public TransportCommun(Date dateTransport) {
        Num++;
        DateTransport = dateTransport;
    }

    public static int getNum() {
        return Num;
    }

    public Date getDateTransport() {
        return DateTransport;
    }
}
