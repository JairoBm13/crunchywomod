// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Canvas;
import android.graphics.Paint$Style;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class u extends q
{
    public u(final o o) {
        super(o);
    }
    
    private int a(final p p2, final b b) {
        int n = 1;
        if (b == u.b.c) {
            return 1;
        }
        if (b == u.b.d) {
            n = 10;
        }
        else if (b == u.b.e) {
            n = 25;
        }
        return Math.max(0, Math.round(n * p2.a(this.l)));
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.r;
                if (this.d(a2.r)) {
                    final float b = ae.b(a2.b, a3.b, a2.c, a3.c, c);
                    final float b2 = ae.b(a2.d, a3.d, a2.e, a3.e, c);
                    final float b3 = ae.b(a2.f, a3.f, a2.g, a3.g, c);
                    final float b4 = ae.b(a2.h, a3.h, a2.i, a3.i, c);
                    final int a4 = ae.a(a2.j, a3.j, a2.k, a3.k, c);
                    final int a5 = ae.a(a2.l, a3.l, a2.m, a3.m, c);
                    final int a6 = ae.a(a2.n, a3.n, a2.o, a3.o, c);
                    final int a7 = ae.a(a2.p, a3.p, a2.q, a3.q, c);
                    final b b5 = u.b.values()[a2.a];
                    final Canvas c2 = p2.c();
                    c2.save();
                    final PointF a8 = com.tremorvideo.sdk.android.richmedia.b.a(b3, b4, this.c.c());
                    p2.a(b - a8.x, b2 - a8.y, b3, b4, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float j = p2.j();
                    final float k = p2.k();
                    float n2 = i;
                    float n3 = h;
                    if (this.c.a() != null) {
                        final k c3 = this.c.a().c(p2, n);
                        final float a9 = c3.a;
                        final float b6 = c3.b;
                        final Point a10 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c3.f), Math.round(c3.g), this.c.b());
                        n3 = h + a9 + a10.x;
                        n2 = i + b6 + a10.y;
                    }
                    final RectF a11 = this.a(p2, n, new RectF(n3, n2, j + n3, k + n2));
                    final float left = a11.left;
                    final float top = a11.top;
                    final float width = a11.width();
                    final float height = a11.height();
                    c2.translate((float)Math.round(left), (float)Math.round(top));
                    final Paint paint = new Paint();
                    paint.setColor(Color.argb(a7, a4, a5, a6));
                    if (b5 == u.b.a) {
                        c2.drawRect(0.0f, 0.0f, (float)Math.round(width), (float)Math.round(height), paint);
                    }
                    else if (b5 == u.b.b) {
                        paint.setAntiAlias(true);
                        c2.drawOval(new RectF(0.0f, 0.0f, (float)Math.round(width), (float)Math.round(height)), paint);
                    }
                    else {
                        final int a12 = this.a(p2, b5);
                        paint.setStyle(Paint$Style.STROKE);
                        paint.setStrokeWidth((float)a12);
                        final int n4 = (int)Math.floor(a12 / 2.0);
                        final int n5 = (int)Math.round(a12 / 2.0);
                        c2.drawRect((float)n4, (float)n4, (float)Math.round(width - n5), (float)Math.round(height - n5), paint);
                    }
                    c2.restore();
                }
            }
        }
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
            final float b = ae.b(a2.b, a3.b, a2.c, a3.c, c);
            final float b2 = ae.b(a2.d, a3.d, a2.e, a3.e, c);
            final float b3 = ae.b(a2.f, a3.f, a2.g, a3.g, c);
            final float b4 = ae.b(a2.h, a3.h, a2.i, a3.i, c);
            final PointF a4 = com.tremorvideo.sdk.android.richmedia.b.a(b3, b4, this.c.c());
            p2.a(b - a4.x, b2 - a4.y, b3, b4, this.l, this.c.c());
            final float h = p2.h();
            final float i = p2.i();
            final float j = p2.j();
            final float k = p2.k();
            float n2 = i;
            float n3 = h;
            if (this.c.a() != null) {
                final k c2 = this.c.a().c(p2, n);
                final float a5 = c2.a;
                final float b5 = c2.b;
                final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c2.f), Math.round(c2.g), this.c.b());
                n3 = h + a5 + a6.x;
                n2 = i + b5 + a6.y;
            }
            final RectF a7 = this.a(p2, n, new RectF(n3, n2, j + n3, k + n2));
            return new k(a7.left, a7.top, a7.right - a7.left, a7.bottom - a7.top, 1.0f, 1.0f, 0.0f);
        }
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    protected a c() {
        return new a();
    }
    
    protected class a extends j
    {
        public int a;
        public float b;
        public int c;
        public float d;
        public int e;
        public float f;
        public int g;
        public float h;
        public int i;
        public float j;
        public int k;
        public float l;
        public int m;
        public float n;
        public int o;
        public float p;
        public int q;
        public boolean r;
        
        @Override
        public void a(final e e) {
            super.a(e);
            try {
                this.a = e.b();
                this.b = e.d();
                this.c = e.b();
                this.d = e.d();
                this.e = e.b();
                this.f = e.d();
                this.g = e.b();
                this.h = e.d();
                this.i = e.b();
                this.j = e.d();
                this.k = e.b();
                this.l = e.d();
                this.m = e.b();
                this.n = e.d();
                this.o = e.b();
                this.p = e.d();
                this.q = e.b();
                this.r = e.f();
            }
            catch (Exception ex) {}
        }
    }
    
    enum b
    {
        a, 
        b, 
        c, 
        d, 
        e;
    }
}
