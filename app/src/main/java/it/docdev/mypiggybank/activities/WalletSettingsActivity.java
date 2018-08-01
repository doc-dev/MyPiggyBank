package it.docdev.mypiggybank.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import it.docdev.mypiggybank.R;
import it.docdev.mypiggybank.WalletConfigs.WalletConfiguration;
import it.docdev.mypiggybank.utils.DBMS;
import it.docdev.mypiggybank.utils.Globals;

public class WalletSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        EditTextPreference edp;
        SwitchPreference security;
        CheckBoxPreference pass;
        CheckBoxPreference finger;
        WalletConfiguration configuration;

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            pass = (CheckBoxPreference) findPreference("password_protect");
            finger = (CheckBoxPreference) findPreference("fingerprint_protect");
            edp = (EditTextPreference) findPreference("nomeWallet");
            edp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //rinomino wallet
                    DBMS.getInstance(getContext()).updateWalletName(Globals.getInstance().getCurrent_wallet().getWid(),
                            newValue.toString());
                    Globals.getInstance().getCurrent_wallet().setNome(newValue.toString());
                    return false;
                }
            });
            security = (SwitchPreference) findPreference("enable_security");

            configuration = DBMS.getInstance(getContext()).getWalletSettings(Globals.getInstance().getCurrent_wallet().getWid());
            configuration.applySettings(security, pass, finger);

            if (security.isChecked()) {
                pass.setEnabled(true);
                finger.setEnabled(true);
            } else {
                pass.setEnabled(false);
                finger.setEnabled(false);
            }
            security.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //1
                    SwitchPreference x = (SwitchPreference) preference;
                    System.out.println("CAMBIO SWITCH " + newValue.toString());
                    if (newValue.toString().equals("true")) {
                        DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                1,
                                true);
                        x.setChecked(true);
                        pass.setEnabled(true);
                        finger.setEnabled(true);
                        pass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                //2
                                System.out.println("CAMBIO PASS " + newValue.toString());
                                if (newValue.toString().equals("true")) {
                                    DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                            2,
                                            true);
                                    finger.setChecked(false);
                                    pass.setChecked(true);
                                } else {
                                    DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                            2,
                                            false);
                                    finger.setChecked(false);
                                    pass.setChecked(false);
                                }

                                return false;
                            }
                        });
                        finger.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                //3
                                System.out.println("CAMBIO FINGER " + newValue.toString());
                                if (newValue.toString().equals("true")) {
                                    DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                            3,
                                            true);
                                    DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                            2,
                                            false);
                                    pass.setChecked(false);
                                    finger.setChecked(true);
                                } else {
                                    DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                            3,
                                            false);
                                    pass.setChecked(false);
                                    finger.setChecked(false);
                                }
                                return false;
                            }
                        });
                    } else {
                        DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                1,
                                false);
                        DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                2,
                                false);
                        DBMS.getInstance(getContext()).updateWalletConfig(Globals.getInstance().getCurrent_wallet().getWid(),
                                3,
                                false);
                        x.setChecked(false);
                        pass.setEnabled(false);
                        finger.setEnabled(false);
                    }
                    return false;
                }
            });

        }
    }
}
