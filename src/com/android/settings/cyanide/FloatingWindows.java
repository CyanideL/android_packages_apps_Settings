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


import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.os.UserHandle;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.List;

public class FloatingWindows extends SettingsPreferenceFragment
            implements OnPreferenceChangeListener  {

    private static final String PREF_HEADS_UP_FLOATING =
	    "heads_up_floating";
	private static final String GESTURE_ANYWHERE_FLOATING =
	    "gesture_anywhere_floating";

    SwitchPreference mHeadsUpFloatingWindow;
    SwitchPreference mGestureAnywhereFloatingWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.floating_windows);

    mHeadsUpFloatingWindow = (SwitchPreference) findPreference(PREF_HEADS_UP_FLOATING);
    mHeadsUpFloatingWindow.setChecked(Settings.System.getIntForUser(getContentResolver(),
            Settings.System.HEADS_UP_FLOATING, 1, UserHandle.USER_CURRENT) == 1);
    mHeadsUpFloatingWindow.setOnPreferenceChangeListener(this);

    mGestureAnywhereFloatingWindow = (SwitchPreference) findPreference(PREF_HEADS_UP_FLOATING);
    mGestureAnywhereFloatingWindow.setChecked(Settings.System.getIntForUser(getContentResolver(),
            Settings.System.GESTURE_ANYWHERE_FLOATING, 1, UserHandle.USER_CURRENT) == 1);
    mGestureAnywhereFloatingWindow.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHeadsUpFloatingWindow) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.HEADS_UP_FLOATING,
            (Boolean) newValue ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mGestureAnywhereFloatingWindow) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.GESTURE_ANYWHERE_FLOATING,
            (Boolean) newValue ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }
}
