// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.util.Iterator;
import java.util.zip.ZipEntry;
import com.tremorvideo.sdk.android.videoad.bw;
import java.util.ArrayList;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.zip.ZipFile;
import android.widget.RelativeLayout$LayoutParams;
import com.tremorvideo.sdk.android.richmedia.a.e;
import com.tremorvideo.sdk.android.richmedia.b.b;
import com.tremorvideo.sdk.android.videoad.ax;
import java.util.GregorianCalendar;
import android.graphics.Bitmap;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.r;

public class a
{
    public r.b a;
    public boolean b;
    private c c;
    private List<o> d;
    private List<String> e;
    private y f;
    private int g;
    private int h;
    private boolean i;
    private int j;
    private int k;
    private String l;
    private int m;
    private Bitmap n;
    private ad[] o;
    private GregorianCalendar p;
    private String q;
    private ax r;
    private String s;
    private String t;
    private String u;
    private boolean v;
    private boolean w;
    private b x;
    private e y;
    
    public a() {
        this.c = new c();
        this.v = false;
        this.b = true;
        this.w = true;
    }
    
    public String a() {
        return this.t;
    }
    
    public String a(final int n) {
        return this.e.get(n);
    }
    
    public void a(final float n) {
        if (this.x != null) {
            this.x.a(n);
        }
    }
    
    public void a(final float n, final int n2) {
        if (this.y != null) {
            this.y.a(n, n2);
        }
    }
    
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams) {
        synchronized (this) {
            if (this.x != null) {
                this.x.a(relativeLayout$LayoutParams);
            }
        }
    }
    
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams, final int n) {
        synchronized (this) {
            if (this.y != null) {
                this.y.a(relativeLayout$LayoutParams, n);
            }
        }
    }
    
    public void a(final e y) {
        this.y = y;
    }
    
    public void a(final b x) {
        this.x = x;
    }
    
    public void a(final ax r) {
        this.r = r;
    }
    
    public void a(final String s) {
        this.s = s;
    }
    
    public void a(final String s, final String s2) {
        if (this.x != null) {
            this.x.a(s, s2);
        }
    }
    
    public void a(final String s, final String s2, final int n) {
        if (this.y != null) {
            this.y.a(s, s2, n);
        }
    }
    
    public void a(final GregorianCalendar p) {
        this.p = p;
    }
    
    public void a(final ZipFile zipFile, final i i, final r.b a, final boolean b) throws Exception {
        final ZipEntry entry = zipFile.getEntry("data");
        final ZipEntry entry2 = zipFile.getEntry("code-android.js");
        if (zipFile.getEntry("compatibility") != null) {
            this.w = false;
        }
        if (entry2 == null) {
            this.l = "";
        }
        else {
            this.l = ac.a(zipFile.getInputStream(entry2));
        }
        final com.tremorvideo.sdk.android.richmedia.e e = new com.tremorvideo.sdk.android.richmedia.e(zipFile.getInputStream(entry));
        switch (this.m = e.a()) {
            default: {
                throw new Exception("Incompatible Version.");
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                this.i = (e.b() != 0);
                e.b();
                this.j = e.b();
                this.k = e.b();
                this.g = e.a();
                this.h = e.a();
                this.b = b;
                final int b2 = e.b();
                this.e = new ArrayList<String>(b2);
                for (int j = 0; j < b2; ++j) {
                    String s2;
                    final String s = s2 = e.c();
                    if (!this.b) {
                        if (s.contains("Ads by Tremor Video:")) {
                            s2 = s.replace("Ads by Tremor Video:", "");
                        }
                        else {
                            s2 = s;
                            if (s.contains("Ads by Tremor Video")) {
                                s2 = s.replace("Ads by Tremor Video", "");
                            }
                        }
                    }
                    this.e.add(s2);
                }
                this.c.a(zipFile, i, i.a(this.g, this.h), e);
                final int b3 = e.b();
                this.d = new ArrayList<o>(b3);
                for (int k = 0; k < b3; ++k) {
                    final o o = new o(this);
                    if (!o.a(e, zipFile)) {
                        this.d.clear();
                        break;
                    }
                    this.d.add(o);
                }
                this.f = new y(this.d.get(0));
                this.n = bw.a(a.a);
                this.a = a;
                if (this.s != null) {
                    this.t = ae.a(zipFile, this.s);
                }
                this.u = ae.a(zipFile, "comps");
            }
        }
    }
    
    public void a(final ad[] o) {
        this.o = o;
    }
    
    public o b(final int n) {
        return this.d.get(n);
    }
    
    public String b() {
        return this.u;
    }
    
    public void b(final String q) {
        this.q = q;
    }
    
    public ax c() {
        return this.r;
    }
    
    public void c(final int n) {
        synchronized (this) {
            if (this.y != null) {
                this.y.a(n);
            }
        }
    }
    
    public Bitmap d() {
        return this.n;
    }
    
    public boolean e() {
        return this.w;
    }
    
    public void f() {
        this.c.b();
    }
    
    public int g() {
        return this.m;
    }
    
    public String h() {
        return this.l;
    }
    
    public boolean i() {
        return this.i;
    }
    
    public o j() {
        if (this.j == 255) {
            return null;
        }
        return this.d.get(this.j);
    }
    
    public o k() {
        if (this.k == 255) {
            return null;
        }
        return this.d.get(this.k);
    }
    
    public int l() {
        return this.g;
    }
    
    public int m() {
        return this.h;
    }
    
    public y n() {
        return this.f;
    }
    
    public ad[] o() {
        return this.o;
    }
    
    public String p() {
        return this.q;
    }
    
    public GregorianCalendar q() {
        return this.p;
    }
    
    public c r() {
        return this.c;
    }
    
    public void s() {
        this.v = true;
    }
    
    public boolean t() {
        return this.v;
    }
    
    public void u() {
        synchronized (this) {
            if (this.x != null) {
                this.x.b();
            }
        }
    }
    
    public void v() {
        if (this.x != null) {
            this.x.a();
        }
    }
    
    public void w() {
        if (this.y != null) {
            this.y.a();
        }
    }
    
    public void x() {
        final Iterator<o> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            final com.tremorvideo.sdk.android.richmedia.b.a j = iterator.next().j();
            if (j != null) {
                j.d();
            }
        }
    }
    
    public void y() {
        final Iterator<o> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            final com.tremorvideo.sdk.android.richmedia.a.i l = iterator.next().l();
            if (l != null) {
                l.d();
            }
        }
    }
}
