/*
 * Copyright (C) 2014-2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.cyanide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.widget.SeekBarPreferenceCham;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBarWeather extends SettingsPreferenceFragment
        implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_TEMPERATURE = "status_bar_temperature";
    private static final String PREF_STATUS_BAR_WEATHER_FONT_STYLE = "status_bar_weather_font_style";
    private static final String STATUS_BAR_TEMPERATURE_STYLE = "status_bar_temperature_style";
    private static final String PREF_STATUS_BAR_WEATHER_COLOR = "status_bar_weather_color";
    private static final String PREF_STATUS_BAR_WEATHER_SIZE = "status_bar_weather_size";

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private static final int CYANIDE_BLUE = 0xff1976D2;

    private ListPreference mStatusBarTemperature;
    private ListPreference mStatusBarTemperatureFontStyle;
    private ListPreference mStatusBarTemperatureStyle;
    private ColorPickerPreference mStatusBarTemperatureColor;
    private SeekBarPreferenceCham mStatusBarTemperatureSize;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    public void refreshSettings() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.status_bar_weather);
        mResolver = getActivity().getContentResolver();

        mStatusBarTemperature = (ListPreference) findPreference(STATUS_BAR_TEMPERATURE);
        int temperatureShow = Settings.System.getIntForUser(mResolver,
                Settings.System.STATUS_BAR_SHOW_WEATHER_TEMP, 0,
                UserHandle.USER_CURRENT);
        mStatusBarTemperature.setValue(String.valueOf(temperatureShow));
        mStatusBarTemperature.setSummary(mStatusBarTemperature.getEntry());
        mStatusBarTemperature.setOnPreferenceChangeListener(this);

        mStatusBarTemperatureFontStyle = (ListPreference) findPreference(PREF_STATUS_BAR_WEATHER_FONT_STYLE);
        mStatusBarTemperatureFontStyle.setOnPreferenceChangeListener(this);
        mStatusBarTemperatureFontStyle.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUS_BAR_WEATHER_FONT_STYLE, 0)));
        mStatusBarTemperatureFontStyle.setSummary(mStatusBarTemperatureFontStyle.getEntry());

        mStatusBarTemperatureStyle = (ListPreference) findPreference(STATUS_BAR_TEMPERATURE_STYLE);
        int temperatureStyle = Settings.System.getIntForUser(mResolver,
                Settings.System.STATUS_BAR_WEATHER_TEMP_STYLE, 0,
                UserHandle.USER_CURRENT);
        mStatusBarTemperatureStyle.setValue(String.valueOf(temperatureStyle));
        mStatusBarTemperatureStyle.setSummary(mStatusBarTemperatureStyle.getEntry());
        mStatusBarTemperatureStyle.setOnPreferenceChangeListener(this);

        mStatusBarTemperatureColor =
            (ColorPickerPreference) findPreference(PREF_STATUS_BAR_WEATHER_COLOR);
        mStatusBarTemperatureColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_WEATHER_COLOR, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mStatusBarTemperatureColor.setSummary(hexColor);
        mStatusBarTemperatureColor.setNewPreviewColor(intColor);

        mStatusBarTemperatureSize = (SeekBarPreferenceCham) findPreference(PREF_STATUS_BAR_WEATHER_SIZE);
        mStatusBarTemperatureSize.setValue(Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_WEATHER_SIZE, 14));
        mStatusBarTemperatureSize.setOnPreferenceChangeListener(this);

        updateWeatherOptions();
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_backup_restore)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                showDialogInner(DLG_RESET);
                return true;
             default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarTemperature) {
            int temperatureShow = Integer.valueOf((String) newValue);
            int index = mStatusBarTemperature.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(
                    mResolver, Settings.System.STATUS_BAR_SHOW_WEATHER_TEMP, temperatureShow,
                    UserHandle.USER_CURRENT);
            mStatusBarTemperature.setSummary(
                    mStatusBarTemperature.getEntries()[index]);
            updateWeatherOptions();
            return true;
        } else if (preference == mStatusBarTemperatureFontStyle) {
            int val = Integer.parseInt((String) newValue);
            int index = mStatusBarTemperatureFontStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_WEATHER_FONT_STYLE, val);
            mStatusBarTemperatureFontStyle.setSummary(mStatusBarTemperatureFontStyle.getEntries()[index]);
            return true;
        } else if (preference == mStatusBarTemperatureStyle) {
            int temperatureStyle = Integer.valueOf((String) newValue);
            int index = mStatusBarTemperatureStyle.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(
                    mResolver, Settings.System.STATUS_BAR_WEATHER_TEMP_STYLE, temperatureStyle,
                    UserHandle.USER_CURRENT);
            mStatusBarTemperatureStyle.setSummary(
                    mStatusBarTemperatureStyle.getEntries()[index]);
            return true;
        } else if (preference == mStatusBarTemperatureColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.STATUS_BAR_WEATHER_COLOR, intHex);
            return true;
        } else if (preference == mStatusBarTemperatureSize) {
            int width = ((Integer)newValue).intValue();
            Settings.System.putInt(mResolver,
                    Settings.System.STATUS_BAR_WEATHER_SIZE, width);
            return true;
        }
        return false;
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        StatusBarWeather getOwner() {
            return (StatusBarWeather) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case DLG_RESET:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reset)
                    .setMessage(R.string.reset_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setNeutralButton(R.string.reset_android,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_SHOW_WEATHER_TEMP, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_FONT_STYLE, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_TEMP_STYLE, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_COLOR,
                                    0xffffffff);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_SIZE,
                                    14);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_SHOW_WEATHER_TEMP, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_FONT_STYLE, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_TEMP_STYLE, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_WEATHER_SIZE,
                                    16);
                            getOwner().refreshSettings();
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    
    private void updateWeatherOptions() {
        if (Settings.System.getInt(mResolver,
            Settings.System.STATUS_BAR_SHOW_WEATHER_TEMP, 0) == 0) {
            mStatusBarTemperatureFontStyle.setEnabled(false);
            mStatusBarTemperatureStyle.setEnabled(false);
            mStatusBarTemperatureColor.setEnabled(false);
            mStatusBarTemperatureSize.setEnabled(false);
        } else {
            mStatusBarTemperatureFontStyle.setEnabled(true);
            mStatusBarTemperatureStyle.setEnabled(true);
            mStatusBarTemperatureColor.setEnabled(true);
            mStatusBarTemperatureSize.setEnabled(true);
        }
    }

}
