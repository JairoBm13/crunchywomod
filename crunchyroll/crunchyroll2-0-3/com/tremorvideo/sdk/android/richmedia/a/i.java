// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import java.io.FilterOutputStream;
import java.io.FilterInputStream;
import com.tremorvideo.sdk.android.richmedia.j;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import com.tremorvideo.sdk.android.richmedia.b;
import java.util.Enumeration;
import com.tremorvideo.sdk.android.richmedia.ae;
import com.tremorvideo.sdk.android.richmedia.k;
import com.tremorvideo.sdk.android.richmedia.p;
import com.tremorvideo.sdk.android.richmedia.m;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.richmedia.e;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;
import com.tremorvideo.sdk.android.richmedia.o;
import android.widget.RelativeLayout$LayoutParams;
import com.tremorvideo.sdk.android.richmedia.q;

public class i extends q
{
    private RelativeLayout$LayoutParams o;
    private boolean p;
    private String q;
    private String r;
    private String s;
    private String t;
    private String u;
    
    public i(final o o) {
        super(o);
        this.p = true;
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
    }
    
    private void a(final File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Can not create dir " + file);
        }
    }
    
    private void a(ZipFile zipFile, ZipEntry zipEntry, final String s, final boolean b) throws IOException {
        if (zipEntry.isDirectory()) {
            this.a(new File(s, zipEntry.getName()));
        }
        else {
            final File file = new File(s, zipEntry.getName());
            if (b || !file.exists()) {
                if (!file.getParentFile().exists()) {
                    this.a(file.getParentFile());
                }
                ac.e("GenericObjec extracting: " + zipEntry);
                zipFile = (ZipFile)new BufferedInputStream(zipFile.getInputStream(zipEntry));
                zipEntry = (ZipEntry)new BufferedOutputStream(new FileOutputStream(file));
                try {
                    final byte[] array = new byte[1024];
                    while (true) {
                        final int read = ((FilterInputStream)zipFile).read(array);
                        if (read == -1) {
                            break;
                        }
                        ((BufferedOutputStream)zipEntry).write(array, 0, read);
                    }
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
                finally {
                    ((FilterOutputStream)zipEntry).close();
                    ((BufferedInputStream)zipFile).close();
                }
            }
        }
    }
    
    private void b(final File file) {
        if (file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                this.b(listFiles[i]);
            }
        }
        file.delete();
    }
    
    @Override
    public void a() {
    }
    
    @Override
    public void a(final e e) {
        super.a(e);
        while (true) {
            try {
                final String c = e.c();
                if (c.length() > 0) {
                    final JSONObject jsonObject = new JSONObject(c);
                    if (jsonObject.has("zip_path") && jsonObject.has("index_file")) {
                        this.q = jsonObject.getString("zip_path") + jsonObject.getString("index_file");
                        this.r = this.q.substring(0, this.q.indexOf("/") + 1);
                        this.g.g().a(this.r);
                    }
                    if (jsonObject.has("params")) {
                        this.u = jsonObject.getString("params");
                    }
                }
                this.h = false;
                this.i = false;
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    @Override
    public void a(final m m, final long n) {
    }
    
    public void a(final p p2, final int n) {
        final k b = this.b(p2, 0L);
        final int round = Math.round(p2.d() / 2.0f + b.a + this.a);
        final int round2 = Math.round(b.b + this.b + p2.e() / 2.0f);
        final RelativeLayout$LayoutParams o = new RelativeLayout$LayoutParams(Math.round(b.f), Math.round(b.g));
        o.leftMargin = round;
        o.topMargin = round2;
        final String string = "file://" + this.t + "/" + this.q;
        ac.e("localWebURL=" + string);
        ac.e("_pageParams=" + this.u);
        this.g.g().a(string, this.u, n);
        this.g.g().a(o, n);
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
                this.k = a2.k;
                if (a2.k) {
                    final k b = this.b(p2, n);
                    final int round = Math.round(p2.d() / 2.0f + b.a + this.a);
                    final int round2 = Math.round(b.b + this.b + p2.e() / 2.0f);
                    final RelativeLayout$LayoutParams o = new RelativeLayout$LayoutParams(Math.round(b.f), Math.round(b.g));
                    o.leftMargin = round;
                    o.topMargin = round2;
                    this.p = true;
                    this.g.g().a(o, this.h());
                    this.o = o;
                    this.g.g().a(ae.a(a2.i, a3.i, a2.j, a3.j, c), this.h());
                    return;
                }
                if (this.p) {
                    this.g.g().c(this.h());
                }
                this.p = false;
            }
        }
    }
    
    public void a(final ZipFile zipFile) {
        this.t = zipFile.getName();
        if (this.t.contains(".zip")) {
            this.t = this.t.replace(".zip", "");
        }
        try {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                if (zipEntry.getName().startsWith(this.r)) {
                    this.a(zipFile, zipEntry, this.t, true);
                }
            }
        }
        catch (Exception ex) {
            ac.a(ex);
        }
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
    
    @Override
    public void b() {
        super.b();
        this.p = true;
    }
    
    protected a c() {
        return new a();
    }
    
    public void d() {
        this.b(new File(this.t + "/" + this.r.substring(0, this.r.indexOf("/"))));
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
        public float i;
        public int j;
        public boolean k;
        
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
                this.i = e.d();
                this.j = e.b();
                this.k = e.f();
            }
            catch (Exception ex) {}
        }
    }
}
