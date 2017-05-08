// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class FeaturesSettingsData
{
    public final boolean collectAnalytics;
    public final boolean collectLoggedException;
    public final boolean collectReports;
    public final boolean promptEnabled;
    
    public FeaturesSettingsData(final boolean promptEnabled, final boolean collectLoggedException, final boolean collectReports, final boolean collectAnalytics) {
        this.promptEnabled = promptEnabled;
        this.collectLoggedException = collectLoggedException;
        this.collectReports = collectReports;
        this.collectAnalytics = collectAnalytics;
    }
}
