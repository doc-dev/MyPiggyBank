package it.docdev.mypiggybank.utils;

import java.util.ArrayList;

import it.docdev.mypiggybank.DataModel.Transazione;
import it.docdev.mypiggybank.DataModel.Wallet;

public class Globals {

    private static Globals instance = null;
    private long userid;
    private String username;
    private ArrayList<Wallet> list;
    private Wallet current_wallet;
    private Transazione current_transaction;

    private Globals() {
    }

    public synchronized static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    public Wallet getCurrent_wallet() {
        return current_wallet;
    }

    public void setCurrent_wallet(Wallet current_wallet) {
        this.current_wallet = current_wallet;
    }

    public Transazione getCurrent_transaction() {
        return current_transaction;
    }

    public void setCurrent_transaction(Transazione current_transaction) {
        this.current_transaction = current_transaction;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Wallet> getList() {
        return list;
    }

    public void setList(ArrayList<Wallet> list) {
        this.list = list;
    }

    public void close() {
        if (instance != null) {
            instance = null;
        }
    }
}
