/*
 * Copyright (C) 2015 CyanideL
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
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.cyanide.util.Helpers;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class LSSColors extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE = "lockscreen_shortcuts_icon_color_mode";
    private static final String LOCKSCREEN_SHORTCUTS_ICON_COLOR = "lockscreen_shortcuts_icon_color";

    private static final int DEFAULT_ICON_COLOR = 0xffffffff;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private ListPreference mShortcutsIconColorMode;
    private ColorPickerPreference mShortcutsIconColor;

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
        addPreferencesFromResource(R.xml.ls_shortcut_colors);

        mResolver = getActivity().getContentResolver();

        mShortcutsIconColorMode =
            (ListPreference) findPreference(LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE);
        int shortcutsIconColorMode = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 3);
        mShortcutsIconColorMode.setValue(String.valueOf(shortcutsIconColorMode));
        mShortcutsIconColorMode.setSummary(mShortcutsIconColorMode.getEntry());
        mShortcutsIconColorMode.setOnPreferenceChangeListener(this);

        mShortcutsIconColor = (ColorPickerPreference) findPreference(LOCKSCREEN_SHORTCUTS_ICON_COLOR);
        int intColor = Settings.System.getInt(mResolver,
                Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                DEFAULT_ICON_COLOR); 
        mShortcutsIconColor.setNewPreviewColor(intColor);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mShortcutsIconColor.setSummary(hexColor);
        mShortcutsIconColor.setOnPreferenceChangeListener(this);
        mShortcutsIconColor.setAlphaSliderEnabled(true);

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
        if (preference == mShortcutsIconColorMode) {
            int index = mShortcutsIconColorMode.findIndexOfValue((String) newValue);
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, value);
            mShortcutsIconColorMode.setSummary(
                mShortcutsIconColorMode.getEntries()[index]);
            refreshSettings();
            return true;
        } else if (preference == mShortcutsIconColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                    intHex);
            Helpers.restartSystemUI();
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

        LSSColors getOwner() {
            return (LSSColors) getTargetFragment();
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
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 3);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                                    DEFAULT_ICON_COLOR);
                                    Helpers.restartSystemUI();
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                                    0xff1976D2);
                                    Helpers.restartSystemUI();
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
