// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.util.AttributeSet;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

public class ConversationFullScreenVideoFrame extends FrameLayout
{
    private WebChromeClient webCromeClient;
    
    public ConversationFullScreenVideoFrame(final Context context) {
        super(context);
    }
    
    public ConversationFullScreenVideoFrame(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public void disableFullScreen() {
        if (this.webCromeClient != null) {
            this.webCromeClient.onHideCustomView();
            this.webCromeClient = null;
        }
        this.setVisibility(8);
    }
    
    public void removeWebCromeClient(final WebChromeClient webChromeClient) {
        if (this.webCromeClient == webChromeClient) {
            this.webCromeClient = null;
        }
    }
    
    public void setWebCromeClient(final WebChromeClient webCromeClient) {
        this.webCromeClient = webCromeClient;
    }
}
