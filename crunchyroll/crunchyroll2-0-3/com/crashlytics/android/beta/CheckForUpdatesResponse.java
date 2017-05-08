// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

class CheckForUpdatesResponse
{
    public final String buildVersion;
    public final String displayVersion;
    public final String instanceId;
    public final String packageName;
    public final String url;
    public final String versionString;
    
    public CheckForUpdatesResponse(final String url, final String versionString, final String displayVersion, final String buildVersion, final String packageName, final String instanceId) {
        this.url = url;
        this.versionString = versionString;
        this.displayVersion = displayVersion;
        this.buildVersion = buildVersion;
        this.packageName = packageName;
        this.instanceId = instanceId;
    }
}
