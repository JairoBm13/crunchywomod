// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.webkit.URLUtil;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.content.Intent;
import java.util.Map;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.os.Build$VERSION;
import android.app.Activity;
import android.os.Looper;
import android.os.Handler;
import android.content.Context;

public class TremorVideo
{
    public static int _Initialized;
    private static Context a;
    private static long b;
    private static AdViewManager c;
    private static final Handler d;
    
    static {
        TremorVideo._Initialized = -1;
        TremorVideo.a = null;
        TremorVideo.b = 0L;
        TremorVideo.c = null;
        d = new Handler(Looper.getMainLooper());
    }
    
    public static void clearAdStateListener() {
        bx.a();
    }
    
    public static void destroy() {
        if (TremorVideo._Initialized != 1 || ac.D()) {
            return;
        }
        try {
            ac.F();
            ac.c();
            TremorVideo._Initialized = -1;
            TremorVideo.a = null;
            TremorVideo.b = 0L;
        }
        catch (Exception ex) {}
    }
    
    public static boolean fireConversion(final Activity a, final String s, final String s2) throws Exception {
        TremorVideo.a = (Context)a;
        ac.a = TremorVideo.a;
        if (!TremorVideo.a.getSharedPreferences("Conversion", 0).getBoolean("convFired", false)) {
            final StringBuilder sb = new StringBuilder("http://dt.videohub.tv/ssframework/dt/cpa.png");
            sb.append("?trackcd=");
            sb.append(s2);
            sb.append("&advid=");
            sb.append(s);
            sb.append("&bundleid=");
            sb.append(ac.j());
            sb.append("&appversion=");
            sb.append(ac.k());
            new Thread(new Runnable() {
                final /* synthetic */ String a = sb.toString();
                
                @Override
                public void run() {
                    if (Build$VERSION.SDK_INT <= 8) {
                        return;
                    }
                    final boolean b = false;
                    final boolean b2 = false;
                    final boolean b3 = false;
                    final boolean b4 = false;
                    final boolean b5 = false;
                    final boolean b6 = false;
                    int n = b2 ? 1 : 0;
                    try {
                        Class.forName("com.google.android.gms.common.GooglePlayServicesUtil");
                        n = (b2 ? 1 : 0);
                        int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ac.x());
                        n = (b6 ? 1 : 0);
                        Label_0144: {
                            if (googlePlayServicesAvailable != 0) {
                                break Label_0144;
                            }
                            googlePlayServicesAvailable = (b ? 1 : 0);
                            n = (b2 ? 1 : 0);
                            int n2 = b3 ? 1 : 0;
                            int n3 = b4 ? 1 : 0;
                            int n4 = b5 ? 1 : 0;
                            try {
                                final AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(ac.x());
                                n = (b6 ? 1 : 0);
                                if (advertisingIdInfo != null) {
                                    googlePlayServicesAvailable = (b ? 1 : 0);
                                    n = (b2 ? 1 : 0);
                                    n2 = (b3 ? 1 : 0);
                                    n3 = (b4 ? 1 : 0);
                                    n4 = (b5 ? 1 : 0);
                                    final String id = advertisingIdInfo.getId();
                                    googlePlayServicesAvailable = 1;
                                    n = 1;
                                    n2 = 1;
                                    n3 = 1;
                                    n4 = 1;
                                    final boolean b7 = true;
                                    TremorVideo.d.post((Runnable)new Runnable() {
                                        @Override
                                        public void run() {
                                            z.a(Runnable.this.a, id);
                                        }
                                    });
                                    n = (b7 ? 1 : 0);
                                }
                                if (n == 0) {
                                    TremorVideo.d.post((Runnable)new Runnable() {
                                        @Override
                                        public void run() {
                                            z.a(Runnable.this.a, ac.q());
                                        }
                                    });
                                }
                            }
                            catch (GooglePlayServicesNotAvailableException ex2) {
                                n = googlePlayServicesAvailable;
                                ac.e("TremorLog_info::ConversionsTracking::GooglePlayServicesNotAvailableException");
                                n = googlePlayServicesAvailable;
                            }
                            catch (IllegalStateException ex3) {
                                ac.e("TremorLog_info::ConversionsTracking::IllegalStateException");
                                n = n2;
                            }
                            catch (GooglePlayServicesRepairableException ex4) {
                                ac.e("TremorLog_info::ConversionsTracking::GooglePlayServicesRepairableException");
                                n = n3;
                            }
                            catch (Exception ex) {
                                ac.e("TremorLog_info::ConversionsTracking::Exception " + ex);
                                n = n4;
                            }
                        }
                    }
                    catch (ClassNotFoundException ex5) {}
                }
            }).start();
            return true;
        }
        ac.e("TremorLog_info::ConversionsTracking::Conversion tracking has already been fired!");
        return false;
    }
    
    public static double getAdDuration() {
        if (isAdReady()) {
            return bx.e();
        }
        return -1.0;
    }
    
    public static double getPlayheadTime() {
        if (isAdReady()) {
            return bx.d();
        }
        return -1.0;
    }
    
    public static String getSDKVersion() {
        return ac.A();
    }
    
    public static void handleAnalyticsEvent(final Activity activity, final String s) {
        if (TremorVideo._Initialized == 1) {
            ac.a(activity, s);
            return;
        }
        ac.e("Unable to send event until Tremor has been initialized");
    }
    
    public static void handleAnalyticsEvent(final Activity activity, final String s, final Map<String, String> map) {
        if (TremorVideo._Initialized == 1) {
            ac.a(activity, s, map);
            return;
        }
        ac.e("Unable to send event until Tremor has been initialized");
    }
    
    public static void handleAnalyticsStateChange(final Activity activity, final String s) {
        if (TremorVideo._Initialized == 1) {
            ac.b(activity, s);
            return;
        }
        ac.e("Unable to send event until Tremor has been initialized");
    }
    
    public static void initialize(final Context a, final String s) {
        if (TremorVideo._Initialized == -1) {
            TremorVideo._Initialized = 0;
            ac.a(TremorVideo.a = a, new String[] { s });
            TremorVideo.b = 0L;
        }
    }
    
    public static void initialize(final Context a, final String[] array) {
        if (TremorVideo._Initialized == -1) {
            TremorVideo._Initialized = 0;
            ac.a(TremorVideo.a = a, array);
            TremorVideo.b = 0L;
        }
    }
    
    public static boolean isAdReady() {
        boolean b = true;
        if (TremorVideo._Initialized != 1) {
            b = false;
        }
        else if (ac.d != 1) {
            return isAdReady(ac.p());
        }
        return b;
    }
    
    public static boolean isAdReady(final String s) {
        boolean b = true;
        if (TremorVideo._Initialized != 1) {
            b = false;
        }
        else if (ac.d != 1) {
            return ac.C().a(s);
        }
        return b;
    }
    
    public static boolean isManualAdReady() {
        boolean b = true;
        if (TremorVideo._Initialized != 1) {
            b = false;
        }
        else if (ac.d != 1) {
            return isManualAdReady(ac.p());
        }
        return b;
    }
    
    public static boolean isManualAdReady(final String s) {
        boolean b = true;
        if (TremorVideo._Initialized != 1) {
            b = false;
        }
        else if (ac.d != 1) {
            return ac.C().c(s);
        }
        return b;
    }
    
    public static void loadAd() throws Exception {
        if (TremorVideo._Initialized == 1) {
            final int n = (int)(Math.random() * 100.0);
            if (n < 100 && n >= ac.i) {
                ac.e("Throttle: " + n + " / " + ac.i + "  Skipping ad request");
                bx.a(bx.a.c, false);
            }
            else {
                ac.e("Throttle: " + n + " / " + ac.i + "  Requesting Ad");
                if (ac.k) {
                    if (!ac.C().b()) {
                        throw new Exception("Ad load  already in progress");
                    }
                }
                else if (!ac.k) {
                    ac.m = true;
                }
            }
            return;
        }
        if (TremorVideo._Initialized == 0) {
            ac.m = true;
            return;
        }
        throw new Exception("Tremor SDK has not been initialized. Please call initialize firstt. ");
    }
    
    public static void setAdStateListener(final TremorAdStateListener tremorAdStateListener) {
        bx.a(tremorAdStateListener);
    }
    
    public static void setFlight(final String s) {
        ac.a(s);
    }
    
    public static boolean showAd(final Activity activity, final int n) throws Exception {
        ac.e("TremorDebug: TremorVideo.ShowAd - Starting");
        if (TremorVideo._Initialized != 1) {
            ac.e(" TremorDebug: TremorVideo.ShowAd - not initialized return false");
            return false;
        }
        if (showAd(activity, ac.p(), n)) {
            ac.e("TremorDebug: TremorVideo.ShowAd - return true");
            return true;
        }
        ac.e("TremorDebug: TremorVideo.ShowAd - return false");
        return false;
    }
    
    public static boolean showAd(final Activity activity, final String s, final int n) throws Exception {
        ac.e("TremorDebug: showAd - start");
        if (TremorVideo._Initialized != 1) {
            return false;
        }
        final au c = ac.C();
        Label_0216: {
            if (ac.d != 1) {
                break Label_0216;
            }
            Label_0158: {
                if (!c.a(s)) {
                    break Label_0158;
                }
                if (!c.e(s)) {
                    ac.e("TremorDebug: startAd - startAdView returned false. Throw an exception");
                    throw new Exception("An ad has already been started.");
                }
                ac.e("TremorDebug: startAd - returned true. Start the play video activity");
                ac.e("Starting ad for: " + s);
                final Intent intent = new Intent(TremorVideo.a, (Class)Playvideo.class);
                intent.putExtra("tremorVideoType", "ad");
                try {
                    ac.e("TremorDebug: startAd - starting activity");
                    activity.startActivityForResult(intent, n);
                    bx.a(bx.a.a, new Object[0]);
                    return true;
                }
                catch (Exception ex) {
                    ac.e("TremorDebug: startAd - Exception starting activity");
                    throw new Exception("Unable to create the ad intent. Make sure you add the 'com.tremorvideo.sdk.android.videoad.Playvideo' activity to your AndroiManifest.xml.");
                }
            }
            final Intent intent2 = new Intent(TremorVideo.a, (Class)Playvideo.class);
            intent2.putExtra("tremorVideoType", "adProgress");
            try {
                ac.e("TremorDebug: showAd - starting activity");
                activity.startActivityForResult(intent2, n);
                return true;
            }
            catch (Exception ex2) {
                ac.e("TremorDebug: showAd - Exception starting activity");
                throw new Exception("Unable to create the ad intent. Make sure you add the 'com.tremorvideo.sdk.android.videoad.Playvideo' activity to your AndroiManifest.xml.");
            }
        }
        if (!c.a(s)) {
            ac.e("TremorDebug: startAd - start");
            c.f(s);
            final long g = ac.G();
            if (g - TremorVideo.b >= 2000L) {
                ac.e("Sending Avail...");
                TremorVideo.b = g;
                z.a(activity, c.i());
            }
            bx.a(bx.a.b, false, 0);
            return false;
        }
        ac.e("TremorDebug: startAd - calling startAdView");
        if (!c.e(s)) {
            ac.e("TremorDebug: startAd - startAdView returned false. Throw an exception");
            throw new Exception("An ad has already been started.");
        }
        ac.e("TremorDebug: startAd - returned true. Start the play video activity");
        ac.e("Starting ad for: " + s);
        final Intent intent3 = new Intent(TremorVideo.a, (Class)Playvideo.class);
        intent3.putExtra("tremorVideoType", "ad");
        try {
            ac.e("TremorDebug: startAd - starting activity");
            activity.startActivityForResult(intent3, n);
            bx.a(bx.a.a, new Object[0]);
            return true;
        }
        catch (Exception ex3) {
            ac.e("TremorDebug: startAd - Exception starting activity");
            throw new Exception("Unable to create the ad intent. Make sure you add the 'com.tremorvideo.sdk.android.videoad.Playvideo' activity to your AndroiManifest.xml.");
        }
    }
    
    public static AdViewManager showAdManual(final Activity activity, final ViewGroup viewGroup) throws Exception {
        if (TremorVideo._Initialized != 1) {
            return null;
        }
        return showAdManual(activity, ac.p(), viewGroup);
    }
    
    public static AdViewManager showAdManual(final Activity activity, final String s, final ViewGroup viewGroup) throws Exception {
        if (TremorVideo._Initialized != 1) {
            return null;
        }
        final au c = ac.C();
        TremorVideo.c = null;
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)activity.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        ac.e("View width=" + viewGroup.getWidth() + " hight=" + viewGroup.getHeight());
        ac.e("Device Density" + displayMetrics.density);
        final int n = (int)displayMetrics.density * 200;
        if (viewGroup.getWidth() >= n && viewGroup.getHeight() >= n) {
            if (ac.d == 1) {
                if (c.c(s)) {
                    if (!c.e(s)) {
                        throw new Exception("An ad has already been started.");
                    }
                    bx.a(bx.a.a, new Object[0]);
                    TremorVideo.c = new AdViewManager(activity, viewGroup, c.h());
                }
                else {
                    TremorVideo.c = new AdViewManager(activity, viewGroup, null);
                }
            }
            else {
                if (!c.c(s)) {
                    c.f(s);
                    final long g = ac.G();
                    if (g - TremorVideo.b >= 2000L) {
                        ac.e("Sending Avail...");
                        TremorVideo.b = g;
                        z.a(activity, c.i());
                    }
                    bx.a(bx.a.b, false, 0);
                    return null;
                }
                if (!c.e(s)) {
                    throw new Exception("An ad has already been started.");
                }
                bx.a(bx.a.a, new Object[0]);
                TremorVideo.c = new AdViewManager(activity, viewGroup, c.h());
            }
        }
        else {
            ac.e("TremorLog_error::FrameVideo::Display minimum dimensions hasn't met");
        }
        return TremorVideo.c;
    }
    
    public static boolean showVASTAd(final Activity activity, final String s, final int n) throws Exception {
        return showVASTAd(activity, s, n, 0, true);
    }
    
    public static boolean showVASTAd(final Activity activity, final String s, final int n, final int n2) throws Exception {
        return showVASTAd(activity, s, n, n2, true);
    }
    
    public static boolean showVASTAd(final Activity a, final String s, final int n, final int n2, final boolean b) throws Exception {
        TremorVideo.a = (Context)a;
        ac.a = TremorVideo.a;
        ac.q = null;
        if (s != null && s.length() > 0 && URLUtil.isValidUrl(s)) {
            final Intent intent = new Intent(TremorVideo.a, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "ad");
            intent.putExtra("vastURL", s);
            intent.putExtra("skipDelaySeconds", n2);
            intent.putExtra("bWaterMark", b);
            try {
                ac.e("TremorDebug: showVASTAd - starting activity");
                a.startActivityForResult(intent, n);
                return true;
            }
            catch (Exception ex) {
                ac.e("TremorDebug: showVASTAd - Exception starting activity");
                throw new Exception("Unable to create the ad intent. Make sure you add the 'com.tremorvideo.sdk.android.videoad.Playvideo' activity to your AndroiManifest.xml.");
            }
        }
        ac.e("TremorDebug: showVASTAd - Not a valid VAST URL");
        return false;
    }
    
    public static boolean showVASTAd(final Activity activity, final String s, final int n, final boolean b) throws Exception {
        return showVASTAd(activity, s, n, 0, b);
    }
    
    public static void stop() {
        if (TremorVideo._Initialized != 1) {
            return;
        }
        try {
            ac.F();
            if (ac.k) {
                ac.C().e();
            }
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public static void stopAd() {
        if (TremorVideo._Initialized == 1 && TremorVideo.c != null) {
            TremorVideo.c.stopAd();
        }
    }
    
    public static void turnValidatorON() {
        ac.a();
    }
    
    public static void updateSettings(final Settings n) {
        if (TremorVideo._Initialized == 1) {
            ac.a(n);
            return;
        }
        ac.n = n;
    }
    
    public enum a
    {
        a("Default"), 
        b("App Start"), 
        c("Pre Roll");
        
        private String d;
        
        private a(final String d) {
            this.d = d;
        }
        
        @Override
        public String toString() {
            return this.d;
        }
    }
}
