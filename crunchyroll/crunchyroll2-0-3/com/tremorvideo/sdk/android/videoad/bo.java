// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.tremorvideo.sdk.android.b.c;
import android.content.Context;

public class bo extends bf
{
    boolean a;
    Context b;
    n c;
    com.tremorvideo.sdk.android.b.c d;
    
    public bo(final a a, final Context b, final n c) {
        super(a);
        this.a = false;
        this.b = b;
        this.c = c;
    }
    
    @Override
    protected void e() {
        this.d = this.c.z();
        if (this.d != null) {
            this.d.a(this.b);
            this.a(bf.b.b);
        }
    }
    
    @Override
    protected void f() {
        this.a(bf.b.e);
    }
    
    @Override
    protected void g() {
        this.d = this.c.z();
        if (this.d != null) {
            this.d.a(this.b);
            this.a(bf.b.b);
        }
    }
    
    @Override
    public String toString() {
        return "JobProcessMovieBoard";
    }
}
