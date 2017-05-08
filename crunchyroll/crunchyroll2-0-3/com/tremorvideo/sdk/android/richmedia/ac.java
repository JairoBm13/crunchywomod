// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class ac extends q
{
    public ac(final o o) {
        super(o);
    }
    
    @Override
    public void a(final p p2, final long n) {
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
            if (a2.j) {
                final float b = ae.b(a2.a, a3.a, a2.b, a3.b, c);
                final float b2 = ae.b(a2.c, a3.c, a2.d, a3.d, c);
                final float b3 = ae.b(a2.e, a3.e, a2.f, a3.f, c);
                final float b4 = ae.b(a2.g, a3.g, a2.h, a3.h, c);
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
                return new k(a7.left, a7.top, a7.width(), a7.height(), 1.0f, 1.0f, 0.0f);
            }
        }
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    protected a c() {
        return new a();
    }
    
    protected class a extends j
    {
        public float a;
        public int b;
        public float c;
        public int d;
        public float e;
        public int f;
        public float g;
        public int h;
        public int i;
        public boolean j;
        
        @Override
        public void a(final e e) {
            super.a(e);
            try {
                this.a = e.d();
                this.b = e.b();
                this.c = e.d();
                this.d = e.b();
                this.e = e.d();
                this.f = e.b();
                this.g = e.d();
                this.h = e.b();
                this.i = (int)e.d();
                this.j = e.f();
            }
            catch (Exception ex) {}
        }
    }
}
