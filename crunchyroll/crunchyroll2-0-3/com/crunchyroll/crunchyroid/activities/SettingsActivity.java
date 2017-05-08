// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.app.Fragment;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import com.crunchyroll.crunchyroid.fragments.SettingsFragment;
import android.content.BroadcastReceiver;

public class SettingsActivity extends TrackedActivity
{
    private boolean mIsPaused;
    private boolean mIsRefreshOnResume;
    private boolean mIsTablet;
    private BroadcastReceiver mReceiver;
    private SettingsFragment mSettingsFragment;
    
    public static void start(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)SettingsActivity.class));
    }
    
    public void finish() {
        super.finish();
        CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.setContentView(2130903068);
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setTitle(LocalizedStrings.SETTINGS.get());
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mIsRefreshOnResume = false;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("USER_LOGGED_IN") || action.equals("USER_LOGGED_OUT") || action.equals("PAYMENT_INFO_SENT")) {
                    if (!SettingsActivity.this.mIsPaused) {
                        SettingsActivity.this.mSettingsFragment = SettingsFragment.newInstance();
                        SettingsActivity.this.getFragmentManager().beginTransaction().replace(2131624055, (Fragment)SettingsActivity.this.mSettingsFragment).commit();
                        return;
                    }
                    SettingsActivity.this.mIsRefreshOnResume = true;
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("USER_LOGGED_IN");
        intentFilter.addAction("USER_LOGGED_OUT");
        intentFilter.addAction("PAYMENT_INFO_SENT");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mReceiver, new IntentFilter(intentFilter));
        this.mSettingsFragment = SettingsFragment.newInstance();
        this.getFragmentManager().beginTransaction().replace(2131624055, (Fragment)this.mSettingsFragment).commit();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mReceiver);
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        this.mIsPaused = true;
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.mIsPaused = false;
        if (this.mIsRefreshOnResume) {
            this.mIsRefreshOnResume = false;
            this.mSettingsFragment = SettingsFragment.newInstance();
            this.getFragmentManager().beginTransaction().replace(2131624055, (Fragment)this.mSettingsFragment).commit();
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        CrunchyrollApplication.getApp((Context)this).onActivityDidStart();
    }
    
    @Override
    protected void onStop() {
        CrunchyrollApplication.getApp((Context)this).onActivityDidStop();
        super.onStop();
    }
}
