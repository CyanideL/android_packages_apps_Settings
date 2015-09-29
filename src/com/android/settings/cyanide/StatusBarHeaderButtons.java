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
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.android.internal.util.cyanide.QsDeviceUtils;

public class StatusBarHeaderButtons extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_BUTTONS_CATEGORY = "expanded_header_cat_qs";
    private static final String STATUS_BAR_POWER_MENU = "status_bar_power_menu";
    private static final String PREF_SHOW_CYANIDE_BUTTON = "show_cyanide_button";
    private static final String PREF_SHOW_CAMERA_BUTTON = "show_camera_button";
    private static final String PREF_SHOW_QS_BUTTON = "expanded_header_show_qs_button";
    private static final String PREF_SHOW_TORCH_BUTTON = "expanded_header_show_torch_button";

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private ListPreference mStatusBarPowerMenu;
    private SwitchPreference mShowCyanideButton;
    private SwitchPreference mShowCameraButton;
    private SwitchPreference mShowQsButton;
    private SwitchPreference mShowTorchButton;

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

        addPreferencesFromResource(R.xml.status_bar_header_buttons);
        mResolver = getActivity().getContentResolver();

        // status bar power menu
        mStatusBarPowerMenu = (ListPreference) findPreference(STATUS_BAR_POWER_MENU);
        mStatusBarPowerMenu.setOnPreferenceChangeListener(this);
        int statusBarPowerMenu = Settings.System.getInt(getContentResolver(),
                STATUS_BAR_POWER_MENU, 2);
        mStatusBarPowerMenu.setValue(String.valueOf(statusBarPowerMenu));
        mStatusBarPowerMenu.setSummary(mStatusBarPowerMenu.getEntry());

        PreferenceCategory buttonsCategory = (PreferenceCategory)
                findPreference(KEY_BUTTONS_CATEGORY);

        mShowCameraButton =
                (SwitchPreference) findPreference(PREF_SHOW_CAMERA_BUTTON);
        mShowCameraButton.setChecked(Settings.System.getInt(mResolver,
                Settings.System.SHOW_CAMERA_BUTTON, 0) == 1);
        mShowCameraButton.setOnPreferenceChangeListener(this);

        mShowCyanideButton =
                (SwitchPreference) findPreference(PREF_SHOW_CYANIDE_BUTTON);
        mShowCyanideButton.setChecked(Settings.System.getInt(mResolver,
                Settings.System.SHOW_CYANIDE_BUTTON, 1) == 1);
        mShowCyanideButton.setOnPreferenceChangeListener(this);

        mShowQsButton =
                (SwitchPreference) findPreference(PREF_SHOW_QS_BUTTON);
        mShowQsButton.setChecked(Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_QS_BUTTON, 0) == 1);
        mShowQsButton.setOnPreferenceChangeListener(this);

        mShowTorchButton =
                (SwitchPreference) findPreference(PREF_SHOW_TORCH_BUTTON);
        mShowTorchButton.setChecked(Settings.System.getInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_TORCH_BUTTON, 0) == 1);
        mShowTorchButton.setOnPreferenceChangeListener(this);

        // Remove Torch button switch for non-flash devices
        if(!QsDeviceUtils.deviceSupportsFlashLight(getActivity()) && buttonsCategory != null) {
            mShowTorchButton = (SwitchPreference) buttonsCategory
                    .findPreference(PREF_SHOW_TORCH_BUTTON);
            if (mShowTorchButton != null) {
                buttonsCategory.removePreference(mShowTorchButton);
            }
        }

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

        if (preference == mShowCameraButton) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.SHOW_CAMERA_BUTTON,
                value ? 1 : 0);
            return true;
        } else if (preference == mShowCyanideButton) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.SHOW_CYANIDE_BUTTON,
                value ? 1 : 0);
            return true;
        } else if (preference == mShowQsButton) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_QS_BUTTON,
                value ? 1 : 0);
            return true;
        } else if (preference == mShowTorchButton) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_TORCH_BUTTON,
                value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarPowerMenu) {
            String statusBarPowerMenu = (String) newValue;
            int statusBarPowerMenuValue = Integer.parseInt(statusBarPowerMenu);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_POWER_MENU, statusBarPowerMenuValue);
            int statusBarPowerMenuIndex = mStatusBarPowerMenu
                    .findIndexOfValue(statusBarPowerMenu);
            mStatusBarPowerMenu
                    .setSummary(mStatusBarPowerMenu.getEntries()[statusBarPowerMenuIndex]);
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

        StatusBarHeaderButtons getOwner() {
            return (StatusBarHeaderButtons) getTargetFragment();
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
                                    Settings.System.SHOW_CAMERA_BUTTON, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.SHOW_CYANIDE_BUTTON, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_QS_BUTTON, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_TORCH_BUTTON, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_POWER_MENU, 0);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.SHOW_CAMERA_BUTTON, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.SHOW_CYANIDE_BUTTON, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_QS_BUTTON, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_EXPANDED_HEADER_SHOW_TORCH_BUTTON, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_POWER_MENU, 2);
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
