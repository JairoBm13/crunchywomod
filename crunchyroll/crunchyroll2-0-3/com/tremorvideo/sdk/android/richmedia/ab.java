// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.PointF;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.graphics.RectF;

public class ab extends q
{
    private static int[] q;
    private static int r;
    protected final int[] o;
    protected String p;
    
    static {
        ab.q = new int[20];
        ab.r = 0;
    }
    
    public ab(final o o) {
        super(o);
        this.o = new int[] { 10, 20, 30 };
    }
    
    private float a(final b b, final float n, final RectF rectF) {
        switch (ab$1.a[b.ordinal()]) {
            default: {
                return rectF.left;
            }
            case 1:
            case 2:
            case 3: {
                return rectF.left;
            }
            case 4:
            case 5:
            case 6: {
                return rectF.left + (rectF.width() - n) / 2.0f;
            }
            case 7:
            case 8:
            case 9: {
                return rectF.right - n;
            }
        }
    }
    
    private void a(final String s, final TextPaint textPaint, final RectF rectF) {
        int i = 0;
        ab.r = 0;
    Label_0106_Outer:
        while (i < s.length()) {
            final int breakText = textPaint.breakText((CharSequence)s, i, s.length(), true, rectF.width(), (float[])null);
            if (breakText == 0) {
                break;
            }
            while (true) {
                Label_0146: {
                    if (i + breakText >= s.length() || Character.isWhitespace(s.charAt(i + breakText))) {
                        break Label_0146;
                    }
                    int n;
                    for (n = breakText; n > 0 && !Character.isWhitespace(s.charAt(i + n)); --n) {}
                    if (n == 0) {
                        break Label_0146;
                    }
                    if (ab.r < ab.q.length) {
                        ab.q[ab.r] = n;
                        ++ab.r;
                    }
                    i += n;
                    continue Label_0106_Outer;
                }
                int n = breakText;
                continue;
            }
        }
    }
    
    private void a(final String s, final b b, final RectF rectF, final TextPaint textPaint, final Canvas canvas) {
        int n = 0;
        this.a(s, textPaint, rectF);
        final float n2 = Math.min(ab.r, Math.max(1, (int)Math.floor(rectF.height() / textPaint.getTextSize())));
        final float textSize = textPaint.getTextSize();
        float n3 = 0.0f;
        if (!this.g.g().e()) {
            n3 = -textPaint.descent();
        }
        int round = Math.round(this.b(b, n2 * textSize, rectF));
        int n4 = 0;
        while (n < n2) {
            final String trim = s.substring(n4, ab.q[n] + n4).trim();
            canvas.drawText(trim, this.a(b, textPaint.measureText(trim), rectF), round + n3 + textPaint.getTextSize(), (Paint)textPaint);
            round += (int)textPaint.getTextSize();
            n4 += ab.q[n];
            ++n;
        }
    }
    
    private float b(final b b, final float n, final RectF rectF) {
        switch (ab$1.a[b.ordinal()]) {
            default: {
                return rectF.top;
            }
            case 1:
            case 4:
            case 7: {
                return rectF.top;
            }
            case 2:
            case 5:
            case 8: {
                return rectF.top + (rectF.height() - n) / 2.0f;
            }
            case 3:
            case 6:
            case 9: {
                return rectF.bottom - n;
            }
        }
    }
    
    protected String a(final p p3, final a a, final long n) {
        if (this.p != null) {
            return this.p;
        }
        return this.g.g().a(a.e);
    }
    
    @Override
    public void a(final p p2, final long n) {
        if (this.n()) {
            final q.a a = this.a(n);
            if (a != null) {
                final a a2 = (a)a.a;
                final a a3 = (a)a.b;
                final float c = a.c;
                this.k = a2.z;
                if (a2.e != -1 && this.d(a2.z)) {
                    final int a4 = ae.a(a2.j, a3.j, a2.k, a3.k, c);
                    final int a5 = ae.a(a2.l, a3.l, a2.m, a3.m, c);
                    final int a6 = ae.a(a2.n, a3.n, a2.o, a3.o, c);
                    final int a7 = ae.a(a2.p, a3.p, a2.q, a3.q, c);
                    final int a8 = ae.a(a2.r, a3.r, a2.s, a3.s, c);
                    final int a9 = ae.a(a2.t, a3.t, a2.u, a3.u, c);
                    final int a10 = ae.a(a2.v, a3.v, a2.w, a3.w, c);
                    final int a11 = ae.a(a2.x, a3.x, a2.y, a3.y, c);
                    final float b = ae.b(a2.h, a3.h, a2.i, a3.i, c);
                    final b b2 = ab.b.values()[a2.g];
                    final int n2 = this.o[a2.f];
                    String s = this.a(p2, a2, n);
                    final int round = Math.round(s.length() * b);
                    if (round != 0) {
                        if (round != s.length()) {
                            int length;
                            if (s.length() < (length = round)) {
                                length = s.length();
                            }
                            s = s.substring(0, length);
                        }
                        final Point a12 = com.tremorvideo.sdk.android.richmedia.b.a(a6, a7, this.c.c());
                        p2.a(a4 - a12.x, a5 - a12.y, a6, a7, this.l, this.c.c());
                        final float h = p2.h();
                        final float i = p2.i();
                        final float j = p2.j();
                        final float k = p2.k();
                        float n3 = i;
                        float n4 = h;
                        if (this.c.a() != null) {
                            final k c2 = this.c.a().c(p2, n);
                            final float n5 = Math.round(c2.a);
                            final float n6 = Math.round(c2.b);
                            final Point a13 = com.tremorvideo.sdk.android.richmedia.b.a(Math.round(c2.f), Math.round(c2.g), this.c.b());
                            n4 = h + n5 + a13.x;
                            n3 = i + n6 + a13.y;
                        }
                        final RectF a14 = this.a(p2, n, new RectF(n4, n3, j + n4, k + n3));
                        final float n7 = Math.round(a14.left);
                        final float n8 = Math.round(a14.top);
                        final float n9 = Math.round(a14.width());
                        final float n10 = Math.round(a14.height());
                        final Canvas c3 = p2.c();
                        c3.save();
                        final TextPaint textPaint = new TextPaint();
                        textPaint.setColor(Color.argb(a11, a8, a9, a10));
                        textPaint.setTextSize(n2 * p2.a(this.l));
                        textPaint.setAntiAlias(true);
                        textPaint.setTypeface(Typeface.create("helvetica", 1));
                        this.a(s, b2, new RectF(n7, n8, n9 + n7, n10 + n8), textPaint, c3);
                        c3.restore();
                    }
                }
            }
        }
    }
    
    void a(final String p) {
        this.p = p;
    }
    
    public k b(final p p2, final long n) {
        final q.a a = this.a(n);
        if (a != null) {
            final a a2 = (a)a.a;
            final a a3 = (a)a.b;
            final float c = a.c;
            if (a2.e != -1) {
                final float b = ae.b(a2.j, a3.j, a2.k, a3.k, c);
                final float b2 = ae.b(a2.l, a3.l, a2.m, a3.m, c);
                final float b3 = ae.b(a2.n, a3.n, a2.o, a3.o, c);
                final float b4 = ae.b(a2.p, a3.p, a2.q, a3.q, c);
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
    
    @Override
    public void b() {
        this.p = null;
        super.b();
    }
    
    protected a c() {
        return new a();
    }
    
    protected class a extends j
    {
        public int e;
        public int f;
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
        public float r;
        public int s;
        public float t;
        public int u;
        public float v;
        public int w;
        public float x;
        public int y;
        public boolean z;
        
        @Override
        public void a(final e e) {
            super.a(e);
            try {
                this.e = e.b();
                this.f = e.b();
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
                this.r = e.d();
                this.s = e.b();
                this.t = e.d();
                this.u = e.b();
                this.v = e.d();
                this.w = e.b();
                this.x = e.d();
                this.y = e.b();
                this.z = e.f();
            }
            catch (Exception ex) {}
        }
        
        protected void b(final e e) {
            super.a(e);
        }
    }
    
    protected enum b
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
