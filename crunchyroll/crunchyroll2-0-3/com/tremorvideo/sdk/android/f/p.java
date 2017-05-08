// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

class p extends o
{
    private final int a;
    private final int b;
    
    p(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    public static p a(final int n, final int n2) {
        return new p(n, n2);
    }
    
    @Override
    public String a() {
        return "screenSize: { width: " + this.a + ", height: " + this.b + " }";
    }
}
