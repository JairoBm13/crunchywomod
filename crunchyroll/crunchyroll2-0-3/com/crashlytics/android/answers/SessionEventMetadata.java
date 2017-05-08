// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

final class SessionEventMetadata
{
    public final String advertisingId;
    public final String androidId;
    public final String appBundleId;
    public final String appVersionCode;
    public final String appVersionName;
    public final String betaDeviceToken;
    public final String buildId;
    public final String deviceModel;
    public final String executionId;
    public final String installationId;
    public final Boolean limitAdTrackingEnabled;
    public final String osVersion;
    private String stringRepresentation;
    
    public SessionEventMetadata(final String appBundleId, final String executionId, final String installationId, final String androidId, final String advertisingId, final Boolean limitAdTrackingEnabled, final String betaDeviceToken, final String buildId, final String osVersion, final String deviceModel, final String appVersionCode, final String appVersionName) {
        this.appBundleId = appBundleId;
        this.executionId = executionId;
        this.installationId = installationId;
        this.androidId = androidId;
        this.advertisingId = advertisingId;
        this.limitAdTrackingEnabled = limitAdTrackingEnabled;
        this.betaDeviceToken = betaDeviceToken;
        this.buildId = buildId;
        this.osVersion = osVersion;
        this.deviceModel = deviceModel;
        this.appVersionCode = appVersionCode;
        this.appVersionName = appVersionName;
    }
    
    @Override
    public String toString() {
        if (this.stringRepresentation == null) {
            this.stringRepresentation = "appBundleId=" + this.appBundleId + ", executionId=" + this.executionId + ", installationId=" + this.installationId + ", androidId=" + this.androidId + ", advertisingId=" + this.advertisingId + ", limitAdTrackingEnabled=" + this.limitAdTrackingEnabled + ", betaDeviceToken=" + this.betaDeviceToken + ", buildId=" + this.buildId + ", osVersion=" + this.osVersion + ", deviceModel=" + this.deviceModel + ", appVersionCode=" + this.appVersionCode + ", appVersionName=" + this.appVersionName;
        }
        return this.stringRepresentation;
    }
}
