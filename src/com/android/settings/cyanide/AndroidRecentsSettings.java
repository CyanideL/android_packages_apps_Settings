/*
 * Copyright (C) 2014 DarkKat
 *               2015 CyanideL
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
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.cyanide.util.Helpers;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class AndroidRecentsSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener { 

    private static final String PREF_CAT_CLEAR_ALL =
            "android_recents_cat_clear_all_button";
    private static final String PREF_SHOW_SEARCH_BAR =
            "android_recents_show_search_bar";
    private static final String PREF_SHOW_CLEAR_ALL =
            "android_recents_show_clear_all";
    private static final String PREF_CLEAR_ALL_POSITION_HORIZONTAL =
            "android_recents_clear_all_position_horizontal";
    private static final String PREF_CLEAR_ALL_POSITION_VERTICAL =
            "android_recents_clear_all_position_vertical";
    private static final String PREF_CLEAR_ALL_USE_ICON_COLOR =
            "android_recents_clear_all_use_icon_color";
    private static final String PREF_CLEAR_ALL_BG_COLOR =
            "android_recents_clear_all_bg_color";
    private static final String PREF_CLEAR_ALL_ICON_COLOR =
            "android_recents_clear_all_icon_color";
    private static final String RECENTS_EMPTY_CYANIDE_LOGO =
            "recents_empty_cyanide_logo";
    private static final String MEM_TEXT_COLOR =
            "mem_text_color";
    private static final String MEMORY_BAR_COLOR =
            "memory_bar_color";
    private static final String MEMORY_BAR_USED_COLOR =
            "memory_bar_used_color";
    private static final String KEY_SCREEN_PINNING = 
            "screen_pinning_settings";

    private static final int DEEP_TEAL_500 = 0xff009688;
    private static final int CYANIDE_BLUE = 0xff1976D2;
    private static final int WHITE = 0xffffffff;
    private static final int HOLO_BLUE_LIGHT = 0xff33b5e5;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private SwitchPreference mShowSearchBar;
    private SwitchPreference mShowClearAll;
    private ListPreference mClearAllPositionHorizontal;
    private ListPreference mClearAllPositionVertical;
    private SwitchPreference mClearAllUseIconColor;
    private ColorPickerPreference mClearAllIconColor;
    private ColorPickerPreference mClearAllBgColor;
    private SwitchPreference mRecentsStyle;
    private ColorPickerPreference mMemTextColor;
    private ColorPickerPreference mMemBarColor;
    private ColorPickerPreference mMemBarUsedColor;
    private PreferenceScreen mScreenPinning;
    private Preference mOmniSwitch;

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

        addPreferencesFromResource(R.xml.android_recents_settings);

        mResolver = getActivity().getContentResolver();
        int intvalue;
        int intColor;
        String hexColor;

        mShowSearchBar = (SwitchPreference) findPreference(PREF_SHOW_SEARCH_BAR);
        mShowSearchBar.setChecked(Settings.System.getInt(mResolver,
               Settings.System.ANDROID_RECENTS_SHOW_SEARCH_BAR, 0) == 1);
        mShowSearchBar.setOnPreferenceChangeListener(this);

        mShowClearAll = (SwitchPreference) findPreference(PREF_SHOW_CLEAR_ALL);
        boolean showClearAll = Settings.System.getInt(mResolver,
               Settings.System.ANDROID_RECENTS_SHOW_CLEAR_ALL, 1) == 1;
        mShowClearAll.setChecked(showClearAll);
        mShowClearAll.setOnPreferenceChangeListener(this);

        PreferenceCategory catClearAll =
                (PreferenceCategory) findPreference(PREF_CAT_CLEAR_ALL);
        if (showClearAll) {
            mClearAllPositionHorizontal =
                    (ListPreference) findPreference(PREF_CLEAR_ALL_POSITION_HORIZONTAL);
            intvalue = Settings.System.getInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_HORIZONTAL, 2);
            mClearAllPositionHorizontal.setValue(String.valueOf(intvalue));
            mClearAllPositionHorizontal.setSummary(mClearAllPositionHorizontal.getEntry());
            mClearAllPositionHorizontal.setOnPreferenceChangeListener(this);

            mClearAllPositionVertical =
                    (ListPreference) findPreference(PREF_CLEAR_ALL_POSITION_VERTICAL);
            intvalue = Settings.System.getInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_VERTICAL, 2);
            mClearAllPositionVertical.setValue(String.valueOf(intvalue));
            mClearAllPositionVertical.setSummary(mClearAllPositionVertical.getEntry());
            mClearAllPositionVertical.setOnPreferenceChangeListener(this);

            mClearAllUseIconColor = (SwitchPreference) findPreference(PREF_CLEAR_ALL_USE_ICON_COLOR);
            boolean clearAllUseIconColor = Settings.System.getInt(mResolver,
                   Settings.System.ANDROID_RECENTS_CLEAR_ALL_USE_ICON_COLOR, 0) == 1;
            mClearAllUseIconColor.setChecked(clearAllUseIconColor);
            mClearAllUseIconColor.setOnPreferenceChangeListener(this);

            mClearAllBgColor =
                    (ColorPickerPreference) findPreference(PREF_CLEAR_ALL_BG_COLOR);
            intColor = Settings.System.getInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_BG_COLOR, DEEP_TEAL_500); 
            mClearAllBgColor.setNewPreviewColor(intColor);
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mClearAllBgColor.setSummary(hexColor);
            mClearAllBgColor.setAlphaSliderEnabled(true);
            mClearAllBgColor.setOnPreferenceChangeListener(this);

            if (clearAllUseIconColor) {
                mClearAllIconColor =
                        (ColorPickerPreference) findPreference(PREF_CLEAR_ALL_ICON_COLOR);
                intColor = Settings.System.getInt(mResolver,
                        Settings.System.ANDROID_RECENTS_CLEAR_ALL_ICON_COLOR, WHITE); 
                mClearAllIconColor.setNewPreviewColor(intColor);
                hexColor = String.format("#%08x", (0xffffffff & intColor));
                mClearAllIconColor.setSummary(hexColor);
                mClearAllIconColor.setAlphaSliderEnabled(true);
                mClearAllIconColor.setOnPreferenceChangeListener(this);
            } else {
                catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_ICON_COLOR));
            }
        } else {
            catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_USE_ICON_COLOR));
            catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_POSITION_HORIZONTAL));
            catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_POSITION_VERTICAL));
            catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_BG_COLOR));
            catClearAll.removePreference(findPreference(PREF_CLEAR_ALL_ICON_COLOR));
        }

        mRecentsStyle = (SwitchPreference) findPreference(RECENTS_EMPTY_CYANIDE_LOGO);
        mRecentsStyle.setChecked(Settings.System.getInt(mResolver,
            Settings.System.RECENTS_EMPTY_CYANIDE_LOGO, 0) == 1);
        mRecentsStyle.setOnPreferenceChangeListener(this);

        mMemTextColor =
                (ColorPickerPreference) findPreference(MEM_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MEM_TEXT_COLOR, WHITE); 
        mMemTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mMemTextColor.setSummary(hexColor);
        mMemTextColor.setOnPreferenceChangeListener(this);

        mMemBarColor =
                (ColorPickerPreference) findPreference(MEMORY_BAR_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MEMORY_BAR_COLOR, WHITE); 
        mMemBarColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mMemBarColor.setSummary(hexColor);
        mMemBarColor.setOnPreferenceChangeListener(this);

        mMemBarUsedColor =
                (ColorPickerPreference) findPreference(MEMORY_BAR_USED_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MEMORY_BAR_USED_COLOR, WHITE); 
        mMemBarUsedColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mMemBarUsedColor.setSummary(hexColor);
        mMemBarUsedColor.setOnPreferenceChangeListener(this);

        final boolean screenPinning = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCK_TO_APP_ENABLED, 0) == 1;
        if (mScreenPinning != null) {
            mScreenPinning.setSummary(screenPinning ?
                getResources().getString(R.string.switch_on_text) :
                getResources().getString(R.string.switch_off_text));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_backup_restore) // use the KitKat backup icon
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
        int intvalue;
        int index;
        String hex;
        int intHex;

        if (preference == mShowSearchBar) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_SHOW_SEARCH_BAR,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowClearAll) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_SHOW_CLEAR_ALL,
                    value ? 1 : 0);
            refreshSettings();
            return true;
        } else if (preference == mClearAllPositionHorizontal) {
            intvalue = Integer.valueOf((String) newValue);
            index = mClearAllPositionHorizontal.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_HORIZONTAL,
                    intvalue);
            preference.setSummary(mClearAllPositionHorizontal.getEntries()[index]);
            return true;
        } else if (preference == mClearAllPositionVertical) {
            intvalue = Integer.valueOf((String) newValue);
            index = mClearAllPositionVertical.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_VERTICAL,
                    intvalue);
            preference.setSummary(mClearAllPositionVertical.getEntries()[index]);
            return true;
        } else if (preference == mClearAllUseIconColor) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_USE_ICON_COLOR,
                    value ? 1 : 0);
            refreshSettings();
        } else if (preference == mClearAllBgColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_BG_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mClearAllIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mRecentsStyle) {
            value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RECENTS_EMPTY_CYANIDE_LOGO,
                    value ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        } else if (preference == mMemTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MEM_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mMemBarColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MEMORY_BAR_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mMemBarUsedColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MEMORY_BAR_USED_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mScreenPinning) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.LOCK_TO_APP_ENABLED,
                    value ? 1 : 0);
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

        AndroidRecentsSettings getOwner() {
            return (AndroidRecentsSettings) getTargetFragment();
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
                                    Settings.System.ANDROID_RECENTS_SHOW_SEARCH_BAR, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_SHOW_CLEAR_ALL, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_HORIZONTAL, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_VERTICAL, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_USE_ICON_COLOR, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_BG_COLOR,
                                    DEEP_TEAL_500);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_ICON_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.RECENTS_EMPTY_CYANIDE_LOGO, 0);
                                    Helpers.restartSystemUI();
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEM_TEXT_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEMORY_BAR_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEMORY_BAR_USED_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.LOCK_TO_APP_ENABLED, 0);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_SHOW_SEARCH_BAR, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_SHOW_CLEAR_ALL, 1);
                             Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_HORIZONTAL, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_POSITION_VERTICAL, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_USE_ICON_COLOR, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_BG_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.ANDROID_RECENTS_CLEAR_ALL_ICON_COLOR,
                                    0xff00ff00);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.RECENTS_EMPTY_CYANIDE_LOGO, 1);
                                    Helpers.restartSystemUI();
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEM_TEXT_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEMORY_BAR_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MEMORY_BAR_USED_COLOR,
                                    0xff00ff00);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.LOCK_TO_APP_ENABLED, 0);
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
