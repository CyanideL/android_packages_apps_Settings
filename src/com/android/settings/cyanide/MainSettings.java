/*
 * Copyright (C) 2014 The Dirty Unicorns Project
 *               2015 Fusion Project && CyanideL
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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.internal.util.cm.ScreenType;
import com.android.settings.cyanide.PagerSlidingTabStrip;
//import com.android.settings.cyanide.ProgressBar;
import com.android.settings.cyanide.AndroidRecentsSettings;
import com.android.settings.cyanide.CyanideNotifs;
import com.android.settings.cyanide.CyanideSound;
import com.android.settings.cyanide.FloatingWindows;
import com.android.settings.cyanide.hfm.HfmSettings;
import com.android.settings.cyanide.identicons.IdenticonsSettings;
import com.android.settings.cyanide.InterfaceSettings;
import com.android.settings.cyanide.MoreAnimations;
import com.android.settings.cyanide.PowerMenu;
import com.android.settings.cyanide.SlimSizer;
import com.android.settings.cyanide.WakeLockBlocker;
import com.android.settings.cyanide.WeatherControl;
import com.android.settings.cyanogenmod.NotificationDrawerSettings;
import com.android.settings.cyanogenmod.StatusBarSettings;
import com.android.settings.ButtonSettings;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.lockscreen.LockScreenSettings;

import java.util.ArrayList;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.List;

public class MainSettings extends SettingsPreferenceFragment {

    private static final int MENU_HELP  = 0;

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;

    static Bundle mSavedState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_settings_cyanide);

        View view = inflater.inflate(R.layout.preference_animations, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mTabs.setViewPager(mViewPager);

        setHasOptionsMenu(true);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }

     @Override
     public void onResume() {
         super.onResume();


        if (!ScreenType.isTablet(getActivity())) {
            mContainer.setPadding(30, 30, 30, 30);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_HELP, 0, "Help Us, Help You!!")
                .setIcon(R.drawable.ic_action_help)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HELP:
                showDialogInner(MENU_HELP);
                Toast.makeText(getActivity(),
                (Html.fromHtml("READ THE WHOLE THING!!")),
                Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
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

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case MENU_HELP:
                    return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.ic_action_help)
                    .setTitle(Html.fromHtml("<font color='" + getResources().getColor(R.color.red) + "'>Help Us, Help You!!</font>"))
                    .setMessage(Html.fromHtml("If you find a bug with any of these settings, please provide at least one of the following to the developers.<br><br><font color='" + getResources().getColor(R.color.red) + "'>1. Logcat</font><br><font color='" + getResources().getColor(R.color.red) + "'>2. What you were doing prior to your issue</font><br><font color='" + getResources().getColor(R.color.red) + "'>3. Your complete setup so we could possibly duplicate the issue</font><br>(<i>This means things like Kernel, Device, any MODs, etc</i>)<br><br>Providing us with little to no information does not help us, help you.<br><br>We are developers, <font color='" + getResources().getColor(R.color.red) + "'><big>NOT WIZARDS</big></font> and so we <font color='" + getResources().getColor(R.color.red) + "'><big>CAN NOT</big></font> read minds.<br><br>THANK YOU for your continued support!"))
                    .setCancelable(false)
                    .setNegativeButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new HfmSettings();
            frags[1] = new CyanideSound();
            frags[2] = new MoreAnimations();
            frags[3] = new PowerUsageSummary();
            frags[4] = new ButtonSettings();
            //frags[4] = new ProgressBar();
            frags[5] = new FloatingWindows();
            frags[6] = new IdenticonsSettings();
            frags[7] = new InterfaceSettings();
            frags[8] = new LockScreenSettings();
            frags[9] = new CyanideNotifs();
            frags[10] = new PowerMenu();
            frags[11] = new NotificationDrawerSettings();
            frags[12] = new AndroidRecentsSettings();
            frags[13] = new StatusBarSettings();
            frags[14] = new SlimSizer();
            frags[15] = new WakeLockBlocker();
            frags[16] = new WeatherControl();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }
    }

    private String[] getTitles() {
        String titleString[];
        if (!ScreenType.isPhone(getActivity())) {
        titleString = new String[]{
                    getString(R.string.hfm_pref_screen_title),
                    getString(R.string.cyanide_sound_title),
                    getString(R.string.cyanide_animations_title),
                    getString(R.string.power_usage_summary_title),
                    getString(R.string.button_pref_title),
                    getString(R.string.floating_windows),
                    getString(R.string.identicons_title),
                    getString(R.string.interface_settings_title),
                    getString(R.string.lockscreen_settings),
                    getString(R.string.cyanide_notifs_title),
                    getString(R.string.power_menu_title),
                    getString(R.string.notification_drawer_title),
                    //getString(R.string.progress_bar_cat_title),
                    getString(R.string.Recents_panel),
                    getString(R.string.status_bar_title),
                    getString(R.string.sizer_title),
                    getString(R.string.wakelock_blocker_title),
                    getString(R.string.weather_control_master_title)};
        } else {
        titleString = new String[]{
                    getString(R.string.hfm_pref_screen_title),
                    getString(R.string.cyanide_sound_title),
                    getString(R.string.cyanide_animations_title),
                    getString(R.string.power_usage_summary_title),
                    getString(R.string.button_pref_title),
                    getString(R.string.floating_windows),
                    getString(R.string.identicons_title),
                    getString(R.string.interface_settings_title),
                    getString(R.string.lockscreen_settings),
                    getString(R.string.cyanide_notifs_title),
                    getString(R.string.power_menu_title),
                    getString(R.string.notification_drawer_title),
                    //getString(R.string.progress_bar_cat_title),
                    getString(R.string.Recents_panel),
                    getString(R.string.status_bar_title),
                    getString(R.string.sizer_title),
                    getString(R.string.wakelock_blocker_title),
                    getString(R.string.weather_control_master_title)};
        }
        return titleString;
    }
}
