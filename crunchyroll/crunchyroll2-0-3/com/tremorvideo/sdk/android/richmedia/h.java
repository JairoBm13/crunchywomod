// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class h
{
    public List<a> a;
    private List<a> b;
    
    public static boolean a(final b b) {
        boolean b2 = true;
        switch (h$1.a[b.ordinal()]) {
            default: {
                b2 = false;
                return b2;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return b2;
            }
        }
    }
    
    public a a(final c c) {
        for (final a a : this.b) {
            if (a.b() == c) {
                return a;
            }
        }
        return null;
    }
    
    public void a(final e e, final int n) throws Exception {
        final int b = e.b();
        this.b = new ArrayList<a>(b);
        this.a = new ArrayList<a>();
        for (int i = 0; i < b; ++i) {
            final int a = e.a();
            final c c = h.c.values()[e.b()];
            final b b2 = h.b.values()[e.b()];
            String c2 = null;
            if (n > 1) {
                c2 = e.c();
            }
            final Object o = null;
            Object o2;
            if (b2 == h.b.b || b2 == h.b.U || b2 == h.b.V) {
                o2 = new Integer(e.b());
            }
            else if (b2 == h.b.W) {
                final JSONObject jsonObject = new JSONObject(e.c());
                o2 = new int[] { jsonObject.getInt("video-index"), jsonObject.getInt("playback-mode") };
            }
            else if (a(b2)) {
                o2 = new Integer(e.b());
            }
            else if (b2 == h.b.c) {
                o2 = new int[] { e.b(), e.a() };
            }
            else if (b2 == h.b.i && n > 2) {
                o2 = new Integer(e.b());
            }
            else if (b2 == h.b.Q && n >= 5) {
                o2 = e.c();
            }
            else if ((b2 == h.b.R || b2 == h.b.S) && n >= 5) {
                o2 = new Integer(e.a());
            }
            else if (b2 == h.b.T && n >= 6) {
                o2 = e.c();
            }
            else if (b2 == h.b.X && n >= 6) {
                final JSONObject jsonObject2 = new JSONObject(e.c());
                o2 = new boolean[] { jsonObject2.getBoolean("playback-forward"), jsonObject2.getBoolean("playback-looping") };
            }
            else {
                o2 = o;
                if (b2 == h.b.Y) {
                    o2 = o;
                    if (n >= 6) {
                        final JSONObject jsonObject3 = new JSONObject(e.c());
                        o2 = new int[] { jsonObject3.getInt("timestamp"), 0, 0 };
                        int n2;
                        if (jsonObject3.getBoolean("playback-forward")) {
                            n2 = 1;
                        }
                        else {
                            n2 = -1;
                        }
                        o2[1] = n2;
                        int n3;
                        if (jsonObject3.getBoolean("playback-looping")) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                        o2[2] = n3;
                    }
                }
            }
            final a a2 = new a(c, b2, o2, a, c2);
            this.b.add(a2);
            if (a2.b() == h.c.p) {
                this.a.add(a2);
            }
        }
    }
    
    public boolean a() {
        final Iterator<a> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().c() == h.b.u) {
                return true;
            }
        }
        return false;
    }
    
    public boolean b() {
        final Iterator<a> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().c() == h.b.l) {
                return true;
            }
        }
        return false;
    }
    
    public class a
    {
        private c b;
        private b c;
        private Object d;
        private int e;
        private String f;
        
        public a(final c b, final b c, final Object d, final int e, final String f) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = null;
            if (f != null && f.length() > 0) {
                this.f = f;
            }
        }
        
        public int a() {
            return this.e;
        }
        
        public c b() {
            return this.b;
        }
        
        public b c() {
            return this.c;
        }
        
        public String d() {
            return this.f;
        }
        
        public Object e() {
            return this.d;
        }
    }
    
    public enum b
    {
        A, 
        B, 
        C, 
        D, 
        E, 
        F, 
        G, 
        H, 
        I, 
        J, 
        K, 
        L, 
        M, 
        N, 
        O, 
        P, 
        Q, 
        R, 
        S, 
        T, 
        U, 
        V, 
        W, 
        X, 
        Y, 
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i, 
        j, 
        k, 
        l, 
        m, 
        n, 
        o, 
        p, 
        q, 
        r, 
        s, 
        t, 
        u, 
        v, 
        w, 
        x, 
        y, 
        z;
    }
    
    public enum c
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i, 
        j, 
        k, 
        l, 
        m, 
        n, 
        o, 
        p, 
        q, 
        r, 
        s, 
        t, 
        u, 
        v;
    }
}
