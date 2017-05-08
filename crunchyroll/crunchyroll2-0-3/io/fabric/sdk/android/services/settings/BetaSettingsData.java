// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class BetaSettingsData
{
    public final int updateSuspendDurationSeconds;
    public final String updateUrl;
    
    public BetaSettingsData(final String updateUrl, final int updateSuspendDurationSeconds) {
        this.updateUrl = updateUrl;
        this.updateSuspendDurationSeconds = updateSuspendDurationSeconds;
    }
}
