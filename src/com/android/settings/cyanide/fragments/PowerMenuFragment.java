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

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PowerMenuFragment extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String POWER_MENU_ONTHEGO_ENABLED = "power_menu_onthego_enabled";

    private SwitchPreference mOnTheGoPowerMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.power_menu_fragment);

        ContentResolver resolver = getActivity().getContentResolver();

        mOnTheGoPowerMenu = (SwitchPreference) findPreference(POWER_MENU_ONTHEGO_ENABLED);
        mOnTheGoPowerMenu.setChecked(Settings.System.getInt(resolver,
                Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1);
        mOnTheGoPowerMenu.setOnPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final ListView list = (ListView) view.findViewById(android.R.id.list);
        // our container already takes care of the padding
        if (list != null) {
            int paddingTop = list.getPaddingTop();
            int paddingBottom = list.getPaddingBottom();
            list.setPadding(0, paddingTop, 0, paddingBottom);
        }
        return view;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mOnTheGoPowerMenu) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.POWER_MENU_ONTHEGO_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }

}
