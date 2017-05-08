// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import org.apache.http.NameValuePair;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.r;
import com.tremorvideo.sdk.android.videoad.n;
import android.app.Activity;
import com.tremorvideo.sdk.android.videoad.bw;
import com.tremorvideo.sdk.android.videoad.h;

public class a extends h
{
    bw a;
    private boolean p;
    private boolean q;
    
    public a(final com.tremorvideo.sdk.android.videoad.a.a a, final Activity activity, final com.tremorvideo.sdk.android.videoad.n n) {
        super(a, activity, (r)n);
        this.q = false;
        this.p = false;
        this.a = n.q();
    }
    
    @Override
    public void a() {
        if (!this.q) {
            this.q = true;
        }
    }
    
    public void a(final String s, final List<NameValuePair> list) {
        this.b(s, list);
    }
    
    @Override
    public void b() {
        super.b();
    }
    
    @Override
    public void c() {
        super.c();
    }
    
    @Override
    public void d() {
    }
    
    public void e() {
        synchronized (this) {
            this.d.a(this);
        }
    }
    
    public void f() {
    }
}
