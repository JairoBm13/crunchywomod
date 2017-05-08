// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.apache.http.NameValuePair;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.content.Context;
import android.app.Activity;
import android.view.ViewGroup;

public class AdViewManager
{
    public static final int ACTIVITY_REQUEST_CODE = 11;
    ViewGroup a;
    u b;
    t c;
    Activity d;
    com.tremorvideo.sdk.android.videoad.a.a e;
    private ay f;
    
    public AdViewManager(final Activity d, final ViewGroup a, final n n) {
        this.c = null;
        if (n != null) {
            this.c = (t)n;
        }
        this.f = new ay();
        this.d = d;
        this.a = a;
        this.e = new a((Context)d, this.f);
        (this.b = new u(d, this.e, this.c, a.getWidth(), a.getHeight())).a();
        a.addView((View)this.b);
    }
    
    private void a() {
        this.a.removeView((View)this.b);
        this.b = null;
        this.e = null;
    }
    
    private void a(final int n) {
        final ay.a b = this.f.b(n);
        if (b != null) {
            if (b.c.c().equals("-1")) {
                ac.e("Event had ID of -1: not fired");
                return;
            }
            b.c.a(b);
        }
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 11) {
            this.b.a(n);
        }
    }
    
    public void onResume() {
        this.b.d();
    }
    
    public void onScreenOff() {
        this.b.e();
    }
    
    public void onScreenOn() {
        this.b.f();
    }
    
    public void onStop() {
        this.b.c();
    }
    
    public void stopAd() {
        if (this.b != null) {
            this.b.g();
        }
    }
    
    class a implements com.tremorvideo.sdk.android.videoad.a.a
    {
        private Context b;
        private boolean c;
        
        public a(final Context b, final ay ay) {
            this.b = b;
            AdViewManager.this.f = ay;
        }
        
        @Override
        public int a(final aw aw) {
            return this.a(aw, -1);
        }
        
        @Override
        public int a(final aw aw, final int n) {
            return this.a(aw, n, null);
        }
        
        @Override
        public int a(final aw aw, int n, final List<NameValuePair> list) {
            int a = -1;
            if (aw != null) {
                ac.e("Starting Event: " + ac.G() + "ms " + aw.a().toString() + " " + aw.h() + " " + aw.c());
                n = (a = AdViewManager.this.f.a(aw, n, null, null));
                if (aw.g()) {
                    AdViewManager.this.a(n);
                    a = n;
                }
            }
            return a;
        }
        
        @Override
        public void a(final int n) {
            if (n != -1) {
                AdViewManager.this.f.a(n);
                final ay.a b = AdViewManager.this.f.b(n);
                final aw c = b.c;
                ac.e("Ending Event: " + b.e + "ms " + c.a().toString() + " " + c.h() + " " + c.c());
                if (!c.g()) {
                    AdViewManager.this.a(n);
                }
            }
        }
        
        @Override
        public void a(final com.tremorvideo.sdk.android.videoad.a a) {
            ac.e(" ");
            ac.e("<<< Ad End");
            ac.e(" ");
            ac.C().j();
            final bx.a b = bx.a.b;
            int n;
            if (this.c) {
                n = -1;
            }
            else {
                n = 1;
            }
            bx.a(b, true, n);
            AdViewManager.this.a();
        }
        
        @Override
        public void b(final int n) {
        }
        
        @Override
        public n g() {
            return AdViewManager.this.c;
        }
        
        @Override
        public Context h() {
            return this.b;
        }
        
        @Override
        public int i() {
            return 0;
        }
        
        @Override
        public int j() {
            return -1;
        }
        
        @Override
        public void l() {
            this.c = true;
        }
    }
}
