// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import android.util.Log;
import android.view.View;
import com.google.ads.interactivemedia.v3.b.b.d;
import com.google.ads.interactivemedia.v3.api.Ad;
import com.google.ads.interactivemedia.v3.b.a.a;
import android.content.Context;
import com.google.ads.interactivemedia.v3.b.b.e;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;

public class h implements v.a
{
    private final String a;
    private s b;
    private u c;
    private AdDisplayContainer d;
    private e e;
    private Context f;
    private com.google.ads.interactivemedia.v3.b.a.a g;
    
    public h(final String a, final u c, final s b, final AdDisplayContainer d, final Context f) {
        this.b = b;
        this.c = c;
        this.f = f;
        this.a = a;
        this.d = d;
    }
    
    private void b(final Ad ad) {
        switch (h$1.b[this.c.b().ordinal()]) {
            default: {}
            case 1: {
                this.e = new e(this.f, this.d.getPlayer(), com.google.ads.interactivemedia.v3.b.b.d.a(ad), this.b, this.a);
                this.b.a((s.c)this.e, this.a);
                this.e.a((e.a)new a());
                this.d.getAdContainer().addView(this.e.a());
                this.e.a(ad);
            }
            case 2: {
                this.d.getAdContainer().addView((View)this.b.a());
            }
        }
    }
    
    public void a() {
        switch (h$1.b[this.c.b().ordinal()]) {
            case 1: {
                if (this.e != null) {
                    this.e.b();
                    this.d.getAdContainer().removeView(this.e.a());
                    this.e = null;
                    this.b.a(this.a);
                    return;
                }
                break;
            }
            case 2: {
                this.d.getAdContainer().removeView((View)this.b.a());
            }
        }
    }
    
    public void a(final Ad ad) {
        if (this.g != null && !this.g.equals(ad)) {
            Log.e("IMASDK", "Cannot stop non current ad UI");
            return;
        }
        this.a();
        this.g = null;
    }
    
    @Override
    public void a(final VideoProgressUpdate videoProgressUpdate) {
        if (this.e != null) {
            this.e.a(videoProgressUpdate);
        }
    }
    
    public void a(final com.google.ads.interactivemedia.v3.b.a.a g) {
        if (this.g != null) {
            this.a((Ad)this.g);
        }
        if (g.isLinear()) {
            this.b(this.g = g);
        }
    }
    
    private class a implements e.a
    {
        @Override
        public void a() {
            h.this.b.b(new r(r.b.videoDisplay, r.c.skip, h.this.a));
        }
        
        @Override
        public void b() {
        }
        
        @Override
        public void c() {
            h.this.b.b(new r(r.b.videoDisplay, r.c.click, h.this.a));
            h.this.b.b(h.this.g.getClickThruUrl());
        }
    }
}
