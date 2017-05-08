// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences$Editor;
import io.fabric.sdk.android.services.common.CommonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;

class DefaultSettingsController implements SettingsController
{
    private final CachedSettingsIo cachedSettingsIo;
    private final CurrentTimeProvider currentTimeProvider;
    private final Kit kit;
    private final PreferenceStore preferenceStore;
    private final SettingsJsonTransform settingsJsonTransform;
    private final SettingsRequest settingsRequest;
    private final SettingsSpiCall settingsSpiCall;
    
    public DefaultSettingsController(final Kit kit, final SettingsRequest settingsRequest, final CurrentTimeProvider currentTimeProvider, final SettingsJsonTransform settingsJsonTransform, final CachedSettingsIo cachedSettingsIo, final SettingsSpiCall settingsSpiCall) {
        this.kit = kit;
        this.settingsRequest = settingsRequest;
        this.currentTimeProvider = currentTimeProvider;
        this.settingsJsonTransform = settingsJsonTransform;
        this.cachedSettingsIo = cachedSettingsIo;
        this.settingsSpiCall = settingsSpiCall;
        this.preferenceStore = new PreferenceStoreImpl(this.kit);
    }
    
    private SettingsData getCachedSettingsData(final SettingsCacheBehavior settingsCacheBehavior) {
        SettingsData settingsData2;
        final SettingsData settingsData = settingsData2 = null;
        Label_0190: {
            try {
                if (SettingsCacheBehavior.SKIP_CACHE_LOOKUP.equals(settingsCacheBehavior)) {
                    return null;
                }
                settingsData2 = settingsData;
                final JSONObject cachedSettings = this.cachedSettingsIo.readCachedSettings();
                if (cachedSettings == null) {
                    break Label_0190;
                }
                settingsData2 = settingsData;
                final SettingsData buildFromJson = this.settingsJsonTransform.buildFromJson(this.currentTimeProvider, cachedSettings);
                if (buildFromJson != null) {
                    settingsData2 = settingsData;
                    this.logSettings(cachedSettings, "Loaded cached settings: ");
                    settingsData2 = settingsData;
                    final long currentTimeMillis = this.currentTimeProvider.getCurrentTimeMillis();
                    settingsData2 = settingsData;
                    if (!SettingsCacheBehavior.IGNORE_CACHE_EXPIRATION.equals(settingsCacheBehavior)) {
                        settingsData2 = settingsData;
                        if (buildFromJson.isExpired(currentTimeMillis)) {
                            settingsData2 = settingsData;
                            Fabric.getLogger().d("Fabric", "Cached settings have expired.");
                            return null;
                        }
                    }
                    settingsData2 = buildFromJson;
                    Fabric.getLogger().d("Fabric", "Returning cached settings.");
                    return settingsData2;
                }
            }
            catch (Exception ex) {
                Fabric.getLogger().e("Fabric", "Failed to get cached settings", ex);
                return settingsData2;
            }
            Fabric.getLogger().e("Fabric", "Failed to transform cached settings data.", null);
            return null;
        }
        Fabric.getLogger().d("Fabric", "No cached settings data found.");
        return null;
    }
    
    private void logSettings(final JSONObject jsonObject, final String s) throws JSONException {
        Fabric.getLogger().d("Fabric", s + jsonObject.toString());
    }
    
    boolean buildInstanceIdentifierChanged() {
        return !this.getStoredBuildInstanceIdentifier().equals(this.getBuildInstanceIdentifierFromContext());
    }
    
    String getBuildInstanceIdentifierFromContext() {
        return CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(this.kit.getContext()));
    }
    
    String getStoredBuildInstanceIdentifier() {
        return this.preferenceStore.get().getString("existing_instance_identifier", "");
    }
    
    @Override
    public SettingsData loadSettingsData() {
        return this.loadSettingsData(SettingsCacheBehavior.USE_CACHE);
    }
    
    @Override
    public SettingsData loadSettingsData(final SettingsCacheBehavior settingsCacheBehavior) {
        final SettingsData settingsData = null;
        SettingsData cachedSettingsData;
        final SettingsData settingsData2 = cachedSettingsData = null;
        SettingsData buildFromJson = settingsData;
        try {
            if (!Fabric.isDebuggable()) {
                cachedSettingsData = settingsData2;
                buildFromJson = settingsData;
                if (!this.buildInstanceIdentifierChanged()) {
                    buildFromJson = settingsData;
                    cachedSettingsData = this.getCachedSettingsData(settingsCacheBehavior);
                }
            }
            if ((buildFromJson = cachedSettingsData) == null) {
                buildFromJson = cachedSettingsData;
                final JSONObject invoke = this.settingsSpiCall.invoke(this.settingsRequest);
                buildFromJson = cachedSettingsData;
                if (invoke != null) {
                    buildFromJson = cachedSettingsData;
                    final SettingsData settingsData3 = buildFromJson = this.settingsJsonTransform.buildFromJson(this.currentTimeProvider, invoke);
                    this.cachedSettingsIo.writeCachedSettings(settingsData3.expiresAtMillis, invoke);
                    buildFromJson = settingsData3;
                    this.logSettings(invoke, "Loaded settings: ");
                    buildFromJson = settingsData3;
                    this.setStoredBuildInstanceIdentifier(this.getBuildInstanceIdentifierFromContext());
                    buildFromJson = settingsData3;
                }
            }
            SettingsData cachedSettingsData2;
            if ((cachedSettingsData2 = buildFromJson) == null) {
                cachedSettingsData2 = this.getCachedSettingsData(SettingsCacheBehavior.IGNORE_CACHE_EXPIRATION);
            }
            return cachedSettingsData2;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Unknown error while loading Crashlytics settings. Crashes will be cached until settings can be retrieved.", ex);
            return buildFromJson;
        }
    }
    
    @SuppressLint({ "CommitPrefEdits" })
    boolean setStoredBuildInstanceIdentifier(final String s) {
        final SharedPreferences$Editor edit = this.preferenceStore.edit();
        edit.putString("existing_instance_identifier", s);
        return this.preferenceStore.save(edit);
    }
}
