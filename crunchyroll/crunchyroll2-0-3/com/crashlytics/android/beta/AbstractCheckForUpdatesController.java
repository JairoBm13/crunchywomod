// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import android.annotation.SuppressLint;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import android.content.Context;
import io.fabric.sdk.android.services.settings.BetaSettingsData;

abstract class AbstractCheckForUpdatesController implements UpdatesController
{
    private Beta beta;
    private BetaSettingsData betaSettings;
    private BuildProperties buildProps;
    private Context context;
    private CurrentTimeProvider currentTimeProvider;
    private final AtomicBoolean externallyReady;
    private HttpRequestFactory httpRequestFactory;
    private IdManager idManager;
    private final AtomicBoolean initialized;
    private long lastCheckTimeMillis;
    private PreferenceStore preferenceStore;
    
    public AbstractCheckForUpdatesController() {
        this(false);
    }
    
    public AbstractCheckForUpdatesController(final boolean b) {
        this.initialized = new AtomicBoolean();
        this.lastCheckTimeMillis = 0L;
        this.externallyReady = new AtomicBoolean(b);
    }
    
    private void performUpdateCheck() {
        Fabric.getLogger().d("Beta", "Performing update check");
        final String value = new ApiKey().getValue(this.context);
        new CheckForUpdatesRequest(this.beta, this.beta.getOverridenSpiEndpoint(), this.betaSettings.updateUrl, this.httpRequestFactory, new CheckForUpdatesResponseTransform()).invoke(value, this.idManager.createIdHeaderValue(value, this.buildProps.packageName), this.buildProps);
    }
    
    @SuppressLint({ "CommitPrefEdits" })
    protected void checkForUpdates() {
        final PreferenceStore preferenceStore = this.preferenceStore;
        Label_0220: {
            final long currentTimeMillis;
            synchronized (preferenceStore) {
                if (this.preferenceStore.get().contains("last_update_check")) {
                    this.preferenceStore.save(this.preferenceStore.edit().remove("last_update_check"));
                }
                // monitorexit(preferenceStore)
                currentTimeMillis = this.currentTimeProvider.getCurrentTimeMillis();
                final long n = this.betaSettings.updateSuspendDurationSeconds * 1000L;
                Fabric.getLogger().d("Beta", "Check for updates delay: " + n);
                Fabric.getLogger().d("Beta", "Check for updates last check time: " + this.getLastCheckTimeMillis());
                final long n2 = this.getLastCheckTimeMillis() + n;
                Fabric.getLogger().d("Beta", "Check for updates current time: " + currentTimeMillis + ", next check time: " + n2);
                if (currentTimeMillis >= n2) {
                    final AbstractCheckForUpdatesController abstractCheckForUpdatesController = this;
                    abstractCheckForUpdatesController.performUpdateCheck();
                    return;
                }
                break Label_0220;
            }
            try {
                final AbstractCheckForUpdatesController abstractCheckForUpdatesController = this;
                abstractCheckForUpdatesController.performUpdateCheck();
                return;
            }
            finally {
                this.setLastCheckTimeMillis(currentTimeMillis);
            }
        }
        Fabric.getLogger().d("Beta", "Check for updates next check time was not passed");
    }
    
    long getLastCheckTimeMillis() {
        return this.lastCheckTimeMillis;
    }
    
    @Override
    public void initialize(final Context context, final Beta beta, final IdManager idManager, final BetaSettingsData betaSettings, final BuildProperties buildProps, final PreferenceStore preferenceStore, final CurrentTimeProvider currentTimeProvider, final HttpRequestFactory httpRequestFactory) {
        this.context = context;
        this.beta = beta;
        this.idManager = idManager;
        this.betaSettings = betaSettings;
        this.buildProps = buildProps;
        this.preferenceStore = preferenceStore;
        this.currentTimeProvider = currentTimeProvider;
        this.httpRequestFactory = httpRequestFactory;
        if (this.signalInitialized()) {
            this.checkForUpdates();
        }
    }
    
    void setLastCheckTimeMillis(final long lastCheckTimeMillis) {
        this.lastCheckTimeMillis = lastCheckTimeMillis;
    }
    
    protected boolean signalExternallyReady() {
        this.externallyReady.set(true);
        return this.initialized.get();
    }
    
    boolean signalInitialized() {
        this.initialized.set(true);
        return this.externallyReady.get();
    }
}
