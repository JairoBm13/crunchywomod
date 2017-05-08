// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.b.a.d;
import com.google.android.a.h;
import android.os.AsyncTask;
import java.util.Random;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import java.util.SortedSet;
import java.util.Iterator;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.util.Log;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdError;
import android.os.Build$VERSION;
import com.google.android.a.c;
import com.google.android.a.f;
import java.util.HashMap;
import java.util.ArrayList;
import android.net.Uri;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.android.a.g;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import java.util.Map;
import java.util.List;
import android.content.Context;
import com.google.ads.interactivemedia.v3.api.AdsLoader;

public class e implements AdsLoader
{
    private final s a;
    private final Context b;
    private final o c;
    private final List<AdsLoadedListener> d;
    private final Map<String, AdsRequest> e;
    private g f;
    private ImaSdkSettings g;
    
    public e(final Context context, final Uri uri, final ImaSdkSettings g) {
        this(new s(context, uri, g), context);
        this.g = g;
    }
    
    public e(final s a, final Context b) {
        this.c = new o();
        this.d = new ArrayList<AdsLoadedListener>(1);
        this.e = new HashMap<String, AdsRequest>();
        this.g = new ImaSdkSettings();
        this.a = a;
        this.b = b;
        this.f = new g(com.google.android.a.f.a("a.3.0b8", b));
    }
    
    private String a() {
        return String.format("android%s:%s:%s", Build$VERSION.RELEASE, "3.0b8", this.b.getPackageName());
    }
    
    private boolean a(final AdsRequest adsRequest) {
        if (adsRequest == null) {
            this.c.a(new b(new AdError(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INVALID_ARGUMENTS, "AdsRequest cannot be null.")));
            return false;
        }
        final AdDisplayContainer adDisplayContainer = adsRequest.getAdDisplayContainer();
        if (adDisplayContainer == null) {
            this.c.a(new b(new AdError(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INVALID_ARGUMENTS, "Ad display container must be provided in the AdsRequest.")));
            return false;
        }
        if (adDisplayContainer.getAdContainer() == null) {
            this.c.a(new b(new AdError(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INVALID_ARGUMENTS, "Ad display container must have a UI container.")));
            return false;
        }
        if (adsRequest.getAdTagUrl() == null || adsRequest.getAdTagUrl().length() == 0) {
            this.c.a(new b(new AdError(AdError.AdErrorType.LOAD, AdError.AdErrorCode.INVALID_ARGUMENTS, "Ad tag url must non-null and non empty.")));
            return false;
        }
        return true;
    }
    
    private String b() {
        if (this.b.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            Log.w("IMASDK", "Host application doesn't have ACCESS_NETWORK_STATE permission");
            return "android:0";
        }
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.b.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return "android:0";
        }
        return String.format("android:%d:%d", activeNetworkInfo.getType(), activeNetworkInfo.getSubtype());
    }
    
    void a(final AdsManagerLoadedEvent adsManagerLoadedEvent) {
        final Iterator<AdsLoadedListener> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAdsManagerLoaded(adsManagerLoadedEvent);
        }
    }
    
    void a(final AdsRequest adsRequest, final String s) {
        if (!this.a(adsRequest)) {
            return;
        }
        this.e.put(s, adsRequest);
        this.a.a((s.a)new s.a() {
            @Override
            public void a(final String s, final AdError.AdErrorType adErrorType, final int n, final String s2) {
                e.this.c.a(new com.google.ads.interactivemedia.v3.b.b(new AdError(adErrorType, n, s2), e.this.e.get(s).getUserRequestContext()));
            }
            
            @Override
            public void a(final String s, final AdError.AdErrorType adErrorType, final AdError.AdErrorCode adErrorCode, final String s2) {
                e.this.c.a(new com.google.ads.interactivemedia.v3.b.b(new AdError(adErrorType, adErrorCode, s2), e.this.e.get(s).getUserRequestContext()));
            }
            
            @Override
            public void a(final String s, final u u, final List<Float> list, final SortedSet<Float> set) {
                final AdsRequest adsRequest = e.this.e.get(s);
                e.this.a(new com.google.ads.interactivemedia.v3.b.g(new com.google.ads.interactivemedia.v3.b.f(s, e.this.a, u, adsRequest.getAdDisplayContainer(), list, set, new v(adsRequest.getAdDisplayContainer().getPlayer(), u.a()), e.this.b), adsRequest.getUserRequestContext()));
            }
        }, s);
        this.a.a(adsRequest.getAdDisplayContainer(), s);
        new a(adsRequest, s).execute((Object[])new String[] { adsRequest.getAdTagUrl() });
    }
    
    @Override
    public void addAdErrorListener(final AdErrorEvent.AdErrorListener adErrorListener) {
        this.c.a(adErrorListener);
    }
    
    @Override
    public void addAdsLoadedListener(final AdsLoadedListener adsLoadedListener) {
        this.d.add(adsLoadedListener);
    }
    
    @Override
    public void contentComplete() {
        this.a.b(new r(r.b.adsLoader, r.c.contentComplete, "*"));
    }
    
    @Override
    public void requestAds(final AdsRequest adsRequest) {
        this.a(adsRequest, "ima_sid_" + Integer.valueOf(new Random().nextInt(1000000000)).toString());
    }
    
    private class a extends AsyncTask<String, Void, String>
    {
        private AdsRequest b;
        private String c;
        
        public a(final AdsRequest b, final String c) {
            this.b = b;
            this.c = c;
        }
        
        protected String a(final String... array) {
            String string;
            final String s = string = array[0];
            try {
                if (com.google.ads.interactivemedia.v3.b.e.this.f != null && (string = s) != null) {
                    final Uri parse = Uri.parse(s);
                    string = s;
                    if (com.google.ads.interactivemedia.v3.b.e.this.f.b(parse)) {
                        string = com.google.ads.interactivemedia.v3.b.e.this.f.a(parse, com.google.ads.interactivemedia.v3.b.e.this.b).toString();
                    }
                }
                return string;
            }
            catch (h h) {
                return s;
            }
        }
        
        protected void a(final String adTagUrl) {
            this.b.setAdTagUrl(adTagUrl);
            com.google.ads.interactivemedia.v3.b.e.this.a.b(new r(r.b.adsLoader, r.c.requestAds, this.c, new d(this.b, com.google.ads.interactivemedia.v3.b.e.this.a(), com.google.ads.interactivemedia.v3.b.e.this.b(), com.google.ads.interactivemedia.v3.b.e.this.g)));
        }
    }
}
