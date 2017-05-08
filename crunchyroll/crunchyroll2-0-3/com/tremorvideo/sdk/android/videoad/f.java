// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.content.Context;
import android.app.Activity;

public class f extends a implements aq.d, ax.c
{
    p a;
    ax b;
    aq e;
    bw f;
    
    public f(final a a, final Activity activity, final p a2) {
        super(a, activity);
        (this.f = new bw()).a();
        (this.a = a2).a(this.f);
        (this.b = new ax(activity, a, this.f)).a((ax.c)this);
        while (true) {
            try {
                this.e = ac.a((Context)this.c);
                this.e.a((aq.d)this);
                this.e.a(this.a.G());
                this.c.requestWindowFeature(1);
                this.c.getWindow().setFlags(1024, 1024);
                ac.a(this.c.getWindow());
                this.c.setContentView((View)this.e, new ViewGroup$LayoutParams(-1, -1));
                ac.a(this.c.getWindow());
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    @Override
    public void a(final aw aw) {
        this.e.e();
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            if (this.e.a()) {
                this.e.b();
            }
            else {
                this.e.c();
                this.d.a(this);
            }
            return true;
        }
        return super.a(n, keyEvent);
    }
    
    @Override
    public boolean a(final String s) {
        final aw c = this.a.c(s);
        if (c != null) {
            if (!c.m()) {
                final aa aa = new aa((Context)this.c, this.f, com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                    @Override
                    public void a(final boolean b) {
                        f.this.e.d();
                    }
                });
                this.e.e();
                aa.setCanceledOnTouchOutside(false);
                aa.setTitle("Unsupported Feature");
                aa.a("Sorry, that feature is not supported on your device.");
                aa.a("OK", "");
                aa.show();
            }
            else {
                if (c.a() != aw.b.v) {
                    this.b.c(c);
                    return false;
                }
                this.b.c(c);
                this.e.c();
                this.d.a(this);
                return false;
            }
        }
        return false;
    }
    
    @Override
    public void b() {
        this.e.e();
        super.b();
    }
    
    @Override
    public void b(final aw aw) {
        this.e.d();
    }
    
    @Override
    public void c() {
        this.e.d();
        super.c();
    }
    
    @Override
    public boolean j() {
        return false;
    }
    
    @Override
    public b n() {
        return com.tremorvideo.sdk.android.videoad.a.b.b;
    }
}
