// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.settings.PromptSettingsData;
import android.content.Context;

class DialogStringResolver
{
    private final Context context;
    private final PromptSettingsData promptData;
    
    public DialogStringResolver(final Context context, final PromptSettingsData promptData) {
        this.context = context;
        this.promptData = promptData;
    }
    
    private boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    private String resourceOrFallbackValue(final String s, final String s2) {
        return this.stringOrFallback(CommonUtils.getStringsFileValue(this.context, s), s2);
    }
    
    private String stringOrFallback(final String s, final String s2) {
        if (this.isNullOrEmpty(s)) {
            return s2;
        }
        return s;
    }
    
    public String getAlwaysSendButtonTitle() {
        return this.resourceOrFallbackValue("com.crashlytics.CrashSubmissionAlwaysSendTitle", this.promptData.alwaysSendButtonTitle);
    }
    
    public String getCancelButtonTitle() {
        return this.resourceOrFallbackValue("com.crashlytics.CrashSubmissionCancelTitle", this.promptData.cancelButtonTitle);
    }
    
    public String getMessage() {
        return this.resourceOrFallbackValue("com.crashlytics.CrashSubmissionPromptMessage", this.promptData.message);
    }
    
    public String getSendButtonTitle() {
        return this.resourceOrFallbackValue("com.crashlytics.CrashSubmissionSendTitle", this.promptData.sendButtonTitle);
    }
    
    public String getTitle() {
        return this.resourceOrFallbackValue("com.crashlytics.CrashSubmissionPromptTitle", this.promptData.title);
    }
}
