// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.KitInfo;
import java.util.Collection;

public class AppRequestData
{
    public final String apiKey;
    public final String appId;
    public final String buildVersion;
    public final String builtSdkVersion;
    public final String displayVersion;
    public final IconRequest icon;
    public final String instanceIdentifier;
    public final String minSdkVersion;
    public final String name;
    public final Collection<KitInfo> sdkKits;
    public final int source;
    
    public AppRequestData(final String apiKey, final String appId, final String displayVersion, final String buildVersion, final String instanceIdentifier, final String name, final int source, final String minSdkVersion, final String builtSdkVersion, final IconRequest icon, final Collection<KitInfo> sdkKits) {
        this.apiKey = apiKey;
        this.appId = appId;
        this.displayVersion = displayVersion;
        this.buildVersion = buildVersion;
        this.instanceIdentifier = instanceIdentifier;
        this.name = name;
        this.source = source;
        this.minSdkVersion = minSdkVersion;
        this.builtSdkVersion = builtSdkVersion;
        this.icon = icon;
        this.sdkKits = sdkKits;
    }
}
