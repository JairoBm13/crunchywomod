// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import android.annotation.TargetApi;
import android.app.Application;
import android.text.TextUtils;
import java.io.InputStream;
import java.io.IOException;
import io.fabric.sdk.android.services.settings.SettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.BetaSettingsData;
import io.fabric.sdk.android.services.cache.ValueLoader;
import io.fabric.sdk.android.Fabric;
import android.os.Build$VERSION;
import android.content.Context;
import io.fabric.sdk.android.services.cache.MemoryValueCache;
import io.fabric.sdk.android.services.common.DeviceIdentifierProvider;
import io.fabric.sdk.android.Kit;

public class Beta extends Kit<Boolean> implements DeviceIdentifierProvider
{
    private final MemoryValueCache<String> deviceTokenCache;
    private final DeviceTokenLoader deviceTokenLoader;
    private UpdatesController updatesController;
    
    public Beta() {
        this.deviceTokenCache = new MemoryValueCache<String>();
        this.deviceTokenLoader = new DeviceTokenLoader();
    }
    
    private String getBetaDeviceToken(final Context context, final String s) {
        if (this.isAppPossiblyInstalledByBeta(s, Build$VERSION.SDK_INT)) {
            Fabric.getLogger().d("Beta", "App was possibly installed by Beta. Getting device token");
            try {
                final String s2 = this.deviceTokenCache.get(context, this.deviceTokenLoader);
                if ("".equals(s2)) {
                    return null;
                }
                return s2;
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Beta", "Failed to load the Beta device token", ex);
                return null;
            }
        }
        Fabric.getLogger().d("Beta", "App was not installed by Beta. Skipping device token");
        return null;
    }
    
    private BetaSettingsData getBetaSettingsData() {
        final SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
        if (awaitSettingsData != null) {
            return awaitSettingsData.betaSettingsData;
        }
        return null;
    }
    
    private BuildProperties loadBuildProperties(Context ex) {
        Object o = null;
        InputStream inputStream = null;
        final InputStream inputStream2 = null;
        final Exception ex2 = null;
        Object fromPropertiesStream = inputStream2;
        try {
            final InputStream open = ((Context)ex).getAssets().open("crashlytics-build.properties");
            ex = ex2;
            if (open != null) {
                fromPropertiesStream = inputStream2;
                inputStream = open;
                o = open;
                ex = (Exception)(fromPropertiesStream = BuildProperties.fromPropertiesStream(open));
                inputStream = open;
                o = open;
                Fabric.getLogger().d("Beta", ((BuildProperties)ex).packageName + " build properties: " + ((BuildProperties)ex).versionName + " (" + ((BuildProperties)ex).versionCode + ")" + " - " + ((BuildProperties)ex).buildId);
            }
            o = ex;
            if (open == null) {
                return (BuildProperties)o;
            }
            try {
                open.close();
                o = ex;
                return (BuildProperties)o;
            }
            catch (IOException fromPropertiesStream) {
                Fabric.getLogger().e("Beta", "Error closing Beta build properties asset", (Throwable)fromPropertiesStream);
                return (BuildProperties)ex;
            }
        }
        catch (Exception ex) {
            o = inputStream;
            Fabric.getLogger().e("Beta", "Error reading Beta build properties", ex);
            o = fromPropertiesStream;
            if (inputStream == null) {
                return (BuildProperties)o;
            }
            try {
                inputStream.close();
                return (BuildProperties)fromPropertiesStream;
            }
            catch (IOException ex3) {
                Fabric.getLogger().e("Beta", "Error closing Beta build properties asset", ex3);
                return (BuildProperties)fromPropertiesStream;
            }
        }
        finally {
            Label_0218: {
                if (o == null) {
                    break Label_0218;
                }
                try {
                    ((InputStream)o).close();
                }
                catch (IOException ex4) {
                    Fabric.getLogger().e("Beta", "Error closing Beta build properties asset", ex4);
                }
            }
        }
    }
    
    boolean canCheckForUpdates(final BetaSettingsData betaSettingsData, final BuildProperties buildProperties) {
        return betaSettingsData != null && !TextUtils.isEmpty((CharSequence)betaSettingsData.updateUrl) && buildProperties != null;
    }
    
    @TargetApi(14)
    UpdatesController createUpdatesController(final int n, final Application application) {
        if (n >= 14) {
            return new ActivityLifecycleCheckForUpdatesController(this.getFabric().getActivityLifecycleManager(), this.getFabric().getExecutorService());
        }
        return new ImmediateCheckForUpdatesController();
    }
    
    @Override
    protected Boolean doInBackground() {
        Fabric.getLogger().d("Beta", "Beta kit initializing...");
        final Context context = this.getContext();
        final IdManager idManager = this.getIdManager();
        if (TextUtils.isEmpty((CharSequence)this.getBetaDeviceToken(context, idManager.getInstallerPackageName()))) {
            Fabric.getLogger().d("Beta", "A Beta device token was not found for this app");
            return false;
        }
        Fabric.getLogger().d("Beta", "Beta device token is present, checking for app updates.");
        final BetaSettingsData betaSettingsData = this.getBetaSettingsData();
        final BuildProperties loadBuildProperties = this.loadBuildProperties(context);
        if (this.canCheckForUpdates(betaSettingsData, loadBuildProperties)) {
            this.updatesController.initialize(context, this, idManager, betaSettingsData, loadBuildProperties, new PreferenceStoreImpl(this), new SystemCurrentTimeProvider(), new DefaultHttpRequestFactory(Fabric.getLogger()));
        }
        return true;
    }
    
    @Override
    public Map<IdManager.DeviceIdentifierType, String> getDeviceIdentifiers() {
        final String betaDeviceToken = this.getBetaDeviceToken(this.getContext(), this.getIdManager().getInstallerPackageName());
        final HashMap<IdManager.DeviceIdentifierType, String> hashMap = new HashMap<IdManager.DeviceIdentifierType, String>();
        if (!TextUtils.isEmpty((CharSequence)betaDeviceToken)) {
            hashMap.put(IdManager.DeviceIdentifierType.FONT_TOKEN, betaDeviceToken);
        }
        return hashMap;
    }
    
    @Override
    public String getIdentifier() {
        return "com.crashlytics.sdk.android:beta";
    }
    
    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(this.getContext(), "com.crashlytics.ApiEndpoint");
    }
    
    @Override
    public String getVersion() {
        return "1.1.3.61";
    }
    
    @TargetApi(11)
    boolean isAppPossiblyInstalledByBeta(final String s, final int n) {
        if (n < 11) {
            return s == null;
        }
        return "io.crash.air".equals(s);
    }
    
    @TargetApi(14)
    @Override
    protected boolean onPreExecute() {
        this.updatesController = this.createUpdatesController(Build$VERSION.SDK_INT, (Application)this.getContext().getApplicationContext());
        return true;
    }
}
