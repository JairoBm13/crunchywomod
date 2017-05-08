// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.settings.AbstractAppSpiCall;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.Iterator;
import java.util.HashMap;
import io.fabric.sdk.android.services.settings.SettingsData;
import io.fabric.sdk.android.services.settings.UpdateAppSpiCall;
import io.fabric.sdk.android.services.settings.CreateAppSpiCall;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.AppSettingsData;
import android.content.Context;
import io.fabric.sdk.android.services.common.DeliveryMechanism;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.settings.AppRequestData;
import io.fabric.sdk.android.services.settings.IconRequest;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.Collection;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import java.util.Map;
import java.util.concurrent.Future;

class Onboarding extends Kit<Boolean>
{
    private String applicationLabel;
    private String installerPackageName;
    private final Future<Map<String, KitInfo>> kitsFinder;
    private PackageInfo packageInfo;
    private PackageManager packageManager;
    private String packageName;
    private final Collection<Kit> providedKits;
    private final HttpRequestFactory requestFactory;
    private String targetAndroidSdkVersion;
    private String versionCode;
    private String versionName;
    
    public Onboarding(final Future<Map<String, KitInfo>> kitsFinder, final Collection<Kit> providedKits) {
        this.requestFactory = new DefaultHttpRequestFactory();
        this.kitsFinder = kitsFinder;
        this.providedKits = providedKits;
    }
    
    private AppRequestData buildAppRequest(final IconRequest iconRequest, final Collection<KitInfo> collection) {
        final Context context = this.getContext();
        return new AppRequestData(new ApiKey().getValue(context), this.getIdManager().getAppIdentifier(), this.versionName, this.versionCode, CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(context)), this.applicationLabel, DeliveryMechanism.determineFrom(this.installerPackageName).getId(), this.targetAndroidSdkVersion, "0", iconRequest, collection);
    }
    
    private boolean performAutoConfigure(final String s, final AppSettingsData appSettingsData, final Collection<KitInfo> collection) {
        boolean loadSettingsSkippingCache = true;
        if ("new".equals(appSettingsData.status)) {
            if (!this.performCreateApp(s, appSettingsData, collection)) {
                Fabric.getLogger().e("Fabric", "Failed to create app with Crashlytics service.", null);
                return false;
            }
            loadSettingsSkippingCache = Settings.getInstance().loadSettingsSkippingCache();
        }
        else {
            if ("configured".equals(appSettingsData.status)) {
                return Settings.getInstance().loadSettingsSkippingCache();
            }
            if (appSettingsData.updateRequired) {
                Fabric.getLogger().d("Fabric", "Server says an update is required - forcing a full App update.");
                this.performUpdateApp(s, appSettingsData, collection);
                return true;
            }
        }
        return loadSettingsSkippingCache;
    }
    
    private boolean performCreateApp(final String s, final AppSettingsData appSettingsData, final Collection<KitInfo> collection) {
        return new CreateAppSpiCall(this, this.getOverridenSpiEndpoint(), appSettingsData.url, this.requestFactory).invoke(this.buildAppRequest(IconRequest.build(this.getContext(), s), collection));
    }
    
    private boolean performUpdateApp(final AppSettingsData appSettingsData, final IconRequest iconRequest, final Collection<KitInfo> collection) {
        return new UpdateAppSpiCall(this, this.getOverridenSpiEndpoint(), appSettingsData.url, this.requestFactory).invoke(this.buildAppRequest(iconRequest, collection));
    }
    
    private boolean performUpdateApp(final String s, final AppSettingsData appSettingsData, final Collection<KitInfo> collection) {
        return this.performUpdateApp(appSettingsData, IconRequest.build(this.getContext(), s), collection);
    }
    
    private SettingsData retrieveSettingsData() {
        try {
            Settings.getInstance().initialize(this, this.idManager, this.requestFactory, this.versionCode, this.versionName, this.getOverridenSpiEndpoint()).loadSettingsData();
            return Settings.getInstance().awaitSettingsData();
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Error dealing with settings", ex);
            return null;
        }
    }
    
    @Override
    protected Boolean doInBackground() {
        final String appIconHashOrNull = CommonUtils.getAppIconHashOrNull(this.getContext());
        final boolean b = false;
        final SettingsData retrieveSettingsData = this.retrieveSettingsData();
        boolean performAutoConfigure = b;
        Label_0072: {
            if (retrieveSettingsData == null) {
                break Label_0072;
            }
            try {
                Map<String, KitInfo> map;
                if (this.kitsFinder != null) {
                    map = this.kitsFinder.get();
                }
                else {
                    map = new HashMap<String, KitInfo>();
                }
                performAutoConfigure = this.performAutoConfigure(appIconHashOrNull, retrieveSettingsData.appData, this.mergeKits(map, this.providedKits).values());
                return performAutoConfigure;
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Fabric", "Error performing auto configuration.", ex);
                performAutoConfigure = b;
                return performAutoConfigure;
            }
        }
    }
    
    @Override
    public String getIdentifier() {
        return "io.fabric.sdk.android:fabric";
    }
    
    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(this.getContext(), "com.crashlytics.ApiEndpoint");
    }
    
    @Override
    public String getVersion() {
        return "1.3.6.79";
    }
    
    Map<String, KitInfo> mergeKits(final Map<String, KitInfo> map, final Collection<Kit> collection) {
        for (final Kit kit : collection) {
            if (!map.containsKey(kit.getIdentifier())) {
                map.put(kit.getIdentifier(), new KitInfo(kit.getIdentifier(), kit.getVersion(), "binary"));
            }
        }
        return map;
    }
    
    @Override
    protected boolean onPreExecute() {
        try {
            this.installerPackageName = this.getIdManager().getInstallerPackageName();
            this.packageManager = this.getContext().getPackageManager();
            this.packageName = this.getContext().getPackageName();
            this.packageInfo = this.packageManager.getPackageInfo(this.packageName, 0);
            this.versionCode = Integer.toString(this.packageInfo.versionCode);
            String versionName;
            if (this.packageInfo.versionName == null) {
                versionName = "0.0";
            }
            else {
                versionName = this.packageInfo.versionName;
            }
            this.versionName = versionName;
            this.applicationLabel = this.packageManager.getApplicationLabel(this.getContext().getApplicationInfo()).toString();
            this.targetAndroidSdkVersion = Integer.toString(this.getContext().getApplicationInfo().targetSdkVersion);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Fabric.getLogger().e("Fabric", "Failed init", (Throwable)ex);
            return false;
        }
    }
}
