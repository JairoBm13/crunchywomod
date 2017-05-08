// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.util.GregorianCalendar;
import android.graphics.Paint$Style;
import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Color;
import com.tremorvideo.sdk.android.videoad.ax;
import com.tremorvideo.sdk.android.videoad.ac;
import com.tremorvideo.sdk.android.videoad.aw;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Paint;

public class aa extends q
{
    double A;
    int B;
    Paint o;
    Paint p;
    Paint q;
    Paint r;
    Paint s;
    int t;
    int u;
    int v;
    int w;
    int x;
    int y;
    int z;
    
    public aa(final o o) {
        super(o);
        this.t = 0;
        this.u = 0;
        this.v = 0;
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.A = 0.0;
        this.B = 0;
    }
    
    private int a(final ad[] array, final int n) {
        int i = 0;
        final int length = array.length;
        int n2 = 0;
        while (i < length) {
            final ad ad = array[i];
            if (ad.c == 0) {
                ad.c = Math.round(this.a(ad.a, n).measureText(ad.b));
            }
            n2 += ad.c;
            ++i;
        }
        return n2;
    }
    
    private Paint a(final ad.a a, final int n) {
        if (a == ad.a.a) {
            return this.c(n);
        }
        if (a == ad.a.c) {
            return this.b(n);
        }
        if (a == ad.a.b) {
            return this.d(n);
        }
        return this.c(n);
    }
    
    private ad i(final int n, int i) {
        i = this.e(i);
        final int w = this.w;
        final ad[] o = this.g.g().o();
        if (o != null) {
            final int length = o.length;
            int n2 = w + i;
            ad ad;
            for (i = 0; i < length; ++i) {
                ad = o[i];
                if (n2 < n && ad.c + n2 >= n) {
                    return ad;
                }
                n2 += ad.c;
            }
        }
        return null;
    }
    
    @Override
    public void a() {
        super.a();
        this.t = 0;
        this.u = 0;
        this.v = 0;
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.o = null;
        this.p = null;
        this.q = null;
        this.r = null;
        this.s = null;
        this.B = 250;
        final ad[] o = this.g.g().o();
        if (o != null) {
            for (int length = o.length, i = 0; i < length; ++i) {
                o[i].c = 0;
            }
        }
    }
    
    protected void a(final Canvas canvas, int i, int n, int length, final int n2) {
        canvas.save();
        final ad[] o = this.g.g().o();
        int a = 0;
        if (o != null) {
            a = this.a(o, n2);
        }
        this.z = a;
        final int n3 = this.e(length) + (i + 2);
        this.b(canvas, i, n, length, n2);
        canvas.clipRect(i + 2, 0, i + length - 2, n2);
        if (o == null) {
            final Paint c = this.c(n2);
            i = Math.round((n2 - c.getTextSize()) / 2.0f);
            canvas.drawText("Loading Show Times...", (float)n3, i + c.getTextSize() - c.descent(), c);
        }
        else {
            length = o.length;
            i = 0;
            n = n3;
            while (i < length) {
                final ad ad = o[i];
                final Paint a2 = this.a(ad.a, n2);
                final int round = Math.round((n2 - a2.getTextSize()) / 2.0f);
                canvas.save();
                canvas.translate((float)n, round + a2.getTextSize() - a2.descent());
                canvas.drawText(ad.b, 0.0f, 0.0f, a2);
                canvas.restore();
                n += ad.c;
                ++i;
            }
        }
        canvas.restore();
    }
    
    @Override
    public void a(final e e) {
        super.a(e);
        this.h = true;
    }
    
    @Override
    public void a(final m m, final long n) {
        if (this.g.g().o() != null && this.z != 0) {
            if (this.B > 0) {
                this.B = (int)Math.max(0L, this.B - n);
                if (this.B == 0) {
                    this.a();
                    this.B = 0;
                }
            }
            this.A -= this.v * 0.2 * n / 1000.0;
        }
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.i;
                if (this.d(a2.i)) {
                    final float b = ae.b(a2.a, a3.a, a2.b, a3.b, c);
                    final float b2 = ae.b(a2.c, a3.c, a2.d, a3.d, c);
                    final float b3 = ae.b(a2.e, a3.e, a2.f, a3.f, c);
                    final float b4 = ae.b(a2.g, a3.g, a2.h, a3.h, c);
                    final Canvas c2 = p2.c();
                    final PointF a4 = com.tremorvideo.sdk.android.richmedia.b.a(b3, b4, this.c.c());
                    p2.a(b - a4.x, b2 - a4.y, b3, b4, this.l, this.c.c());
                    final float h = p2.h();
                    final float i = p2.i();
                    final float j = p2.j();
                    final float k = p2.k();
                    float n2 = i;
                    float n3 = h;
                    if (this.c.a() != null) {
                        final k c3 = this.c.a().c(p2, n);
                        final float a5 = c3.a;
                        final float b5 = c3.b;
                        final Point a6 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c3.f), Math.round(c3.g), this.c.b());
                        n3 = h + a5 + a6.x;
                        n2 = i + b5 + a6.y;
                    }
                    final RectF a7 = this.a(p2, n, new RectF(n3, n2, j + n3, k + n2));
                    final float left = a7.left;
                    final float top = a7.top;
                    final float width = a7.width();
                    final float height = a7.height();
                    final int round = Math.round(height);
                    final int round2 = Math.round(width);
                    final int round3 = Math.round(height);
                    final int e = this.e(round2, round3);
                    final int f = this.f(round2, round3);
                    final int c4 = this.c(round2, round3);
                    final int d = this.d(round2, round3);
                    final int g = this.g(round2, round3);
                    final int h2 = this.h(round2, round3);
                    c2.save();
                    c2.translate((float)Math.round(left), (float)Math.round(top));
                    this.c(c2, e, 0, f, round);
                    this.a(c2, g, 0, h2, round);
                    this.d(c2, c4, 0, d, round);
                    c2.restore();
                }
            }
        }
    }
    
    @Override
    public boolean a(final int n, final int n2) {
        if (n >= this.x && n < this.x + this.y) {
            this.g.g().c().a(aw.b.J);
        }
        else if (n >= this.t && n < this.t + this.u) {
            this.g.g().c().a(aw.b.K);
            return true;
        }
        if (n >= this.w && n <= this.w + this.v) {
            final ad i = this.i(n, this.v);
            if (i != null && i.d != null && i.d != "") {
                ac.e("TMS URL: " + i.d);
                final ax c = this.g.g().c();
                c.a(aw.b.I);
                c.c(i.d);
            }
        }
        return false;
    }
    
    protected Paint b(final int n) {
        if (this.s != null) {
            return this.s;
        }
        (this.s = new Paint()).setTextSize(n * 0.4f);
        this.s.setColor(-1);
        this.s.setAntiAlias(true);
        return this.s;
    }
    
    protected Paint b(final int n, final int n2) {
        if (this.o != null) {
            return this.o;
        }
        final LinearGradient shader = new LinearGradient(0.0f, (float)n, 0.0f, (float)n2, new int[] { Color.argb(255, 54, 54, 54), Color.argb(255, 35, 35, 35), Color.argb(255, 35, 35, 35) }, new float[] { 0.0f, 0.75f, 1.0f }, Shader$TileMode.CLAMP);
        (this.o = new Paint()).setDither(true);
        this.o.setShader((Shader)shader);
        return this.o;
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
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
            return new k(a7.left, a7.top, a7.right - a7.left, a7.bottom - a7.top, 1.0f, 1.0f, 0.0f);
        }
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    protected void b(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final Paint c = this.c();
        canvas.drawRect((float)n, (float)n2, (float)(n + n3), (float)(n2 + n4), this.b(n2, n2 + n4));
        canvas.drawRect((float)n, (float)n2, (float)(n + n3 - 1), (float)(n2 + n4 - 1), c);
    }
    
    protected int c(final int n, final int n2) {
        if (this.t > 0) {
            return this.t;
        }
        return this.t = this.e(n, n2) + this.f(n, n2) + 2;
    }
    
    protected Paint c() {
        if (this.q != null) {
            return this.q;
        }
        (this.q = new Paint()).setColor(Color.argb(255, 73, 73, 73));
        this.q.setStyle(Paint$Style.STROKE);
        return this.q;
    }
    
    protected Paint c(final int n) {
        if (this.p != null) {
            return this.p;
        }
        (this.p = new Paint()).setTextSize(n * 0.4f);
        this.p.setColor(-1);
        this.p.setAntiAlias(true);
        return this.p;
    }
    
    protected void c(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final int f = this.f(n3, n4);
        final Paint c = this.c(n4);
        final String p5 = this.g.g().p();
        final int n5 = (f - Math.round(c.measureText(p5))) / 2;
        final int round = Math.round((n4 - c.getTextSize()) / 2.0f);
        this.b(canvas, n, n2, n3, n4);
        canvas.drawText(p5, (float)(n5 + n), round + n2 + c.getTextSize() - c.descent(), c);
    }
    
    protected int d(final int n, final int n2) {
        if (this.u > 0) {
            return this.u;
        }
        return this.u = Math.round(this.c(n2).measureText("0000-00-00") + 20.0f);
    }
    
    protected Paint d(final int n) {
        if (this.r != null) {
            return this.r;
        }
        (this.r = new Paint()).setTextSize(n * 0.4f);
        this.r.setColor(-1);
        this.r.setAntiAlias(true);
        this.r.setFakeBoldText(true);
        return this.r;
    }
    
    protected a d() {
        return new a();
    }
    
    protected void d(final Canvas canvas, final int n, final int n2, final int n3, final int n4) {
        final int d = this.d(n3, n4);
        final Paint c = this.c(n4);
        final GregorianCalendar q = this.g.g().q();
        final String format = String.format("%d-%02d-%02d", q.get(1), q.get(2) + 1, q.get(5));
        final int n5 = (d - Math.round(c.measureText(format))) / 2;
        final int round = Math.round((n4 + 0 - c.getTextSize()) / 2.0f);
        this.b(canvas, n, n2, n3, n4);
        canvas.drawText(format, (float)(n5 + (n + 0)), 0 + n2 + round + c.getTextSize() - c.descent(), c);
    }
    
    protected int e(final int n) {
        if (this.g.g().o() == null) {
            return 0;
        }
        if (this.A <= -(this.z + n)) {
            this.A = 0.0;
        }
        return (int)this.A + n;
    }
    
    protected int e(final int n, final int n2) {
        return 0;
    }
    
    protected int f(final int n, final int n2) {
        if (this.y > 0) {
            return this.y;
        }
        return this.y = Math.round(this.c(n2).measureText("00000") + 20.0f);
    }
    
    protected int g(final int n, final int n2) {
        if (this.w > 0) {
            return this.w;
        }
        return this.w = this.c(n, n2) + this.d(n, n2) + 2;
    }
    
    protected int h(final int n, int g) {
        if (this.v > 0) {
            return this.v;
        }
        g = this.g(n, g);
        return this.v = Math.round(n) - g;
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
        public boolean i;
        
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
                this.i = e.f();
            }
            catch (Exception ex) {}
        }
    }
}
