// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

class n extends o
{
    private final s.j a;
    
    n(final s.j a) {
        this.a = a;
    }
    
    public static n a(final s.j j) {
        return new n(j);
    }
    
    @Override
    public String a() {
        return "placementType: '" + this.a.toString().toLowerCase() + "'";
    }
}
