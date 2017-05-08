// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.webkit.WebView;
import java.util.Map;

abstract class d
{
    protected Map<String, String> a;
    protected WebView b;
    
    d(final Map<String, String> a, final WebView b) {
        this.a = a;
        this.b = b;
    }
    
    abstract void a();
}
