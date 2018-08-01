package it.docdev.mypiggybank.DataModel;

import java.sql.Timestamp;

public class Transazione {
    private int tid;
    private Timestamp data;
    private int tipo;
    private double importo;
    private String causale;

    public Transazione(int tid, Timestamp data, int tipo, double importo, String causale) {
        this.tid = tid;
        this.data = data;
        this.tipo = tipo;
        this.importo = importo;
        this.causale = causale;
    }

    public Transazione(Timestamp data, int tipo, double importo, String causale) {
        this.data = data;
        this.tipo = tipo;
        this.importo = importo;
        this.causale = causale;
    }

    public Transazione(int tipo, double importo, String causale) {
        this.tipo = tipo;
        this.importo = importo;
        this.causale = causale;
    }


    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public String getCausale() {
        return causale;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }
}
