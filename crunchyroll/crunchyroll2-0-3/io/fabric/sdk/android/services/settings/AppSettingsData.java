// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class AppSettingsData
{
    public final AppIconSettingsData icon;
    public final String identifier;
    public final String reportsUrl;
    public final String status;
    public final boolean updateRequired;
    public final String url;
    
    public AppSettingsData(final String identifier, final String status, final String url, final String reportsUrl, final boolean updateRequired, final AppIconSettingsData icon) {
        this.identifier = identifier;
        this.status = status;
        this.url = url;
        this.reportsUrl = reportsUrl;
        this.updateRequired = updateRequired;
        this.icon = icon;
    }
}
