/*
 * Copyright (C) 2016 The VRToxin Project
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

package com.android.settings.vrtoxin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.vrtoxin.SeekBarPreference;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class VolumeDialogSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String VOLUME_DIALOG_BG_COLOR = "volume_dialog_bg_color";
    private static final String VOLUME_DIALOG_ICON_COLOR = "volume_dialog_icon_color";
    private static final String VOLUME_DIALOG_SLIDER_COLOR = "volume_dialog_slider_color";
    private static final String VOLUME_DIALOG_SLIDER_INACTIVE_COLOR = "volume_dialog_slider_inactive_color";
    private static final String VOLUME_DIALOG_SLIDER_ICON_COLOR = "volume_dialog_slider_icon_color";
    private static final String VOLUME_DIALOG_EXPAND_BUTTON_COLOR = "volume_dialog_expand_button_color";
    private static final String PREF_VOLUME_DIALOG_STROKE = "volume_dialog_stroke";
    private static final String PREF_VOLUME_DIALOG_STROKE_COLOR = "volume_dialog_stroke_color";
    private static final String PREF_VOLUME_DIALOG_STROKE_THICKNESS = "volume_dialog_stroke_thickness";
    private static final String PREF_VOLUME_DIALOG_CORNER_RADIUS = "volume_dialog_corner_radius";

    private static final int WHITE = 0xffffffff;
    private static final int BLACK = 0xff000000;
    private static final int VRTOXIN_BLUE = 0xff1976D2;
    private static final int MATERIAL_GREEN = 0xff009688;
    private static final int MATERIAL_BLUE_GREY = 0xff37474f;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private ColorPickerPreference mBgColor;
    private ColorPickerPreference mIconColor;
    private ColorPickerPreference mSliderColor;
    private ColorPickerPreference mSliderInactiveColor;
    private ColorPickerPreference mSliderIconColor;
    private ColorPickerPreference mExpandButtonColor;
    private ListPreference mVolumeDialogStroke;
    private ColorPickerPreference mVolumeDialogStrokeColor;
    private SeekBarPreference mVolumeDialogStrokeThickness;
    private SeekBarPreference mVolumeDialogCornerRadius;

    static final int DEFAULT_VOLUME_DIALOG_STROKE_COLOR = 0xFF80CBC4;

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

        addPreferencesFromResource(R.xml.volume_dialog_settings);
        mResolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

        mBgColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_BG_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_BG_COLOR,
                MATERIAL_BLUE_GREY);
        mBgColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mBgColor.setSummary(hexColor);
        mBgColor.setOnPreferenceChangeListener(this);

        mIconColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_ICON_COLOR,
                MATERIAL_GREEN);
        mIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mIconColor.setSummary(hexColor);
        mIconColor.setOnPreferenceChangeListener(this);

        mSliderColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_SLIDER_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_SLIDER_COLOR,
                MATERIAL_GREEN);
        mSliderColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mSliderColor.setSummary(hexColor);
        mSliderColor.setOnPreferenceChangeListener(this);

        mSliderInactiveColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_SLIDER_INACTIVE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_SLIDER_INACTIVE_COLOR, WHITE); 
        mSliderInactiveColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mSliderInactiveColor.setSummary(hexColor);
        mSliderInactiveColor.setOnPreferenceChangeListener(this);

        mSliderIconColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_SLIDER_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_SLIDER_ICON_COLOR, WHITE); 
        mSliderIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mSliderIconColor.setSummary(hexColor);
        mSliderIconColor.setOnPreferenceChangeListener(this);

        mExpandButtonColor =
                (ColorPickerPreference) findPreference(VOLUME_DIALOG_EXPAND_BUTTON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_EXPAND_BUTTON_COLOR, WHITE); 
        mExpandButtonColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpandButtonColor.setSummary(hexColor);
        mExpandButtonColor.setOnPreferenceChangeListener(this);

        // Volume dialog stroke
        mVolumeDialogStroke =
                (ListPreference) findPreference(PREF_VOLUME_DIALOG_STROKE);
        int volumeDialogStroke = Settings.System.getIntForUser(mResolver,
                        Settings.System.VOLUME_DIALOG_STROKE, 1,
                        UserHandle.USER_CURRENT);
        mVolumeDialogStroke.setValue(String.valueOf(volumeDialogStroke));
        mVolumeDialogStroke.setSummary(mVolumeDialogStroke.getEntry());
        mVolumeDialogStroke.setOnPreferenceChangeListener(this);

        // Volume dialog stroke color
        mVolumeDialogStrokeColor =
                (ColorPickerPreference) findPreference(PREF_VOLUME_DIALOG_STROKE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_STROKE_COLOR, DEFAULT_VOLUME_DIALOG_STROKE_COLOR); 
        mVolumeDialogStrokeColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mVolumeDialogStrokeColor.setSummary(hexColor);
        mVolumeDialogStrokeColor.setOnPreferenceChangeListener(this);

        // Volume dialog stroke thickness
        mVolumeDialogStrokeThickness =
                (SeekBarPreference) findPreference(PREF_VOLUME_DIALOG_STROKE_THICKNESS);
        int volumeDialogStrokeThickness = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_STROKE_THICKNESS, 4);
        mVolumeDialogStrokeThickness.setValue(volumeDialogStrokeThickness / 1);
        mVolumeDialogStrokeThickness.setOnPreferenceChangeListener(this);

        // Volume dialog corner radius
        mVolumeDialogCornerRadius =
                (SeekBarPreference) findPreference(PREF_VOLUME_DIALOG_CORNER_RADIUS);
        int volumeDialogCornerRadius = Settings.System.getInt(mResolver,
                Settings.System.VOLUME_DIALOG_CORNER_RADIUS, 2);
        mVolumeDialogCornerRadius.setValue(volumeDialogCornerRadius / 1);
        mVolumeDialogCornerRadius.setOnPreferenceChangeListener(this);

        VolumeDialogSettingsDisabler(volumeDialogStroke);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int color = Settings.System.getInt(mResolver,
                Settings.System.SETTINGS_ICON_COLOR, 0xFFFFFFFF);
        Drawable d = getResources().getDrawable(com.android.internal.R.drawable.ic_settings_backup_restore).mutate();
        d.setColorFilter(color, Mode.SRC_IN);
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(d)
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
        String hex;
        int intHex;

        if (preference == mBgColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_BG_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mSliderColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_SLIDER_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mSliderInactiveColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_SLIDER_INACTIVE_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mSliderIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_SLIDER_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mExpandButtonColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_EXPAND_BUTTON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mVolumeDialogStroke) {
            int volumeDialogStroke = Integer.parseInt((String) newValue);
            int index = mVolumeDialogStroke.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(mResolver, Settings.System.
                    VOLUME_DIALOG_STROKE, volumeDialogStroke, UserHandle.USER_CURRENT);
            mVolumeDialogStroke.setSummary(mVolumeDialogStroke.getEntries()[index]);
            VolumeDialogSettingsDisabler(volumeDialogStroke);
            return true;
        } else if (preference == mVolumeDialogStrokeColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_STROKE_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mVolumeDialogStrokeThickness) {
            int val = (Integer) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_STROKE_THICKNESS, val * 1);
            return true;
        } else if (preference == mVolumeDialogCornerRadius) {
            int val = (Integer) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.VOLUME_DIALOG_CORNER_RADIUS, val * 1);
            return true;
        }
        return false;
    }

    private void VolumeDialogSettingsDisabler(int volumeDialogStroke) {
        if (volumeDialogStroke == 0) {
            mVolumeDialogStrokeColor.setEnabled(false);
            mVolumeDialogStrokeThickness.setEnabled(false);
        } else if (volumeDialogStroke == 1) {
            mVolumeDialogStrokeColor.setEnabled(false);
            mVolumeDialogStrokeThickness.setEnabled(true);
        } else {
            mVolumeDialogStrokeColor.setEnabled(true);
            mVolumeDialogStrokeThickness.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_SHIT;
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

        VolumeDialogSettings getOwner() {
            return (VolumeDialogSettings) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case DLG_RESET:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reset)
                    .setMessage(R.string.dlg_reset_colors_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setNeutralButton(R.string.reset_android,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_BG_COLOR,
                                    MATERIAL_BLUE_GREY);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_ICON_COLOR,
                                    MATERIAL_GREEN);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_COLOR,
                                    MATERIAL_GREEN);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_INACTIVE_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_ICON_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_EXPAND_BUTTON_COLOR,
                                    WHITE);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_vrtoxin,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_BG_COLOR,
                                    0xff1b1f23);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_ICON_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_INACTIVE_COLOR,
                                    0xffff0000);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_SLIDER_ICON_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.VOLUME_DIALOG_EXPAND_BUTTON_COLOR,
                                    VRTOXIN_BLUE);
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
