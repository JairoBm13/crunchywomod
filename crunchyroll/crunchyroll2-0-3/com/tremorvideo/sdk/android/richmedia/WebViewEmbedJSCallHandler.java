// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.app.Activity;

public class WebViewEmbedJSCallHandler
{
    ag a;
    
    public WebViewEmbedJSCallHandler(final ag a) {
        this.a = a;
    }
    
    public void reportProgressTime(final int n) {
        this.a.a(n);
    }
    
    public void videoPlaying() {
        this.a.l = ag.b;
    }
    
    public void videoStart() {
        ((Activity)this.a.getContext()).runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                WebViewEmbedJSCallHandler.this.a.e();
            }
        });
    }
}
