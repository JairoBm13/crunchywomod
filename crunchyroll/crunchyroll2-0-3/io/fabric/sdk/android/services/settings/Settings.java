// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import android.content.Context;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import java.util.Locale;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import io.fabric.sdk.android.services.common.DeliveryMechanism;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Settings
{
    private boolean initialized;
    private SettingsController settingsController;
    private final AtomicReference<SettingsData> settingsData;
    private final CountDownLatch settingsDataLatch;
    
    private Settings() {
        this.settingsData = new AtomicReference<SettingsData>();
        this.settingsDataLatch = new CountDownLatch(1);
        this.initialized = false;
    }
    
    public static Settings getInstance() {
        return LazyHolder.INSTANCE;
    }
    
    private void setSettingsData(final SettingsData settingsData) {
        this.settingsData.set(settingsData);
        this.settingsDataLatch.countDown();
    }
    
    public SettingsData awaitSettingsData() {
        try {
            this.settingsDataLatch.await();
            return this.settingsData.get();
        }
        catch (InterruptedException ex) {
            Fabric.getLogger().e("Fabric", "Interrupted while waiting for settings data.");
            return null;
        }
    }
    
    public Settings initialize(final Kit kit, final IdManager idManager, final HttpRequestFactory httpRequestFactory, final String s, final String s2, final String s3) {
        synchronized (this) {
            if (!this.initialized) {
                if (this.settingsController == null) {
                    final Context context = kit.getContext();
                    final String appIdentifier = idManager.getAppIdentifier();
                    final String value = new ApiKey().getValue(context);
                    this.settingsController = new DefaultSettingsController(kit, new SettingsRequest(value, idManager.createIdHeaderValue(value, appIdentifier), CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(context)), s2, s, DeliveryMechanism.determineFrom(idManager.getInstallerPackageName()).getId(), CommonUtils.getAppIconHashOrNull(context)), new SystemCurrentTimeProvider(), new DefaultSettingsJsonTransform(), new DefaultCachedSettingsIo(kit), new DefaultSettingsSpiCall(kit, s3, String.format(Locale.US, "https://settings.crashlytics.com/spi/v2/platforms/android/apps/%s/settings", appIdentifier), httpRequestFactory));
                }
                this.initialized = true;
            }
            return this;
        }
    }
    
    public boolean loadSettingsData() {
        synchronized (this) {
            final SettingsData loadSettingsData = this.settingsController.loadSettingsData();
            this.setSettingsData(loadSettingsData);
            return loadSettingsData != null;
        }
    }
    
    public boolean loadSettingsSkippingCache() {
        synchronized (this) {
            final SettingsData loadSettingsData = this.settingsController.loadSettingsData(SettingsCacheBehavior.SKIP_CACHE_LOOKUP);
            this.setSettingsData(loadSettingsData);
            if (loadSettingsData == null) {
                Fabric.getLogger().e("Fabric", "Failed to force reload of settings from Crashlytics.", null);
            }
            return loadSettingsData != null;
        }
    }
    
    public <T> T withSettings(final SettingsAccess<T> settingsAccess, final T t) {
        final SettingsData settingsData = this.settingsData.get();
        if (settingsData == null) {
            return t;
        }
        return settingsAccess.usingSettings(settingsData);
    }
    
    static class LazyHolder
    {
        private static final Settings INSTANCE;
        
        static {
            INSTANCE = new Settings(null);
        }
    }
    
    public interface SettingsAccess<T>
    {
        T usingSettings(final SettingsData p0);
    }
}
