// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class SettingsData
{
    public final AnalyticsSettingsData analyticsSettingsData;
    public final AppSettingsData appData;
    public final BetaSettingsData betaSettingsData;
    public final int cacheDuration;
    public final long expiresAtMillis;
    public final FeaturesSettingsData featuresData;
    public final PromptSettingsData promptData;
    public final SessionSettingsData sessionData;
    public final int settingsVersion;
    
    public SettingsData(final long expiresAtMillis, final AppSettingsData appData, final SessionSettingsData sessionData, final PromptSettingsData promptData, final FeaturesSettingsData featuresData, final AnalyticsSettingsData analyticsSettingsData, final BetaSettingsData betaSettingsData, final int settingsVersion, final int cacheDuration) {
        this.expiresAtMillis = expiresAtMillis;
        this.appData = appData;
        this.sessionData = sessionData;
        this.promptData = promptData;
        this.featuresData = featuresData;
        this.settingsVersion = settingsVersion;
        this.cacheDuration = cacheDuration;
        this.analyticsSettingsData = analyticsSettingsData;
        this.betaSettingsData = betaSettingsData;
    }
    
    public boolean isExpired(final long n) {
        return this.expiresAtMillis < n;
    }
}
