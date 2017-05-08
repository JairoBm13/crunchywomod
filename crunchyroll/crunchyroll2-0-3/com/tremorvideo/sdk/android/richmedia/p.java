// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import com.tremorvideo.sdk.android.videoad.ac;
import android.graphics.Canvas;

public class p
{
    Canvas a;
    m.a b;
    float c;
    float d;
    float e;
    public float f;
    public boolean g;
    public boolean h;
    public float i;
    public float j;
    public boolean k;
    private float l;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    
    public p() {
        this.f = 1.0f;
        this.g = true;
        this.h = true;
        this.i = 1.0f;
        this.j = 1.0f;
        this.k = false;
    }
    
    public float a() {
        return this.f;
    }
    
    public float a(final q.b b) {
        if (b == com.tremorvideo.sdk.android.richmedia.q.b.b) {
            return Math.min(this.i, this.j);
        }
        if (b == com.tremorvideo.sdk.android.richmedia.q.b.c) {
            return Math.max(this.i, this.j);
        }
        return this.c;
    }
    
    public void a(float n, float f, float p6, float q, final q.b b, final b.b b2) {
        float n2 = 0.0f;
        Label_0031: {
            if (b2 == b.b.b || b2 == b.b.e || b2 == b.b.h) {
                n2 = n + p6 / 2.0f;
            }
            else {
                if (b2 != b.b.c && b2 != b.b.f) {
                    n2 = n;
                    if (b2 != b.b.i) {
                        break Label_0031;
                    }
                }
                n2 = n + p6;
            }
        }
        Label_0062: {
            if (b2 == b.b.d || b2 == b.b.e || b2 == b.b.f) {
                n = f + q / 2.0f;
            }
            else {
                if (b2 != b.b.g && b2 != b.b.h) {
                    n = f;
                    if (b2 != b.b.i) {
                        break Label_0062;
                    }
                }
                n = f + q;
            }
        }
        f = this.f();
        final float g = this.g();
        p6 *= this.a(b);
        q *= this.a(b);
        this.n = f * n2;
        this.o = g * n;
        if (b2 == b.b.b || b2 == b.b.e || b2 == b.b.h) {
            this.n -= p6 / 2.0f;
        }
        else if (b2 == b.b.c || b2 == b.b.f || b2 == b.b.i) {
            this.n -= p6;
        }
        if (b2 == b.b.d || b2 == b.b.e || b2 == b.b.f) {
            this.o -= q / 2.0f;
        }
        else if (b2 == b.b.g || b2 == b.b.h || b2 == b.b.i) {
            this.o -= q;
        }
        this.p = p6;
        this.q = q;
    }
    
    public void a(final Canvas a) {
        this.k = true;
        try {
            this.a = a;
            this.l = a.getWidth();
            this.m = a.getHeight();
            this.d = 1.0f;
            this.e = 1.0f;
            this.c = 1.0f;
            this.i = 1.0f;
            this.j = 1.0f;
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public void a(final m.a b, final Canvas a, final int n, final int n2) {
        this.k = true;
        this.b = b;
        this.a = a;
        this.l = a.getWidth();
        this.m = a.getHeight();
        this.d = a.getWidth() / n;
        this.e = a.getHeight() / n2;
        final int max = Math.max(a.getWidth(), a.getHeight());
        final int min = Math.min(a.getWidth(), a.getHeight());
        this.i = max / n;
        this.j = min / n2;
        this.c = Math.min(this.d, this.e);
    }
    
    public m.a b() {
        return this.b;
    }
    
    public Canvas c() {
        return this.a;
    }
    
    public float d() {
        return this.l;
    }
    
    public float e() {
        return this.m;
    }
    
    public float f() {
        return this.d;
    }
    
    public float g() {
        return this.e;
    }
    
    public float h() {
        return this.n;
    }
    
    public float i() {
        return this.o;
    }
    
    public float j() {
        return Math.max(1.0f, this.p);
    }
    
    public float k() {
        return Math.max(1.0f, this.q);
    }
}
