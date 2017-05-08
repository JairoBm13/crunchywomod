// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.RectF;
import com.tremorvideo.sdk.android.richmedia.a.i;
import java.util.zip.ZipFile;
import com.tremorvideo.sdk.android.videoad.aw;
import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Paint;
import java.util.List;

public class o
{
    public c a;
    public boolean b;
    private int c;
    private long d;
    private h e;
    private List<q> f;
    private com.tremorvideo.sdk.android.richmedia.a g;
    private d h;
    private a i;
    private Object j;
    private int k;
    private b l;
    
    public o(final com.tremorvideo.sdk.android.richmedia.a g) {
        this.b = false;
        this.g = g;
        this.e = new h();
        this.a = new c();
    }
    
    private void a(final p p) {
        if (this.i == o.a.b) {
            final Paint paint = new Paint();
            paint.setColor((int)this.j);
            final int round = Math.round(p.d() / -2.0f);
            final int round2 = Math.round(p.e() / -2.0f);
            p.c().drawRect((float)round, (float)round2, (float)(-round), (float)(-round2), paint);
        }
        else if (this.i == o.a.c) {
            final Bitmap a = this.g.r().a((int)this.j);
            final Rect rect = new Rect(0, 0, a.getWidth(), a.getHeight());
            final Rect rect2 = new Rect(Math.round(p.d() / -2.0f), Math.round(p.e() / -2.0f), Math.round(p.d() / 2.0f), Math.round(p.e() / 2.0f));
            final Paint paint2 = new Paint();
            paint2.setFilterBitmap(true);
            p.c().drawBitmap(a, rect, rect2, paint2);
        }
    }
    
    private void a(final p p4, final q q, final long n, final g g) {
        if ((!p4.g && q.f()) || (!p4.h && q.g()) || !this.a(q)) {
            return;
        }
        if (g == null) {
            q.a(p4, n);
            return;
        }
        if (q == g.b()) {
            g.a(p4);
            return;
        }
        q.a(p4, n);
    }
    
    public int a() {
        final boolean b = false;
        int n = 0;
        int n2;
        while (true) {
            n2 = (b ? 1 : 0);
            if (n >= this.f.size()) {
                break;
            }
            if (this.f.get(n) instanceof ac) {
                n2 = n + 1;
                break;
            }
            ++n;
        }
        return n2;
    }
    
    public Rect a(final p p2, final long n) {
        final Rect rect = new Rect(0, 0, 0, 0);
        final Iterator<q> iterator = this.f.iterator();
        while (iterator.hasNext()) {
            final k c = iterator.next().c(p2, n);
            rect.left = Math.min(rect.left, Math.round(c.a));
            rect.top = Math.min(rect.top, Math.round(c.b));
            rect.right = Math.max(rect.right, Math.round(c.a + c.f));
            rect.bottom = Math.max(rect.bottom, Math.round(c.g + c.b));
        }
        return rect;
    }
    
    public h.a a(final h.c c) {
        return this.e.a(c);
    }
    
    public h.a a(final p p4, final int n, final int n2, final long n3) {
        int n4;
        if (this.g.g() > 1) {
            n4 = -1;
        }
        else {
            n4 = 1;
        }
        int i;
        if (this.g.g() > 1) {
            i = this.f.size() - 1;
        }
        else {
            i = 0;
        }
        int size;
        if (this.g.g() > 1) {
            size = -1;
        }
        else {
            size = this.f.size();
        }
        while (i != size) {
            final q q = this.f.get(i);
            if (q.k() && this.a(q) && q.d(q.k)) {
                final k c = q.c(p4, n3);
                if (c.a < n && c.b < n2 && c.f + c.a >= n && c.g + c.b >= n2) {
                    if (q.a(Math.round(n - c.a), Math.round(n2 - c.b))) {
                        return null;
                    }
                    return q.m();
                }
            }
            i += n4;
        }
        return null;
    }
    
    public q a(final int n) {
        if (n == 254) {
            return this.g.n();
        }
        return this.f.get(n);
    }
    
    public void a(final long b, final af af, final int c, final boolean d, final ArrayList<aw> list) {
        this.a.a = true;
        this.a.b = b;
        this.a.c = c;
        this.a.d = d;
        if (af != null && af.d) {
            this.a.e = af.n;
            this.a.f = af.p;
            this.a.g = af.g();
        }
        else {
            this.a.e = null;
            this.a.f = -1;
            this.a.g = -1;
        }
        this.a.h.clear();
        final Iterator<aw> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.a.h.add(iterator.next());
        }
    }
    
    public void a(final m m, final long n) {
        final Iterator<q> iterator = this.f.iterator();
        while (iterator.hasNext()) {
            iterator.next().a(m, n);
        }
    }
    
    public void a(final p p3, final long n, final g g) {
        final Iterator<q> iterator = this.f.iterator();
        while (iterator.hasNext()) {
            this.a(p3, iterator.next(), n, g);
        }
    }
    
    public boolean a(final e e, final ZipFile zipFile) {
        while (true) {
            while (true) {
                int n;
                q a;
                try {
                    this.c = e.a();
                    this.h = o.d.values()[e.b()];
                    this.d = e.a();
                    this.l = o.b.values()[e.b()];
                    this.k = e.b();
                    if (this.k == 255) {
                        this.k = -1;
                    }
                    this.i = o.a.values()[e.b()];
                    if (this.i == o.a.b) {
                        this.j = new Integer(e.a());
                    }
                    else if (this.i == o.a.c) {
                        this.j = new Integer(e.b());
                    }
                    this.e.a(e, this.g.g());
                    final int b = e.b();
                    this.f = new ArrayList<q>(b);
                    n = 0;
                    if (n >= b) {
                        break;
                    }
                    a = q.a(this, e.a());
                    if (a == null) {
                        return false;
                    }
                }
                catch (Exception ex) {
                    com.tremorvideo.sdk.android.videoad.ac.e("Scene:load Exception =" + ex);
                    return false;
                }
                a.a(e);
                if (a instanceof com.tremorvideo.sdk.android.richmedia.b.a) {
                    ((com.tremorvideo.sdk.android.richmedia.b.a)a).a(zipFile);
                }
                if (a instanceof i) {
                    ((i)a).a(zipFile);
                }
                this.f.add(a);
                ++n;
                continue;
            }
        }
        return true;
    }
    
    public boolean a(final q q) {
        return this.g.t() || !(q instanceof aa);
    }
    
    public RectF b(final p p2, final long n) {
        final RectF rectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        final Iterator<q> iterator = this.f.iterator();
        while (iterator.hasNext()) {
            final k c = iterator.next().c(p2, n);
            rectF.left = Math.min(rectF.left, c.a);
            rectF.top = Math.min(rectF.top, c.b);
            rectF.right = Math.max(rectF.right, c.a + c.f);
            rectF.bottom = Math.max(rectF.bottom, c.g + c.b);
        }
        return rectF;
    }
    
    public h.a b(final p p4, final int n, final int n2, final long n3) {
        for (final q q : this.f) {
            if (q.l() && q.d(q.k)) {
                final boolean b = false;
                final k c = q.c(p4, n3);
                boolean b2 = b;
                if (c.a < n) {
                    b2 = b;
                    if (c.b < n2) {
                        b2 = b;
                        if (c.f + c.a >= n) {
                            final float g = c.g;
                            b2 = b;
                            if (c.b + g >= n2) {
                                b2 = true;
                            }
                        }
                    }
                }
                q.b(b2);
            }
        }
        return null;
    }
    
    public void b() {
        final Iterator<q> iterator = this.f.iterator();
        while (iterator.hasNext()) {
            iterator.next().b();
        }
    }
    
    public void b(final p p3, final long n, final g g) {
        this.a(p3);
        for (int a = this.a(), i = 0; i < a; ++i) {
            final q q = this.f.get(i);
            if (!(q instanceof z)) {
                this.a(p3, q, n, g);
            }
        }
    }
    
    public long c() {
        return this.d;
    }
    
    public h.a c(final p p4, final int n, final int n2, final long n3) {
        for (final q q : this.f) {
            if (q.l() && q.d(q.k)) {
                final boolean b = false;
                final k c = q.c(p4, n3);
                boolean b2 = b;
                if (c.a < n) {
                    b2 = b;
                    if (c.b < n2) {
                        b2 = b;
                        if (c.f + c.a >= n) {
                            final float g = c.g;
                            b2 = b;
                            if (c.b + g >= n2) {
                                b2 = true;
                            }
                        }
                    }
                }
                q.c(b2);
            }
        }
        return null;
    }
    
    public void c(final p p3, final long n, final g g) {
        for (int i = this.a(); i < this.f.size(); ++i) {
            final q q = this.f.get(i);
            if (!(q instanceof z)) {
                this.a(p3, q, n, g);
            }
        }
        for (int j = 0; j < this.f.size(); ++j) {
            final q q2 = this.f.get(j);
            if (q2 instanceof z) {
                this.a(p3, q2, n, g);
                break;
            }
        }
    }
    
    public int d() {
        return this.c;
    }
    
    public h.a d(final p p4, final int n, final int n2, final long n3) {
        int n4;
        if (this.g.g() > 1) {
            n4 = -1;
        }
        else {
            n4 = 1;
        }
        int i;
        if (this.g.g() > 1) {
            i = this.f.size() - 1;
        }
        else {
            i = 0;
        }
        int size;
        if (this.g.g() > 1) {
            size = -1;
        }
        else {
            size = this.f.size();
        }
        while (i != size) {
            final q q = this.f.get(i);
            if (q.k() && this.a(q) && q.d(q.k)) {
                final k c = q.c(p4, n3);
                final h.a a = q.a(c.a < n && c.b < n2 && c.f + c.a >= n && c.b + c.g >= n2);
                if (a != null) {
                    return a;
                }
            }
            i += n4;
        }
        return null;
    }
    
    public List<q> e() {
        return this.f;
    }
    
    public b f() {
        return this.l;
    }
    
    public com.tremorvideo.sdk.android.richmedia.a g() {
        return this.g;
    }
    
    public int h() {
        return this.k;
    }
    
    public List<h.a> i() {
        return this.e.a;
    }
    
    public com.tremorvideo.sdk.android.richmedia.b.a j() {
        for (final q q : this.f) {
            if (q instanceof com.tremorvideo.sdk.android.richmedia.b.a) {
                return (com.tremorvideo.sdk.android.richmedia.b.a)q;
            }
        }
        return null;
    }
    
    public ArrayList<i> k() {
        final ArrayList<i> list = new ArrayList<i>(2);
        for (final q q : this.f) {
            if (q instanceof i) {
                list.add((i)q);
            }
        }
        return list;
    }
    
    public i l() {
        if (this.f != null) {
            for (final q q : this.f) {
                if (q instanceof i) {
                    return (i)q;
                }
            }
        }
        return null;
    }
    
    public long m() {
        return this.a.b;
    }
    
    public enum a
    {
        a, 
        b, 
        c;
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
    
    public class c
    {
        boolean a;
        long b;
        int c;
        boolean d;
        l e;
        int f;
        int g;
        ArrayList<aw> h;
        
        public c() {
            this.a = false;
            this.b = 0L;
            this.c = 1;
            this.d = true;
            this.e = null;
            this.f = -1;
            this.g = -1;
            this.h = new ArrayList<aw>();
        }
    }
    
    public enum d
    {
        a, 
        b, 
        c;
    }
}
