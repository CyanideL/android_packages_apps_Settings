/* 
 * Copyright (C) 2014 DarkKat
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBarExpandedHeaderSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PREF_ENABLE_TASK_MANAGER = "enable_task_manager";
    private static final String STATUS_BAR_POWER_MENU = "status_bar_power_menu";
    private static final String PREF_SHOW_WEATHER = "expanded_header_show_weather";
    private static final String PREF_SHOW_LOCATION = "expanded_header_show_weather_location";
    private static final String PREF_BG_COLOR = "expanded_header_background_color";
    private static final String PREF_TEXT_COLOR = "expanded_header_text_color";
    private static final String PREF_ICON_COLOR = "expanded_header_icon_color";

    private static final int DEFAULT_COLOR = 0xffffffff;
    private static final int CYANIDE_BLUE = 0xff1976D2;
    private static final int DEFAULT_BG_COLOR = 0xff384248;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private SwitchPreference mEnableTaskManager;
    private ListPreference mStatusBarPowerMenu;
    private SwitchPreference mShowWeather;
    private SwitchPreference mShowLocation;
    private ColorPickerPreference mBackgroundColor;
    private ColorPickerPreference mTextColor;
    private ColorPickerPreference mIconColor;

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

        addPreferencesFromResource(R.xml.cyanide_status_bar_expanded_header_settings);
        mResolver = getActivity().getContentResolver();
        
        boolean enableTaskManager = Settings.System.getInt(mResolver,
                Settings.System.ENABLE_TASK_MANAGER, 0) == 1;
        
        boolean showWeather = Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER, 0) == 1;

        int intColor;
        String hexColor;

        // status bar power menu
        mStatusBarPowerMenu = (ListPreference) findPreference(STATUS_BAR_POWER_MENU);
        mStatusBarPowerMenu.setOnPreferenceChangeListener(this);
        int statusBarPowerMenu = Settings.System.getInt(getContentResolver(),
                STATUS_BAR_POWER_MENU, 2);
        mStatusBarPowerMenu.setValue(String.valueOf(statusBarPowerMenu));
        mStatusBarPowerMenu.setSummary(mStatusBarPowerMenu.getEntry());
        
        mEnableTaskManager = (SwitchPreference) findPreference(PREF_ENABLE_TASK_MANAGER);
        mEnableTaskManager.setChecked(enableTaskManager);
        mEnableTaskManager.setOnPreferenceChangeListener(this);
        
        mShowWeather = (SwitchPreference) findPreference(PREF_SHOW_WEATHER);
        mShowWeather.setChecked(showWeather);
        mShowWeather.setOnPreferenceChangeListener(this);

        if (showWeather) {
            mShowLocation = (SwitchPreference) findPreference(PREF_SHOW_LOCATION);
            mShowLocation.setChecked(Settings.System.getInt(mResolver,
                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER_LOCATION, 1) == 1);
            mShowLocation.setOnPreferenceChangeListener(this);
        } else {
            removePreference(PREF_SHOW_LOCATION);
        }
        
        mBackgroundColor =
                (ColorPickerPreference) findPreference(PREF_BG_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_BG_COLOR,
                DEFAULT_BG_COLOR); 
        mBackgroundColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mBackgroundColor.setSummary(hexColor);
        mBackgroundColor.setOnPreferenceChangeListener(this);
        mBackgroundColor.setAlphaSliderEnabled(true);

        mTextColor = (ColorPickerPreference) findPreference(PREF_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_TEXT_COLOR,
                DEFAULT_COLOR); 
        mTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTextColor.setSummary(hexColor);
        mTextColor.setOnPreferenceChangeListener(this);
        mTextColor.setAlphaSliderEnabled(true);

        mIconColor = (ColorPickerPreference) findPreference(PREF_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_ICON_COLOR,
                DEFAULT_COLOR); 
        mIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mIconColor.setSummary(hexColor);
        mIconColor.setOnPreferenceChangeListener(this);
        mIconColor.setAlphaSliderEnabled(true);

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

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value;
        String hex;
        int intHex;

        if (preference == mStatusBarPowerMenu) {
            String statusBarPowerMenu = (String) newValue;
            int statusBarPowerMenuValue = Integer.parseInt(statusBarPowerMenu);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_POWER_MENU, statusBarPowerMenuValue);
            int statusBarPowerMenuIndex = mStatusBarPowerMenu
                    .findIndexOfValue(statusBarPowerMenu);
            mStatusBarPowerMenu
                    .setSummary(mStatusBarPowerMenu.getEntries()[statusBarPowerMenuIndex]);
            return true;
        } else if (preference == mEnableTaskManager) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.ENABLE_TASK_MANAGER,
                value ? 1 : 0);
            return true;
        } else if (preference == mShowWeather) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER,
                value ? 1 : 0);
            refreshSettings();
            return true;
        } else if (preference == mShowLocation) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER_LOCATION,
                value ? 1 : 0);
            return true;
        } else if (preference == mBackgroundColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_BG_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_ICON_COLOR, intHex);
            preference.setSummary(hex);
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

        StatusBarExpandedHeaderSettings getOwner() {
            return (StatusBarExpandedHeaderSettings) getTargetFragment();
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
                                    Settings.System.STATUS_BAR_POWER_MENU, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ENABLE_TASK_MANAGER, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER_LOCATION, 1);
                             Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_BG_COLOR,
                                    DEFAULT_BG_COLOR);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_TEXT_COLOR,
                                    DEFAULT_COLOR);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_ICON_COLOR,
                                    DEFAULT_COLOR);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_POWER_MENU, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ENABLE_TASK_MANAGER, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_WEATHER_LOCATION, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_BG_COLOR,
                                    0x00000000);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_TEXT_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_ICON_COLOR,
                                    CYANIDE_BLUE);
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
}
