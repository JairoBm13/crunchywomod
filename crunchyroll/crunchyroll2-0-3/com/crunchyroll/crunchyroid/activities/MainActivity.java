// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.os.Parcelable;
import java.util.Locale;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.support.design.widget.Snackbar;
import com.crunchyroll.crunchyroid.receivers.CastBarReciever;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.fragments.MainDrawerFragment;
import com.crunchyroll.cast.CastHandler;
import tv.ouya.console.api.OuyaFacade;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import com.crunchyroll.android.api.models.Media;
import android.content.Intent;
import android.view.KeyEvent;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import android.app.Activity;
import com.astuetz.PagerSlidingTabStrip;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.crunchyroll.crunchyroid.fragments.SeasonEpisodesPillTabsFragment;
import com.crunchyroll.crunchyroid.fragments.QueueHistoryPillTabsFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import com.crunchyroll.crunchyroid.fragments.FilteredSeriesFragment;

public final class MainActivity extends TrackedActivity
{
    private static String CURRENT_ITEM;
    public static final int NUM_TABS = 4;
    public static final int POSITION_ANIME = 2;
    public static final int POSITION_DRAMA = 3;
    public static final int POSITION_HOME = 0;
    public static final int POSITION_NEW = 1;
    private MainActivityAdapter fragmentAdapter;
    private boolean loadFragmentsOnResume;
    private FilteredSeriesFragment mAnimeFragment;
    private ViewGroup mCastBar;
    private BroadcastReceiver mCastReceiver;
    private FilteredSeriesFragment mDramaFragment;
    private View mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mFilterType;
    private boolean mIsPaused;
    private boolean mIsTablet;
    private Type mMainType;
    private QueueHistoryPillTabsFragment mQueueHistoryFragment;
    private SeasonEpisodesPillTabsFragment mSeasonEpisodesFragment;
    private boolean mShowSwitchViewMenu;
    private boolean refreshForLoginLogout;
    private BroadcastReceiver updatesReceiver;
    private ViewPager viewPager;
    
    static {
        MainActivity.CURRENT_ITEM = "current item";
    }
    
    public MainActivity() {
        this.loadFragmentsOnResume = false;
        this.refreshForLoginLogout = false;
    }
    
    private void resetFragments() {
        this.fragmentAdapter = new MainActivityAdapter(this.getSupportFragmentManager(), true);
        ((ViewPager)this.findViewById(2131624064)).setAdapter(this.fragmentAdapter);
        ((PagerSlidingTabStrip)this.findViewById(2131624063)).notifyDataSetChanged();
    }
    
    public static void signup(final Activity activity, final PrepareToWatch.Type type, final boolean b) {
        if (!CrunchyrollApplication.getApp((Context)activity).isPrepareToWatchLoading()) {
            final PrepareToWatch prepareToWatchNoMedia = CrunchyrollApplication.getApp((Context)activity).prepareToWatchNoMedia(activity, type, false, 0, null);
            prepareToWatchNoMedia.prepare(new BaseListener<Void>() {
                @Override
                public void onException(final Exception ex) {
                    if (ex instanceof ApiNetworkException) {
                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                        return;
                    }
                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                }
                
                @Override
                public void onFinally() {
                    if (b) {
                        super.onFinally();
                        Util.hideProgressBar(activity);
                    }
                }
                
                @Override
                public void onPreExecute() {
                    if (b) {
                        Util.showProgressBar(activity, activity.getResources().getColor(2131558518));
                    }
                }
                
                @Override
                public void onSuccess(final Void void1) {
                    prepareToWatchNoMedia.go(PrepareToWatch.Event.NONE);
                }
            });
        }
    }
    
    private void trackPosition(final int n) {
        switch (n) {
            case 0: {
                if (this.mQueueHistoryFragment != null) {
                    Tracker.swrveScreenView("main-home");
                    this.mQueueHistoryFragment.trackView();
                    return;
                }
                break;
            }
            case 1: {
                if (this.mSeasonEpisodesFragment != null) {
                    Tracker.swrveScreenView("main-new");
                    this.mSeasonEpisodesFragment.trackView();
                    return;
                }
                break;
            }
            case 2: {
                if (this.mAnimeFragment != null) {
                    Tracker.swrveScreenView("main-anime");
                    this.mAnimeFragment.trackView();
                    return;
                }
                break;
            }
            case 3: {
                if (this.mDramaFragment != null) {
                    Tracker.swrveScreenView("main-drama");
                    this.mDramaFragment.trackView();
                    return;
                }
                break;
            }
        }
    }
    
    private void updateSwitchViewMenuVisibility(final int n) {
        switch (n) {
            case 0: {
                this.setSwitchViewMenuVisibility(false);
            }
            case 1: {
                if (this.mSeasonEpisodesFragment != null) {
                    this.setSwitchViewMenuVisibility(this.mSeasonEpisodesFragment.getSwitchViewMenuVisibility());
                    return;
                }
                break;
            }
            case 2: {
                this.setSwitchViewMenuVisibility(true);
            }
            case 3: {
                this.setSwitchViewMenuVisibility(true);
            }
        }
    }
    
    public void closeDrawer() {
        if (this.mDrawerLayout.isDrawerOpen(this.mDrawer)) {
            this.mDrawerLayout.closeDrawer(this.mDrawer);
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            switch (keyEvent.getKeyCode()) {
                case 104: {
                    final ViewPager viewPager = (ViewPager)this.findViewById(2131624064);
                    viewPager.setCurrentItem(Math.max(0, viewPager.getCurrentItem() - 1));
                    return true;
                }
                case 105: {
                    final ViewPager viewPager2 = (ViewPager)this.findViewById(2131624064);
                    viewPager2.setCurrentItem(Math.min(this.fragmentAdapter.getCount() - 1, viewPager2.getCurrentItem() + 1));
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }
    
    public void finish() {
        if (this.mDrawerLayout.isDrawerOpen(this.mDrawer)) {
            this.mDrawerLayout.closeDrawer(this.mDrawer);
            return;
        }
        super.finish();
        CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 1 && n2 == 11) {
            this.resetFragments();
        }
        if (n2 == 22) {
            Util.showErrorPopup((Context)this, LocalizedStrings.ERROR_NOT_AVAILABLE.get());
        }
        if (n2 == 23) {
            final Media media = (Media)intent.getExtras().getSerializable("media");
            if (!CrunchyrollApplication.getApp((Context)this).isPrepareToWatchLoading() && media != null) {
                final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this).prepareToWatch(this, media, false, 0);
                prepareToWatch.prepare(new BaseListener<Void>() {
                    @Override
                    public void onException(final Exception ex) {
                        if (ex instanceof ApiNetworkException) {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                            return;
                        }
                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                    }
                    
                    @Override
                    public void onFinally() {
                        Util.hideProgressBar(MainActivity.this);
                    }
                    
                    @Override
                    public void onPreExecute() {
                        Util.showProgressBar(MainActivity.this, MainActivity.this.getResources().getColor(2131558518));
                    }
                    
                    @Override
                    public void onSuccess(final Void void1) {
                        prepareToWatch.go(PrepareToWatch.Event.NONE);
                    }
                });
            }
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        if (this.getIntent().getExtras() == null) {
            this.mMainType = Type.TYPE_NORMAL;
            this.mFilterType = null;
        }
        else {
            this.mMainType = (Type)this.getIntent().getExtras().getSerializable("mainType");
            this.mFilterType = this.getIntent().getExtras().getString("filter");
        }
        this.setContentView(2130903071);
        if (bundle == null) {
            this.loadFragmentsOnResume = true;
        }
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setLogo(2130837755);
        this.setSupportActionBar(supportActionBar);
        this.mDrawerLayout = (DrawerLayout)this.findViewById(2131624061);
        this.mDrawer = this.findViewById(2131624065);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, supportActionBar, 2131165360, 2131165359) {
            @Override
            public void onDrawerClosed(final View view) {
                super.onDrawerClosed(view);
                MainActivity.this.invalidateOptionsMenu();
            }
            
            @Override
            public void onDrawerOpened(final View view) {
                super.onDrawerOpened(view);
                Tracker.trackView((Context)MainActivity.this, Tracker.Screen.OPEN_DRAWER);
                Tracker.swrveScreenView("side-menu");
                MainActivity.this.invalidateOptionsMenu();
            }
        };
        this.mDrawerLayout.setDrawerListener((DrawerLayout.DrawerListener)this.mDrawerToggle);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.mDrawerToggle.syncState();
        this.fragmentAdapter = new MainActivityAdapter(this.getSupportFragmentManager(), false);
        (this.viewPager = (ViewPager)this.findViewById(2131624064)).addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }
            
            @Override
            public void onPageSelected(final int n) {
                switch (n) {
                    case 0: {
                        Tracker.mainMenu((Context)MainActivity.this, "home");
                        break;
                    }
                    case 1: {
                        Tracker.mainMenu((Context)MainActivity.this, "new");
                        break;
                    }
                    case 2: {
                        Tracker.mainMenu((Context)MainActivity.this, "anime");
                        break;
                    }
                    case 3: {
                        Tracker.mainMenu((Context)MainActivity.this, "drama");
                        break;
                    }
                }
                MainActivity.this.updateSwitchViewMenuVisibility(n);
            }
        });
        this.viewPager.setAdapter(this.fragmentAdapter);
        this.viewPager.setOffscreenPageLimit(1);
        this.viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new TrackingPageChangeListener());
        if (!this.getApplicationState().hasLoggedInUser()) {
            this.viewPager.setCurrentItem(2);
            switch (this.mMainType) {
                case TYPE_CREATE_ACCOUNT: {
                    signup(this, PrepareToWatch.Type.PREPARE_SIGNUP, false);
                    break;
                }
                case TYPE_UPSELL: {
                    signup(this, PrepareToWatch.Type.PREPARE_UPSELL_NONE, false);
                    break;
                }
            }
        }
        if (OuyaFacade.getInstance().isRunningOnOUYAHardware()) {
            this.viewPager.setSaveEnabled(false);
        }
        if (bundle != null) {
            this.viewPager.setCurrentItem(bundle.getInt(MainActivity.CURRENT_ITEM));
        }
        ((PagerSlidingTabStrip)this.findViewById(2131624063)).setViewPager(this.viewPager);
        this.updatesReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("LOCALE_CHANGED")) {
                    CastHandler.get().setLocale(CrunchyrollApplication.getApp((Context)MainActivity.this).getApplicationState().getCustomLocale());
                    if (!MainActivity.this.mIsPaused) {
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(2131624065, MainDrawerFragment.newInstance()).commit();
                        MainActivity.this.resetFragments();
                        return;
                    }
                    MainActivity.this.refreshForLoginLogout = true;
                }
                else if (action.equals("USER_LOGGED_IN") || action.equals("USER_LOGGED_OUT") || action.equals("PAYMENT_INFO_SENT")) {
                    if (action.equals("USER_LOGGED_IN")) {
                        MainActivity.this.viewPager.setCurrentItem(0);
                    }
                    if (MainActivity.this.mIsPaused) {
                        MainActivity.this.refreshForLoginLogout = true;
                        return;
                    }
                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(2131624065, MainDrawerFragment.newInstance()).commit();
                    MainActivity.this.resetFragments();
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOCALE_CHANGED");
        intentFilter.addAction("USER_LOGGED_IN");
        intentFilter.addAction("USER_LOGGED_OUT");
        intentFilter.addAction("PAYMENT_INFO_SENT");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.updatesReceiver, new IntentFilter(intentFilter));
        this.getSupportFragmentManager().beginTransaction().replace(2131624065, MainDrawerFragment.newInstance()).commit();
        switch (this.mMainType) {
            case TYPE_ANIME: {
                this.viewPager.setCurrentItem(2);
                break;
            }
            case TYPE_DRAMA: {
                this.viewPager.setCurrentItem(3);
                break;
            }
            case TYPE_QUEUE:
            case TYPE_HISTORY: {
                this.viewPager.setCurrentItem(0);
                break;
            }
            case TYPE_THIS_SEASON:
            case TYPE_UPDATED: {
                this.viewPager.setCurrentItem(1);
                break;
            }
        }
        this.mCastBar = (ViewGroup)this.findViewById(2131624092);
        this.mCastReceiver = new CastBarReciever(this, this.mCastBar);
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mCastReceiver, CastBarReciever.getIntentFilter());
        Util.initCastBar(this.mCastBar);
        Util.updateCastBar(this, this.mCastBar);
    }
    
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mCastReceiver);
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.updatesReceiver);
        super.onDestroy();
    }
    
    public void onEvent(final ErrorEvent errorEvent) {
        if (this.mDrawerLayout.isDrawerOpen(this.mDrawer)) {
            Snackbar.make(this.findViewById(2131624060), errorEvent.getMessage(), 0).show();
            return;
        }
        Snackbar.make(this.findViewById(2131624186), errorEvent.getMessage(), 0).show();
    }
    
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.mMainType = (Type)intent.getExtras().getSerializable("mainType");
        switch (this.mMainType) {
            default: {}
            case TYPE_NORMAL: {
                this.viewPager.setCurrentItem(0);
            }
            case TYPE_ANIME: {
                this.viewPager.setCurrentItem(2);
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        this.mIsPaused = true;
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(1).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(this.mShowSwitchViewMenu);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.mIsPaused = false;
        if (this.loadFragmentsOnResume) {
            this.loadFragmentsOnResume = false;
            final PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip)this.findViewById(2131624063);
            this.fragmentAdapter.notifyDataSetChanged();
            pagerSlidingTabStrip.notifyDataSetChanged();
        }
        if (this.refreshForLoginLogout) {
            this.refreshForLoginLogout = false;
            this.getSupportFragmentManager().beginTransaction().replace(2131624065, MainDrawerFragment.newInstance()).commitAllowingStateLoss();
            this.resetFragments();
        }
        Util.updateCastBar(this, this.mCastBar);
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        bundle.putInt(MainActivity.CURRENT_ITEM, this.viewPager.getCurrentItem());
        super.onSaveInstanceState(bundle);
    }
    
    @Override
    protected void onSearch() {
        this.startActivity(new Intent((Context)this, (Class)SearchActivity.class));
        this.overridePendingTransition(0, 0);
    }
    
    public void setSwitchViewMenuVisibility(final boolean mShowSwitchViewMenu) {
        this.mShowSwitchViewMenu = mShowSwitchViewMenu;
        if (this.mMenu != null) {
            this.onPrepareOptionsMenu(this.mMenu);
        }
    }
    
    private class MainActivityAdapter extends PagerAdapter
    {
        private FragmentTransaction mCurTransaction;
        private Fragment mCurrentPrimaryItem;
        private final FragmentManager mFragmentManager;
        private boolean mUseNew;
        
        public MainActivityAdapter(final FragmentManager mFragmentManager, final boolean mUseNew) {
            this.mCurTransaction = null;
            this.mCurrentPrimaryItem = null;
            this.mUseNew = false;
            this.mFragmentManager = mFragmentManager;
            this.mUseNew = mUseNew;
        }
        
        private boolean hasLoggedInUser() {
            return MainActivity.this.getApplicationState().hasLoggedInUser();
        }
        
        private String makeFragmentName(final int n, final long n2) {
            return "android:switcher:" + n + ":" + n2;
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            if (this.mCurTransaction == null) {
                this.mCurTransaction = this.mFragmentManager.beginTransaction();
            }
            this.mCurTransaction.detach((Fragment)o);
        }
        
        @Override
        public void finishUpdate(final ViewGroup viewGroup) {
            if (this.mCurTransaction != null) {
                this.mCurTransaction.commitAllowingStateLoss();
                this.mCurTransaction = null;
                this.mFragmentManager.executePendingTransactions();
            }
        }
        
        @Override
        public int getCount() {
            return 4;
        }
        
        public Fragment getItem(int n) {
            final String s = null;
            String access$1000 = null;
            switch (n) {
                default: {
                    throw new IllegalAccessError("There shouldn't be anything here");
                }
                case 0: {
                    n = 0;
                    if (MainActivity.this.mMainType == Type.TYPE_HISTORY) {
                        n = 1;
                    }
                    MainActivity.this.mQueueHistoryFragment = QueueHistoryPillTabsFragment.newInstance(n);
                    return MainActivity.this.mQueueHistoryFragment;
                }
                case 1: {
                    n = 0;
                    if (MainActivity.this.mMainType == Type.TYPE_UPDATED) {
                        n = 1;
                    }
                    MainActivity.this.mSeasonEpisodesFragment = SeasonEpisodesPillTabsFragment.newInstance(n);
                    return MainActivity.this.mSeasonEpisodesFragment;
                }
                case 2: {
                    final MainActivity this$0 = MainActivity.this;
                    if (MainActivity.this.mMainType == Type.TYPE_ANIME) {
                        access$1000 = MainActivity.this.mFilterType;
                    }
                    this$0.mAnimeFragment = FilteredSeriesFragment.newInstance("anime", access$1000, true, false);
                    return MainActivity.this.mAnimeFragment;
                }
                case 3: {
                    final MainActivity this$2 = MainActivity.this;
                    String access$1001 = s;
                    if (MainActivity.this.mMainType == Type.TYPE_DRAMA) {
                        access$1001 = MainActivity.this.mFilterType;
                    }
                    this$2.mDramaFragment = FilteredSeriesFragment.newInstance("drama", access$1001, true, false);
                    return MainActivity.this.mDramaFragment;
                }
            }
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public CharSequence getPageTitle(final int n) {
            final Locale locale = MainActivity.this.getResources().getConfiguration().locale;
            switch (n) {
                default: {
                    throw new IllegalAccessError("There shouldn't be anything here");
                }
                case 0: {
                    return LocalizedStrings.HOME.get().toUpperCase(locale);
                }
                case 1: {
                    return LocalizedStrings.NEW_HOME_TAB.get().toUpperCase(locale);
                }
                case 2: {
                    return LocalizedStrings.ANIME.get().toUpperCase(locale);
                }
                case 3: {
                    return LocalizedStrings.DRAMA.get().toUpperCase(locale);
                }
            }
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            if (this.mCurTransaction == null) {
                this.mCurTransaction = this.mFragmentManager.beginTransaction();
            }
            final String fragmentName = this.makeFragmentName(viewGroup.getId(), this.getItemId(n));
            final Fragment fragmentByTag = this.mFragmentManager.findFragmentByTag(fragmentName);
            Fragment fragment;
            if (fragmentByTag != null && !this.mUseNew) {
                this.mCurTransaction.attach(fragmentByTag);
                fragment = fragmentByTag;
            }
            else {
                final Fragment item = this.getItem(n);
                this.mCurTransaction.add(viewGroup.getId(), item, fragmentName);
                fragment = item;
            }
            if (fragment != this.mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }
            switch (n) {
                case 1: {
                    MainActivity.this.mSeasonEpisodesFragment = (SeasonEpisodesPillTabsFragment)fragment;
                    break;
                }
            }
            if (MainActivity.this.viewPager.getCurrentItem() == n) {
                MainActivity.this.updateSwitchViewMenuVisibility(n);
            }
            return fragment;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return ((Fragment)o).getView() == view;
        }
        
        @Override
        public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        }
        
        @Override
        public Parcelable saveState() {
            return null;
        }
        
        @Override
        public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
            final Fragment mCurrentPrimaryItem = (Fragment)o;
            if (mCurrentPrimaryItem != this.mCurrentPrimaryItem) {
                if (this.mCurrentPrimaryItem != null) {
                    this.mCurrentPrimaryItem.setMenuVisibility(false);
                    this.mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (mCurrentPrimaryItem != null) {
                    mCurrentPrimaryItem.setMenuVisibility(true);
                    mCurrentPrimaryItem.setUserVisibleHint(true);
                }
                this.mCurrentPrimaryItem = mCurrentPrimaryItem;
            }
        }
        
        @Override
        public void startUpdate(final ViewGroup viewGroup) {
        }
    }
    
    private class TrackingPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
        }
        
        @Override
        public void onPageSelected(final int n) {
            MainActivity.this.trackPosition(n);
        }
    }
    
    public enum Type
    {
        TYPE_ANIME, 
        TYPE_CREATE_ACCOUNT, 
        TYPE_DRAMA, 
        TYPE_HISTORY, 
        TYPE_NORMAL, 
        TYPE_QUEUE, 
        TYPE_THIS_SEASON, 
        TYPE_UPDATED, 
        TYPE_UPSELL;
    }
}
