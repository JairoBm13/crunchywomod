// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.crunchyroll.android.util.SafeAsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.text.Html;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.support.v7.widget.Toolbar;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;

public class PrivacyPolicyActivity extends TrackedActivity
{
    protected TextView mContent;
    private boolean mIsTablet;
    
    public static void start(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)PrivacyPolicyActivity.class));
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.setContentView(2130903073);
        final String customLocale = CrunchyrollApplication.getApp((Context)this).getApplicationState().getCustomLocale();
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setTitle(LocalizedStrings.PRIVACY_POLICY.get());
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mContent = (TextView)this.findViewById(2131624067);
        new GetPrivacyPolicyTask(this.getString(2131165515, new Object[] { customLocale })) {
            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                Util.hideProgressBar(PrivacyPolicyActivity.this);
            }
            
            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();
                Util.showProgressBar(PrivacyPolicyActivity.this, PrivacyPolicyActivity.this.getResources().getColor(2131558518));
            }
            
            @Override
            protected void onSuccess(final String s) throws Exception {
                PrivacyPolicyActivity.this.mContent.setText((CharSequence)Html.fromHtml(s));
            }
        }.execute();
        Tracker.swrveScreenView("settings-privacy-policy");
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }
    
    class GetPrivacyPolicyTask extends SafeAsyncTask<String>
    {
        String mUrl;
        
        public GetPrivacyPolicyTask(final String mUrl) {
            this.mUrl = mUrl;
        }
        
        @Override
        public String call() throws Exception {
            final URLConnection openConnection = new URL(this.mUrl).openConnection();
            String string = "";
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            bufferedReader.readLine();
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                string = string + "<p align=\"justify\">" + line + "\n";
            }
            bufferedReader.close();
            return string;
        }
    }
}
