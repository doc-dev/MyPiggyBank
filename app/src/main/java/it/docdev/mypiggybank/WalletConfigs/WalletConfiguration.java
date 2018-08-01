package it.docdev.mypiggybank.WalletConfigs;

import android.preference.CheckBoxPreference;
import android.preference.SwitchPreference;

public interface WalletConfiguration {
    public void applySettings(SwitchPreference security, CheckBoxPreference pass, CheckBoxPreference finger);
}
