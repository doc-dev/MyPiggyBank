package it.docdev.mypiggybank.WalletConfigs;

import android.preference.CheckBoxPreference;
import android.preference.SwitchPreference;

public class WalletDefaultConfig implements WalletConfiguration {

    @Override
    public void applySettings(SwitchPreference security, CheckBoxPreference pass, CheckBoxPreference finger) {
        security.setChecked(false);
        pass.setEnabled(false);
        pass.setChecked(false);
        finger.setEnabled(false);
        finger.setChecked(false);
    }
}
