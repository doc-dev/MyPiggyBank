package it.docdev.mypiggybank.WalletConfigs;

import android.preference.CheckBoxPreference;
import android.preference.SwitchPreference;

public class WalletSecWithFingerConfig implements WalletConfiguration {

    @Override
    public void applySettings(SwitchPreference security, CheckBoxPreference pass, CheckBoxPreference finger) {
        security.setChecked(true);
        pass.setEnabled(true);
        pass.setChecked(false);
        finger.setEnabled(true);
        finger.setChecked(true);
    }

}

