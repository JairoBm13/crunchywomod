// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class SettingsRequest
{
    public final String apiKey;
    public final String buildVersion;
    public final String deviceId;
    public final String displayVersion;
    public final String iconHash;
    public final String instanceId;
    public final int source;
    
    public SettingsRequest(final String apiKey, final String deviceId, final String instanceId, final String displayVersion, final String buildVersion, final int source, final String iconHash) {
        this.apiKey = apiKey;
        this.deviceId = deviceId;
        this.instanceId = instanceId;
        this.displayVersion = displayVersion;
        this.buildVersion = buildVersion;
        this.source = source;
        this.iconHash = iconHash;
    }
}
