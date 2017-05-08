// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.app.Activity;
import com.tremorvideo.sdk.android.a.c;
import android.content.Context;
import com.tremorvideo.sdk.android.a.i;

public class bm extends bf implements i.b
{
    boolean a;
    Context b;
    n c;
    c d;
    
    public bm(final bf.a a, final Context b, final n c) {
        super(a);
        this.a = false;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public void a(final boolean b, final boolean b2) {
        if (this.d != null) {
            this.d.f();
        }
        if (b) {
            this.a(bf.b.b);
            return;
        }
        if (b2) {
            ac.e("Xml Validation timed out");
            this.a(bf.b.c);
            return;
        }
        ac.e("Buy It Now Xml invalid");
        this.a(bf.b.c);
    }
    
    @Override
    protected void e() {
        this.d = this.c.y();
        if (this.d != null) {
            this.d.a(this.b);
            this.d.e();
            ((Activity)this.b).runOnUiThread((Runnable)new a(this.b, this, this.d));
        }
    }
    
    @Override
    protected void f() {
        this.a(bf.b.e);
    }
    
    @Override
    protected void g() {
        this.d = this.c.y();
        if (this.d != null) {
            this.d.a(this.b);
            this.d.e();
            ((Activity)this.b).runOnUiThread((Runnable)new a(this.b, this, this.d));
        }
    }
    
    @Override
    public String toString() {
        return "JobProcessBuyItNow";
    }
    
    public class a implements Runnable
    {
        Context a;
        i.b b;
        c c;
        
        public a(final Context a, final i.b b, final c c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        @Override
        public void run() {
            this.c.a(this.a, this.b);
        }
    }
}
