package it.docdev.mypiggybank.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;

import it.docdev.mypiggybank.DataModel.Transazione;
import it.docdev.mypiggybank.DataModel.Wallet;
import it.docdev.mypiggybank.WalletConfigs.WalletConfiguration;
import it.docdev.mypiggybank.WalletConfigs.WalletDefaultConfig;
import it.docdev.mypiggybank.WalletConfigs.WalletSecWithFingerConfig;
import it.docdev.mypiggybank.WalletConfigs.WalletSecWithPassConfig;

public class DBMS extends SQLiteOpenHelper {

    private static final String TAG = "DBMS";

    private static final String DATABASE_NAME = "mypiggybank.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase db;
    private static DBMS instance;
    private String USER;
    private String TIPO;
    private String TRANSAZIONE;
    private String WALLET;


    private DBMS(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        USER = "CREATE TABLE utente( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "   username VARCHAR(255) NOT NULL UNIQUE,\n" +
                "   password VARCHAR(255) NOT NULL DEFAULT 'fingerprint',\n" +
                "   welcome INTEGER DEFAULT 1," +
                "   avatar INTEGER DEFAULT 0" +
                ");\n";

        TIPO = "CREATE TABLE tipo_transazione( \n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "nome VARCHAR(127) NOT NULL\n" +
                ");\n";

        WALLET =
                "CREATE TABLE wallet(\n" +
                        " \tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  \tnome VARCHAR(255) NOT NULL UNIQUE,\n" +
                        "  \tvaluta INTEGER NOT NULL,\n" +
                        "  \tsaldo_iniziale FLOAT NOT NULL DEFAULT 0.00,\n" +
                        "  \tsaldo_corrente FLOAT NOT NULL DEFAULT 0.00,\n" +
                        "  \tproprietario INTEGER NOT NULL,\n" +
                        "    avatar INTEGER NOT NULL,\n" +
                        "  \tdata_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "    pass_sec INTEGER NOT NULL DEFAULT 0,\n" +
                        "    finger_sec INTEGER NOT NULL DEFAULT 0,\n" +
                        "    sec_enabled INTEGER NOT NULL DEFAULT 0,\n" +
                        "  \tFOREIGN KEY (proprietario) REFERENCES utente(uid)\n" +
                        "  \tON UPDATE CASCADE\n" +
                        "  \tON DELETE CASCADE \n" +
                        " );\n";

        TRANSAZIONE = " CREATE TABLE transazione(\n" +
                "  \tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  \tdata TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "  \ttipo INTEGER NOT NULL,\n" +
                "  \timporto FLOAT NOT NULL,\n" +
                "  \tcausale VARCHAR(500) NOT NULL,\n" +
                "  \tid_utente INTEGER NOT NULL,\n" +
                "  \tid_wallet INTEGER NOT NULL,\n" +
                "  \tFOREIGN KEY (tipo) REFERENCES tipo_transazione(id)\n" +
                "  \tON UPDATE CASCADE\n" +
                "  \tON DELETE CASCADE,\n" +
                "  \tFOREIGN KEY (id_utente) REFERENCES utente(id)\n" +
                "  \tON UPDATE CASCADE\n" +
                "  \tON DELETE CASCADE,\n" +
                "  \tFOREIGN KEY (id_wallet) REFERENCES wallet(id)\n" +
                "  \tON UPDATE CASCADE\n" +
                "  \tON DELETE CASCADE \n" +
                ");";
    }

    public static synchronized DBMS getInstance(Context context) {
        if (instance == null) {
            instance = new DBMS(context);
            db = instance.getWritableDatabase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            String DATABASE_INIT = "" +
                    "INSERT INTO tipo_transazione(nome)\n" +
                    "VALUES (\'ACCREDITO\'),(\'ADDEBITO\') ; ";
            db.execSQL(TIPO);
            db.execSQL(USER);
            db.execSQL(WALLET);
            db.execSQL(TRANSAZIONE);
            SQLiteStatement statement = db.compileStatement(DATABASE_INIT);
            statement.executeInsert();
            db.setTransactionSuccessful();
        } catch (SQLException sqle) {
            Log.e(TAG, sqle.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        if (instance != null)
            db.close();
    }

    public synchronized long addUser(String username, String password) {
        String insert = "INSERT INTO utente(username,password)" +
                "VALUES (? , ?) ";
        SQLiteStatement statement = db.compileStatement(insert);
        statement.bindString(1, username);
        statement.bindString(2, password);
        long res = statement.executeInsert();

        if (res > 0) {
            String query = "SELECT id FROM utente " +
                    "WHERE username = ?";

            String[] args = {username};

            Cursor result = db.rawQuery(query, args);
            if (result != null && result.moveToFirst()) {
                System.out.println(result);
                System.out.println(result.getInt(0));
                return result.getInt(0);
            } else return -2;

        } else return -1;

    }

    public synchronized void updateWalletName(long wallet_id, String nome) {
        String update = "UPDATE wallet SET nome = " + nome + " WHERE id =  " + wallet_id + ";";
        db.execSQL(update);
    }


    public synchronized void updateWalletConfig(long wallet_id, int object, boolean newValue) {
        String x = "";
        switch (object) {
            case 1:
                x = " sec_enabled ";
                break;
            case 2:
                x = " pass_sec ";
                break;
            case 3:
                x = " finger_sec ";
                break;
        }
        int y = newValue ? 1 : 0;
        String update = "UPDATE wallet SET " + x + "= " + y + "  WHERE id =  " + wallet_id + ";";
        db.execSQL(update);
    }

    public synchronized WalletConfiguration getWalletSettings(long wallet_id) {
        String query = "SELECT sec_enabled,pass_sec,finger_sec FROM wallet " +
                "WHERE id = " + wallet_id;
        Cursor result = db.rawQuery(query, null);
        if (result != null && result.moveToFirst()) {
            int x = result.getInt(0);
            int y = result.getInt(1);
            int z = result.getInt(2);
            result.close();
            if (x == 1 && y == 1) return new WalletSecWithPassConfig();
            if (x == 1 && z == 1) return new WalletSecWithFingerConfig();
        }

        return new WalletDefaultConfig();
    }

    public synchronized void addUser(String username) {
        String insert = "INSERT INTO utente(username)" +
                "VALUES (?) ";
        SQLiteStatement statement = db.compileStatement(insert);
        statement.bindString(1, username);

        statement.executeInsert();

    }

    public long getFirstUser() {
        String query = "SELECT id FROM utente " +
                "LIMIT 1";

        Cursor result = db.rawQuery(query, null);
        if (result != null && result.moveToFirst()) {
            int x = result.getInt(0);
            result.close();
            return x;
        } else return -1;
    }

    public ArrayList<Transazione> getWalletTransactionList(long wid) {
        ArrayList<Transazione> res = new ArrayList<>();
        String query = "SELECT data,tipo,importo,causale FROM transazione " +
                "WHERE id_wallet = ?";
        String[] args = {Long.toString(wid)};
        Cursor result = db.rawQuery(query, args);
        result.moveToFirst();
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Transazione t = new Transazione(Timestamp.valueOf(result.getString(0)), result.getInt(1),
                    result.getDouble(2), result.getString(3));

            res.add(t);
        }
        result.close();
        return res;
    }

    public long getUserId(String username) {
        String query = "SELECT id FROM utente " +
                "WHERE username = ?";

        String[] args = {username};
        Cursor result = db.rawQuery(query, args);
        result.moveToFirst();
        int x = result.getInt(0);
        result.close();
        return x;

    }


    public synchronized void addWallet(String nome, int valuta, Double saldo, long proprietario, int avatar) throws DatabaseException {
        String insert = "INSERT INTO wallet(nome,saldo_corrente,proprietario,valuta,avatar,saldo_iniziale)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(insert);

        statement.bindString(1, nome);
        statement.bindDouble(2, saldo);
        statement.bindLong(3, proprietario);
        statement.bindLong(4, valuta);
        statement.bindLong(5, avatar);
        statement.bindDouble(6, saldo);
        long res = statement.executeInsert();

        if (res < 0) throw new DatabaseException("Errore durante l'operazione di inserimento");
    }

    public ArrayList<Wallet> getUserWallets(long uid) {
        ArrayList<Wallet> w = new ArrayList<>();
        String query = "SELECT id,nome,valuta,saldo_iniziale,data_creazione,avatar,saldo_corrente FROM wallet " +
                "WHERE proprietario = ?";
        String[] args = {Long.toString(uid)};

        Cursor res = db.rawQuery(query, args);
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            Wallet x = new Wallet(res.getLong(0), res.getString(1), res.getInt(2),
                    res.getDouble(3), Timestamp.valueOf(res.getString(4)),
                    res.getInt(5), res.getDouble(6));
            w.add(x);
        }
        res.close();
        return w;
    }

    public synchronized boolean checkAuth(String username, String password) {
        String query = "SELECT password FROM utente WHERE username = ?";
        String[] args = {username};
        Cursor res = db.rawQuery(query, args);
        res.moveToFirst();
        String x = res.getString(0);
        res.close();
        return x.equals(Utilities.hash(password, "SHA-512"));

    }

    public synchronized void addTransaction(long tipo, long walletid, long userid, double importo, String causale, Timestamp tempo) throws InsufficientBalanceException {
        double saldo;

        if (tipo == Utilities.ADDEBITO) {
            String query = "SELECT saldo_corrente FROM wallet " +
                    "WHERE id = ?";

            String[] args = {Long.toString(walletid)};

            Cursor res = db.rawQuery(query, args);
            res.moveToFirst();
            saldo = res.getDouble(0);
            res.close();

            if (saldo < importo)
                throw new InsufficientBalanceException("Saldo insufficiente per coprire la spesa");

        }

        String insert = "INSERT INTO transazione(tipo,importo,causale,id_utente,id_wallet,data)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(insert);

        statement.bindLong(1, tipo);
        statement.bindDouble(2, importo);
        statement.bindString(3, causale);
        statement.bindLong(4, userid);
        statement.bindLong(5, walletid);
        statement.bindString(6, tempo.toString());
        statement.executeInsert();

        String update = "";
        if (tipo == Utilities.ADDEBITO) {
            update = "UPDATE wallet SET saldo_corrente = saldo_corrente - " + importo + "  WHERE id =  " + walletid + ";";
        }
        if (tipo == Utilities.ACCREDITO) {
            update = "UPDATE wallet SET saldo_corrente = saldo_corrente + " + importo + "  WHERE id =  " + walletid + ";";
        }

        db.execSQL(update);

    }
}

