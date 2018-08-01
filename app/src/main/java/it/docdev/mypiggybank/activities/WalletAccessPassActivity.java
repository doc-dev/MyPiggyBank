package it.docdev.mypiggybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;

/**
 * A login screen that offers login via email/password.
 */
public class WalletAccessPassActivity extends AppCompatActivity {


    // UI references.
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_access_pass);
        // Set up the login form.
        username = findViewById(R.id.usernameAuth);
        password = findViewById(R.id.passwordAuth);
        username.setText(Globals.getInstance().getUsername());
        Button confirm = findViewById(R.id.email_sign_in_button);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (DBMS.getInstance(getApplicationContext()).checkAuth(usr, pass)) {

                    Intent i = new Intent(getApplicationContext(), WalletDetailedActivity.class);
                    i.putExtra("auth", true);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}

