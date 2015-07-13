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

    private static final String PA_PIE_AMBIENT_DISPLAY = "pa_pie_ambient_display";
    private static final String PA_PIE_APP_CIRCLE_BAR = "pa_pie_app_circle_bar";
    private static final String PA_PIE_APP_SIDEBAR = "pa_pie_app_sidebar";
    private static final String PA_PIE_EXPANDED_DESKTOP = "pa_pie_expanded_desktop";
    private static final String PA_PIE_FLOATING_WINDOWS = "pa_pie_floating_windows";
    private static final String PA_PIE_GESTURE_ANYWHERE = "pa_pie_gesture_anywhere";
    private static final String PA_PIE_HEADS_UP = "pa_pie_heads_up";
    private static final String PA_PIE_HWKEYS = "pa_pie_hwkeys";
    private static final String PA_PIE_KILLTASK = "pa_pie_killtask";
    private static final String PA_PIE_LASTAPP = "pa_pie_lastapp";
    private static final String PA_PIE_MENU = "pa_pie_menu";
    private static final String PA_PIE_NAVBAR = "pa_pie_navbar";
    private static final String PA_PIE_NOTIFICATIONS = "pa_pie_notifications";
    private static final String PA_PIE_SETTINGS_PANEL = "pa_pie_settings_panel";
    private static final String PA_PIE_POWER_MENU = "pa_pie_power_menu";
    private static final String PA_PIE_RESTARTUI = "pa_pie_restartui";
    private static final String PA_PIE_SCREEN_OFF = "pa_pie_screen_off";
    private static final String PA_PIE_SCREENSHOT = "pa_pie_screenshot";
    private static final String PA_PIE_SLIMPIE = "pa_pie_slimpie";
    private static final String PA_PIE_TORCH = "pa_pie_torch";

    private SwitchPreference mPieAmbientDisplay;
    private SwitchPreference mPieAppcirclesidebar;
    private SwitchPreference mPieAppsidebar;
    private SwitchPreference mPieExpandedDesktop;
    private SwitchPreference mPieFloatingWindows;
    private SwitchPreference mPieGestureAnywhere;
    private SwitchPreference mPieHeadsUp;
    private SwitchPreference mPieHWKeys;
    private SwitchPreference mPieKillTask;
    private SwitchPreference mPieLastApp;
    private SwitchPreference mPieMenu;
    private SwitchPreference mPieNavbar;
    private SwitchPreference mPieNotifications;
    private SwitchPreference mPieQsPanel;
    private SwitchPreference mPiePowerMenu;
    private SwitchPreference mPieRestartui;
    private SwitchPreference mPieScreenOff;
    private SwitchPreference mPieScreenshot;
    private SwitchPreference mPieSlimPie;
    private SwitchPreference mPieTorch;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pa_pie_targets);

        PreferenceScreen prefSet = getPreferenceScreen();

        Context context = getActivity();
        mResolver = context.getContentResolver();

        mPieAmbientDisplay = (SwitchPreference) prefSet.findPreference(PA_PIE_AMBIENT_DISPLAY);
        mPieAmbientDisplay.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_AMBIENT_DISPLAY, 0) != 0);

        mPieAppcirclesidebar = (SwitchPreference) prefSet.findPreference(PA_PIE_APP_CIRCLE_BAR);
        mPieAppcirclesidebar.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_APP_CIRCLE_BAR, 0) != 0);

        mPieAppsidebar = (SwitchPreference) prefSet.findPreference(PA_PIE_APP_SIDEBAR);
        mPieAppsidebar.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_APP_SIDEBAR, 0) != 0);

        mPieExpandedDesktop = (SwitchPreference) prefSet.findPreference(PA_PIE_EXPANDED_DESKTOP);
        mPieExpandedDesktop.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_EXPANDED_DESKTOP, 0) != 0);

        mPieFloatingWindows = (SwitchPreference) prefSet.findPreference(PA_PIE_FLOATING_WINDOWS);
        mPieFloatingWindows.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_FLOATING_WINDOWS, 0) != 0);

        mPieGestureAnywhere = (SwitchPreference) prefSet.findPreference(PA_PIE_GESTURE_ANYWHERE);
        mPieGestureAnywhere.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_GESTURE_ANYWHERE, 0) != 0);

        mPieHeadsUp = (SwitchPreference) prefSet.findPreference(PA_PIE_HEADS_UP);
        mPieHeadsUp.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_HEADS_UP, 0) != 0);

        mPieHWKeys = (SwitchPreference) prefSet.findPreference(PA_PIE_HWKEYS);
        mPieHWKeys.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_HWKEYS, 0) != 0);

        mPieKillTask = (SwitchPreference) prefSet.findPreference(PA_PIE_KILLTASK);
        mPieKillTask.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_KILL_TASK, 0) != 0);

        mPieLastApp = (SwitchPreference) prefSet.findPreference(PA_PIE_LASTAPP);
        mPieLastApp.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_LAST_APP, 0) != 0);

        mPieMenu = (SwitchPreference) prefSet.findPreference(PA_PIE_MENU);
        mPieMenu.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_MENU, 0) != 0);

        mPieNavbar = (SwitchPreference) prefSet.findPreference(PA_PIE_NAVBAR);
        mPieNavbar.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_NAVBAR, 0) != 0);

        mPieNotifications = (SwitchPreference) prefSet.findPreference(PA_PIE_NOTIFICATIONS);
        mPieNotifications.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_NOTIFICATIONS, 0) != 0);

        mPieQsPanel = (SwitchPreference) prefSet.findPreference(PA_PIE_SETTINGS_PANEL);
        mPieQsPanel.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SETTINGS_PANEL, 0) != 0);

        mPiePowerMenu = (SwitchPreference) prefSet.findPreference(PA_PIE_POWER_MENU);
        mPiePowerMenu.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_POWER_MENU, 0) != 0);

        mPieRestartui = (SwitchPreference) prefSet.findPreference(PA_PIE_RESTARTUI);
        mPieRestartui.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_RESTARTUI, 1) != 0);

        mPieScreenOff = (SwitchPreference) prefSet.findPreference(PA_PIE_SCREEN_OFF);
        mPieScreenOff.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SCREEN_OFF, 1) != 0);

        mPieScreenshot = (SwitchPreference) prefSet.findPreference(PA_PIE_SCREENSHOT);
        mPieScreenshot.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SCREENSHOT, 0) != 0);

        mPieSlimPie = (SwitchPreference) prefSet.findPreference(PA_PIE_SLIMPIE);
        mPieSlimPie.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_SLIMPIE, 0) != 0);

        mPieTorch = (SwitchPreference) prefSet.findPreference(PA_PIE_TORCH);
        mPieTorch.setChecked(Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_TORCH, 1) != 0);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mPieAmbientDisplay) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_AMBIENT_DISPLAY,
                    mPieAmbientDisplay.isChecked() ? 1 : 0);
        } else if (preference == mPieAppcirclesidebar) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_APP_CIRCLE_BAR,
                    mPieAppcirclesidebar.isChecked() ? 1 : 0);
        } else if (preference == mPieAppsidebar) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_APP_SIDEBAR,
                    mPieAppsidebar.isChecked() ? 1 : 0);
        } else if (preference == mPieExpandedDesktop) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_EXPANDED_DESKTOP,
                    mPieExpandedDesktop.isChecked() ? 1 : 0);
        } else if (preference == mPieFloatingWindows) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_FLOATING_WINDOWS,
                    mPieFloatingWindows.isChecked() ? 1 : 0);
        } else if (preference == mPieGestureAnywhere) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_GESTURE_ANYWHERE,
                    mPieGestureAnywhere.isChecked() ? 1 : 0);
        } else if (preference == mPieHeadsUp) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_HEADS_UP,
                    mPieHeadsUp.isChecked() ? 1 : 0);
        } else if (preference == mPieHWKeys) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_HWKEYS,
                    mPieHWKeys.isChecked() ? 1 : 0);
        } else if (preference == mPieKillTask) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_KILL_TASK,
                    mPieKillTask.isChecked() ? 1 : 0);
        } else if (preference == mPieLastApp) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_LAST_APP,
                    mPieLastApp.isChecked() ? 1 : 0);
        } else if (preference == mPieMenu) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_MENU,
                    mPieMenu.isChecked() ? 1 : 0);
        } else if (preference == mPieNavbar) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_NAVBAR,
                    mPieNavbar.isChecked() ? 1 : 0);
        } else if (preference == mPieNotifications) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_NOTIFICATIONS,
                    mPieNotifications.isChecked() ? 1 : 0);
        } else if (preference == mPieQsPanel) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SETTINGS_PANEL,
                    mPieQsPanel.isChecked() ? 1 : 0);
        } else if (preference == mPiePowerMenu) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_POWER_MENU,
                    mPiePowerMenu.isChecked() ? 1 : 0);
        } else if (preference == mPieRestartui) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_RESTARTUI,
                    mPieRestartui.isChecked() ? 1 : 0);
        } else if (preference == mPieScreenOff) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SCREEN_OFF,
                    mPieScreenOff.isChecked() ? 1 : 0);
        } else if (preference == mPieScreenshot) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SCREENSHOT,
                    mPieScreenshot.isChecked() ? 1 : 0);
        } else if (preference == mPieSlimPie) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_SLIMPIE,
                    mPieSlimPie.isChecked() ? 1 : 0);
        } else if (preference == mPieTorch) {
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.PA_PIE_TORCH,
                    mPieTorch.isChecked() ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
