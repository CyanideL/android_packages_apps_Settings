/*
 * Copyright (C) 2013 Slimroms
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

package com.android.settings.cyanide.fragments;

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
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class LockscreenShortcutFragment extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final int MENU_RESET = Menu.FIRST;

    private static final int DLG_RESET = 0;

    private static final String PREF_LOCKSCREEN_SHORTCUTS_LAUNCH_TYPE =
            "lockscreen_shortcuts_launch_type";
    private static final String LOCKSCREEN_SHORTCUTS_COLOR_MODE =
            "pref_lockscreen_shortcuts_color_mode";
    private static final String LOCKSCREEN_SHORTCUTS_ICON_COLOR =
            "pref_lockscreen_shortcuts_icon_color";

    private static final int DEFAULT_ICON_COLOR = 0xffffffff;
    private static final int CYANIDE_BLUE = 0xff1976D2;

    private ListPreference mLockscreenShortcutsLaunchType;
    private ColorPickerPreference mShortcutsIconColor;
    private ListPreference mShortcutsIconColorMode;

    private boolean mCheckPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    public PreferenceScreen refreshSettings() {
        mCheckPreferences = false;
        PreferenceScreen prefSet = getPreferenceScreen();
        if (prefSet != null) {
            prefSet.removeAll();
        }

        addPreferencesFromResource(R.xml.lockscreen_shortcut_fragment);

        prefSet = getPreferenceScreen();

        mLockscreenShortcutsLaunchType = (ListPreference) findPreference(
                PREF_LOCKSCREEN_SHORTCUTS_LAUNCH_TYPE);
        mLockscreenShortcutsLaunchType.setOnPreferenceChangeListener(this);

        mShortcutsIconColor =
            (ColorPickerPreference) findPreference(LOCKSCREEN_SHORTCUTS_ICON_COLOR);
        int intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                    DEFAULT_ICON_COLOR);
        if (intColor == -2) {
            intColor = getResources().getColor(
                com.android.internal.R.color.power_menu_icon_default_color);
            mShortcutsIconColor.setSummary(
                getResources().getString(R.string.default_string));
        } else {
            String hexColor = String.format("#%08x", (0xffffffff & intColor));
            mShortcutsIconColor.setSummary(hexColor);
        }
        mShortcutsIconColor.setNewPreviewColor(intColor);
        mShortcutsIconColor.setOnPreferenceChangeListener(this);

        mShortcutsIconColorMode = (ListPreference) prefSet.findPreference(
                LOCKSCREEN_SHORTCUTS_COLOR_MODE);
        mShortcutsIconColorMode.setValue(String.valueOf(
                Settings.System.getIntForUser(getContentResolver(),
                Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 0,
                UserHandle.USER_CURRENT_OR_SELF)));
        mShortcutsIconColorMode.setSummary(mShortcutsIconColorMode.getEntry());
        mShortcutsIconColorMode.setOnPreferenceChangeListener(this);

        updateColorPreference();

        setHasOptionsMenu(false);
        mCheckPreferences = true;
        return prefSet;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_backup_restore) // use the backup icon
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
        if (!mCheckPreferences) {
            return false;
        }
        if (preference == mLockscreenShortcutsLaunchType) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SHORTCUTS_LONGPRESS,
                    Integer.valueOf((String) newValue));
            return true;
        } else if (preference == mShortcutsIconColorMode) {
            int index = mShortcutsIconColorMode.findIndexOfValue((String) newValue);
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, value);
            mShortcutsIconColorMode.setSummary(
                mShortcutsIconColorMode.getEntries()[index]);
            updateColorPreference();
            return true;
        } else if (preference == mShortcutsIconColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                    intHex);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateColorPreference() {
        int colorMode = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 0);
        mShortcutsIconColor.setEnabled(colorMode != 3);
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

        LockscreenShortcutFragment getOwner() {
            return (LockscreenShortcutFragment) getTargetFragment();
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
                            Settings.System.putInt(getActivity().getContentResolver(),
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                                    DEFAULT_ICON_COLOR);
                            Settings.System.putInt(getActivity().getContentResolver(),
                                   Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 0);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getActivity().getContentResolver(),
                                    Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR,
                                    CYANIDE_BLUE); 
                            Settings.System.putInt(getActivity().getContentResolver(),
                                   Settings.System.LOCKSCREEN_SHORTCUTS_ICON_COLOR_MODE, 0);
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
