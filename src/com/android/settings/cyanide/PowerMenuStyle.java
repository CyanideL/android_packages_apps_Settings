/*
 * Copyright (C) 2012 Slimroms
 * Copyright (C) 2015 The Fusion Project
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.SlimSeekBarPreference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class PowerMenuStyle extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String POWER_MENU_ONTHEGO_ENABLED =
            "power_menu_onthego_enabled";
    private static final String PREF_ON_THE_GO_ALPHA =
            "on_the_go_alpha";
    private static final String PREF_ICON_NORMAL_COLOR =
            "power_menu_icon_normal_color";
    private static final String PREF_ICON_ENABLED_SELECTED_COLOR =
            "power_menu_icon_enabled_selected_color";
    private static final String PREF_RIPPLE_COLOR =
            "power_menu_ripple_color";
    private static final String PREF_TEXT_COLOR =
            "power_menu_text_color";

    private static final int WHITE = 0xffffffff;
    private static final int BLACK = 0xff000000;
    private static final int CYANIDE_BLUE = 0xff1976D2;
    private static final int MATERIAL_TEAL_500 = 0xff009688;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private SwitchPreference mOnTheGoPowerMenu;
    private SlimSeekBarPreference mOnTheGoAlphaPref;

    private ColorPickerPreference mIconNormalColor;
    private ColorPickerPreference mIconEnabledSelectedColor;
    private ColorPickerPreference mRippleColor;
    private ColorPickerPreference mTextColor;

    private ContentResolver mResolver;

    private static final int MIN_DELAY_VALUE = 1;
    private static final int MAX_DELAY_VALUE = 30;

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

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.power_menu_style);
        mResolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

        mOnTheGoPowerMenu = (SwitchPreference) findPreference(POWER_MENU_ONTHEGO_ENABLED);
        mOnTheGoPowerMenu.setChecked(Settings.System.getInt(mResolver,
                Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1);
        mOnTheGoPowerMenu.setOnPreferenceChangeListener(this);

        mOnTheGoAlphaPref = (SlimSeekBarPreference) findPreference(PREF_ON_THE_GO_ALPHA);
        mOnTheGoAlphaPref.setDefault(50);
        mOnTheGoAlphaPref.setInterval(1);
        mOnTheGoAlphaPref.setOnPreferenceChangeListener(this);

        mIconNormalColor =
                (ColorPickerPreference) findPreference(PREF_ICON_NORMAL_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.POWER_MENU_ICON_NORMAL_COLOR,
                WHITE); 
        mIconNormalColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mIconNormalColor.setSummary(hexColor);
        mIconNormalColor.setOnPreferenceChangeListener(this);

        mIconEnabledSelectedColor =
                (ColorPickerPreference) findPreference(PREF_ICON_ENABLED_SELECTED_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.POWER_MENU_ICON_ENABLED_SELECTED_COLOR,
                MATERIAL_TEAL_500); 
        mIconEnabledSelectedColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mIconEnabledSelectedColor.setSummary(hexColor);
        mIconEnabledSelectedColor.setOnPreferenceChangeListener(this);

        mRippleColor =
                (ColorPickerPreference) findPreference(PREF_RIPPLE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.POWER_MENU_RIPPLE_COLOR, WHITE); 
        mRippleColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mRippleColor.setSummary(hexColor);
        mRippleColor.setOnPreferenceChangeListener(this);

        mTextColor =
                (ColorPickerPreference) findPreference(PREF_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.POWER_MENU_TEXT_COLOR,
                WHITE); 
        mTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTextColor.setSummary(hexColor);
        mTextColor.setOnPreferenceChangeListener(this);

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
        String hex;
        int intHex;

        if (preference == mOnTheGoPowerMenu) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.POWER_MENU_ONTHEGO_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mOnTheGoAlphaPref) {
            float val = Float.parseFloat((String) newValue);
            Settings.System.putFloat(mResolver, Settings.System.ON_THE_GO_ALPHA,
                    val / 100);
            return true;
        } else if (preference == mIconNormalColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.POWER_MENU_ICON_NORMAL_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mIconEnabledSelectedColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.POWER_MENU_ICON_ENABLED_SELECTED_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mRippleColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.POWER_MENU_RIPPLE_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.POWER_MENU_TEXT_COLOR, intHex);
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

        PowerMenuStyle getOwner() {
            return (PowerMenuStyle) getTargetFragment();
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
                                    Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_ICON_NORMAL_COLOR,
                                    MATERIAL_TEAL_500);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_ICON_ENABLED_SELECTED_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_RIPPLE_COLOR, WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_TEXT_COLOR, BLACK);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_ONTHEGO_ENABLED, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_ICON_NORMAL_COLOR,
                                    0xff00ff00);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_ICON_ENABLED_SELECTED_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_RIPPLE_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.POWER_MENU_TEXT_COLOR, CYANIDE_BLUE);
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
