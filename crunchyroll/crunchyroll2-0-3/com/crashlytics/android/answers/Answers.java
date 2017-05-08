// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import java.io.File;
import android.os.Build$VERSION;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.common.Crash;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.settings.SettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.FileStore;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import android.content.Context;
import io.fabric.sdk.android.ActivityLifecycleManager;
import io.fabric.sdk.android.Kit;

public class Answers extends Kit<Boolean>
{
    SessionAnalyticsManager analyticsManager;
    private long installedAt;
    ActivityLifecycleManager lifecycleManager;
    AnswersPreferenceManager preferenceManager;
    private String versionCode;
    private String versionName;
    
    private void initializeSessionAnalytics(final Context context) {
        try {
            final AnswersEventsHandler answersEventsHandler = new AnswersEventsHandler(this, context, new AnswersFilesManagerProvider(context, new FileStoreImpl(this)), new SessionMetadataCollector(context, this.getIdManager(), this.versionCode, this.versionName), new DefaultHttpRequestFactory(Fabric.getLogger()));
            answersEventsHandler.enable();
            this.analyticsManager = new SessionAnalyticsManager(answersEventsHandler);
            this.lifecycleManager.registerCallbacks((ActivityLifecycleManager.Callbacks)new AnswersLifecycleCallbacks(this.analyticsManager));
            if (this.isFirstLaunch(this.installedAt)) {
                Fabric.getLogger().d("Answers", "New app install detected");
                this.analyticsManager.onInstall();
                this.preferenceManager.setAnalyticsLaunched();
            }
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Answers", "Failed to initialize", ex);
        }
    }
    
    @Override
    protected Boolean doInBackground() {
        try {
            final SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
            if (awaitSettingsData == null) {
                Fabric.getLogger().e("Answers", "Failed to retrieve settings");
                return false;
            }
            if (awaitSettingsData.featuresData.collectAnalytics) {
                Fabric.getLogger().d("Answers", "Analytics collection enabled");
                this.analyticsManager.setAnalyticsSettingsData(awaitSettingsData.analyticsSettingsData, this.getOverridenSpiEndpoint());
                return true;
            }
            Fabric.getLogger().d("Answers", "Analytics collection disabled");
            this.lifecycleManager.resetCallbacks();
            this.analyticsManager.disable();
            return false;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Answers", "Error dealing with settings", ex);
            return false;
        }
    }
    
    @Override
    public String getIdentifier() {
        return "com.crashlytics.sdk.android:answers";
    }
    
    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(this.getContext(), "com.crashlytics.ApiEndpoint");
    }
    
    @Override
    public String getVersion() {
        return "1.3.2.79";
    }
    
    boolean installedRecently(final long n) {
        return System.currentTimeMillis() - n < 3600000L;
    }
    
    boolean isFirstLaunch(final long n) {
        return !this.preferenceManager.hasAnalyticsLaunched() && this.installedRecently(n);
    }
    
    public void onException(final Crash.FatalException ex) {
        if (this.analyticsManager != null) {
            this.analyticsManager.onCrash(ex.getSessionId());
        }
    }
    
    @SuppressLint({ "NewApi" })
    @Override
    protected boolean onPreExecute() {
        try {
            final Context context = this.getContext();
            this.preferenceManager = new AnswersPreferenceManager(new PreferenceStoreImpl(context, "settings"));
            this.lifecycleManager = new ActivityLifecycleManager(context);
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.versionCode = Integer.toString(packageInfo.versionCode);
            String versionName;
            if (packageInfo.versionName == null) {
                versionName = "0.0";
            }
            else {
                versionName = packageInfo.versionName;
            }
            this.versionName = versionName;
            if (Build$VERSION.SDK_INT >= 9) {
                this.installedAt = packageInfo.firstInstallTime;
            }
            else {
                this.installedAt = new File(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir).lastModified();
            }
            this.initializeSessionAnalytics(context);
            return true;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Answers", "Error retrieving app properties", ex);
            return false;
        }
    }
}
