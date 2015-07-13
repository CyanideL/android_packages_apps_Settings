package com.android.settings.cyanide;

import android.os.Bundle;
import android.os.UserHandle;
import android.content.res.Resources;
import android.content.ContentResolver;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import android.provider.Settings;

public class CyanideNotifs extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "Notifs";
    
    private static final String KEY_CATEGORY_LIGHTS = "lights";

    private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
    private static final String KEY_HEADS_UP_SETTINGS = "heads_up_enabled";
    private static final String KEY_NOTIFICATION_LIGHT = "notification_light";
    private static final String KEY_BATTERY_LIGHT = "battery_light";

    private SwitchPreference mDisableIM;
    private Preference mHeadsUp;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.cyanide_notifs);

        final ContentResolver resolver = getActivity().getContentResolver();

        mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_IMMERSIVE_MESSAGE, 0) == 1));

        mHeadsUp = findPreference(KEY_HEADS_UP_SETTINGS);

        initPulse((PreferenceCategory) findPreference(KEY_CATEGORY_LIGHTS));
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean headsUpEnabled = Settings.System.getInt(
                getContentResolver(), Settings.System.HEADS_UP_USER_ENABLED, Settings.System.HEADS_UP_USER_ON) != 0;
        mHeadsUp.setSummary(headsUpEnabled
                ? R.string.summary_heads_up_enabled : R.string.summary_heads_up_disabled);
    }

    // === Pulse notification light ===

    private void initPulse(PreferenceCategory parent) {
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed)) {
            parent.removePreference(parent.findPreference(KEY_NOTIFICATION_LIGHT));
        }
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveBatteryLed)
                || UserHandle.myUserId() != UserHandle.USER_OWNER) {
            parent.removePreference(parent.findPreference(KEY_BATTERY_LIGHT));
        }
        if (parent.getPreferenceCount() == 0) {
            getPreferenceScreen().removePreference(parent);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if  (preference == mDisableIM) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_IMMERSIVE_MESSAGE, checked ? 1:0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }
}
