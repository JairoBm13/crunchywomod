// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.Menu;
import android.view.MenuItem;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.net.Uri;
import android.webkit.WebViewClient;
import android.view.KeyEvent;
import android.view.View;
import android.view.View$OnKeyListener;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.webkit.WebView;

public class HelpActivity extends TrackedActivity
{
    private boolean mIsTablet;
    protected WebView webView;
    
    public static void start(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)HelpActivity.class));
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.setContentView(2130903069);
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setTitle(LocalizedStrings.HELP.get());
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ApplicationState applicationState = CrunchyrollApplication.getApp((Context)this).getApplicationState();
        this.webView = (WebView)this.findViewById(2131624057);
        final String customLocale = applicationState.getCustomLocale();
        if (customLocale.startsWith("es")) {
            this.webView.loadUrl(this.getString(2131165440));
        }
        else if (customLocale.startsWith("pt")) {
            this.webView.loadUrl(this.getString(2131165443));
        }
        else if (customLocale.startsWith("fr")) {
            this.webView.loadUrl(this.getString(2131165441));
        }
        else if (customLocale.startsWith("de")) {
            this.webView.loadUrl(this.getString(2131165438));
        }
        else if (customLocale.startsWith("ar")) {
            this.webView.loadUrl(this.getString(2131165437));
        }
        else if (customLocale.startsWith("it")) {
            this.webView.loadUrl(this.getString(2131165442));
        }
        else {
            this.webView.loadUrl(this.getString(2131165439));
        }
        this.webView.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (n == 97) {
                    HelpActivity.this.finish();
                    return true;
                }
                return false;
            }
        });
        this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                if (s.contains("/help?topic=contact")) {
                    ContactUsActivity.start(HelpActivity.this);
                }
                else {
                    final Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(s));
                    HelpActivity.this.startActivity(intent);
                }
                return true;
            }
        });
        Tracker.swrveScreenView("settings-help");
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
}
