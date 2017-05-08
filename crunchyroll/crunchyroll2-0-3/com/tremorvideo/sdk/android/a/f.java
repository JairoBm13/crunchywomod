// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.webkit.WebView;
import java.util.HashMap;
import java.util.Map;

class f
{
    private static Map<String, a> a;
    
    static {
        (f.a = new HashMap<String, a>()).put("trigger-event", (a)new a() {
            public g a(final Map<String, String> map, final WebView webView) {
                return new g(map, webView);
            }
        });
        f.a.put("cancel-auto-skip", (a)new a() {
            public e a(final Map<String, String> map, final WebView webView) {
                return new e(map, webView);
            }
        });
        f.a.put("validate-complete", (a)new a() {
            public h a(final Map<String, String> map, final WebView webView) {
                return new h(map, webView);
            }
        });
    }
    
    static d a(final String s, final Map<String, String> map, final WebView webView) {
        final a a = f.a.get(s);
        if (a != null) {
            return a.b(map, webView);
        }
        return null;
    }
    
    private interface a
    {
        d b(final Map<String, String> p0, final WebView p1);
    }
}
