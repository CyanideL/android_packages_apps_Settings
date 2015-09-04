/*
 * Copyright (C) 2014 The Dirty Unicorns Project
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.TextUtils;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.settings.Utils;
import com.android.settings.widget.SeekBarPreferenceCham;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class CarrierLabel extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "CarrierLabel";

    private static final String STATUS_BAR_CUSTOM_CARRIER = "status_bar_custom_carrier";
    private static final String CUSTOM_CARRIER_LABEL = "custom_carrier_label";
    private static final String STATUS_BAR_CARRIER_FONT_SIZE  = "status_bar_carrier_font_size";
    private static final String STATUS_BAR_CARRIER_COLOR = "status_bar_carrier_color";

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;
    
    private static final int WHITE = 0xffffffff;
    private static final int CYANIDE_BLUE = 0xff1976D2;

    private ListPreference mStatusBarCarrier;
    private PreferenceScreen mCustomCarrierLabel;
    private String mCustomCarrierLabelText;
    private SeekBarPreferenceCham mStatusBarCarrierSize;
    private ColorPickerPreference mCarrierColorPicker;

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

        mResolver = getActivity().getContentResolver();
        addPreferencesFromResource(R.xml.cyanide_carrierlabel);

        String hexColor;

        mStatusBarCarrier = (ListPreference) findPreference(STATUS_BAR_CUSTOM_CARRIER);
        int statusBarCarrier = Settings.System.getInt(mResolver, Settings.System.STATUS_BAR_CUSTOM_CARRIER, 1);
        mStatusBarCarrier.setValue(String.valueOf(statusBarCarrier));
        mStatusBarCarrier.setSummary(mStatusBarCarrier.getEntry());
        mStatusBarCarrier.setOnPreferenceChangeListener(this);
        mCustomCarrierLabel = (PreferenceScreen) findPreference(CUSTOM_CARRIER_LABEL);

        mStatusBarCarrierSize = (SeekBarPreferenceCham) findPreference(STATUS_BAR_CARRIER_FONT_SIZE);
        mStatusBarCarrierSize.setValue(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.STATUS_BAR_CARRIER_FONT_SIZE, 14));
        mStatusBarCarrierSize.setOnPreferenceChangeListener(this);

        mCarrierColorPicker = (ColorPickerPreference) findPreference(STATUS_BAR_CARRIER_COLOR);
        mCarrierColorPicker.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(mResolver,
                    Settings.System.STATUS_BAR_CARRIER_COLOR, WHITE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mCarrierColorPicker.setSummary(hexColor);
        mCarrierColorPicker.setNewPreviewColor(intColor);
        mCarrierColorPicker.setAlphaSliderEnabled(true);

        updateCustomLabelTextSummary();
        enableCarrierOptions();
        setHasOptionsMenu(true);

    }

    private void updateCustomLabelTextSummary() {
		if (mCustomCarrierLabel == null) {
            return;
        }
        mCustomCarrierLabelText = Settings.System.getString(
            mResolver, Settings.System.CUSTOM_CARRIER_LABEL);

        if (TextUtils.isEmpty(mCustomCarrierLabelText)) {
            mCustomCarrierLabel.setSummary(R.string.custom_carrier_label_notset);
        } else {
            mCustomCarrierLabel.setSummary(mCustomCarrierLabelText);
        }
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
        if (preference == mStatusBarCarrier) {
            int statusBarCarrier = Integer.valueOf((String) newValue);
            int index = mStatusBarCarrier.findIndexOfValue((String) newValue);
            Settings.System.putInt(
                    mResolver, Settings.System.STATUS_BAR_CUSTOM_CARRIER, statusBarCarrier);
            mStatusBarCarrier.setSummary(mStatusBarCarrier.getEntries()[index]);
            enableCarrierOptions();
         } else if (preference == mCarrierColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.STATUS_BAR_CARRIER_COLOR, intHex);
            return true;
         } else if (preference == mStatusBarCarrierSize) {
            int width = ((Integer)newValue).intValue();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_CARRIER_FONT_SIZE, width);
            return true;
         }
         return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {
        if (preference.getKey().equals(CUSTOM_CARRIER_LABEL)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(R.string.custom_carrier_label_title);
            alert.setMessage(R.string.custom_carrier_label_explain);

            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
            input.setText(TextUtils.isEmpty(mCustomCarrierLabelText) ? "" : mCustomCarrierLabelText);
            input.setSelection(input.getText().length());
            alert.setView(input);
            alert.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = ((Spannable) input.getText()).toString().trim();
                            Settings.System.putString(mResolver, Settings.System.CUSTOM_CARRIER_LABEL, value);
                            updateCustomLabelTextSummary();
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_CUSTOM_CARRIER_LABEL_CHANGED);
                            getActivity().sendBroadcast(i);
                }
            });
            alert.setNegativeButton(getString(android.R.string.cancel), null);
            alert.show();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
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

        CarrierLabel getOwner() {
            return (CarrierLabel) getTargetFragment();
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
                                    Settings.System.STATUS_BAR_CUSTOM_CARRIER, 1);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_CARRIER_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_CARRIER_FONT_SIZE, 14);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_CUSTOM_CARRIER, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_CARRIER_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.STATUS_BAR_CARRIER_FONT_SIZE, 16);
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

    private void enableCarrierOptions() {
        if (Settings.System.getInt(mResolver,
            Settings.System.STATUS_BAR_CUSTOM_CARRIER, 0) == 0) {
            mCustomCarrierLabel.setEnabled(false);
            mStatusBarCarrierSize.setEnabled(false);
            mCarrierColorPicker.setEnabled(false);
        } else {
            mCustomCarrierLabel.setEnabled(true);
            mStatusBarCarrierSize.setEnabled(true);
            mCarrierColorPicker.setEnabled(true);
        }
    }
}
