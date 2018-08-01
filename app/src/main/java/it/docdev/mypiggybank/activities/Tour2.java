package it.docdev.mypiggybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.adapter.CustomAdapter;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;
import it.docdev.mypiggybank.utils.Utilities;

public class Tour2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText wname;
    private Spinner dropdown;
    private EditText amount;
    private String[] countryNames = {"SAR", "ARS", "AUD", "BTC", "CAD", "ETH", "EUR", "JPY", "MXN", "XMR", "NOK", "RUB"
            , "CHF", "GBP", "USD"};
    private int flags[] = {R.drawable.arabia, R.drawable.argentina, R.drawable.australia, R.drawable.bitcoin,
            R.drawable.canada, R.drawable.ethereum, R.drawable.europe, R.drawable.japan
            , R.drawable.mexico, R.drawable.monero, R.drawable.norway, R.drawable.russia, R.drawable.switzerland
            , R.drawable.uk, R.drawable.usa};
    private boolean new_wallet;
    private String usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour2);
        new_wallet = getIntent().getBooleanExtra("new_wallet", false);
        if (new_wallet) {
            setTitle("Aggiungi nuovo wallet");
        } else {
            setTitle("Configurazione Account - Parte 2");
            usr = Globals.getInstance().getUsername();
            boolean flag = getIntent().getExtras().getBoolean("fingerprint_flag");
            if (flag) {
                try {
                    if (appKeyExists(Utilities.KEY_NAME)) {
                        DBMS.getInstance(getApplicationContext()).addUser(usr);
                    }
                } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException | IOException | CertificateException e) {
                    e.printStackTrace();
                }
            }

        }
        wname = findViewById(R.id.walletName);
        dropdown = findViewById(R.id.currencyChoice);
        amount = findViewById(R.id.startAmount);
        dropdown.setOnItemSelectedListener(this);

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), flags, countryNames);
        dropdown.setAdapter(adapter);

        Button succ;
        Button prec;
        if (new_wallet) {
            prec = findViewById(R.id.backto1);
            prec.setVisibility(View.INVISIBLE);
            succ = findViewById(R.id.phase3);
            succ.setText("Procedi");
            TextView tmp = findViewById(R.id.textView8);
            tmp.setText("Aggiungi un nuovo wallet");
        } else {
            prec = findViewById(R.id.backto1);
            succ = findViewById(R.id.phase3);
            prec.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Tour1.class);
                    i.putExtra("user", usr);
                    startActivity(i);
                    finish();
                }
            });
        }

        succ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long user;
                if (new_wallet) user = Globals.getInstance().getUserid();
                else user = DBMS.getInstance(getApplicationContext()).getUserId(usr);
                String wallet_name = wname.getText().toString();
                int choice = dropdown.getSelectedItemPosition();
                double number = Double.valueOf(amount.getText().toString());
                if (wallet_name.isEmpty() || choice < 0 || number < 0.0) {
                    Toast.makeText(getApplicationContext(), "Attenzione! Inserisci tutti i campi richiesti", Toast.LENGTH_LONG).show();
                } else {
                    DBMS.getInstance(getApplicationContext()).addWallet(wallet_name.trim(), choice, number, user, R.drawable.wallet);
                    Intent i = new Intent(getApplicationContext(), UserActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    private boolean appKeyExists(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException, IOException, CertificateException {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                .getEntry(alias, null);

        return secretKeyEntry != null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
