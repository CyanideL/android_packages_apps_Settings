/*
 * Copyright (C) 2010-2014 ParanoidAndroid Project
 * Copyright (C) 2015 Fusion & Cyanidel Project (PIE2.0 - Ported & modified)
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PAPieControl extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PA_PIE_STATE = "pa_pie_state";
    private static final String PA_PIE_SIZE = "pa_pie_size";
    private static final String PA_PIE_GRAVITY = "pa_pie_gravity";
    private static final String PA_PIE_MODE = "pa_pie_mode";
    private static final String PA_PIE_ANGLE = "pa_pie_angle";
    private static final String PA_PIE_GAP = "pa_pie_gap";

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    private SwitchPreference mPieState;
    private ListPreference mPieSize;
    private ListPreference mPieGravity;
    private ListPreference mPieMode;
    private ListPreference mPieAngle;
    private ListPreference mPieGap;

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
        addPreferencesFromResource(R.xml.pa_pie_control);

        Context context = getActivity();
        mResolver = context.getContentResolver();

        mPieState = (SwitchPreference) findPreference(PA_PIE_STATE);
        mPieState.setChecked(Settings.System.getInt(mResolver,
            Settings.System.PA_PIE_STATE, 0) == 1);
        mPieState.setOnPreferenceChangeListener(this);

        mPieSize = (ListPreference) findPreference(PA_PIE_SIZE);
            float pieSize = Settings.System.getFloat(mResolver,
                    Settings.System.PA_PIE_SIZE, 1.0f);
            mPieSize.setValue(String.valueOf(pieSize));
        mPieSize.setOnPreferenceChangeListener(this);

        mPieGravity = (ListPreference) findPreference(PA_PIE_GRAVITY);
        int pieGravity = Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_GRAVITY, 2);
        mPieGravity.setValue(String.valueOf(pieGravity));
        mPieGravity.setOnPreferenceChangeListener(this);

        mPieMode = (ListPreference) findPreference(PA_PIE_MODE);
        int pieMode = Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_MODE, 2);
        mPieMode.setValue(String.valueOf(pieMode));
        mPieMode.setOnPreferenceChangeListener(this);

        mPieGap = (ListPreference) findPreference(PA_PIE_GAP);
        int pieGap = Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_GAP, 2);
        mPieGap.setValue(String.valueOf(pieGap));
        mPieGap.setOnPreferenceChangeListener(this);

        mPieAngle = (ListPreference) findPreference(PA_PIE_ANGLE);
        int pieAngle = Settings.System.getInt(mResolver,
                Settings.System.PA_PIE_ANGLE, 12);
        mPieAngle.setValue(String.valueOf(pieAngle));
        mPieAngle.setOnPreferenceChangeListener(this);

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

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mPieState) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                Settings.System.PA_PIE_STATE,
                value ? 1 : 0);
            return true;
        } else if (preference == mPieSize) {
            float pieSize = Float.valueOf((String) newValue);
            Settings.System.putFloat(mResolver,
                    Settings.System.PA_PIE_SIZE, pieSize);
            return true;
        } else if (preference == mPieGravity) {
            int pieGravity = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.PA_PIE_GRAVITY, pieGravity);
            return true;
        } else if (preference == mPieMode) {
            int pieMode = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.PA_PIE_MODE, pieMode);
            return true;
        } else if (preference == mPieAngle) {
            int pieAngle = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.PA_PIE_ANGLE, pieAngle);
            return true;
        } else if (preference == mPieGap) {
            int pieGap = Integer.valueOf((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.PA_PIE_GAP, pieGap);
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

        PAPieControl getOwner() {
            return (PAPieControl) getTargetFragment();
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
                                    Settings.System.PA_PIE_STATE, 0);
                            Settings.System.putFloat(getOwner().mResolver,
                                    Settings.System.PA_PIE_SIZE, 1.0f);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_GRAVITY, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_MODE, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_GAP, 2);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_ANGLE, 12);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_cyanide,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_STATE, 1);
                            Settings.System.putFloat(getOwner().mResolver,
                                    Settings.System.PA_PIE_SIZE, 1.2f);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_GRAVITY, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_MODE, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_GAP, 0);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.PA_PIE_ANGLE, 6);
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
