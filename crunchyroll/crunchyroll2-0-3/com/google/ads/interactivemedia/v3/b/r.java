// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.HashMap;
import com.google.ads.interactivemedia.v3.a.t;
import com.google.ads.interactivemedia.v3.b.a.e;
import java.net.MalformedURLException;
import android.net.Uri;
import com.google.ads.interactivemedia.v3.a.q;
import com.google.ads.interactivemedia.v3.a.l;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.s;
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot;
import com.google.ads.interactivemedia.v3.a.g;
import com.google.ads.interactivemedia.v3.a.f;

public class r
{
    private static final f a;
    private final b b;
    private final Object c;
    private final String d;
    private final c e;
    
    static {
        a = new g().a(CompanionAdSlot.class, new s<CompanionAdSlot>() {
            @Override
            public l a(final CompanionAdSlot companionAdSlot, final Type type, final com.google.ads.interactivemedia.v3.a.r r) {
                return new q(companionAdSlot.getWidth() + "x" + companionAdSlot.getHeight());
            }
        }).a();
    }
    
    public r(final b b, final c c, final String s) {
        this(b, c, s, null);
    }
    
    public r(final b b, final c e, final String d, final Object c) {
        this.b = b;
        this.e = e;
        this.d = d;
        this.c = c;
    }
    
    public static r a(final String s) throws MalformedURLException, t {
        final Uri parse = Uri.parse(s);
        final String substring = parse.getPath().substring(1);
        if (parse.getQueryParameter("sid") == null) {
            throw new MalformedURLException("Session id must be provided in message.");
        }
        return new r(b.valueOf(substring), c.valueOf(parse.getQueryParameter("type")), parse.getQueryParameter("sid"), r.a.a(parse.getQueryParameter("data"), e.class));
    }
    
    public b a() {
        return this.b;
    }
    
    public c b() {
        return this.e;
    }
    
    public Object c() {
        return this.c;
    }
    
    public String d() {
        return this.d;
    }
    
    public String e() {
        final HashMap<String, c> hashMap = new HashMap<String, c>(3);
        hashMap.put("type", this.e);
        hashMap.put("sid", (c)this.d);
        hashMap.put("data", (c)this.c);
        return String.format("%s('%s', %s);", "javascript:adsense.mobileads.afmanotify.receiveMessage", this.b, r.a.a(hashMap));
    }
    
    @Override
    public String toString() {
        return String.format("JavaScriptMessage [command=%s, type=%s, sid=%s, data=%s]", this.b, this.e, this.d, this.c);
    }
    
    public enum a
    {
        a, 
        b, 
        c;
    }
    
    public enum b
    {
        adsLoader, 
        adsManager, 
        contentTimeUpdate, 
        displayContainer, 
        i18n, 
        log, 
        videoDisplay, 
        webViewLoaded;
    }
    
    public enum c
    {
        adMetadata, 
        adRemainingTime, 
        adsLoaded, 
        allAdsCompleted, 
        click, 
        companionView, 
        complete, 
        contentComplete, 
        contentPauseRequested, 
        contentResumeRequested, 
        contentTimeUpdate, 
        csi, 
        destroy, 
        displayCompanions, 
        end, 
        error, 
        firstquartile, 
        fullscreen, 
        hide, 
        init, 
        initialized, 
        learnMore, 
        load, 
        loaded, 
        log, 
        midpoint, 
        mute, 
        pause, 
        play, 
        preSkipButton, 
        requestAds, 
        resume, 
        showVideo, 
        skip, 
        skipButton, 
        start, 
        startTracking, 
        stop, 
        stopTracking, 
        thirdquartile, 
        timeupdate, 
        unmute;
    }
}
