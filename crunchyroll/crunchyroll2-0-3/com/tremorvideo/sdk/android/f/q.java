// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

class q extends o
{
    private final s.k a;
    
    q(final s.k a) {
        this.a = a;
    }
    
    public static q a(final s.k k) {
        return new q(k);
    }
    
    @Override
    public String a() {
        return "state: '" + this.a.toString().toLowerCase() + "'";
    }
}
