package it.docdev.mypiggybank.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.Timestamp;

import it.docdev.mypiggybank.DataModel.Transazione;
import it.docdev.mypiggybank.DataModel.Wallet;
import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.WalletConfigs.WalletConfiguration;
import it.docdev.mypiggybank.WalletConfigs.WalletSecWithFingerConfig;
import it.docdev.mypiggybank.WalletConfigs.WalletSecWithPassConfig;
import it.docdev.mypiggybank.adapter.RVWalletAdapterTransaction;
import it.docdev.mypiggybank.auth.FingerprintAuthActivity;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;
import it.docdev.mypiggybank.utils.Utilities;

public class WalletDetailedActivity extends AppCompatActivity {

    EditText tr1;
    RadioButton b1;
    RadioButton b2;
    EditText cause;
    RecyclerView r;
    private String[] countryNames = {"SAR", "ARS", "AUD", "BTC", "CAD", "ETH", "EUR", "JPY", "MXN", "XMR", "NOK", "RUB"
            , "CHF", "GBP", "USD"};
    private int flags[] = {R.drawable.arabia, R.drawable.argentina, R.drawable.australia, R.drawable.bitcoin,
            R.drawable.canada, R.drawable.ethereum, R.drawable.europe, R.drawable.japan
            , R.drawable.mexico, R.drawable.monero, R.drawable.norway, R.drawable.russia, R.drawable.switzerland
            , R.drawable.uk, R.drawable.usa};
    private long wid;
    private Wallet w;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private RVWalletAdapterTransaction a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detailed);
        t1 = findViewById(R.id.wname);
        t2 = findViewById(R.id.saldo);
        t3 = findViewById(R.id.valuta);
        w = Globals.getInstance().getCurrent_wallet();
        w.setTransazioni(DBMS.getInstance(getApplicationContext()).getWalletTransactionList(w.getWid()));
        WalletConfiguration wc = DBMS.getInstance(getApplicationContext()).getWalletSettings(w.getWid());
        boolean auth_status;
        if (wc instanceof WalletSecWithPassConfig) {
            auth_status = getIntent().getBooleanExtra("auth", false);
            if (!auth_status) {
                Intent i = new Intent(getApplicationContext(), WalletAccessPassActivity.class);
                startActivity(i);
                finish();
            }
        }
        if (wc instanceof WalletSecWithFingerConfig) {
            auth_status = getIntent().getBooleanExtra("auth", false);
            if (!auth_status) {
                Intent i = new Intent(getApplicationContext(), FingerprintAuthActivity.class);
                i.putExtra("level2", true);
                startActivity(i);
                finish();
            }
        }
        t1.setText(w.getNome());
        wid = w.getWid();
        t2.setText(String.format("%.2f", w.getSaldo_corrente()).replace('.', ','));
        t3.setText(countryNames[w.getValuta()]);
        ImageView i1 = findViewById(R.id.curImgD);
        i1.setBackgroundResource(flags[w.getValuta()]);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Dettaglio Wallet");
        toolbar.inflateMenu(R.menu.menu_wallet_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_settings) {
                    Intent i = new Intent(getApplicationContext(), WalletSettingsActivity.class);
                    startActivity(i);
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(WalletDetailedActivity.this, R.style.myDialog));
                View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.new_transaction, null);
                builder.setView(content);
                builder.setTitle("Nuova Transazione");
                tr1 = content.findViewById(R.id.tamount);
                b1 = content.findViewById(R.id.rbadd);
                b2 = content.findViewById(R.id.rbminus);
                cause = content.findViewById(R.id.tcausale);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        operation();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog a = builder.create();
                a.show();
            }
        });

        r = findViewById(R.id.rv2);
        r.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(WalletDetailedActivity.this);
        r.setLayoutManager(llm);
        a = new RVWalletAdapterTransaction(w.getTransazioni());
        r.setAdapter(a);


    }

    private void operation() {
        int tipo = -1;
        if (getB1().isChecked()) tipo = 0;
        if (getB2().isChecked()) tipo = 1;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transazione t = new Transazione(timestamp, tipo, Double.valueOf(getTr1()), getCause());
        DBMS.getInstance(getApplicationContext()).addTransaction(tipo, wid, Globals.getInstance().getUserid(), t.getImporto(), t.getCausale(), t.getData());
        if (t.getTipo() == Utilities.ADDEBITO) {
            w.setSaldo_corrente(w.getSaldo_corrente() - t.getImporto());
        } else {
            w.setSaldo_corrente(w.getSaldo_corrente() + t.getImporto());
        }
        w.addTransazione(t);
        t2.setText(String.format("%.2f", w.getSaldo_corrente()).replace('.', ','));
        a = new RVWalletAdapterTransaction(w.getTransazioni());
        r.setAdapter(a);
    }

    private String getTr1() {
        return this.tr1.getText().toString().trim();
    }

    private RadioButton getB1() {
        return this.b1;
    }

    private RadioButton getB2() {
        return this.b2;
    }

    private Wallet getW() {
        return this.w;
    }

    private String getCause() {
        return this.cause.getText().toString().trim();
    }

}


