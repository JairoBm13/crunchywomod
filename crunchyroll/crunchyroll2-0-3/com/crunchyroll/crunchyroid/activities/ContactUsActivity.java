// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.Menu;
import com.crunchyroll.crunchyroid.events.ClickEvent;
import de.greenrobot.event.EventBus;
import android.view.MenuItem;
import com.crunchyroll.crunchyroid.fragments.ContactUsFragment;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.support.v4.app.Fragment;

public class ContactUsActivity extends TrackedActivity
{
    private Fragment contactUsFragment;
    private boolean mIsTablet;
    
    public ContactUsActivity() {
        this.contactUsFragment = null;
    }
    
    public static void start(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)ContactUsActivity.class));
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tracker.settingsContactUsBack();
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.setContentView(2130903067);
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setTitle(LocalizedStrings.CONTACT_US.get());
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.contactUsFragment = ContactUsFragment.newInstance();
        this.getSupportFragmentManager().beginTransaction().add(2131624055, this.contactUsFragment).commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 2131624357) {
            EventBus.getDefault().post(new ClickEvent());
        }
        else if (menuItem.getItemId() == 16908332) {
            Tracker.settingsContactUsBack();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }
}
