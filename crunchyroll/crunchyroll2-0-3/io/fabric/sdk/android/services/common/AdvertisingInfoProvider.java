// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import android.content.Context;

class AdvertisingInfoProvider
{
    private final Context context;
    private final PreferenceStore preferenceStore;
    
    public AdvertisingInfoProvider(final Context context) {
        this.context = context.getApplicationContext();
        this.preferenceStore = new PreferenceStoreImpl(context, "TwitterAdvertisingInfoPreferences");
    }
    
    private AdvertisingInfo getAdvertisingInfoFromStrategies() {
        final AdvertisingInfo advertisingInfo = this.getReflectionStrategy().getAdvertisingInfo();
        if (this.isInfoValid(advertisingInfo)) {
            Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Reflection Provider");
            return advertisingInfo;
        }
        final AdvertisingInfo advertisingInfo2 = this.getServiceStrategy().getAdvertisingInfo();
        if (!this.isInfoValid(advertisingInfo2)) {
            Fabric.getLogger().d("Fabric", "AdvertisingInfo not present");
            return advertisingInfo2;
        }
        Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Service Provider");
        return advertisingInfo2;
    }
    
    private boolean isInfoValid(final AdvertisingInfo advertisingInfo) {
        return advertisingInfo != null && !TextUtils.isEmpty((CharSequence)advertisingInfo.advertisingId);
    }
    
    private void refreshInfoIfNeededAsync(final AdvertisingInfo advertisingInfo) {
        new Thread(new BackgroundPriorityRunnable() {
            public void onRun() {
                final AdvertisingInfo access$000 = AdvertisingInfoProvider.this.getAdvertisingInfoFromStrategies();
                if (!advertisingInfo.equals(access$000)) {
                    Fabric.getLogger().d("Fabric", "Asychronously getting Advertising Info and storing it to preferences");
                    AdvertisingInfoProvider.this.storeInfoToPreferences(access$000);
                }
            }
        }).start();
    }
    
    @SuppressLint({ "CommitPrefEdits" })
    private void storeInfoToPreferences(final AdvertisingInfo advertisingInfo) {
        if (this.isInfoValid(advertisingInfo)) {
            this.preferenceStore.save(this.preferenceStore.edit().putString("advertising_id", advertisingInfo.advertisingId).putBoolean("limit_ad_tracking_enabled", advertisingInfo.limitAdTrackingEnabled));
            return;
        }
        this.preferenceStore.save(this.preferenceStore.edit().remove("advertising_id").remove("limit_ad_tracking_enabled"));
    }
    
    public AdvertisingInfo getAdvertisingInfo() {
        final AdvertisingInfo infoFromPreferences = this.getInfoFromPreferences();
        if (this.isInfoValid(infoFromPreferences)) {
            Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Preference Store");
            this.refreshInfoIfNeededAsync(infoFromPreferences);
            return infoFromPreferences;
        }
        final AdvertisingInfo advertisingInfoFromStrategies = this.getAdvertisingInfoFromStrategies();
        this.storeInfoToPreferences(advertisingInfoFromStrategies);
        return advertisingInfoFromStrategies;
    }
    
    protected AdvertisingInfo getInfoFromPreferences() {
        return new AdvertisingInfo(this.preferenceStore.get().getString("advertising_id", ""), this.preferenceStore.get().getBoolean("limit_ad_tracking_enabled", false));
    }
    
    public AdvertisingInfoStrategy getReflectionStrategy() {
        return new AdvertisingInfoReflectionStrategy(this.context);
    }
    
    public AdvertisingInfoStrategy getServiceStrategy() {
        return new AdvertisingInfoServiceStrategy(this.context);
    }
}
