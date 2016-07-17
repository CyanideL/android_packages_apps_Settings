/*
 * Copyright (C) 2015-2016 Cyanide Android (rogersb11)
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

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.internal.util.vrtoxin.ScreenType;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.vrtoxin.PagerSlidingTabStrip;
import com.android.settings.vrtoxin.AndroidRecentsSettings;
import com.android.settings.vrtoxin.DashboardOptions;
import com.android.settings.vrtoxin.FloatingWindows;
import com.android.settings.vrtoxin.HwKeySettings;
import com.android.settings.vrtoxin.InterfaceSettings;
import com.android.settings.vrtoxin.LockS;
import com.android.settings.vrtoxin.MasterAnimationControl;
import com.android.settings.vrtoxin.ModsMenuCustomizations;
import com.android.settings.vrtoxin.PowerMenuSettings;
import com.android.settings.vrtoxin.QuickSettings;
import com.android.settings.vrtoxin.SlimSizer;
import com.android.settings.vrtoxin.StatusBarSettings;
import com.android.settings.vrtoxin.VrtoxinNotifs;
import com.android.settings.vrtoxin.WakelockBlocker;
import com.android.settings.notification.OtherSoundSettings;
import com.android.settings.vrtoxin.viewpager.transforms.*;
import com.android.settings.vrtoxin.drawer.*;

import com.android.internal.util.vrtoxin.NavDrawerColorHelper;

import java.util.ArrayList;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;

import java.util.List;

public class MainSettings extends SettingsPreferenceFragment implements OnBackStackChangedListener {

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;
    FrameLayout mFragContainer;
    ContentResolver mResolver;

    // Navdrawer
    String[] mTitles;
    ListView mDrawerList;
    TypedArray mIcons;
    ArrayList<DrawerItems> navDrawerItems;
    DrawerAdapter mAdapter;
    DrawerLayout mDrawerLayout;
    CharSequence mDrawerTitle;
    CharSequence mTitle;
    ActionBar actionBar;
    ActionBarDrawerToggle mDrawerToggle;
    private int mScrimAlpha;

    private SettingsObserver mSettingsObserver;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_SHIT;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;
        mResolver = getActivity().getContentResolver();
        final ActionBar actionBar = getActivity().getActionBar();
        int color = Settings.System.getInt(mResolver,
                Settings.System.SETTINGS_ICON_COLOR, 0xFFFFFFFF);
        Drawable d = getResources().getDrawable(R.drawable.ic_settings_vrtoxin).mutate();
        d.setColorFilter(color, Mode.SRC_IN);
        actionBar.setIcon(d);

        View view = inflater.inflate(R.layout.preference_vrtoxin_shit, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mSettingsObserver = new SettingsObserver(new Handler());
        mFragContainer = (FrameLayout) view.findViewById(R.id.fragment_content);
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mTabs.setViewPager(mViewPager);

        mTitle = mDrawerTitle = getActivity().getTitle();
        mTitles = getResources().getStringArray(R.array.drawer_titles);
        mIcons = getResources().obtainTypedArray(R.array.drawer_icons);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) view.findViewById(R.id.left_drawer);
        navDrawerItems = new ArrayList<DrawerItems>();

        navDrawerItems.add(new DrawerItems(mTitles[0], mIcons
                 .getResourceId(0, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[1], mIcons
                 .getResourceId(1, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[2], mIcons
                 .getResourceId(2, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[3], mIcons
                 .getResourceId(3, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[4], mIcons
                 .getResourceId(4, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[5], mIcons
                 .getResourceId(5, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[6], mIcons
                 .getResourceId(6, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[7], mIcons
                .getResourceId(7, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[8], mIcons
                .getResourceId(8, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[9], mIcons
                .getResourceId(9, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[10], mIcons
               .getResourceId(10, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[11], mIcons
                .getResourceId(11, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[12], mIcons
               .getResourceId(12, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[13], mIcons
                .getResourceId(13, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[14], mIcons
                .getResourceId(14, -1)));
        navDrawerItems.add(new DrawerItems(mTitles[15], mIcons
                .getResourceId(15, -1)));

		mIcons.recycle();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActivity().getActionBar().setTitle(mTitle);
                getActivity().invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().getActionBar().setTitle(mDrawerTitle);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        addDrawerItems();
        mSettingsObserver.observe();
        getFragmentManager().addOnBackStackChangedListener(this);

        return view;
    }

    private void addDrawerItems() {
        mAdapter = new DrawerAdapter(getActivity(), navDrawerItems);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
                setTitle(mTitles[position]);
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void selectItem(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                Fragment otherSoundSettings = new OtherSoundSettings();
                fragmentTransaction.replace(R.id.fragment_content, otherSoundSettings);
                fragmentTransaction.addToBackStack("otherSoundsSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 1:
                Fragment masterAnimationControl = new MasterAnimationControl();
                fragmentTransaction.replace(R.id.fragment_content, masterAnimationControl);
                fragmentTransaction.addToBackStack("masterAnimationControl");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 2:
                Fragment powerUsageSummary = new PowerUsageSummary();
                fragmentTransaction.replace(R.id.fragment_content, powerUsageSummary);
                fragmentTransaction.addToBackStack("powerUsageSummary");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 3:
                Fragment hwKeySettings = new HwKeySettings();
                fragmentTransaction.replace(R.id.fragment_content, hwKeySettings);
                fragmentTransaction.addToBackStack("hwKeySettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 4:
                Fragment dashboardOptions = new DashboardOptions();
                fragmentTransaction.replace(R.id.fragment_content, dashboardOptions);
                fragmentTransaction.addToBackStack("dashboardOptions");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 5:
                Fragment floatingWindows = new FloatingWindows();
                fragmentTransaction.replace(R.id.fragment_content, floatingWindows);
                fragmentTransaction.addToBackStack("floatingWindows");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 6:
                Fragment interfaceSettings = new InterfaceSettings();
                fragmentTransaction.replace(R.id.fragment_content, interfaceSettings);
                fragmentTransaction.addToBackStack("interfaceSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 7:
                Fragment lockS = new LockS();
                fragmentTransaction.replace(R.id.fragment_content, lockS);
                fragmentTransaction.addToBackStack("lockS");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 8:
                Fragment modsMenuCustomizations = new ModsMenuCustomizations();
                fragmentTransaction.replace(R.id.fragment_content, modsMenuCustomizations);
                fragmentTransaction.addToBackStack("modsMenuCustomizations");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 9:
                Fragment vrtoxinNotifs = new VrtoxinNotifs();
                fragmentTransaction.replace(R.id.fragment_content, vrtoxinNotifs);
                fragmentTransaction.addToBackStack("vrtoxinNotifs");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 10:
                Fragment powerMenuSettings = new PowerMenuSettings();
                fragmentTransaction.replace(R.id.fragment_content, powerMenuSettings);
                fragmentTransaction.addToBackStack("powerMenuSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 11:
                Fragment quickSettings = new QuickSettings();
                fragmentTransaction.replace(R.id.fragment_content, quickSettings);
                fragmentTransaction.addToBackStack("quickSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 12:
                Fragment androidRecentsSettings = new AndroidRecentsSettings();
                fragmentTransaction.replace(R.id.fragment_content, androidRecentsSettings);
                fragmentTransaction.addToBackStack("androidRecentsSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 13:
                Fragment statusBarSettings = new StatusBarSettings();
                fragmentTransaction.replace(R.id.fragment_content, statusBarSettings);
                fragmentTransaction.addToBackStack("statusBarSettings");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 14:
                Fragment slimSizer = new SlimSizer();
                fragmentTransaction.replace(R.id.fragment_content, slimSizer);
                fragmentTransaction.addToBackStack("slimSizer");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
            case 15:
                Fragment wakelockBlocker = new WakelockBlocker();
                fragmentTransaction.replace(R.id.fragment_content, wakelockBlocker);
                fragmentTransaction.addToBackStack("wakelockBlocker");
                fragmentTransaction.commit();
                mFragContainer.setVisibility(View.VISIBLE);
                break;
         }
         getFragmentManager().addOnBackStackChangedListener(this);
         mDrawerList.setItemChecked(position, true);
         mDrawerLayout.closeDrawer(mDrawerList);

    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        getActivity().getActionBar().setTitle(mTitle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        //super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount > 0){
            getActivity().getActionBar().setTitle(mTitle);
        } else {
            mFragContainer.setVisibility(View.GONE);
            getActivity().getActionBar().setTitle("CyanideMods");
        }
    }

     @Override
     public void onResume() {
         super.onResume();

        getActivity().getActionBar().setTitle(mTitle);
        if (!ScreenType.isTablet(getActivity())) {
            mContainer.setPadding(30, 30, 30, 30);
        }
    }

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new OtherSoundSettings();
            frags[1] = new MasterAnimationControl();
            frags[2] = new PowerUsageSummary();
            frags[3] = new HwKeySettings();
            frags[4] = new DashboardOptions();
            frags[5] = new FloatingWindows();
            frags[6] = new InterfaceSettings();
            frags[7] = new LockS();
            frags[8] = new ModsMenuCustomizations();
            frags[9] = new VrtoxinNotifs();
            frags[10] = new PowerMenuSettings();
            frags[11] = new QuickSettings();
            frags[12] = new AndroidRecentsSettings();
            frags[13] = new StatusBarSettings();
            frags[14] = new SlimSizer();
            frags[15] = new WakelockBlocker();
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
                    getString(R.string.advanced_sound_title),
                    getString(R.string.vrtoxin_animations_settings),
                    getString(R.string.power_usage_summary_title),
                    getString(R.string.buttons_settings_title),
                    getString(R.string.settings_colors_title),
                    getString(R.string.floating_windows),
                    getString(R.string.interface_settings_title),
                    getString(R.string.lockscreen_settings),
                    getString(R.string.mods_menu_title),
                    getString(R.string.vrtoxin_notifications_title),
                    getString(R.string.power_menu_settings_title),
                    getString(R.string.quick_settings_title),
                    getString(R.string.recents_panel),
                    getString(R.string.status_bar_title),
                    getString(R.string.sizer_title),
                    getString(R.string.wakelock_blocker_title)};
        } else {
        titleString = new String[]{
                    getString(R.string.advanced_sound_title),
                    getString(R.string.vrtoxin_animations_settings),
                    getString(R.string.power_usage_summary_title),
                    getString(R.string.buttons_settings_title),
                    getString(R.string.settings_colors_title),
                    getString(R.string.floating_windows),
                    getString(R.string.interface_settings_title),
                    getString(R.string.lockscreen_settings),
                    getString(R.string.mods_menu_title),
                    getString(R.string.vrtoxin_notifications_title),
                    getString(R.string.power_menu_settings_title),
                    getString(R.string.quick_settings_title),
                    getString(R.string.recents_panel),
                    getString(R.string.status_bar_title),
                    getString(R.string.sizer_title),
                    getString(R.string.wakelock_blocker_title)};
        }
        return titleString;
    }

    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.MODS_TABS_EFFECT),
                    false, this, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DRAWER_BG_COLOR),
                    false, this, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DRAWER_FONT_STYLE),
                    false, this, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DRAWER_TEXT_COLOR),
                    false, this, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DRAWER_ICON_COLOR),
                    false, this, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DRAWER_SCRIM_COLOR),
                    false, this, UserHandle.USER_ALL);
            update();
        }

        void unobserve() {
            mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void update() {
            updateAnimation();
            updateBgColor();
            updateDrawerItems();
            updateScrimAlpha();

        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(Settings.System.getUriFor(
                    Settings.System.MODS_TABS_EFFECT))) {
                updateAnimation();
            } else if (uri.equals(Settings.System.getUriFor(
                    Settings.System.DRAWER_BG_COLOR))) {
                updateBgColor();
            } else if (uri.equals(Settings.System.getUriFor(
                    Settings.System.DRAWER_FONT_STYLE))
                || uri.equals(Settings.System.getUriFor(
                    Settings.System.DRAWER_ICON_COLOR))
                || uri.equals(Settings.System.getUriFor(
                    Settings.System.DRAWER_TEXT_COLOR))) {
                updateDrawerItems();
            } else if (uri.equals(Settings.System.getUriFor(
                    Settings.System.DRAWER_SCRIM_COLOR))) {
                updateScrimAlpha();
            }
        }
    }

    private void updateAnimation() {
        int effect = Settings.System.getIntForUser(mResolver,
            Settings.System.MODS_TABS_EFFECT, 0,
            UserHandle.USER_CURRENT);
        switch (effect) {
            case 0:
                mViewPager.setPageTransformer(true, new DefaultTransformer());
                break;
            case 1:
                mViewPager.setPageTransformer(true, new AccordionTransformer());
                break;
            case 2:
                mViewPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
                break;
            case 3:
                mViewPager.setPageTransformer(true, new CubeInTransformer());
                break;
            case 4:
                mViewPager.setPageTransformer(true, new CubeOutTransformer());
                break;
            case 5:
                mViewPager.setPageTransformer(true, new DepthPageTransformer());
                break;
            case 6:
                mViewPager.setPageTransformer(true, new FlipHorizontalTransformer());
                break;
            case 7:
                mViewPager.setPageTransformer(true, new FlipVerticalTransformer());
                break;
            case 8:
                mViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
                break;
            case 9:
                mViewPager.setPageTransformer(true, new RotateDownTransformer());
                break;
            case 10:
                mViewPager.setPageTransformer(true, new RotateUpTransformer());
                break;
            case 11:
                mViewPager.setPageTransformer(true, new ScaleInOutTransformer());
                break;
            case 12:
                mViewPager.setPageTransformer(true, new StackTransformer());
                break;
            case 13:
                mViewPager.setPageTransformer(true, new TabletTransformer());
                break;
            case 14:
                mViewPager.setPageTransformer(true, new ZoomInTransformer());
                break;
            case 15:
                mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                break;
            case 16:
                mViewPager.setPageTransformer(true, new ZoomOutTranformer());
                break;
            default:
                break;
        }
    }

    private void updateDrawerItems() {
        addDrawerItems();
    }

    private void updateBgColor() {
        final int bgColor = NavDrawerColorHelper.getBackgroundColor(mContext);
        mDrawerList.setBackgroundColor(bgColor);
    }

    private void updateScrimAlpha() {
        mScrimAlpha = Settings.System.getInt(mResolver,
                Settings.System.DRAWER_SCRIM_COLOR, 0x80000000);

        if (mDrawerLayout != null) {
            mDrawerLayout.setScrimColor(mScrimAlpha);
        }
    }
}
