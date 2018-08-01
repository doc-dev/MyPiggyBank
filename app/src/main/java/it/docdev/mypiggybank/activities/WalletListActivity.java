package it.docdev.mypiggybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import it.docdev.mypiggybank.DataModel.Wallet;
import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.adapter.RVWalletAdapter;
import it.docdev.mypiggybank.adapter.RecyclerTouchListener;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;

public class WalletListActivity extends AppCompatActivity {

    private ArrayList<Wallet> w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);
        long user_id = Globals.getInstance().getUserid();
        w = DBMS.getInstance(getApplicationContext()).getUserWallets(user_id);
        RecyclerView r = findViewById(R.id.rv);
        r.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(WalletListActivity.this);
        r.setLayoutManager(llm);
        RVWalletAdapter a = new RVWalletAdapter(w);
        r.setAdapter(a);
        r.addOnItemTouchListener(new RecyclerTouchListener(WalletListActivity.this, r, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), WalletDetailedActivity.class);
                Globals.getInstance().setCurrent_wallet(w.get(position));
                startActivity(i);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
