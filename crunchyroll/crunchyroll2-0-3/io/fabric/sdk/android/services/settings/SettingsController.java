// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public interface SettingsController
{
    SettingsData loadSettingsData();
    
    SettingsData loadSettingsData(final SettingsCacheBehavior p0);
}
