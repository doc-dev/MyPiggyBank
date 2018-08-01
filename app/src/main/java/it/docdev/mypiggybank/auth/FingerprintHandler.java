package it.docdev.mypiggybank.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import it.docdev.mypiggybank.activities.Tour2;
import it.docdev.mypiggybank.activities.WalletDetailedActivity;

import static android.Manifest.permission;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private boolean flag;
    private int state;

    // Constructor
    FingerprintHandler(Context mContext, int state, boolean flag) {
        this.context = mContext;
        this.state = state;
        this.flag = flag;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Errore nel sistema di autenticazione, controlla le impostazioni di sicurezza del tuo dispositivo\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update(helpString.toString(), false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Autenticazione tramite impronta fallita.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("OK", true);
    }


    private void update(String e, Boolean success) {
        if (success) {
            if (state == 1) {
                Intent i = new Intent(context, WalletDetailedActivity.class);
                i.putExtra("auth", true);
                context.startActivity(i);
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                return;
            }
            Intent i = new Intent(context, Tour2.class);
            i.putExtra("fingerprint_flag", flag);
            context.startActivity(i);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } else {
            Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
        }
    }
}
