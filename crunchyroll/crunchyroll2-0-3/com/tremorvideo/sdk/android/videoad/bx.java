// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.app.Activity;

public class bx
{
    private static TremorAdStateListener a;
    private static i b;
    private static boolean c;
    
    static {
        bx.c = false;
    }
    
    public static void a() {
        bx.a = null;
    }
    
    public static void a(final TremorAdStateListener a) {
        bx.a = a;
    }
    
    public static void a(final a a, final Object... array) {
        final Activity activity = (Activity)ac.x();
        if (activity != null) {
            activity.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    switch (bx$2.a[a.ordinal()]) {
                        default: {}
                        case 1: {
                            bx.b();
                        }
                        case 2: {
                            bx.a((boolean)array[0], (int)array[1]);
                        }
                        case 3: {
                            bx.a((boolean)array[0]);
                        }
                        case 4: {
                            bx.c();
                        }
                    }
                }
            });
        }
    }
    
    public static void a(final i b) {
        bx.b = b;
    }
    
    public static void a(final boolean b) {
        if (bx.a != null) {
            ac.e("Invoke callback: onAdReady");
            bx.a.adReady(b);
            return;
        }
        ac.e("Invoke callback: No listener for onAdReady");
    }
    
    public static void a(final boolean b, final int n) {
        if (bx.a != null) {
            ac.e("Invoke callback: onAdComplete");
            bx.a.adComplete(b, n);
            bx.c = true;
            return;
        }
        ac.e("Invoke callback: No listener for onAdComplete");
    }
    
    public static void b() {
        if (bx.a != null) {
            ac.e("Invoke callback: onAdStart");
            bx.a.adStart();
            bx.c = false;
            return;
        }
        ac.e("Invoke callback: No listener for onAdStart");
    }
    
    public static void c() {
        if (bx.a != null) {
            ac.e("Invoke callback: onLeaveApp");
            bx.a.leftApp();
            return;
        }
        ac.e("Invoke callback: No listener for onLeaveApp");
    }
    
    public static int d() {
        int m = -1;
        if (bx.b != null) {
            m = bx.b.m();
        }
        ac.e("returning PlayHead Time: " + m);
        return m;
    }
    
    public static int e() {
        int n = -1;
        if (bx.b != null) {
            n = bx.b.n();
        }
        ac.e("returning Duration Time: " + n);
        return n;
    }
    
    public static boolean f() {
        return bx.c;
    }
    
    public enum a
    {
        a, 
        b, 
        c, 
        d;
    }
}
