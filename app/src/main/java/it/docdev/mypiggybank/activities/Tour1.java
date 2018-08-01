package it.docdev.mypiggybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.auth.FingerprintAuthActivity;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;
import it.docdev.mypiggybank.utils.Utilities;


public class Tour1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour1);

        this.setTitle("Configurazione Account - Parte 1");

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final Switch s = findViewById(R.id.switch1);
        Button back = findViewById(R.id.button2);
        Button forward = findViewById(R.id.button3);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Torno indietro
                Intent intent = new Intent(v.getContext(), StartScreen.class);
                startActivity(intent);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //vado avanti
                if (s.isChecked()) {
                    String usr = username.getText().toString();
                    if (usr.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Attenzione! Inserire un username", Toast.LENGTH_LONG).show();
                    } else {
                        Globals.getInstance().setUsername(usr);
                        Intent intent = new Intent(getApplicationContext(), FingerprintAuthActivity.class);
                        intent.putExtra("fingerprint_flag", true);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    if (password.getText().toString().isEmpty() || username.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Attenzione! Inserire tutti i campi richiesti", Toast.LENGTH_LONG).show();
                    } else {
                        String pwd = Utilities.hash(password.getText().toString().trim(), "SHA-512");
                        String usr = username.getText().toString().trim();
                        long uid = DBMS.getInstance(getApplicationContext()).addUser(usr, pwd);
                        Globals.getInstance().setUserid(uid);
                        if (uid > 0) {
                            Intent intent = new Intent(getApplicationContext(), Tour2.class);
                            intent.putExtra("fingerprint_flag", false);
                            Globals.getInstance().setUsername(usr);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            }
        });

    }


}
