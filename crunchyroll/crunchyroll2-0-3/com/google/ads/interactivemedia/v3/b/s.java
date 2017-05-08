// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.SortedSet;
import java.util.List;
import android.content.Intent;
import android.webkit.WebView;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdError;
import android.util.Log;
import com.google.ads.interactivemedia.v3.b.a.e;
import android.view.View;
import com.google.ads.interactivemedia.v3.b.a.c;
import java.util.Iterator;
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot;
import android.view.ViewGroup;
import java.util.Set;
import android.os.SystemClock;
import java.util.LinkedList;
import java.util.HashMap;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import android.net.Uri;
import java.util.Queue;
import android.content.Context;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import java.util.Map;

public class s implements t.a
{
    private Map<String, b> a;
    private Map<String, a> b;
    private Map<String, c> c;
    private Map<String, VideoAdPlayer> d;
    private Map<String, AdDisplayContainer> e;
    private final Context f;
    private final t g;
    private u h;
    private boolean i;
    private Queue<r> j;
    private long k;
    
    public s(final Context f, final Uri uri, final ImaSdkSettings imaSdkSettings) {
        this.a = new HashMap<String, b>();
        this.b = new HashMap<String, a>();
        this.c = new HashMap<String, c>();
        this.d = new HashMap<String, VideoAdPlayer>();
        this.e = new HashMap<String, AdDisplayContainer>();
        this.i = false;
        this.j = new LinkedList<r>();
        this.k = SystemClock.elapsedRealtime();
        this.f = f;
        (this.g = new t(f, (t.a)this)).a(a(uri, imaSdkSettings).toString());
    }
    
    static Uri a(final Uri uri, final ImaSdkSettings imaSdkSettings) {
        return uri.buildUpon().appendQueryParameter("sdk_version", "a.3.0b8").appendQueryParameter("hl", imaSdkSettings.getLanguage()).build();
    }
    
    private String a(final String s, final String s2) {
        if (s2 == null || s2.length() == 0) {
            return s;
        }
        return s + " Caused by: " + s2;
    }
    
    private Map<String, ViewGroup> a(final com.google.ads.interactivemedia.v3.b.a a, final Set<String> set) {
        final HashMap<String, ViewGroup> hashMap = new HashMap<String, ViewGroup>(set.size());
        for (final String s : set) {
            final CompanionAdSlot companionAdSlot = a.a().get(s);
            if (companionAdSlot.getContainer() == null) {
                return null;
            }
            hashMap.put(s, companionAdSlot.getContainer());
        }
        return hashMap;
    }
    
    private void a(final long n, final String s) {
        final HashMap<String, Long> hashMap = new HashMap<String, Long>();
        hashMap.put("webViewLoadingTime", n);
        this.b(new r(r.b.webViewLoaded, r.c.csi, s, hashMap));
    }
    
    private void a(final ViewGroup viewGroup, final com.google.ads.interactivemedia.v3.b.a.c c, final String s) {
        viewGroup.removeAllViews();
        final View view = null;
        Object o = null;
        switch (s$1.c[c.type.ordinal()]) {
            default: {
                o = view;
                break;
            }
            case 1:
            case 2: {
                o = new l(viewGroup.getContext(), this, c);
                break;
            }
            case 3: {
                o = new q(viewGroup.getContext(), this, c, s);
                break;
            }
        }
        viewGroup.addView((View)o);
    }
    
    private void a(final r.c c, final String s, final e e) {
        while (true) {
            switch (s$1.b[c.ordinal()]) {
                default: {
                    this.a("other", c);
                }
                case 1: {
                    Enum<r.a> enum1;
                    final r.a a = (r.a)(enum1 = r.a.b);
                    while (true) {
                        try {
                            if (e.adUiStyle != null) {
                                enum1 = r.a.valueOf(e.adUiStyle);
                            }
                            this.h = new u(e.adTimeUpdateMs, (r.a)enum1);
                            this.i = true;
                            this.a(SystemClock.elapsedRealtime() - this.k, s);
                            this.b();
                            return;
                            Label_0294: {
                                final String string;
                                Log.e(string, e.m);
                            }
                            return;
                            Label_0244:
                            Log.w("IMASDK", "Unrecognized log level: " + e.ln);
                            final String string;
                            Log.w(string, e.m);
                            return;
                            Label_0304:
                            Log.i(string, e.m);
                            return;
                            Label_0152:
                            string = "SDK_LOG:" + e.n;
                            // switch([Lcom.strobel.decompiler.ast.Label;@4f75468a, e.ln.charAt(0))
                            Label_0324:
                            Log.w(string, e.m);
                            return;
                            // iftrue(Label_0152:, e.ln != null && e.n != null && e.m != null)
                            Label_0124: {
                                break Label_0124;
                                Label_0314:
                                Log.v(string, e.m);
                                return;
                            }
                            Log.e("IMASDK", "Invalid logging message data: " + e);
                            return;
                            Label_0284:
                            Log.d(string, e.m);
                            return;
                        }
                        catch (IllegalArgumentException ex) {
                            enum1 = a;
                            continue;
                        }
                        break;
                    }
                    break;
                }
                case 2: {
                    continue;
                }
            }
            break;
        }
    }
    
    private void a(final String s, final r.c c) {
        Log.i("IMASDK", "Illegal message type " + c + " received for " + s + " channel");
    }
    
    private void b() {
        while (this.i && !this.j.isEmpty()) {
            this.g.a(this.j.remove());
        }
    }
    
    private void b(final r.c c, final String s, final e e) {
        final com.google.ads.interactivemedia.v3.b.a a = this.e.get(s);
        final b b = this.a.get(s);
        if (a == null || b == null) {
            Log.e("IMASDK", "Received displayContainer message: " + c + " for invalid session id: " + s);
        }
        else {
            switch (s$1.b[c.ordinal()]) {
                case 4:
                case 5: {
                    break;
                }
                default: {
                    this.a(r.b.displayContainer.toString(), c);
                }
                case 3: {
                    if (e == null || e.companions == null) {
                        b.a(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INTERNAL_ERROR, "Display companions message requires companions in data.");
                        return;
                    }
                    final Map<String, ViewGroup> a2 = this.a(a, e.companions.keySet());
                    if (a2 == null) {
                        b.a(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INTERNAL_ERROR, "Display requested for invalid companion slot.");
                        return;
                    }
                    for (final String s2 : a2.keySet()) {
                        this.a(a2.get(s2), e.companions.get(s2), s);
                    }
                    break;
                }
            }
        }
    }
    
    private void c(final r.c c, final String s, final e e) {
        final c c2 = this.c.get(s);
        if (c2 != null) {
            c2.a(c, e.translation);
        }
    }
    
    private void d(final r.c c, final String s, final e e) {
        final a a = this.b.get(s);
        if (a == null) {
            Log.e("IMASDK", "Received request message: " + c + " for invalid session id: " + s);
            return;
        }
        switch (s$1.b[c.ordinal()]) {
            default: {
                this.a(r.b.adsLoader.toString(), c);
            }
            case 6: {
                if (e == null) {
                    a.a(s, AdError.AdErrorType.LOAD, AdError.AdErrorCode.INTERNAL_ERROR, "adsLoaded message did not contain cue points.");
                    return;
                }
                a.a(s, this.h, e.adCuePoints, e.internalCuePoints);
            }
            case 7: {
                a.a(s, AdError.AdErrorType.LOAD, e.errorCode, this.a(e.errorMessage, e.innerError));
            }
        }
    }
    
    private void e(final r.c c, final String s, final e e) {
        final VideoAdPlayer videoAdPlayer = this.d.get(s);
        if (videoAdPlayer == null) {
            Log.e("IMASDK", "Received player message: " + c + " for invalid session id: " + s);
        }
        else {
            switch (s$1.b[c.ordinal()]) {
                case 11:
                case 12: {
                    break;
                }
                default: {
                    this.a(r.b.videoDisplay.toString(), c);
                }
                case 8: {
                    if (e != null && e.videoUrl != null) {
                        videoAdPlayer.loadAd(e.videoUrl);
                    }
                    videoAdPlayer.playAd();
                }
                case 9: {
                    videoAdPlayer.pauseAd();
                }
                case 10: {
                    if (e != null && e.videoUrl != null) {
                        videoAdPlayer.loadAd(e.videoUrl);
                        return;
                    }
                    Log.e("IMASDK", "Load message must contain video url");
                    final b b = this.a.get(s);
                    if (b != null) {
                        b.a(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INTERNAL_ERROR, "Loading message did not contain a video url.");
                        return;
                    }
                    break;
                }
            }
        }
    }
    
    private void f(final r.c c, final String s, final e e) {
        final b b = this.a.get(s);
        if (b == null) {
            Log.e("IMASDK", "Received manager message: " + c + " for invalid session id: " + s);
        }
        else {
            com.google.ads.interactivemedia.v3.b.a.a adData;
            if (e != null && e.adData != null) {
                adData = e.adData;
            }
            else {
                adData = null;
            }
            switch (s$1.b[c.ordinal()]) {
                case 13: {
                    break;
                }
                default: {
                    this.a(r.b.adsManager.toString(), c);
                }
                case 14: {
                    if (adData != null) {
                        b.a(AdEvent.AdEventType.LOADED, adData);
                        return;
                    }
                    Log.e("IMASDK", "Ad loaded message requires adData");
                    b.a(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INTERNAL_ERROR, "Ad loaded message did not contain adData.");
                }
                case 15: {
                    b.a(AdEvent.AdEventType.CONTENT_PAUSE_REQUESTED, null);
                }
                case 16: {
                    b.a(AdEvent.AdEventType.CONTENT_RESUME_REQUESTED, null);
                }
                case 17: {
                    b.a(AdEvent.AdEventType.COMPLETED, adData);
                }
                case 18: {
                    b.a(AdEvent.AdEventType.ALL_ADS_COMPLETED, null);
                }
                case 19: {
                    this.d.get(s).stopAd();
                    b.a(AdEvent.AdEventType.SKIPPED, adData);
                }
                case 20: {
                    b.a(AdEvent.AdEventType.STARTED, adData);
                }
                case 9: {
                    b.a(AdEvent.AdEventType.PAUSED, adData);
                }
                case 21: {
                    b.a(AdEvent.AdEventType.RESUMED, adData);
                }
                case 22: {
                    b.a(AdEvent.AdEventType.FIRST_QUARTILE, adData);
                }
                case 23: {
                    b.a(AdEvent.AdEventType.MIDPOINT, adData);
                }
                case 24: {
                    b.a(AdEvent.AdEventType.THIRD_QUARTILE, adData);
                }
                case 25: {
                    b.a(AdEvent.AdEventType.CLICKED, adData);
                }
                case 7: {
                    b.a(AdError.AdErrorType.PLAY, e.errorCode, this.a(e.errorMessage, e.innerError));
                }
                case 2: {
                    b.a(AdEvent.AdEventType.LOG, adData, e.logData.constructMap());
                }
            }
        }
    }
    
    public WebView a() {
        return this.g.a();
    }
    
    public void a(final AdDisplayContainer adDisplayContainer, final String s) {
        this.e.put(s, adDisplayContainer);
    }
    
    public void a(final VideoAdPlayer videoAdPlayer, final String s) {
        this.d.put(s, videoAdPlayer);
    }
    
    @Override
    public void a(final r r) {
        final e e = (e)r.c();
        final String d = r.d();
        final r.c b = r.b();
        switch (s$1.a[r.a().ordinal()]) {
            default: {
                Log.e("IMASDK", "Unknown message channel: " + r.a());
            }
            case 1: {
                this.f(b, d, e);
            }
            case 2: {
                this.e(b, d, e);
            }
            case 3: {
                this.d(b, d, e);
            }
            case 4: {
                this.b(b, d, e);
            }
            case 5: {
                this.c(b, d, e);
            }
            case 6:
            case 7: {
                this.a(b, d, e);
            }
        }
    }
    
    public void a(final a a, final String s) {
        this.b.put(s, a);
    }
    
    public void a(final b b, final String s) {
        this.a.put(s, b);
    }
    
    public void a(final c c, final String s) {
        this.c.put(s, c);
    }
    
    public void a(final String s) {
        this.c.remove(s);
    }
    
    public void b(final r r) {
        this.j.add(r);
        this.b();
    }
    
    public void b(final String s) {
        this.f.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
    }
    
    public interface a
    {
        void a(final String p0, final AdError.AdErrorType p1, final int p2, final String p3);
        
        void a(final String p0, final AdError.AdErrorType p1, final AdError.AdErrorCode p2, final String p3);
        
        void a(final String p0, final u p1, final List<Float> p2, final SortedSet<Float> p3);
    }
    
    public interface b
    {
        void a(final AdError.AdErrorType p0, final int p1, final String p2);
        
        void a(final AdError.AdErrorType p0, final AdError.AdErrorCode p1, final String p2);
        
        void a(final AdEvent.AdEventType p0, final com.google.ads.interactivemedia.v3.b.a.a p1);
        
        void a(final AdEvent.AdEventType p0, final com.google.ads.interactivemedia.v3.b.a.a p1, final Map<String, String> p2);
    }
    
    public interface c
    {
        void a(final r.c p0, final String p1);
    }
}
