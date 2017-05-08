// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class PromptSettingsData
{
    public final String alwaysSendButtonTitle;
    public final String cancelButtonTitle;
    public final String message;
    public final String sendButtonTitle;
    public final boolean showAlwaysSendButton;
    public final boolean showCancelButton;
    public final String title;
    
    public PromptSettingsData(final String title, final String message, final String sendButtonTitle, final boolean showCancelButton, final String cancelButtonTitle, final boolean showAlwaysSendButton, final String alwaysSendButtonTitle) {
        this.title = title;
        this.message = message;
        this.sendButtonTitle = sendButtonTitle;
        this.showCancelButton = showCancelButton;
        this.cancelButtonTitle = cancelButtonTitle;
        this.showAlwaysSendButton = showAlwaysSendButton;
        this.alwaysSendButtonTitle = alwaysSendButtonTitle;
    }
}
