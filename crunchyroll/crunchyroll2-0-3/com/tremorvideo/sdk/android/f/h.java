// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import java.util.HashMap;
import java.util.Map;

class h
{
    private static Map<String, a> a;
    
    static {
        (h.a = new HashMap<String, a>()).put("close", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new e(map, s);
            }
        });
        h.a.put("usecustomclose", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new k(map, s);
            }
        });
        h.a.put("open", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new f(map, s);
            }
        });
        h.a.put("playVideo", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new g(map, s);
            }
        });
        h.a.put("storePicture", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new j(map, s);
            }
        });
        h.a.put("setOrientationProperties", (a)new a() {
            @Override
            public d a(final Map<String, String> map, final s s) {
                return new i(map, s);
            }
        });
    }
    
    static d a(final String s, final Map<String, String> map, final s s2) {
        final a a = h.a.get(s);
        if (a != null) {
            return a.a(map, s2);
        }
        return null;
    }
    
    private interface a
    {
        d a(final Map<String, String> p0, final s p1);
    }
}
