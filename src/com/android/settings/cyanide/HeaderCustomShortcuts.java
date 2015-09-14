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

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.quicklaunch.BookmarkPicker;

import java.net.URISyntaxException;

import com.android.settings.cyanide.AppSelectListPreference;

public class HeaderCustomShortcuts extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String HEADER_BUTTON_SHORTCUT = "header_button_shortcut";
    private static final String HEADER_BUTTON_LONG_SHORTCUT = "header_button_long_shortcut";
    private static final String CYANIDE_SHORTCUT = "cyanide_shortcut";
    private static final String CYANIDE_LONG_SHORTCUT = "cyanide_long_shortcut";
    private static final String WEATHER_LONG_SHORTCUT = "weather_long_shortcut";

    private AppSelectListPreference mHeaderShortcut;
    private AppSelectListPreference mHeaderLongShortcut;
    private AppSelectListPreference mCyanideShortcut;
    private AppSelectListPreference mCyanideLongShortcut;
    private AppSelectListPreference mWeatherLongShortcut;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cyanide_header_custom_shortcuts);
        mResolver = getActivity().getContentResolver();

        mHeaderShortcut = (AppSelectListPreference) findPreference(HEADER_BUTTON_SHORTCUT);
        mHeaderShortcut.setOnPreferenceChangeListener(this);

        mHeaderLongShortcut = (AppSelectListPreference) findPreference(HEADER_BUTTON_LONG_SHORTCUT);
        mHeaderLongShortcut.setOnPreferenceChangeListener(this);

        mCyanideShortcut = (AppSelectListPreference) findPreference(CYANIDE_SHORTCUT);
        mCyanideShortcut.setOnPreferenceChangeListener(this);

        mCyanideLongShortcut = (AppSelectListPreference) findPreference(CYANIDE_LONG_SHORTCUT);
        mCyanideLongShortcut.setOnPreferenceChangeListener(this);

        mWeatherLongShortcut = (AppSelectListPreference) findPreference(WEATHER_LONG_SHORTCUT);
        mWeatherLongShortcut.setOnPreferenceChangeListener(this);

        updateShortcutSummary();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHeaderShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.HEADER_BUTTON_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mHeaderLongShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.HEADER_BUTTON_LONG_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mCyanideShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CYANIDE_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mCyanideLongShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CYANIDE_LONG_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mWeatherLongShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.WEATHER_LONG_SHORTCUT, value);
            updateShortcutSummary();
        }
        return false;
    }

    private void updateShortcutSummary() {
        final PackageManager packageManager = getPackageManager();

        mHeaderShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String headerShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.HEADER_BUTTON_SHORTCUT);
        if (headerShortcutIntentUri != null) {
            Intent headerShortcutIntent = null;
            try {
                headerShortcutIntent = Intent.parseUri(headerShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                headerShortcutIntent = null;
            }

            if(headerShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(headerShortcutIntent, 0);
                if (info != null) {
                    mHeaderShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mHeaderLongShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String headerLongShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.HEADER_BUTTON_LONG_SHORTCUT);
        if (headerLongShortcutIntentUri != null) {
            Intent headerLongShortcutIntent = null;
            try {
                headerLongShortcutIntent = Intent.parseUri(headerLongShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                headerLongShortcutIntent = null;
            }

            if(headerLongShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(headerLongShortcutIntent, 0);
                if (info != null) {
                    mHeaderLongShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mCyanideShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String cyanideShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CYANIDE_SHORTCUT);
        if (cyanideShortcutIntentUri != null) {
            Intent cyanideShortcutIntent = null;
            try {
                cyanideShortcutIntent = Intent.parseUri(cyanideShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                cyanideShortcutIntent = null;
            }

            if(cyanideShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(cyanideShortcutIntent, 0);
                if (info != null) {
                    mCyanideShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mCyanideLongShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String cyanideLongShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CYANIDE_LONG_SHORTCUT);
        if (cyanideLongShortcutIntentUri != null) {
            Intent cyanideLongShortcutIntent = null;
            try {
                cyanideLongShortcutIntent = Intent.parseUri(cyanideLongShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                cyanideLongShortcutIntent = null;
            }

            if(cyanideLongShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(cyanideLongShortcutIntent, 0);
                if (info != null) {
                    mCyanideLongShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mWeatherLongShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String weatherLongShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.WEATHER_LONG_SHORTCUT);
        if (weatherLongShortcutIntentUri != null) {
            Intent weatherLongShortcutIntent = null;
            try {
                weatherLongShortcutIntent = Intent.parseUri(weatherLongShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                weatherLongShortcutIntent = null;
            }

            if(weatherLongShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(weatherLongShortcutIntent, 0);
                if (info != null) {
                    mWeatherLongShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }
    }
}
