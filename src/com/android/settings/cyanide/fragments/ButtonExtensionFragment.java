/* 
 * Copyright (C) 2015 DarkKat
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

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class ButtonExtensionFragment extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PREF_CAT_NOTIFICATIONS =
            "buttons_cat_notifications";
    private static final String PREF_BUTTONS_BAR_EXTENSION_LAUNCH_TYPE =
            "buttons_bar_extension_launch_type";
    private static final String PREF_HIDE_BUTTONS_BAR_EXTENSION =
            "buttons_bar_hide_bar_extension";
    private static final String PREF_NUMBER_OF_NOTIFICATIONS =
            "buttons_bar_extension_number_of_notifications";

    private ListPreference mButtonsBarExtensionLaunchType;
    private ListPreference mHideButtonsBarExtension;
    private ListPreference mNumberOfNotifications;

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

        addPreferencesFromResource(R.xml.ls_buttons_extension);

        mResolver = getActivity().getContentResolver();

        int intValue;

        PreferenceCategory catNotifications =
                (PreferenceCategory) findPreference(PREF_CAT_NOTIFICATIONS);

        mButtonsBarExtensionLaunchType =
                (ListPreference) findPreference(PREF_BUTTONS_BAR_EXTENSION_LAUNCH_TYPE);
        intValue = Settings.System.getInt(mResolver,
                Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_LAUNCH_TYPE, 2);
        mButtonsBarExtensionLaunchType.setValue(String.valueOf(intValue));
        mButtonsBarExtensionLaunchType.setSummary(mButtonsBarExtensionLaunchType.getEntry());
        mButtonsBarExtensionLaunchType.setOnPreferenceChangeListener(this);

        mHideButtonsBarExtension =
                (ListPreference) findPreference(PREF_HIDE_BUTTONS_BAR_EXTENSION);
        int hideButtonsBarExtension = Settings.System.getInt(mResolver,
                Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_HIDE_BAR, 1);
        mHideButtonsBarExtension.setValue(String.valueOf(hideButtonsBarExtension));
        mHideButtonsBarExtension.setOnPreferenceChangeListener(this);

        if (hideButtonsBarExtension == 0) {
            mHideButtonsBarExtension.setSummary(R.string.buttons_bar_hide_bar_auto_summary);
            catNotifications.removePreference(findPreference(PREF_NUMBER_OF_NOTIFICATIONS));
        } else if (hideButtonsBarExtension == 1) {
            mNumberOfNotifications =
                    (ListPreference) findPreference(PREF_NUMBER_OF_NOTIFICATIONS);
            intValue = Settings.System.getInt(mResolver,
                   Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_NUMBER_OF_NOTIFICATIONS, 4);
            mNumberOfNotifications.setValue(String.valueOf(intValue));
            mNumberOfNotifications.setSummary(mNumberOfNotifications.getEntry());
            mNumberOfNotifications.setOnPreferenceChangeListener(this);

            mHideButtonsBarExtension.setSummary(getString(R.string.buttons_bar_hide_bar_custom_summary,
                    mNumberOfNotifications.getEntry()));
        } else {
            mHideButtonsBarExtension.setSummary(R.string.buttons_bar_hide_bar_never_summary);
            catNotifications.removePreference(findPreference(PREF_NUMBER_OF_NOTIFICATIONS));
        }

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int intValue;
        int index;

        if (preference == mButtonsBarExtensionLaunchType) {
            intValue = Integer.valueOf((String) newValue);
            index = mButtonsBarExtensionLaunchType.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_LAUNCH_TYPE, intValue);
            preference.setSummary(mButtonsBarExtensionLaunchType.getEntries()[index]);
            return true;
        } else if (preference == mHideButtonsBarExtension) {
            intValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_HIDE_BAR, intValue);
            refreshSettings();
            return true;
        } else if (preference == mNumberOfNotifications) {
            intValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.LOCK_SCREEN_BUTTONS_BAR_EXTENSION_NUMBER_OF_NOTIFICATIONS, intValue);
            refreshSettings();
            return true;
        }
        return false;
    }
}
