// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import com.tremorvideo.sdk.android.videoad.aw;
import com.tremorvideo.sdk.android.videoad.r;

public class l
{
    public r.a a;
    private String b;
    private String c;
    private int d;
    private int e;
    private aw[] f;
    private boolean g;
    
    public l(final String b, final String c, final int d, final int e, final aw[] f, final boolean g, final r.a a) {
        this.d = d;
        this.e = e;
        this.b = b;
        this.c = c;
        this.f = f;
        this.g = g;
        this.a = a;
    }
    
    public int a() {
        return this.d;
    }
    
    public void a(final String c) {
        this.c = c;
    }
    
    public int b() {
        return this.e;
    }
    
    public String c() {
        return this.b;
    }
    
    public int d() {
        if (this.b != null && this.b.startsWith("video-")) {
            return Integer.parseInt(this.b.substring(this.b.length() - 1)) - 1;
        }
        return -1;
    }
    
    public String e() {
        return this.c;
    }
    
    public boolean f() {
        return this.g;
    }
    
    public aw[] g() {
        return this.f;
    }
    
    public r.a h() {
        return this.a;
    }
}
