// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.b;

import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import java.util.HashMap;
import com.google.ads.interactivemedia.v3.b.r;
import com.google.ads.interactivemedia.v3.api.Ad;
import java.util.Iterator;
import android.text.TextUtils;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import java.util.ArrayList;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import android.content.Context;
import java.util.List;
import android.widget.FrameLayout;
import com.google.ads.interactivemedia.v3.b.v;
import com.google.ads.interactivemedia.v3.b.s;
import android.view.View$OnClickListener;
import android.widget.RelativeLayout;

public class e extends RelativeLayout implements View$OnClickListener, c, v.a
{
    private FrameLayout a;
    private List<a> b;
    private final float c;
    private final String d;
    private s e;
    private boolean f;
    private float g;
    private String h;
    private b i;
    private com.google.ads.interactivemedia.v3.b.b.b j;
    private d k;
    private com.google.ads.interactivemedia.v3.b.b.a l;
    
    public e(final Context context, final VideoAdPlayer videoAdPlayer, final d k, final s e, final String d) {
        super(context);
        this.b = new ArrayList<a>();
        this.f = false;
        this.e = e;
        this.d = d;
        this.k = k;
        this.c = this.getResources().getDisplayMetrics().density;
        this.b(context);
        if (k.a) {
            this.a(context);
        }
        this.a(this.f);
    }
    
    private void a(final Context context) {
        this.a = new FrameLayout(context);
        this.j = new com.google.ads.interactivemedia.v3.b.b.b(context);
        this.a.addView((View)this.j, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-2, -2));
        final int a = com.google.ads.interactivemedia.v3.b.b.c.a(15, this.c);
        this.a.setPadding(a, a, 0, a);
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-2, -2);
        layoutParams.addRule(12);
        layoutParams.addRule(11);
        this.a.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.a.setOnClickListener((View$OnClickListener)this);
        this.addView((View)this.a);
    }
    
    private void a(final String s) {
        if (!this.f) {
            this.l.a(s);
            return;
        }
        if (!TextUtils.isEmpty((CharSequence)this.h)) {
            this.l.a(s + ": " + this.h + "»");
            return;
        }
        this.l.a(s);
    }
    
    private void b(final Context context) {
        this.l = new com.google.ads.interactivemedia.v3.b.b.a(context, this.k);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -2);
        relativeLayout$LayoutParams.addRule(10);
        this.addView((View)this.l, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.l.a((com.google.ads.interactivemedia.v3.b.b.a.a)new com.google.ads.interactivemedia.v3.b.b.a.a() {
            @Override
            public void c() {
                final Iterator<e.a> iterator = com.google.ads.interactivemedia.v3.b.b.e.this.b.iterator();
                while (iterator.hasNext()) {
                    ((a.a)iterator.next()).c();
                }
            }
        });
    }
    
    private void b(final String s) {
        this.l.b(s);
    }
    
    public View a() {
        return (View)this;
    }
    
    public void a(final Ad ad) {
        this.a("");
        this.b(this.k.m);
        this.e.b(new r(r.b.i18n, r.c.learnMore, this.d));
        if (ad.isSkippable()) {
            this.i = com.google.ads.interactivemedia.v3.b.b.e.b.b;
            this.a.setVisibility(0);
            final HashMap<String, Integer> hashMap = new HashMap<String, Integer>(1);
            hashMap.put("seconds", 5);
            this.e.b(new r(r.b.i18n, r.c.preSkipButton, this.d, hashMap));
        }
        else {
            this.i = com.google.ads.interactivemedia.v3.b.b.e.b.a;
            if (this.a != null) {
                this.a.setVisibility(4);
            }
        }
        this.setVisibility(0);
    }
    
    public void a(final VideoProgressUpdate videoProgressUpdate) {
        if (videoProgressUpdate != null && videoProgressUpdate.getDuration() >= 0.0f) {
            final float g = videoProgressUpdate.getDuration() - videoProgressUpdate.getCurrentTime();
            boolean b;
            if (Math.floor(g) != Math.floor(this.g)) {
                b = true;
            }
            else {
                b = false;
            }
            if (b) {
                final HashMap<String, Float> hashMap = new HashMap<String, Float>(2);
                hashMap.put("minutes", g / 60.0f);
                hashMap.put("seconds", g % 60.0f);
                this.e.b(new r(r.b.i18n, r.c.adRemainingTime, this.d, hashMap));
            }
            if (this.i == com.google.ads.interactivemedia.v3.b.b.e.b.b) {
                final float n = 5.0f - videoProgressUpdate.getCurrentTime();
                if (n <= 0.0f) {
                    this.i = com.google.ads.interactivemedia.v3.b.b.e.b.c;
                    this.e.b(new r(r.b.i18n, r.c.skipButton, this.d));
                    final Iterator<a> iterator = this.b.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().b();
                    }
                }
                else if (b) {
                    final HashMap<String, Float> hashMap2 = new HashMap<String, Float>(1);
                    hashMap2.put("seconds", n);
                    this.e.b(new r(r.b.i18n, r.c.preSkipButton, this.d, hashMap2));
                }
                this.g = g;
            }
        }
    }
    
    public void a(final a a) {
        this.b.add(a);
    }
    
    public void a(final r.c c, final String s) {
        switch (e$2.a[c.ordinal()]) {
            default: {}
            case 1: {
                this.a(s);
            }
            case 2: {
                this.b(s);
            }
            case 3:
            case 4: {
                this.j.a(s);
            }
        }
    }
    
    public void a(final boolean f) {
        this.f = f;
        if (this.k.a) {
            ((RelativeLayout$LayoutParams)this.a.getLayoutParams()).setMargins(0, 0, 0, com.google.ads.interactivemedia.v3.b.b.c.a(25, this.c));
        }
    }
    
    public void b() {
        this.setVisibility(4);
    }
    
    public void onClick(final View view) {
        if (view == this.a && this.i == com.google.ads.interactivemedia.v3.b.b.e.b.c) {
            final Iterator<a> iterator = this.b.iterator();
            while (iterator.hasNext()) {
                iterator.next().a();
            }
        }
    }
    
    public interface a extends com.google.ads.interactivemedia.v3.b.b.a.a
    {
        void a();
        
        void b();
    }
    
    private enum b
    {
        a, 
        b, 
        c;
    }
}
