// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

class t extends o
{
    private final boolean a;
    
    t(final boolean a) {
        this.a = a;
    }
    
    public static t a(final boolean b) {
        return new t(b);
    }
    
    @Override
    public String a() {
        final StringBuilder append = new StringBuilder().append("viewable: ");
        String s;
        if (this.a) {
            s = "true";
        }
        else {
            s = "false";
        }
        return append.append(s).toString();
    }
}
