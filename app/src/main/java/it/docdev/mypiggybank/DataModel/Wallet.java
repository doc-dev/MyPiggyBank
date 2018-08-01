package it.docdev.mypiggybank.DataModel;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Wallet {
    private long wid;
    private String nome;
    private int valuta;
    private double saldo_corrente;
    private double saldo_iniziale;
    private long id_proprietario;
    private Timestamp data_creazione;
    private int drawable_immagine;
    private ArrayList<Transazione> transazioni = new ArrayList<>();


    public Wallet(long wid, String nome, int valuta, double saldo, Timestamp data_creazione, int drawable_immagine) {
        this.wid = wid;
        this.nome = nome;
        this.valuta = valuta;
        this.saldo_iniziale = saldo;
        this.saldo_corrente = saldo;
        this.data_creazione = data_creazione;
        this.drawable_immagine = drawable_immagine;
    }

    public Wallet(long wid, String nome, int valuta, double saldo_iniziale, Timestamp data_creazione, int drawable_immagine, double saldo_corrente) {
        this.wid = wid;
        this.nome = nome;
        this.valuta = valuta;
        this.saldo_iniziale = saldo_iniziale;
        this.saldo_corrente = saldo_corrente;
        this.data_creazione = data_creazione;
        this.drawable_immagine = drawable_immagine;
    }

    public int getDrawable_immagine() {
        return drawable_immagine;
    }

    public void setDrawable_immagine(int drawable_immagine) {
        this.drawable_immagine = drawable_immagine;
    }

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getValuta() {
        return valuta;
    }

    public void setValuta(int valuta) {
        this.valuta = valuta;
    }

    public double getSaldo_corrente() {
        return saldo_corrente;
    }

    public void setSaldo_corrente(double saldo_corrente) {
        this.saldo_corrente = saldo_corrente;
    }

    public double getSaldo_iniziale() {
        return saldo_iniziale;
    }

    public void setSaldo_iniziale(double saldo_iniziale) {
        this.saldo_iniziale = saldo_iniziale;
    }

    public long getId_proprietario() {
        return id_proprietario;
    }

    public void setId_proprietario(long id_proprietario) {
        this.id_proprietario = id_proprietario;
    }

    public Timestamp getData_creazione() {
        return data_creazione;
    }

    public void setData_creazione(Timestamp data_creazione) {
        this.data_creazione = data_creazione;
    }

    public void addTransazione(Transazione t) {
        this.transazioni.add(t);
    }

    public void removeTransazione(Transazione t) {
        this.transazioni.remove(t);
    }

    public ArrayList<Transazione> getTransazioni() {
        return transazioni;
    }

    public void setTransazioni(ArrayList<Transazione> transazioni) {
        this.transazioni = transazioni;
        if (transazioni.size() == 0) {
            transazioni.add(0, new Transazione(this.data_creazione, 3, this.saldo_iniziale, "Saldo Iniziale"));
        } else {
            String t = transazioni.get(0).getCausale();
            if (!t.equals("Saldo Iniziale")) {
                transazioni.add(0, new Transazione(this.data_creazione, 3, this.saldo_iniziale, "Saldo Iniziale"));
            }
        }
    }
}
