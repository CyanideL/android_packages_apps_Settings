/*
 * Copyright (C) 2015 The CyanogenMod Project
 * Copyright (C) 2015 The TeamEos Project
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
package com.android.settings.cyanogenmod;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import android.provider.SearchIndexableResource;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.cyanogenmod.qs.DraggableGridView;
import com.android.settings.cyanogenmod.qs.QSTiles;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.List;
import com.android.internal.widget.LockPatternUtils;

public class NotificationDrawerSettings extends SettingsPreferenceFragment implements Indexable,
        Preference.OnPreferenceChangeListener {

    private static final String PREF_QS_TYPE = "qs_type";
    private static final String QUICK_PULLDOWN = "quick_pulldown";
    private static final String PREF_SMART_PULLDOWN = "smart_pulldown";
    private static final String PREF_BLOCK_ON_SECURE_KEYGUARD = "block_on_secure_keyguard";
    private static final String PANEL = "qs_cat_panel";
    private static final String BAR = "qs_cat_bar";
    private static final String PREF_QS_OPTIONS_CAT = "qs_options_cat";
    private static final String QS_MAIN_TILES = "sysui_qs_main_tiles";
    private static final String QS_NUM_COLUMNS = "sysui_qs_num_columns";
    private static final String QS_VIBRATE = "quick_settings_vibrate";
    private static final String QS_COLLAPSE_PANEL = "quick_settings_collapse_panel";
    private static final String QS_SHOW_BRIGHTNESS_SLIDER = "qs_show_brightness_slider";
    private static final String QS_WIFI_DETAIL = "qs_wifi_detail";
    private static final String QS_LOCATION_ADVANCED = "qs_location_advanced";
    private static final String QS_LS_HIDE_TILES_SENSITIVE_DATA = "lockscreen_hide_qs_tiles_with_sensitive_data";

    private PreferenceCategory mPanelBarOptions;

    private ListPreference mQSType;
    private ListPreference mQuickPulldown;
    private ListPreference mSmartPulldown;
    private SwitchPreference mBlockOnSecureKeyguard;
    private ListPreference mNumColumns;
    private SwitchPreference mQsMainTiles;
    private SwitchPreference mQsVibrate;
    private SwitchPreference mQsCollapsePanel;
    private SwitchPreference mQsHideSensitiveData;
    private SwitchPreference mQsBrightnessSlider;
    private SwitchPreference mQsWifiDetail;
    private SwitchPreference mQsLocationAdvanced;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshSettings();
    }

    public void refreshSettings() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.notification_drawer_settings);
        mResolver = getActivity().getContentResolver();

        mQSType = (ListPreference) findPreference(PREF_QS_TYPE);
        mQSType.setOnPreferenceChangeListener(this);
        int type = Settings.System.getInt(mResolver,
               Settings.System.QS_TYPE, 0);
        mQSType.setValue(String.valueOf(type));
        mQSType.setSummary(mQSType.getEntry());

        mPanelBarOptions = (PreferenceCategory) findPreference(PREF_QS_OPTIONS_CAT);

        mQuickPulldown = (ListPreference) findPreference(QUICK_PULLDOWN);
        mQuickPulldown.setOnPreferenceChangeListener(this);
        int quickPulldownValue = Settings.System.getIntForUser(mResolver,
                Settings.System.QS_QUICK_PULLDOWN, 1, UserHandle.USER_CURRENT);
        mQuickPulldown.setValue(String.valueOf(quickPulldownValue));
        updatePulldownSummary(quickPulldownValue);

        // Smart Pulldown
        mSmartPulldown = (ListPreference) findPreference(PREF_SMART_PULLDOWN);
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldown = Settings.System.getInt(mResolver,
                Settings.System.QS_SMART_PULLDOWN, 1);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);
        
        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
        mBlockOnSecureKeyguard = (SwitchPreference) findPreference(PREF_BLOCK_ON_SECURE_KEYGUARD);
        if (lockPatternUtils.isSecure()) {
            mBlockOnSecureKeyguard.setChecked(Settings.Secure.getInt(mResolver,
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD, 0) == 1);
            mBlockOnSecureKeyguard.setOnPreferenceChangeListener(this);
		}

        mNumColumns = (ListPreference) findPreference("sysui_qs_num_columns");
        int numColumns = Settings.Secure.getIntForUser(mResolver,
                Settings.Secure.QS_NUM_TILE_COLUMNS, getDefaultNumColums(),
                UserHandle.USER_CURRENT);
        mNumColumns.setValue(String.valueOf(numColumns));
        updateNumColumnsSummary(numColumns);
        mNumColumns.setOnPreferenceChangeListener(this);
        DraggableGridView.setColumnCount(numColumns);

        mQsMainTiles = (SwitchPreference) findPreference(QS_MAIN_TILES);
        mQsMainTiles.setChecked(Settings.Secure.getInt(mResolver,
            Settings.Secure.QS_USE_MAIN_TILES, 0) == 1);
        mQsMainTiles.setOnPreferenceChangeListener(this);

        mQsVibrate = (SwitchPreference) findPreference(QS_VIBRATE);
        mQsVibrate.setChecked(Settings.System.getInt(mResolver,
            Settings.System.QUICK_SETTINGS_TILES_VIBRATE, 0) == 1);
        mQsVibrate.setOnPreferenceChangeListener(this);

        mQsCollapsePanel = (SwitchPreference) findPreference(QS_COLLAPSE_PANEL);
        mQsCollapsePanel.setChecked(Settings.System.getInt(mResolver,
            Settings.System.QUICK_SETTINGS_COLLAPSE_PANEL, 0) == 1);
        mQsCollapsePanel.setOnPreferenceChangeListener(this);

        mQsHideSensitiveData = (SwitchPreference) findPreference(QS_LS_HIDE_TILES_SENSITIVE_DATA);
        mQsHideSensitiveData.setChecked(Settings.Secure.getInt(mResolver,
            Settings.Secure.LOCKSCREEN_HIDE_TILES_WITH_SENSITIVE_DATA, 0) == 1);
        mQsHideSensitiveData.setOnPreferenceChangeListener(this);

        mQsBrightnessSlider = (SwitchPreference) findPreference(QS_SHOW_BRIGHTNESS_SLIDER);
        mQsBrightnessSlider.setChecked(Settings.Secure.getInt(mResolver,
            Settings.Secure.QS_SHOW_BRIGHTNESS_SLIDER, 1) == 1);
        mQsBrightnessSlider.setOnPreferenceChangeListener(this);

        mQsWifiDetail = (SwitchPreference) findPreference(QS_WIFI_DETAIL);
        mQsWifiDetail.setChecked(Settings.Secure.getInt(mResolver,
            Settings.Secure.QS_WIFI_DETAIL, 0) == 1);
        mQsWifiDetail.setOnPreferenceChangeListener(this);

        mQsLocationAdvanced = (SwitchPreference) findPreference(QS_LOCATION_ADVANCED);
        mQsLocationAdvanced.setChecked(Settings.Secure.getInt(mResolver,
            Settings.Secure.QS_LOCATION_ADVANCED, 0) == 1);
        mQsLocationAdvanced.setOnPreferenceChangeListener(this);

        hideBarOptions();
        hideAllOptions();
    }

    /*@Override
    public void onResume() {
        super.onResume();

        int qsTileCount = QSTiles.determineTileCount(getActivity());
        mQSTiles.setSummary(getResources().getQuantityString(R.plurals.qs_tiles_summary,
                    qsTileCount, qsTileCount));
    }*/

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mQSType) {
            int intValue = Integer.valueOf((String) newValue);
            int index = mQSType.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                Settings.System.QS_TYPE, intValue);
            preference.setSummary(mQSType.getEntries()[index]);
            hideBarOptions();
            hideAllOptions();
            refreshSettings();
            return true;
        } else if (preference == mQuickPulldown) {
            int quickPulldownValue = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(mResolver, Settings.System.QS_QUICK_PULLDOWN,
                    quickPulldownValue, UserHandle.USER_CURRENT);
            updatePulldownSummary(quickPulldownValue);
            return true;
        } else if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(mResolver, Settings.System.QS_SMART_PULLDOWN,
                    smartPulldown, UserHandle.USER_CURRENT);
            updateSmartPulldownSummary(smartPulldown);
            return true;
        } else if (preference == mBlockOnSecureKeyguard) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD,
                    (Boolean) newValue ? 1 : 0);
            return true;
		} else if (preference == mNumColumns) {
            int numColumns = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(mResolver, Settings.Secure.QS_NUM_TILE_COLUMNS,
                    numColumns, UserHandle.USER_CURRENT);
            updateNumColumnsSummary(numColumns);
            DraggableGridView.setColumnCount(numColumns);
            return true;
		} else if (preference == mQsMainTiles) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.QS_USE_MAIN_TILES,
            (Boolean) newValue ? 1 : 0);
            return true;
		} else if (preference == mQsVibrate) {
            Settings.System.putInt(mResolver,
                    Settings.System.QUICK_SETTINGS_TILES_VIBRATE,
            (Boolean) newValue ? 1 : 0);
            return true;
       } else if (preference == mQsCollapsePanel) {
            Settings.System.putInt(mResolver,
                    Settings.System.QUICK_SETTINGS_COLLAPSE_PANEL,
            (Boolean) newValue ? 1 : 0);
            return true;
       } else if (preference == mQsHideSensitiveData) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.LOCKSCREEN_HIDE_TILES_WITH_SENSITIVE_DATA,
            (Boolean) newValue ? 1 : 0);
            return true;
       } else if (preference == mQsBrightnessSlider) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.QS_SHOW_BRIGHTNESS_SLIDER,
            (Boolean) newValue ? 1 : 0);
            return true;
       } else if (preference == mQsWifiDetail) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.QS_WIFI_DETAIL,
            (Boolean) newValue ? 1 : 0);
            return true;
       } else if (preference == mQsLocationAdvanced) {
            Settings.Secure.putInt(mResolver,
                    Settings.Secure.QS_LOCATION_ADVANCED,
            (Boolean) newValue ? 1 : 0);
            return true;
        }
        return false;
    }

    private void updatePulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_off));
        } else {
            String direction = res.getString(value == 2
                    ? R.string.quick_pulldown_summary_left
                    : R.string.quick_pulldown_summary_right);
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_summary, direction));
        }
    }

    private void updateSmartPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // Smart pulldown deactivated
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
        } else {
            String type = null;
            switch (value) {
                case 1:
                    type = res.getString(R.string.smart_pulldown_dismissable);
                    break;
                case 2:
                    type = res.getString(R.string.smart_pulldown_persistent);
                    break;
                default:
                    type = res.getString(R.string.smart_pulldown_all);
                    break;
            }
            // Remove title capitalized formatting
            type = type.toLowerCase();
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
        }
    }

    private void updateNumColumnsSummary(int numColumns) {
        String prefix = (String) mNumColumns.getEntries()[mNumColumns.findIndexOfValue(String
                .valueOf(numColumns))];
        mNumColumns.setSummary(getResources().getString(R.string.qs_num_columns_showing, prefix));
    }

    private int getDefaultNumColums() {
        try {
            Resources res = getPackageManager()
                    .getResourcesForApplication("com.android.systemui");
            int val = res.getInteger(res.getIdentifier("quick_settings_num_columns", "integer",
                    "com.android.systemui")); // better not be larger than 5, that's as high as the
                                              // list goes atm
            return Math.max(1, val);
        } catch (Exception e) {
            return 3;
        }
    }

    private void hideBarOptions() {
        if (Settings.System.getInt(mResolver,
            Settings.System.QS_TYPE, 0) == 0) {
            removePreference(BAR);
        } else {
            removePreference(PANEL);
            hidePanelOptions();
        }
    }

    private void hidePanelOptions() {
        if (Settings.System.getInt(mResolver,
            Settings.System.QS_TYPE, 1) == 1) {
            mPanelBarOptions.removePreference(mQsMainTiles);
            mPanelBarOptions.removePreference(mNumColumns);
            mPanelBarOptions.removePreference(mQsVibrate);
            mPanelBarOptions.removePreference(mQsCollapsePanel);
            mPanelBarOptions.removePreference(mQsHideSensitiveData);
            //mPanelBarOptions.removePreference(mQsBrightnessSlider);
            mPanelBarOptions.removePreference(mQsWifiDetail);
            mPanelBarOptions.removePreference(mQsLocationAdvanced);
        }
    }

    private void hideAllOptions() {
        if (Settings.System.getInt(mResolver,
            Settings.System.QS_TYPE, 2) == 2) {
            removePreference(BAR);
            removePreference(PREF_QS_OPTIONS_CAT);
        }
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.notification_drawer_settings;
                    result.add(sir);

                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    return new ArrayList<String>();
                }
            };
}
