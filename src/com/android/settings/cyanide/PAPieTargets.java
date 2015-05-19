/*
 * Copyright (C) 2010-2014 ParanoidAndroid Project
 * Copyright (C) 2015 Fusion & CyanideL Project (PIE2.0 - Ported & modified)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanide;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PAPieTargets extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PA_PIE_MENU = "pa_pie_menu";
    private static final String PA_PIE_LASTAPP = "pa_pie_lastapp";
    private static final String PA_PIE_KILLTASK = "pa_pie_killtask";
    private static final String PA_PIE_ACTNOTIF = "pa_pie_actnotif";
    private static final String PA_PIE_SETTINGS_PANEL = "pa_pie_settings_panel";
    private static final String PA_PIE_POWER = "pa_pie_power";
    private static final String PA_PIE_TORCH = "pa_pie_torch";
    private static final String PA_PIE_SCREENSHOT = "pa_pie_screenshot";
    private static final String PA_PIE_POWER_MENU = "pa_pie_power_menu";
    private static final String PA_PIE_EXPANDED_DESKTOP = "pa_pie_expanded_desktop";
    private static final String PA_PIE_THEME_SWITCH = "pa_pie_theme_switch";

    private SwitchPreference mPieMenu;
    private SwitchPreference mPieLastApp;
    private SwitchPreference mPieKillTask;
    private SwitchPreference mPieActNotif;
    private SwitchPreference mPieQsPanel;
    private SwitchPreference mPiePower;
    private SwitchPreference mPieTorch;
    private SwitchPreference mPieScreenshot;
    private SwitchPreference mPiePowerMenu;
    private SwitchPreference mPieExpandedDesktop;
    private SwitchPreference mPieThemeSwitch;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pa_pie_targets);

        PreferenceScreen prefSet = getPreferenceScreen();

        Context context = getActivity();
        mResolver = context.getContentResolver();

        mPieMenu = (SwitchPreference) prefSet.findPreference(PA_PIE_MENU);
        mPieMenu.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_MENU, 1) != 0);

        mPieLastApp = (SwitchPreference) prefSet.findPreference(PA_PIE_LASTAPP);
        mPieLastApp.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_LAST_APP, 0) != 0);

        mPieKillTask = (SwitchPreference) prefSet.findPreference(PA_PIE_KILLTASK);
        mPieKillTask.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_KILL_TASK, 0) != 0);

        mPieActNotif = (SwitchPreference) prefSet.findPreference(PA_PIE_ACTNOTIF);
        mPieActNotif.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_ACT_NOTIF, 0) != 0);

        mPieQsPanel = (SwitchPreference) prefSet.findPreference(PA_PIE_SETTINGS_PANEL);
        mPieQsPanel.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SETTINGS_PANEL, 0) != 0);
        
        mPiePower = (SwitchPreference) prefSet.findPreference(PA_PIE_POWER);
        mPiePower.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_POWER, 0) != 0);

        mPieTorch = (SwitchPreference) prefSet.findPreference(PA_PIE_TORCH);
        mPieTorch.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_TORCH, 0) != 0);

        mPieScreenshot = (SwitchPreference) prefSet.findPreference(PA_PIE_SCREENSHOT);
        mPieScreenshot.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SCREENSHOT, 0) != 0);
                
        mPiePowerMenu = (SwitchPreference) prefSet.findPreference(PA_PIE_POWER_MENU);
        mPiePowerMenu.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_POWER_MENU, 0) != 0);
                
        mPieExpandedDesktop = (SwitchPreference) prefSet.findPreference(PA_PIE_EXPANDED_DESKTOP);
        mPieExpandedDesktop.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_EXPANDED_DESKTOP, 0) != 0);
                
        mPieThemeSwitch = (SwitchPreference) prefSet.findPreference(PA_PIE_THEME_SWITCH);
        mPieThemeSwitch.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_THEME_SWITCH, 0) != 0);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mPieMenu) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_MENU,
                    mPieMenu.isChecked() ? 1 : 0);
        } else if (preference == mPieLastApp) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_LAST_APP,
                    mPieLastApp.isChecked() ? 1 : 0);
        } else if (preference == mPieKillTask) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_KILL_TASK,
                    mPieKillTask.isChecked() ? 1 : 0);
        } else if (preference == mPieActNotif) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_ACT_NOTIF,
                    mPieActNotif.isChecked() ? 1 : 0);
        } else if (preference == mPieQsPanel) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SETTINGS_PANEL,
                    mPieQsPanel.isChecked() ? 1 : 0);
        } else if (preference == mPiePower) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_POWER,
                    mPiePower.isChecked() ? 1 : 0);
        } else if (preference == mPieTorch) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_TORCH,
                    mPieTorch.isChecked() ? 1 : 0);
        } else if (preference == mPieScreenshot) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SCREENSHOT,
                    mPieScreenshot.isChecked() ? 1 : 0);
        } else if (preference == mPiePowerMenu) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_POWER_MENU,
                    mPiePowerMenu.isChecked() ? 1 : 0);
        } else if (preference == mPieExpandedDesktop) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_EXPANDED_DESKTOP,
                    mPieExpandedDesktop.isChecked() ? 1 : 0);
        } else if (preference == mPieThemeSwitch) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_THEME_SWITCH,
                    mPieThemeSwitch.isChecked() ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
