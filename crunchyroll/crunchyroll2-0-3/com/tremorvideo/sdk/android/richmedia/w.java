// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap;

public class w extends q
{
    Bitmap o;
    
    public w(final o o) {
        super(o);
    }
    
    private Bitmap a(final a a) {
        if (this.o != null) {
            return this.o;
        }
        return this.g.g().r().a(a.n);
    }
    
    public void a(final Bitmap o) {
        this.o = o;
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.m;
                if (a2.n != -1 && this.d(a2.m)) {
                    final int a4 = ae.a(a2.i, a3.i, a2.j, a3.j, c);
                    final int a5 = ae.a(a2.k, a3.k, a2.l, a3.l, c);
                    final float b = ae.b(a2.a, a3.a, a2.b, a3.b, c);
                    final float b2 = ae.b(a2.c, a3.c, a2.d, a3.d, c);
                    final float b3 = ae.b(a2.e, a3.e, a2.f, a3.f, c);
                    final float b4 = ae.b(a2.g, a3.g, a2.h, a3.h, c);
                    final Bitmap a6 = this.a(a2);
                    final float n2 = b * (a6.getWidth() * p2.a());
                    final float n3 = b2 * (a6.getHeight() * p2.a());
                    final PointF a7 = com.tremorvideo.sdk.android.richmedia.b.a(n2, n3, this.c.c());
                    p2.a((int)(a4 - a7.x), (int)(a5 - a7.y), n2, n3, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float j = p2.j();
                    final float k = p2.k();
                    float n4 = i;
                    float n5 = h;
                    if (this.c.a() != null) {
                        final k c2 = this.c.a().c(p2, n);
                        final float a8 = c2.a;
                        final float b5 = c2.b;
                        final PointF a9 = com.tremorvideo.sdk.android.richmedia.b.a(c2.f, c2.g, this.c.b());
                        n5 = h + a8 + a9.x;
                        n4 = i + b5 + a9.y;
                    }
                    final RectF a10 = this.a(p2, n, new RectF(n5, n4, j + n5, k + n4));
                    final float left = a10.left;
                    final float top = a10.top;
                    final float width = a10.width();
                    final float height = a10.height();
                    final float n6 = width / a6.getWidth();
                    final float n7 = height / a6.getHeight();
                    final PointF a11 = com.tremorvideo.sdk.android.richmedia.b.a(width, height, this.c.c());
                    final Paint paint = new Paint();
                    paint.setAlpha(Math.round(255.0f * b4));
                    paint.setFilterBitmap(true);
                    final Canvas c3 = p2.c();
                    c3.save();
                    c3.translate((float)Math.round(left), (float)Math.round(top));
                    c3.translate((float)Math.round(a11.x), (float)Math.round(a11.y));
                    c3.rotate(b3);
                    c3.translate((float)(-Math.round(a11.x)), (float)(-Math.round(a11.y)));
                    c3.scale(n6, n7);
                    c3.drawBitmap(a6, 0.0f, 0.0f, paint);
                    c3.restore();
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
            if (a2.n != -1) {
                final Bitmap a4 = this.a(a2);
                final float b = ae.b(a2.i, a3.i, a2.j, a3.j, c);
                final float b2 = ae.b(a2.k, a3.k, a2.l, a3.l, c);
                final float b3 = ae.b(a2.a, a3.a, a2.b, a3.b, c);
                final float b4 = ae.b(a2.c, a3.c, a2.d, a3.d, c);
                final float b5 = ae.b(a2.e, a3.e, a2.f, a3.f, c);
                final float n2 = a4.getWidth() * b3 * p2.a();
                final float n3 = a4.getHeight() * b4 * p2.a();
                final PointF a5 = com.tremorvideo.sdk.android.richmedia.b.a(n2, n3, this.c.c());
                p2.a(b - a5.x, b2 - a5.y, n2, n3, this.l, this.c.c());
                final float h = p2.h();
                final float i = p2.i();
                final float j = p2.j();
                final float k = p2.k();
                float n4 = i;
                float n5 = h;
                if (this.c.a() != null) {
                    final k c2 = this.c.a().c(p2, n);
                    final float n6 = Math.round(c2.a);
                    final float n7 = Math.round(c2.b);
                    final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c2.f), Math.round(c2.g), this.c.b());
                    n5 = h + n6 + a6.x;
                    n4 = i + n7 + a6.y;
                }
                final RectF a7 = this.a(p2, n, new RectF(n5, n4, j + n5, k + n4));
                return new k(a7.left, a7.top, a7.width(), a7.height(), b3, b4, b5);
            }
        }
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    @Override
    public void b() {
        super.b();
        this.o = null;
    }
    
    protected a c() {
        return new a();
    }
    
    private class a extends j
    {
        public float a;
        public int b;
        public float c;
        public int d;
        public float e;
        public int f;
        public float g;
        public int h;
        public float i;
        public int j;
        public float k;
        public int l;
        public boolean m;
        public int n;
        
        @Override
        public void a(final e e) {
            super.a(e);
            try {
                this.n = e.b();
                this.i = e.d();
                this.j = e.b();
                this.k = e.d();
                this.l = e.b();
                e.d();
                e.b();
                this.a = e.d();
                this.b = e.b();
                this.c = e.d();
                this.d = e.b();
                this.e = e.d();
                this.f = e.b();
                this.g = e.d();
                this.h = e.b();
                this.m = e.f();
            }
            catch (Exception ex) {}
        }
    }
}
