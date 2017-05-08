// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

abstract class o
{
    private String a(final String s) {
        if (s != null) {
            return s.replaceAll("[^a-zA-Z0-9_,:\\s\\{\\}\\'\\\"]", "");
        }
        return "";
    }
    
    public abstract String a();
    
    @Override
    public String toString() {
        return this.a(this.a());
    }
}
