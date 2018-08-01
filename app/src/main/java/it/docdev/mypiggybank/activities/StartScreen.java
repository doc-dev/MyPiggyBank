package it.docdev.mypiggybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBMS m = DBMS.getInstance(getApplicationContext());
        long l = m.getFirstUser();

        if (l != -1) {
            Intent i = new Intent(this, UserActivity.class);
            if (l > 0) Globals.getInstance().setUserid(l);
            i.putExtra("user", l);
            startActivity(i);
            finish();
        }

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Inizia il tour operativo
                Intent intent = new Intent(v.getContext(), Tour1.class);
                startActivity(intent);
            }
        });
    }
}
