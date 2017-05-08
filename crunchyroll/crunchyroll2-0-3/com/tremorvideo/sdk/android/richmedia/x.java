// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

public class x extends q
{
    o u;
    
    public x(final o o) {
        super(o);
    }
    
    protected o a(final a a, final long n) {
        if (this.u != null) {
            return this.u;
        }
        return this.g.g().b(a.e);
    }
    
    void a(final o u) {
        this.u = u;
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.s;
                if (this.d(a2.s) && a2.e != 255) {
                    final o a4 = this.a(a2, n);
                    final long b = this.b(a2, n);
                    final int a5 = ae.a(a2.f, a3.f, a2.g, a3.g, c);
                    final int a6 = ae.a(a2.h, a3.h, a2.i, a3.i, c);
                    final float b2 = ae.b(a2.m, a3.m, a2.n, a3.n, c);
                    final float b3 = ae.b(a2.o, a3.o, a2.p, a3.p, c);
                    final float b4 = ae.b(a2.q, a3.q, a2.r, a3.r, c);
                    final p p3 = new p();
                    p3.a(p2.c());
                    p3.f = p2.f;
                    p3.g = p2.g;
                    p3.h = p2.h;
                    final Rect a7 = a4.a(p3, b);
                    final int width = a7.width();
                    final int height = a7.height();
                    final PointF a8 = com.tremorvideo.sdk.android.richmedia.b.a(width, (float)height, this.c.c());
                    final float n2 = a5;
                    final float n3 = a6;
                    final float n4 = width;
                    final float n5 = height;
                    if (this.c.c() == com.tremorvideo.sdk.android.richmedia.b.b.e) {
                        a8.set(0.0f, 0.0f);
                    }
                    else {
                        a8.x += a7.left;
                        a8.y += a7.top;
                    }
                    p2.a(a5, a6, 0.0f, 0.0f, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float a9 = p2.a(this.l);
                    float n6 = i;
                    float n7 = h;
                    if (this.c.a() != null) {
                        final k c2 = this.c.a().c(p2, n);
                        final float n8 = Math.round(c2.a);
                        final float n9 = Math.round(c2.b);
                        final PointF a10 = com.tremorvideo.sdk.android.richmedia.b.a(c2.f, c2.g, this.c.b());
                        n7 = h + n8 + a10.x;
                        n6 = i + n9 + a10.y;
                    }
                    final PointF a11 = com.tremorvideo.sdk.android.richmedia.b.a(n4 * b2, n5 * b3, this.c.c());
                    a11.set(0.0f, 0.0f);
                    final Canvas c3 = p3.c();
                    c3.save();
                    c3.translate((float)Math.round(n7), (float)Math.round(n6));
                    c3.translate((float)Math.round(a11.x), (float)Math.round(a11.y));
                    c3.rotate(b4);
                    c3.translate((float)(-Math.round(a11.x)), (float)(-Math.round(a11.y)));
                    c3.scale(b2, b3);
                    c3.scale(a9, a9);
                    c3.translate(-a8.x, -a8.y);
                    a4.a(p3, b, null);
                    c3.restore();
                }
            }
        }
    }
    
    protected long b(final a a, final long n) {
        if (this.u != null) {
            return n % this.u.c();
        }
        return n % this.g.g().b(a.e).c();
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
            if (a2.e != 255) {
                final o a4 = this.a(a2, n);
                final long b = this.b(a2, n);
                final float b2 = ae.b(a2.f, a3.f, a2.g, a3.g, c);
                final float b3 = ae.b(a2.h, a3.h, a2.i, a3.i, c);
                final float b4 = ae.b(a2.m, a3.m, a2.n, a3.n, c);
                final float b5 = ae.b(a2.o, a3.o, a2.p, a3.p, c);
                final float b6 = ae.b(a2.q, a3.q, a2.r, a3.r, c);
                final p p3 = new p();
                p3.a(p2.c());
                p3.f = p2.f;
                p3.g = p2.g;
                final RectF b7 = a4.b(p3, b);
                final float width = b7.width();
                final float height = b7.height();
                final PointF a5 = com.tremorvideo.sdk.android.richmedia.b.a(width, height, this.c.c());
                if (this.c.c() == com.tremorvideo.sdk.android.richmedia.b.b.e) {
                    a5.set(0.0f, 0.0f);
                }
                else {
                    a5.x += b7.left;
                    a5.y += b7.top;
                }
                p2.a(b2, b3, 0.0f, 0.0f, this.l, this.c.c());
                final float h = p2.h();
                float i = p2.i();
                float n2 = h;
                if (this.c.a() != null) {
                    final k c2 = this.c.a().c(p2, n);
                    final float n3 = Math.round(c2.a);
                    final float n4 = Math.round(c2.b);
                    final PointF a6 = com.tremorvideo.sdk.android.richmedia.b.a(c2.f, c2.g, this.c.b());
                    n2 = h + n3 + a6.x;
                    i = i + n4 + a6.y;
                }
                final PointF a7 = com.tremorvideo.sdk.android.richmedia.b.a(width, height, this.c.c());
                if (this.c.c() == com.tremorvideo.sdk.android.richmedia.b.b.e) {
                    a7.set(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                matrix.preTranslate(n2, i);
                matrix.preTranslate(a7.x, a7.y);
                matrix.preScale(b4, b5);
                matrix.preTranslate(-a7.x, -a7.y);
                matrix.preScale(p2.a(this.l), p2.a(this.l));
                matrix.preTranslate(-a5.x, -a5.y);
                matrix.mapRect(b7);
                return new k(b7.left, b7.top, b7.width(), b7.height(), b4, b5, b6);
            }
        }
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    @Override
    public void b() {
        super.b();
        this.u = null;
    }
    
    protected a c() {
        return new a();
    }
    
    protected class a extends j
    {
        public int e;
        public float f;
        public int g;
        public float h;
        public int i;
        public int j;
        public float k;
        public int l;
        public float m;
        public int n;
        public float o;
        public int p;
        public float q;
        public int r;
        public boolean s;
        
        @Override
        public void a(final e e) {
            super.a(e);
            try {
                this.e = e.b();
                this.f = e.d();
                this.g = e.b();
                this.h = e.d();
                this.i = e.b();
                this.j = 0;
                this.k = e.d();
                this.l = e.b();
                this.m = e.d();
                this.n = e.b();
                this.o = e.d();
                this.p = e.b();
                this.q = e.d();
                this.r = e.b();
                this.s = e.f();
            }
            catch (Exception ex) {}
        }
        
        protected void b(final e e) {
            super.a(e);
        }
    }
}
