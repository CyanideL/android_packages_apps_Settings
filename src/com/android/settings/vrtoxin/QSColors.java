/*
 * Copyright (C) 2015 DarkKat
 *               2015 CyanideL
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
package com.android.settings.vrtoxin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.vrtoxin.qs.QSTiles;
import com.android.internal.logging.MetricsLogger;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class QSColors extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String QS_BRIGHTNESS_SLIDER_COLOR =
            "qs_brightness_slider_color";
    private static final String QS_BRIGHTNESS_SLIDER_BG_COLOR =
            "qs_brightness_slider_bg_color";
    private static final String QS_BRIGHTNESS_SLIDER_ICON_COLOR =
            "qs_brightness_slider_icon_color";
    private static final String PREF_QS_ICON_COLOR =
            "qs_icon_color";
    private static final String PREF_QS_RIPPLE_COLOR =
            "qs_ripple_color";
    private static final String PREF_QS_TEXT_COLOR =
            "qs_text_color";
    private static final String PREF_GRADIENT_ORIENTATION =
            "qs_background_gradient_orientation";
    private static final String PREF_USE_CENTER_COLOR =
            "qs_background_gradient_use_center_color";
    private static final String PREF_START_COLOR =
            "qs_background_color_start";
    private static final String PREF_CENTER_COLOR =
            "qs_background_color_center";
    private static final String PREF_END_COLOR =
            "qs_background_color_end";
    private static final String BG_COLORS =
            "qs_bg_colors";

    private static final int DEFAULT_BACKGROUND_COLOR = 0xff263238;
    private static final int WHITE = 0xffffffff;
    private static final int CYANIDE_BLUE = 0xff1976D2;
    private static final int BLACK = 0xff000000;

    private static final int BACKGROUND_ORIENTATION_T_B = 270;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private ColorPickerPreference mQSBrightnessSliderColor;
    private ColorPickerPreference mQSBrightnessSliderBgColor;
    private ColorPickerPreference mQSBrightnessSliderIconColor;
    private ColorPickerPreference mQSIconColor;
    private ColorPickerPreference mQSRippleColor;
    private ColorPickerPreference mQSTextColor;
    private SwitchPreference mUseCenterColor;
    private ColorPickerPreference mStartColor;
    private ColorPickerPreference mCenterColor;
    private ColorPickerPreference mEndColor;
    private ListPreference mGradientOrientation;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        refreshSettings();
    }

    public void refreshSettings() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.qs_color_settings);
        mResolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

        PreferenceCategory catBgColors =
                (PreferenceCategory) findPreference(BG_COLORS);

        mQSBrightnessSliderColor =
                (ColorPickerPreference) findPreference(QS_BRIGHTNESS_SLIDER_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_COLOR,
                DEFAULT_BACKGROUND_COLOR); 
        mQSBrightnessSliderColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSBrightnessSliderColor.setSummary(hexColor);
        mQSBrightnessSliderColor.setOnPreferenceChangeListener(this);

        mQSBrightnessSliderBgColor =
                (ColorPickerPreference) findPreference(QS_BRIGHTNESS_SLIDER_BG_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_BG_COLOR,
                DEFAULT_BACKGROUND_COLOR); 
        mQSBrightnessSliderBgColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSBrightnessSliderBgColor.setSummary(hexColor);
        mQSBrightnessSliderBgColor.setOnPreferenceChangeListener(this);

        mQSBrightnessSliderIconColor =
                (ColorPickerPreference) findPreference(QS_BRIGHTNESS_SLIDER_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_ICON_COLOR,
                WHITE); 
        mQSBrightnessSliderIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSBrightnessSliderIconColor.setSummary(hexColor);
        mQSBrightnessSliderIconColor.setOnPreferenceChangeListener(this);

        mQSIconColor =
                (ColorPickerPreference) findPreference(PREF_QS_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_ICON_COLOR, WHITE); 
        mQSIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSIconColor.setSummary(hexColor);
        mQSIconColor.setOnPreferenceChangeListener(this);
        mQSIconColor.setAlphaSliderEnabled(true);

        mQSRippleColor =
                (ColorPickerPreference) findPreference(PREF_QS_RIPPLE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_RIPPLE_COLOR, WHITE); 
        mQSRippleColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSRippleColor.setSummary(hexColor);
        mQSRippleColor.setOnPreferenceChangeListener(this);

        mQSTextColor =
                (ColorPickerPreference) findPreference(PREF_QS_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_TEXT_COLOR, WHITE); 
        mQSTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQSTextColor.setSummary(hexColor);
        mQSTextColor.setOnPreferenceChangeListener(this);
        mQSTextColor.setAlphaSliderEnabled(true);

        mGradientOrientation =
                (ListPreference) findPreference(PREF_GRADIENT_ORIENTATION);
        final int orientation = Settings.System.getInt(mResolver,
                Settings.System.QS_BACKGROUND_GRADIENT_ORIENTATION,
                BACKGROUND_ORIENTATION_T_B);
        mGradientOrientation.setValue(String.valueOf(orientation));
        mGradientOrientation.setSummary(mGradientOrientation.getEntry());
        mGradientOrientation.setOnPreferenceChangeListener(this);

        mStartColor =
                (ColorPickerPreference) findPreference(PREF_START_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BACKGROUND_COLOR_START, BLACK); 
        mStartColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mStartColor.setSummary(hexColor);
        mStartColor.setDefaultColors(BLACK, BLACK);
        mStartColor.setOnPreferenceChangeListener(this);

        final boolean useCenterColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BACKGROUND_GRADIENT_USE_CENTER_COLOR, 0) == 1;;

        mUseCenterColor = (SwitchPreference) findPreference(PREF_USE_CENTER_COLOR);
        mUseCenterColor.setChecked(useCenterColor);
        mUseCenterColor.setOnPreferenceChangeListener(this);

        mStartColor.setTitle(getResources().getString(R.string.background_start_color_title));

        if (useCenterColor) {
            mCenterColor =
                    (ColorPickerPreference) findPreference(PREF_CENTER_COLOR);
            intColor = Settings.System.getInt(mResolver,
                    Settings.System.QS_BACKGROUND_COLOR_CENTER, BLACK); 
            mCenterColor.setNewPreviewColor(intColor);
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mCenterColor.setSummary(hexColor);
            mCenterColor.setDefaultColors(BLACK, BLACK);
            mCenterColor.setOnPreferenceChangeListener(this);
        } else {
            catBgColors.removePreference(findPreference(PREF_CENTER_COLOR));
        }

        mEndColor =
                (ColorPickerPreference) findPreference(PREF_END_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.QS_BACKGROUND_COLOR_END, BLACK); 
        mEndColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mEndColor.setSummary(hexColor);
        mEndColor.setDefaultColors(BLACK, BLACK);
        mEndColor.setOnPreferenceChangeListener(this);

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

        if (preference == mQSIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mQSBrightnessSliderColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mQSBrightnessSliderBgColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_BG_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mQSBrightnessSliderIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_BRIGHTNESS_SLIDER_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mQSRippleColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_RIPPLE_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mQSTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.QS_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mUseCenterColor) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.QS_BACKGROUND_GRADIENT_USE_CENTER_COLOR,
                    value ? 1 : 0);
            refreshSettings();
            return true;
        } else if (preference == mStartColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.QS_BACKGROUND_COLOR_START, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mCenterColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.QS_BACKGROUND_COLOR_CENTER, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mEndColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.QS_BACKGROUND_COLOR_END, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mGradientOrientation) {
            int intValue = Integer.valueOf((String) newValue);
            int index = mGradientOrientation.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.QS_BACKGROUND_GRADIENT_ORIENTATION,
                    intValue);
            mGradientOrientation.setSummary(mGradientOrientation.getEntries()[index]);
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

        QSColors getOwner() {
            return (QSColors) getTargetFragment();
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
                                    Settings.System.QS_BACKGROUND_COLOR_START,
                                    DEFAULT_BACKGROUND_COLOR);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_COLOR,
                                    DEFAULT_BACKGROUND_COLOR);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_BG_COLOR,
                                    DEFAULT_BACKGROUND_COLOR);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_ICON_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_ICON_COLOR, WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_RIPPLE_COLOR, WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_TEXT_COLOR, WHITE);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_vrtoxin,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BACKGROUND_COLOR_START,
                                    0xff000000);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_COLOR,
                                    0xffff0000);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_BG_COLOR,
                                    0xff00ff00);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_BRIGHTNESS_SLIDER_ICON_COLOR,
                                    0xff00ff00);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_ICON_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_RIPPLE_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.QS_TEXT_COLOR,
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

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_SHIT;
    }
}
