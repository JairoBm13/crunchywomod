// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.PointF;
import android.graphics.Point;

public class b
{
    b a;
    b b;
    q c;
    int d;
    a[] e;
    
    public b(final q c) {
        this.c = c;
    }
    
    public static Point a(final int n, final int n2, final b b) {
        switch (b$1.a[b.ordinal()]) {
            default: {
                return new Point(0, 0);
            }
            case 1: {
                return new Point(0, 0);
            }
            case 2: {
                return new Point(n / 2, 0);
            }
            case 3: {
                return new Point(n, 0);
            }
            case 4: {
                return new Point(0, n2 / 2);
            }
            case 5: {
                return new Point(n / 2, n2 / 2);
            }
            case 6: {
                return new Point(n, n2 / 2);
            }
            case 7: {
                return new Point(0, n2);
            }
            case 8: {
                return new Point(n / 2, n2);
            }
            case 9: {
                return new Point(n, n2);
            }
        }
    }
    
    public static PointF a(final float n, final float n2, final b b) {
        switch (b$1.a[b.ordinal()]) {
            default: {
                return new PointF(0.0f, 0.0f);
            }
            case 1: {
                return new PointF(0.0f, 0.0f);
            }
            case 2: {
                return new PointF(n / 2.0f, 0.0f);
            }
            case 3: {
                return new PointF(n, 0.0f);
            }
            case 4: {
                return new PointF(0.0f, n2 / 2.0f);
            }
            case 5: {
                return new PointF(n / 2.0f, n2 / 2.0f);
            }
            case 6: {
                return new PointF(n, n2 / 2.0f);
            }
            case 7: {
                return new PointF(0.0f, n2);
            }
            case 8: {
                return new PointF(n / 2.0f, n2);
            }
            case 9: {
                return new PointF(n, n2);
            }
        }
    }
    
    public static PointF a(final k k, final b b) {
        switch (b$1.a[b.ordinal()]) {
            default: {
                return new PointF(0.0f, 0.0f);
            }
            case 1: {
                return new PointF(k.a, k.b);
            }
            case 2: {
                return new PointF(k.a + k.f / 2.0f, k.b);
            }
            case 3: {
                return new PointF(k.a + k.f, k.b);
            }
            case 4: {
                return new PointF(k.a, k.b + k.g / 2.0f);
            }
            case 5: {
                return new PointF(k.a + k.f / 2.0f, k.b + k.g / 2.0f);
            }
            case 6: {
                return new PointF(k.a + k.f, k.b + k.g / 2.0f);
            }
            case 7: {
                return new PointF(k.a, k.b + k.g);
            }
            case 8: {
                return new PointF(k.a + k.f / 2.0f, k.b + k.g);
            }
            case 9: {
                return new PointF(k.a + k.f, k.b + k.g);
            }
        }
    }
    
    public a a(final int n) {
        return this.e[n];
    }
    
    public q a() {
        if (this.d == 255) {
            return null;
        }
        return this.c.j().a(this.d);
    }
    
    public void a(final e e) {
        try {
            this.d = e.b();
            this.a = com.tremorvideo.sdk.android.richmedia.b.b.values()[e.b()];
            this.b = com.tremorvideo.sdk.android.richmedia.b.b.values()[e.b()];
            this.e = new a[4];
            if (this.c.j().g().g() > 1) {
                for (int i = 0; i < 4; ++i) {
                    if (e.b() == 1) {
                        (this.e[i] = new a()).a(e);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public b b() {
        return this.b;
    }
    
    public b c() {
        return this.a;
    }
    
    public class a
    {
        int a;
        b b;
        
        public b a() {
            return this.b;
        }
        
        public q a(final o o) {
            return o.a(this.a);
        }
        
        public void a(final e e) throws Exception {
            this.a = e.b();
            this.b = com.tremorvideo.sdk.android.richmedia.b.b.values()[e.b()];
        }
    }
    
    public enum b
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i;
    }
}
