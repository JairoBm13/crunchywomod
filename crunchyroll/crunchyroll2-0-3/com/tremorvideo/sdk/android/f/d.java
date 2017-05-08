// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import java.util.Map;

abstract class d
{
    protected Map<String, String> a;
    protected s b;
    
    d(final Map<String, String> a, final s b) {
        this.a = a;
        this.b = b;
    }
    
    protected String a(final String s) {
        return this.a.get(s);
    }
    
    abstract void a();
    
    protected boolean b(final String s) {
        return "true".equals(this.a.get(s));
    }
}
