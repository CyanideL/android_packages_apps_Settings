/*
 * Copyright (C) 2016 Cyanide Android
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

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.vrtoxin.SeekBarPreference;
import com.android.internal.logging.MetricsLogger;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ModsMenuCustomizations extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PREF_BG_COLOR = "drawer_bg_color";
    private static final String PREF_ICON_COLOR = "drawer_icon_color";
    private static final String PREF_TEXT_COLOR = "drawer_text_color";
    private static final String PREF_FONT_STYLE = "drawer_font_style";
    private static final String MODS_UNDERLINE_COLOR = "mods_underline_color";
    private static final String MODS_DIVIDER_COLOR = "mods_divider_color";
    private static final String MODS_TAB_TEXT_COLOR = "mods_tab_text_color";
    private static final String KEY_CM_TABS_EFFECT = "tabs_effect";
    private static final String DRAWER_SCRIM_COLOR = "drawer_scrim_color";

    private ColorPickerPreference mBgColor;
    private ColorPickerPreference mIconColor;
    private ColorPickerPreference mTextColor;
    private ListPreference mDrawerFontStyle;
    private ColorPickerPreference mUnderlineColor;
    private ColorPickerPreference mDividerColor;
    private ColorPickerPreference mTabTextColor;
    private ListPreference mListViewTabsEffect;
    private ColorPickerPreference mScrimAlpha;

    private static final int TRANSLUCENT_BLACK = 0x80000000;
    private static final int CYANIDE_BLUE = 0xff1976D2;
    private static final int CYANIDE_GREEN = 0xff00ff00;
    private static final int WHITE = 0xffffffff;
    private static final int BLACK = 0xff000000;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

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

        addPreferencesFromResource(R.xml.mods_menu_options);
        mResolver = getActivity().getContentResolver();

        int intColor = 0xffffffff;
        String hexColor = String.format("#%08x", (0xffffffff & 0xffffffff));

        mBgColor =
                (ColorPickerPreference) findPreference(PREF_BG_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.DRAWER_BG_COLOR, TRANSLUCENT_BLACK);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mBgColor.setNewPreviewColor(intColor);
        mBgColor.setSummary(hexColor);
        mBgColor.setOnPreferenceChangeListener(this);

        mIconColor =
                (ColorPickerPreference) findPreference(PREF_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.DRAWER_ICON_COLOR, CYANIDE_BLUE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mIconColor.setNewPreviewColor(intColor);
        mIconColor.setSummary(hexColor);
        mIconColor.setOnPreferenceChangeListener(this);

        mTextColor =
                (ColorPickerPreference) findPreference(PREF_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.DRAWER_TEXT_COLOR, WHITE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTextColor.setNewPreviewColor(intColor);
        mTextColor.setSummary(hexColor);
        mTextColor.setOnPreferenceChangeListener(this);

        mDrawerFontStyle = (ListPreference) findPreference(PREF_FONT_STYLE);
        mDrawerFontStyle.setOnPreferenceChangeListener(this);
        mDrawerFontStyle.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.DRAWER_FONT_STYLE, 0)));
        mDrawerFontStyle.setSummary(mDrawerFontStyle.getEntry());

        mUnderlineColor =
                (ColorPickerPreference) findPreference(MODS_UNDERLINE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MODS_UNDERLINE_COLOR, WHITE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mUnderlineColor.setNewPreviewColor(intColor);
        mUnderlineColor.setSummary(hexColor);
        mUnderlineColor.setOnPreferenceChangeListener(this);

        mDividerColor =
                (ColorPickerPreference) findPreference(MODS_DIVIDER_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MODS_DIVIDER_COLOR, WHITE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mDividerColor.setNewPreviewColor(intColor);
        mDividerColor.setSummary(hexColor);
        mDividerColor.setOnPreferenceChangeListener(this);

        mTabTextColor =
                (ColorPickerPreference) findPreference(MODS_TAB_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.MODS_TAB_TEXT_COLOR, WHITE);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTabTextColor.setNewPreviewColor(intColor);
        mTabTextColor.setSummary(hexColor);
        mTabTextColor.setOnPreferenceChangeListener(this);

        mListViewTabsEffect = (ListPreference) findPreference(KEY_CM_TABS_EFFECT);
        int tabsEffect = Settings.System.getInt(mResolver,
                Settings.System.MODS_TABS_EFFECT, 0);
        mListViewTabsEffect.setValue(String.valueOf(tabsEffect));
        mListViewTabsEffect.setSummary(mListViewTabsEffect.getEntry());
        mListViewTabsEffect.setOnPreferenceChangeListener(this);

        mScrimAlpha =
                (ColorPickerPreference) findPreference(DRAWER_SCRIM_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.DRAWER_SCRIM_COLOR, 0x00000000);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mScrimAlpha.setNewPreviewColor(intColor);
        mScrimAlpha.setSummary(hexColor);
        mScrimAlpha.setOnPreferenceChangeListener(this);

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
        int intHex;
        String hex;

        if (preference == mBgColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.DRAWER_BG_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.DRAWER_ICON_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.DRAWER_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mDrawerFontStyle) {
            int val = Integer.parseInt((String) newValue);
            int index = mDrawerFontStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DRAWER_FONT_STYLE, val);
            mDrawerFontStyle.setSummary(mDrawerFontStyle.getEntries()[index]);
            return true;
        } else if (preference == mUnderlineColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MODS_UNDERLINE_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mDividerColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MODS_DIVIDER_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mTabTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.MODS_TAB_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mListViewTabsEffect) {
            int value = Integer.valueOf((String) newValue);
            int index = mListViewTabsEffect.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                     Settings.System.MODS_TABS_EFFECT, value);
            mListViewTabsEffect.setSummary(mListViewTabsEffect.getEntries()[index]);
            return true;
        } else if (preference == mScrimAlpha) {
            hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                    Settings.System.DRAWER_SCRIM_COLOR, intHex);
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

        ModsMenuCustomizations getOwner() {
            return (ModsMenuCustomizations) getTargetFragment();
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
                                    Settings.System.DRAWER_BG_COLOR,
                                    TRANSLUCENT_BLACK);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_ICON_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_TEXT_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_FONT_STYLE,
                                    0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_UNDERLINE_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_DIVIDER_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_TAB_TEXT_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_SCRIM_COLOR,
                                    0x80000000);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_vrtoxin,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_BG_COLOR,
                                    TRANSLUCENT_BLACK);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_ICON_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_TEXT_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_FONT_STYLE,
                                    20);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_UNDERLINE_COLOR,
                                    CYANIDE_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_DIVIDER_COLOR,
                                    CYANIDE_GREEN);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.MODS_TAB_TEXT_COLOR,
                                    CYANIDE_GREEN);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.DRAWER_SCRIM_COLOR,
                                    0x00000000);
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