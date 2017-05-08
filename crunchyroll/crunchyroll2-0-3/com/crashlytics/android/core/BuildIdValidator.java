// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import android.util.Log;
import io.fabric.sdk.android.services.common.CommonUtils;

class BuildIdValidator
{
    private final String buildId;
    private final boolean requiringBuildId;
    
    public BuildIdValidator(final String buildId, final boolean requiringBuildId) {
        this.buildId = buildId;
        this.requiringBuildId = requiringBuildId;
    }
    
    protected String getMessage(final String s, final String s2) {
        return "This app relies on Crashlytics. Please sign up for access at https://fabric.io/sign_up,\ninstall an Android build tool and ask a team member to invite you to this app's organization.";
    }
    
    public void validate(String message, final String s) {
        if (CommonUtils.isNullOrEmpty(this.buildId) && this.requiringBuildId) {
            message = this.getMessage(message, s);
            Log.e("CrashlyticsCore", ".");
            Log.e("CrashlyticsCore", ".     |  | ");
            Log.e("CrashlyticsCore", ".     |  |");
            Log.e("CrashlyticsCore", ".     |  |");
            Log.e("CrashlyticsCore", ".   \\ |  | /");
            Log.e("CrashlyticsCore", ".    \\    /");
            Log.e("CrashlyticsCore", ".     \\  /");
            Log.e("CrashlyticsCore", ".      \\/");
            Log.e("CrashlyticsCore", ".");
            Log.e("CrashlyticsCore", message);
            Log.e("CrashlyticsCore", ".");
            Log.e("CrashlyticsCore", ".      /\\");
            Log.e("CrashlyticsCore", ".     /  \\");
            Log.e("CrashlyticsCore", ".    /    \\");
            Log.e("CrashlyticsCore", ".   / |  | \\");
            Log.e("CrashlyticsCore", ".     |  |");
            Log.e("CrashlyticsCore", ".     |  |");
            Log.e("CrashlyticsCore", ".     |  |");
            Log.e("CrashlyticsCore", ".");
            throw new CrashlyticsMissingDependencyException(message);
        }
        if (!this.requiringBuildId) {
            Fabric.getLogger().d("CrashlyticsCore", "Configured not to require a build ID.");
        }
    }
}
