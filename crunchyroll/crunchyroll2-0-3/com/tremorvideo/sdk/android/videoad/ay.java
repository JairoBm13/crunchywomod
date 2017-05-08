// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Iterator;
import org.apache.http.NameValuePair;
import java.util.ArrayList;
import java.util.List;

public class ay
{
    private static int c;
    List<a> a;
    long b;
    
    static {
        ay.c = 0;
    }
    
    public ay() {
        this.a = new ArrayList<a>();
    }
    
    public int a(final aw c, final int n, final b g, final List<NameValuePair> h) {
        if (c != null && c.a() != aw.b.a) {
            final a a = new a(this);
            a.c = c;
            final int c2 = ay.c;
            ay.c = c2 + 1;
            a.b = c2;
            a.d = ac.G();
            int f;
            if ((f = n) < 0) {
                f = 0;
            }
            a.f = f;
            a.g = g;
            a.h = h;
            this.a.add(a);
            return a.b;
        }
        return -1;
    }
    
    public void a() {
        this.b = ac.G();
    }
    
    public boolean a(final int n) {
        for (final a a : this.a) {
            if (a.b == n) {
                a.e = ac.G() - a.d;
                return true;
            }
        }
        return false;
    }
    
    public a b(final int n) {
        for (final a a : this.a) {
            if (a.b == n) {
                return a;
            }
        }
        return null;
    }
    
    public class a
    {
        public ay a;
        public int b;
        public aw c;
        public long d;
        public long e;
        public int f;
        public b g;
        public List<NameValuePair> h;
        
        public a(final ay a) {
            this.g = null;
            this.a = a;
            this.b = 0;
            this.c = null;
            this.d = 0L;
            this.e = 0L;
            this.g = null;
            this.h = null;
        }
    }
    
    public static class b
    {
        public float a;
        public float b;
        public float c;
        public float d;
        
        public b(final float a, final float b, final float c, final float d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }
}
