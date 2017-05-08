// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.AdError;
import java.util.HashMap;
import com.google.ads.interactivemedia.v3.api.AdsRenderingSettings;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import java.util.Iterator;
import com.google.ads.interactivemedia.v3.api.Ad;
import java.util.Map;
import java.util.ArrayList;
import android.content.Context;
import java.util.SortedSet;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import java.util.List;
import com.google.ads.interactivemedia.v3.b.a.a;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.AdsManager;

public class f implements AdsManager
{
    v a;
    private final s b;
    private final String c;
    private final VideoAdPlayer d;
    private com.google.ads.interactivemedia.v3.b.a.a e;
    private n f;
    private d g;
    private List<Float> h;
    private h i;
    private VideoAdPlayer.VideoAdPlayerCallback j;
    private final List<AdEvent.AdEventListener> k;
    private final o l;
    
    f(final String c, final s b, final u u, final AdDisplayContainer adDisplayContainer, final List<Float> h, final SortedSet<Float> set, final v a, final Context context) {
        this.k = new ArrayList<AdEvent.AdEventListener>(1);
        this.l = new o();
        this.i = new h(c, u, b, adDisplayContainer, context);
        this.c = c;
        this.b = b;
        this.d = adDisplayContainer.getPlayer();
        this.h = h;
        (this.a = a).a((v.a)this.i);
        if (set != null && !set.isEmpty()) {
            a.c((v.a)(this.f = new n(b, set, c)));
            this.j = new m(a);
        }
    }
    
    private void a(final r.c c) {
        this.b.b(new r(r.b.adsManager, c, this.c));
    }
    
    void a(final AdEvent.AdEventType adEventType) {
        this.a(adEventType, null);
    }
    
    void a(final AdEvent.AdEventType adEventType, final Map<String, String> map) {
        final c c = new c(adEventType, this.e, map);
        final Iterator<AdEvent.AdEventListener> iterator = this.k.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAdEvent(c);
        }
    }
    
    @Override
    public void addAdErrorListener(final AdErrorEvent.AdErrorListener adErrorListener) {
        this.l.a(adErrorListener);
    }
    
    @Override
    public void addAdEventListener(final AdEvent.AdEventListener adEventListener) {
        this.k.add(adEventListener);
    }
    
    @Override
    public void destroy() {
        this.a.c();
        this.a.b((v.a)this.i);
        this.i.a((Ad)this.e);
        this.d.removeCallback((VideoAdPlayer.VideoAdPlayerCallback)this.g);
        if (this.j != null) {
            this.d.removeCallback(this.j);
        }
        this.a(r.c.destroy);
    }
    
    @Override
    public void init() {
        this.init(null);
    }
    
    public void init(final AdsRenderingSettings adsRenderingSettings) {
        this.b.a((s.b)new a(), this.c);
        final HashMap<String, AdsRenderingSettings> hashMap = new HashMap<String, AdsRenderingSettings>();
        hashMap.put("adsRenderingSettings", adsRenderingSettings);
        this.b.b(new r(r.b.adsManager, r.c.init, this.c, hashMap));
    }
    
    @Override
    public void start() {
        if (this.d == null) {
            this.l.a(new b(new AdError(AdError.AdErrorType.PLAY, AdError.AdErrorCode.INVALID_ARGUMENTS, "Ad Display Container must contain a non-null video player.")));
            return;
        }
        this.b.a(this.d, this.c);
        this.a(r.c.start);
    }
    
    private class a implements b
    {
        @Override
        public void a(final AdError.AdErrorType adErrorType, final int n, final String s) {
            f.this.l.a(new com.google.ads.interactivemedia.v3.b.b(new AdError(adErrorType, n, s)));
            f.this.i.a((Ad)f.this.e);
        }
        
        @Override
        public void a(final AdError.AdErrorType adErrorType, final AdError.AdErrorCode adErrorCode, final String s) {
            f.this.l.a(new com.google.ads.interactivemedia.v3.b.b(new AdError(adErrorType, adErrorCode, s)));
            f.this.i.a((Ad)f.this.e);
        }
        
        @Override
        public void a(final AdEvent.AdEventType adEventType, final com.google.ads.interactivemedia.v3.b.a.a a) {
            if (a != null) {
                f.this.e = a;
            }
            switch (f$1.a[adEventType.ordinal()]) {
                case 1: {
                    f.this.g = new d(f.this.b, f.this.c, f.this.a);
                    f.this.d.addCallback((VideoAdPlayer.VideoAdPlayerCallback)f.this.g);
                    f.this.a.a((v.a)f.this.g);
                    if (f.this.j != null) {
                        f.this.d.removeCallback(f.this.j);
                        break;
                    }
                    break;
                }
                case 2: {
                    f.this.d.removeCallback((VideoAdPlayer.VideoAdPlayerCallback)f.this.g);
                    f.this.a.b((v.a)f.this.g);
                    if (f.this.j != null) {
                        f.this.d.addCallback(f.this.j);
                        break;
                    }
                    break;
                }
                case 3: {
                    f.this.i.a(f.this.e);
                    break;
                }
                case 4:
                case 5: {
                    f.this.i.a((Ad)f.this.e);
                    break;
                }
                case 6: {
                    f.this.destroy();
                    break;
                }
            }
            f.this.a(adEventType);
            if (adEventType == AdEvent.AdEventType.COMPLETED) {
                f.this.e = null;
            }
        }
        
        @Override
        public void a(final AdEvent.AdEventType adEventType, final com.google.ads.interactivemedia.v3.b.a.a a, final Map<String, String> map) {
            if (a != null) {
                f.this.e = a;
            }
            f.this.a(adEventType, map);
        }
    }
}
