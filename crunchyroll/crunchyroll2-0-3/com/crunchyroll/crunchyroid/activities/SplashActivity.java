// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import com.crunchyroll.android.api.requests.LogInstallReferrerRequest;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.LogFirstLaunchRequest;
import com.crunchyroll.android.util.SafeAsyncTask;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.tracking.FacebookTracker;
import android.view.Menu;
import android.view.Window;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.app.Activity;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.os.Build;
import android.net.Uri;
import android.os.Build$VERSION;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import com.crunchyroll.crunchyroid.app.Extras;
import com.crunchyroll.android.api.models.Session;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.app.AlertDialog$Builder;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.text.TextUtils;
import com.crunchyroll.android.util.LoggerFactory;
import android.content.BroadcastReceiver;
import java.util.concurrent.atomic.AtomicBoolean;
import com.crunchyroll.android.util.Logger;

public class SplashActivity extends TrackedActivity
{
    private static final Logger log;
    private final AtomicBoolean didLogFirstLaunch;
    private final AtomicBoolean didLogReferrer;
    private boolean isShowingPopup;
    private boolean mIsTablet;
    private BroadcastReceiver mStartupReceiver;
    
    static {
        log = LoggerFactory.getLogger(SplashActivity.class);
    }
    
    public SplashActivity() {
        this.didLogFirstLaunch = new AtomicBoolean(false);
        this.didLogReferrer = new AtomicBoolean(false);
        this.isShowingPopup = false;
    }
    
    private void blockStart(String value) {
        if (TextUtils.isEmpty((CharSequence)value)) {
            value = LocalizedStrings.ERROR_BLOCKED.get();
        }
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setMessage((CharSequence)value);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                SplashActivity.this.finish();
            }
        });
        alertDialog$Builder.show();
    }
    
    private void checkStartupResult(final boolean b) {
        final CrunchyrollApplication app = CrunchyrollApplication.getApp((Context)this);
        final Exception launchError = app.getLaunchError();
        final Session session = this.getApplicationState().getSession();
        if (launchError == null && session != null) {
            this.onSessionStarted(session);
            return;
        }
        if (b) {
            app.runStartupFlow();
            return;
        }
        this.onSessionFailedToStart(launchError);
    }
    
    private void clearStart() {
        this.countLaunch();
        final Uri data = this.getIntent().getData();
        if (data == null || (!"series".equalsIgnoreCase(Extras.getUriMethod(data)) && !"media".equalsIgnoreCase(Extras.getUriMethod(data)) && !"playmedia".equalsIgnoreCase(Extras.getUriMethod(data)) && !"queue".equalsIgnoreCase(Extras.getUriMethod(data)) && !"history".equalsIgnoreCase(Extras.getUriMethod(data)) && !"create_account".equalsIgnoreCase(Extras.getUriMethod(data)) && !"upsell".equalsIgnoreCase(Extras.getUriMethod(data)) && !"filter".equalsIgnoreCase(Extras.getUriMethod(data)) && !"this_season".equalsIgnoreCase(Extras.getUriMethod(data)) && !"updated".equalsIgnoreCase(Extras.getUriMethod(data))) || !CrunchyrollApplication.getApp((Context)this).getDeepLinker().open(this, true, data, false)) {
            final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
            if (Build$VERSION.SDK_INT < 16) {
                intent.setFlags(65536);
            }
            this.startActivity(intent);
            this.finish();
            if (Build$VERSION.SDK_INT < 16) {
                this.overridePendingTransition(0, 0);
            }
        }
    }
    
    private void countLaunch() {
        final ApplicationState applicationState = this.getApplicationState();
        applicationState.incrementLaunchCount();
        final long launchCount = applicationState.getLaunchCount();
        final Optional<String> installReferrer = applicationState.getInstallReferrer();
        final boolean shouldLogReferrer = applicationState.getShouldLogReferrer();
        if (!this.didLogFirstLaunch.get() && 1L == launchCount) {
            new LogFirstLaunchTask().execute();
        }
        if (!this.didLogReferrer.get() && shouldLogReferrer) {
            if (Build.MANUFACTURER.equals("Amazon")) {
                new LogReferrerTask(installReferrer.or("utm_campaign=amazon")).execute();
            }
            else if (installReferrer.isPresent()) {
                new LogReferrerTask(installReferrer.get()).execute();
            }
        }
    }
    
    private void holdStart(final String message) {
        if (TextUtils.isEmpty((CharSequence)message)) {
            this.clearStart();
            return;
        }
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setMessage((CharSequence)message);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                SplashActivity.this.clearStart();
            }
        });
        alertDialog$Builder.show();
    }
    
    private void runStartOperation(JsonNode path) {
        if (!path.isMissingNode()) {
            for (final JsonNode jsonNode : path) {
                final String text = jsonNode.path("op").asText();
                final JsonNode path2 = jsonNode.path("params");
                if ("client_start".equals(text)) {
                    final String text2 = path2.path("code").asText();
                    path = path2.path("message");
                    String trim;
                    if (path.isNull()) {
                        trim = null;
                    }
                    else {
                        trim = path.asText().trim();
                    }
                    if ("block".equals(text2)) {
                        this.blockStart(trim);
                        return;
                    }
                    if (!"hold".equals(text2)) {
                        continue;
                    }
                    this.holdStart(trim);
                }
            }
        }
        if (this.isShowingPopup) {
            this.holdStart(this.getString(2131165534));
            return;
        }
        if (Build$VERSION.SDK_INT < 16) {
            this.holdStart(LocalizedStrings.SUPPORT_LIMIT.get());
            return;
        }
        this.clearStart();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903077);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        this.mStartupReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("APPLICATION_STARTUP_COMPLETE_EVENT")) {
                    SplashActivity.this.checkStartupResult(false);
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mStartupReceiver, new IntentFilter("APPLICATION_STARTUP_COMPLETE_EVENT"));
        final CrunchyrollApplication app = CrunchyrollApplication.getApp((Context)this);
        switch (app.getStartupState()) {
            case COMPLETE: {
                this.checkStartupResult(true);
                break;
            }
        }
        app.setTremorInitActivity(this);
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = this.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(this.getResources().getColor(2131558473));
        }
        Tracker.swrveScreenView("splash-screen");
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return false;
    }
    
    @Override
    protected void onDestroy() {
        if (this.mStartupReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mStartupReceiver);
        }
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        FacebookTracker.install((Context)this);
    }
    
    @Override
    protected void onSessionExpired() {
        this.isShowingPopup = true;
    }
    
    public void onSessionFailedToStart(final Exception ex) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setCancelable(false);
        if (ex instanceof ApiNetworkException) {
            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_NETWORK.get());
        }
        else {
            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_STARTING_APP.get());
        }
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.RETRY.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                CrunchyrollApplication.getApp((Context)SplashActivity.this).runStartupFlow();
            }
        });
        alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.CLOSE.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                SplashActivity.this.finish();
            }
        });
        alertDialog$Builder.show();
    }
    
    protected void onSessionStarted(final Session session) {
        this.runStartOperation(session.getOps());
    }
    
    private class LogFirstLaunchTask extends SafeAsyncTask<Void>
    {
        @Override
        public Void call() throws Exception {
            ((CrunchyrollApplication)SplashActivity.this.getApplication()).getApiService().run(new LogFirstLaunchRequest());
            return null;
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            SplashActivity.log.error("Error logging first launch", ex);
        }
        
        @Override
        protected void onSuccess(final Void void1) throws Exception {
            SplashActivity.this.didLogFirstLaunch.set(true);
        }
    }
    
    private class LogReferrerTask extends SafeAsyncTask<Void>
    {
        protected final String installReferrer;
        
        protected LogReferrerTask(final String installReferrer) {
            this.installReferrer = installReferrer;
        }
        
        @Override
        public Void call() throws Exception {
            ((CrunchyrollApplication)SplashActivity.this.getApplication()).getApiService().run(new LogInstallReferrerRequest(this.installReferrer));
            return null;
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            SplashActivity.log.error("Error logging install referrer", ex);
        }
        
        @Override
        protected void onSuccess(final Void void1) throws Exception {
            SplashActivity.this.didLogReferrer.set(true);
            SplashActivity.this.getApplicationState().setShouldLogReferrer(false);
        }
    }
}
