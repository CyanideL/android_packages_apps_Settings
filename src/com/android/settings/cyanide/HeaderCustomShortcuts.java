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

    private static final String CLOCK_SHORTCUT = "clock_shortcut";
    private static final String CLOCK_LONG_SHORTCUT = "clock_long_shortcut";
    private static final String CALENDAR_SHORTCUT = "calendar_shortcut";
    private static final String CALENDAR_LONG_SHORTCUT = "calendar_long_shortcut";
    private static final String CYANIDE_SHORTCUT = "cyanide_shortcut";
    private static final String CYANIDE_LONG_SHORTCUT = "cyanide_long_shortcut";
    private static final String WEATHER_LONG_SHORTCUT = "weather_long_shortcut";

    private AppSelectListPreference mClockShortcut;
    private AppSelectListPreference mClockLongShortcut;
    private AppSelectListPreference mCalendarShortcut;
    private AppSelectListPreference mCalendarLongShortcut;
    private AppSelectListPreference mCyanideShortcut;
    private AppSelectListPreference mCyanideLongShortcut;
    private AppSelectListPreference mWeatherLongShortcut;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cyanide_header_custom_shortcuts);
        mResolver = getActivity().getContentResolver();

        mClockShortcut = (AppSelectListPreference) findPreference(CLOCK_SHORTCUT);
        mClockShortcut.setOnPreferenceChangeListener(this);

        mClockLongShortcut = (AppSelectListPreference) findPreference(CLOCK_LONG_SHORTCUT);
        mClockLongShortcut.setOnPreferenceChangeListener(this);

        mCalendarShortcut = (AppSelectListPreference) findPreference(CALENDAR_SHORTCUT);
        mCalendarShortcut.setOnPreferenceChangeListener(this);

        mCalendarLongShortcut = (AppSelectListPreference) findPreference(CALENDAR_LONG_SHORTCUT);
        mCalendarLongShortcut.setOnPreferenceChangeListener(this);

        mCyanideShortcut = (AppSelectListPreference) findPreference(CYANIDE_SHORTCUT);
        mCyanideShortcut.setOnPreferenceChangeListener(this);

        mCyanideLongShortcut = (AppSelectListPreference) findPreference(CYANIDE_LONG_SHORTCUT);
        mCyanideLongShortcut.setOnPreferenceChangeListener(this);

        mWeatherLongShortcut = (AppSelectListPreference) findPreference(WEATHER_LONG_SHORTCUT);
        mWeatherLongShortcut.setOnPreferenceChangeListener(this);

        updateShortcutSummary();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mClockShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CLOCK_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mClockLongShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CLOCK_LONG_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mCalendarShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CALENDAR_SHORTCUT, value);
            updateShortcutSummary();
        } else if (preference == mCalendarLongShortcut) {
            String value = (String) newValue;
            Settings.System.putString(mResolver, Settings.System.CALENDAR_LONG_SHORTCUT, value);
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

        mClockShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String clockShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CLOCK_SHORTCUT);
        if (clockShortcutIntentUri != null) {
            Intent clockShortcutIntent = null;
            try {
                clockShortcutIntent = Intent.parseUri(clockShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                clockShortcutIntent = null;
            }

            if(clockShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(clockShortcutIntent, 0);
                if (info != null) {
                    mClockShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mClockLongShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String clockLongShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CLOCK_LONG_SHORTCUT);
        if (clockLongShortcutIntentUri != null) {
            Intent clockLongShortcutIntent = null;
            try {
                clockLongShortcutIntent = Intent.parseUri(clockLongShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                clockLongShortcutIntent = null;
            }

            if(clockLongShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(clockLongShortcutIntent, 0);
                if (info != null) {
                    mClockLongShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mCalendarShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String calendarShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CALENDAR_SHORTCUT);
        if (calendarShortcutIntentUri != null) {
            Intent calendarShortcutIntent = null;
            try {
                calendarShortcutIntent = Intent.parseUri(calendarShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                calendarShortcutIntent = null;
            }

            if(calendarShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(calendarShortcutIntent, 0);
                if (info != null) {
                    mCalendarShortcut.setSummary(info.loadLabel(packageManager));
                }
            }
        }

        mCalendarLongShortcut.setSummary(getResources().getString(R.string.default_shortcut));
        String calendarLongShortcutIntentUri = Settings.System.getString(mResolver, Settings.System.CALENDAR_LONG_SHORTCUT);
        if (calendarLongShortcutIntentUri != null) {
            Intent calendarLongShortcutIntent = null;
            try {
                calendarLongShortcutIntent = Intent.parseUri(calendarLongShortcutIntentUri, 0);
            } catch (URISyntaxException e) {
                calendarLongShortcutIntent = null;
            }

            if(calendarLongShortcutIntent != null) {
                ResolveInfo info = packageManager.resolveActivity(calendarLongShortcutIntent, 0);
                if (info != null) {
                    mCalendarLongShortcut.setSummary(info.loadLabel(packageManager));
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
