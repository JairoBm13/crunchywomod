// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.tremorvideo.sdk.android.e.aj;
import com.tremorvideo.sdk.android.e.ao;
import com.tremorvideo.sdk.android.e.an;
import com.tremorvideo.sdk.android.e.am;
import com.tremorvideo.sdk.android.e.al;
import com.tremorvideo.sdk.android.e.aq;
import com.tremorvideo.sdk.android.e.ap;
import com.tremorvideo.sdk.android.e.m;
import com.tremorvideo.sdk.android.e.l;
import com.tremorvideo.sdk.android.e.ai;
import com.tremorvideo.sdk.android.e.ah;
import com.tremorvideo.sdk.android.e.ag;
import com.tremorvideo.sdk.android.e.au;
import com.tremorvideo.sdk.android.e.at;
import com.tremorvideo.sdk.android.e.ar;
import com.tremorvideo.sdk.android.e.as;
import com.tremorvideo.sdk.android.e.k;
import com.tremorvideo.sdk.android.e.j;
import com.tremorvideo.sdk.android.e.i;
import com.tremorvideo.sdk.android.e.o;
import com.tremorvideo.sdk.android.e.n;
import com.tremorvideo.sdk.android.e.r;
import com.tremorvideo.sdk.android.e.q;
import com.tremorvideo.sdk.android.e.p;
import com.tremorvideo.sdk.android.e.aw;
import com.tremorvideo.sdk.android.e.av;
import com.tremorvideo.sdk.android.e.ak;
import com.tremorvideo.sdk.android.e.h;
import com.tremorvideo.sdk.android.e.f;
import com.tremorvideo.sdk.android.e.d;
import com.tremorvideo.sdk.android.e.g;
import com.tremorvideo.sdk.android.e.e;
import com.tremorvideo.sdk.android.e.c;
import com.tremorvideo.sdk.android.e.b;
import com.tremorvideo.sdk.android.e.v;
import com.tremorvideo.sdk.android.e.y;
import com.tremorvideo.sdk.android.e.t;
import com.tremorvideo.sdk.android.e.aa;
import com.tremorvideo.sdk.android.e.ab;
import com.tremorvideo.sdk.android.e.af;
import com.tremorvideo.sdk.android.e.x;
import com.tremorvideo.sdk.android.e.ad;
import com.tremorvideo.sdk.android.e.w;
import com.tremorvideo.sdk.android.e.z;
import com.tremorvideo.sdk.android.e.u;
import com.tremorvideo.sdk.android.e.ae;
import android.graphics.Color;
import android.graphics.BitmapFactory$Options;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.util.Enumeration;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import android.graphics.BitmapFactory;
import android.os.Build$VERSION;
import java.io.File;
import java.util.Hashtable;
import android.graphics.Bitmap;
import java.util.Dictionary;

public class bw
{
    int[] a;
    Dictionary<String, Bitmap> b;
    
    public bw() {
        this.b = new Hashtable<String, Bitmap>();
        this.a = new int[c.values().length];
    }
    
    public static Bitmap a(final File file) {
        if (Integer.parseInt(Build$VERSION.SDK) < 4) {
            Bitmap bitmap2;
            final Bitmap bitmap = bitmap2 = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (ac.M() < 1.0f) {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, Math.max(1, Math.round(bitmap.getWidth() * ac.M())), Math.max(1, Math.round(bitmap.getHeight() * ac.M())), true);
            }
            return bitmap2;
        }
        return new a().a(file);
    }
    
    private static Bitmap a(final ZipEntry zipEntry, final ZipFile zipFile) {
        if (zipEntry != null) {
            try {
                final InputStream inputStream = zipFile.getInputStream(zipEntry);
                final int n = (int)zipEntry.getSize();
                final byte[] array = new byte[n];
                int n2 = 0;
                while ((n2 += inputStream.read(array, n2, n - n2)) < n) {}
                return a(array);
            }
            catch (Exception ex) {
                ac.a("Error loading theme image: " + zipEntry.getName(), ex);
            }
        }
        return null;
    }
    
    public static Bitmap a(final byte[] array) {
        return a(array, false);
    }
    
    public static Bitmap a(final byte[] array, final boolean b) {
        if (array == null) {
            return null;
        }
        return new b().a(array);
    }
    
    private boolean b(final ZipFile zipFile) {
        boolean b = false;
        try {
            final ZipEntry entry = zipFile.getEntry("theme.json");
            if (entry != null) {
                final byte[] array = new byte[(int)entry.getSize()];
                zipFile.getInputStream(entry).read(array);
                final JSONObject jsonObject = new JSONObject(new String(array));
                for (int i = 0; i < c.values().length; ++i) {
                    this.a[i] = jsonObject.getInt(c.values()[i].l);
                }
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            ac.a("Error loading theme.json.", ex);
            return false;
        }
    }
    
    public int a(final c c) {
        return this.a[c.ordinal()];
    }
    
    public Bitmap a(final d d) {
        if (d == null) {
            return null;
        }
        return this.b.get(d.au);
    }
    
    public Bitmap a(final String s) {
        return this.b.get(s);
    }
    
    public void a() {
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= d.values().length) {
                break;
            }
            final d d = bw.d.values()[n2];
            this.b.put(d.au, a(d.at, d.av));
            ++n2;
        }
        while (i < c.values().length) {
            this.a[i] = c.values()[i].k;
            ++i;
        }
    }
    
    public void a(final ZipFile zipFile) {
        final int n = 0;
        for (int i = 0; i < c.values().length; ++i) {
            this.a[i] = c.values()[i].k;
        }
        if (!this.b(zipFile)) {
            this.a();
        }
        else {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            int j;
            while (true) {
                j = n;
                if (!entries.hasMoreElements()) {
                    break;
                }
                final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                final String lowerCase = zipEntry.getName().toLowerCase();
                if (!lowerCase.endsWith(".png")) {
                    continue;
                }
                final String replace = lowerCase.replace(".png", "");
                final Bitmap a = a(zipEntry, zipFile);
                if (a == null) {
                    continue;
                }
                this.b.put(replace, a);
            }
            while (j < d.values().length) {
                final d d = bw.d.values()[j];
                if (this.b.get(d.au) == null) {
                    this.b.put(d.au, a(d.at, d.av));
                }
                ++j;
            }
        }
    }
    
    public void b() {
        final Enumeration<String> keys = this.b.keys();
        while (keys.hasMoreElements()) {
            final Bitmap bitmap = this.b.get(keys.nextElement());
            if (ac.r() <= 10) {
                bitmap.recycle();
            }
        }
    }
    
    class a
    {
        public Bitmap a(final File file) {
            ((WindowManager)ac.x().getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            bitmapFactory$Options.inPurgeable = true;
            Bitmap bitmap2;
            final Bitmap bitmap = bitmap2 = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapFactory$Options);
            if (ac.M() < 1.0f) {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, Math.max(1, Math.round(bitmap.getWidth() * ac.M())), Math.max(1, Math.round(bitmap.getHeight() * ac.M())), true);
            }
            return bitmap2;
        }
    }
    
    class b
    {
        final /* synthetic */ boolean a;
        
        b(final boolean a) {
            this.a = a;
        }
        
        public Bitmap a(final byte[] array) {
            ((WindowManager)ac.x().getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            bitmapFactory$Options.inPurgeable = true;
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
            if (this.a) {
                return Bitmap.createScaledBitmap(decodeByteArray, Math.max(1, Math.round(decodeByteArray.getWidth() * (ac.M() / 2.0f))), Math.max(1, Math.round(decodeByteArray.getHeight() * (ac.M() / 2.0f))), true);
            }
            return Bitmap.createScaledBitmap(decodeByteArray, Math.max(1, Math.round(decodeByteArray.getWidth() * ac.M())), Math.max(1, Math.round(decodeByteArray.getHeight() * ac.M())), true);
        }
    }
    
    public enum c
    {
        a(-1, "button_text_color"), 
        b(Color.argb(150, 0, 0, 0), "button_text_shadow_color"), 
        c(-1, "watermark_text_color"), 
        d(-16777216, "input_text_color"), 
        e(-7829368, "input_hint_text_color"), 
        f(Color.argb(153, 0, 0, 0), "dialog_color"), 
        g(-1, "dialog_title_color"), 
        h(Color.argb(153, 0, 0, 0), "dialog_title_shadow_color"), 
        i(-7829368, "dialog_text_color"), 
        j(-16777216, "dialog_button_text_color");
        
        public final int k;
        public final String l;
        
        private c(final int k, final String l) {
            this.k = k;
            this.l = l;
        }
    }
    
    public enum d
    {
        A(com.tremorvideo.sdk.android.e.v.a, "icon-coupon"), 
        B(com.tremorvideo.sdk.android.e.v.a, "icon-coupon-small-size"), 
        C(com.tremorvideo.sdk.android.e.b.a, "buttonbar-divider"), 
        D(com.tremorvideo.sdk.android.e.c.a, "buttonbar-left"), 
        E(com.tremorvideo.sdk.android.e.e.a, "buttonbar-middle"), 
        F(com.tremorvideo.sdk.android.e.g.a, "buttonbar-right"), 
        G(com.tremorvideo.sdk.android.e.d.a, "buttonbar-left-press"), 
        H(com.tremorvideo.sdk.android.e.f.a, "buttonbar-middle-press"), 
        I(com.tremorvideo.sdk.android.e.h.a, "buttonbar-right-press"), 
        J(com.tremorvideo.sdk.android.e.b.a, "buttonbar-divider-small-size"), 
        K(com.tremorvideo.sdk.android.e.c.a, "buttonbar-left-small-size"), 
        L(com.tremorvideo.sdk.android.e.e.a, "buttonbar-middle-small-size"), 
        M(com.tremorvideo.sdk.android.e.g.a, "buttonbar-right-small-size"), 
        N(com.tremorvideo.sdk.android.e.d.a, "buttonbar-left-press-small-size"), 
        O(com.tremorvideo.sdk.android.e.f.a, "buttonbar-middle-press-small-size"), 
        P(com.tremorvideo.sdk.android.e.h.a, "buttonbar-right-press-small-size"), 
        Q(com.tremorvideo.sdk.android.e.ak.a, "replay-big"), 
        R(av.a, "watermark-left"), 
        S(av.a, "watermark-left-small-size"), 
        T(com.tremorvideo.sdk.android.e.aw.a, "watermark-middle"), 
        U(com.tremorvideo.sdk.android.e.aw.a, "watermark-middle-small-size"), 
        V(com.tremorvideo.sdk.android.e.p.a, "dialog-top-left"), 
        W(com.tremorvideo.sdk.android.e.q.a, "dialog-top-middle"), 
        X(com.tremorvideo.sdk.android.e.r.a, "dialog-top-right"), 
        Y(com.tremorvideo.sdk.android.e.n.a, "dialog-middle-left"), 
        Z(com.tremorvideo.sdk.android.e.o.a, "dialog-middle-right"), 
        a(com.tremorvideo.sdk.android.e.ae.a, "icon-web"), 
        aa(com.tremorvideo.sdk.android.e.i.a, "dialog-bottom-left"), 
        ab(com.tremorvideo.sdk.android.e.j.a, "dialog-bottom-middle"), 
        ac(com.tremorvideo.sdk.android.e.k.a, "dialog-bottom-right"), 
        ad(com.tremorvideo.sdk.android.e.as.a, "twitter-left-top"), 
        ae(com.tremorvideo.sdk.android.e.ar.a, "twitter-left-bottom"), 
        af(at.a, "twitter-middle"), 
        ag(au.a, "twitter-right"), 
        ah(com.tremorvideo.sdk.android.e.ag.a, "input-left"), 
        ai(com.tremorvideo.sdk.android.e.ah.a, "input-middle"), 
        aj(com.tremorvideo.sdk.android.e.ai.a, "input-right"), 
        ak(com.tremorvideo.sdk.android.e.l.a, "dialog-button"), 
        al(com.tremorvideo.sdk.android.e.m.a, "dialog-button-press"), 
        am(com.tremorvideo.sdk.android.e.ap.a, "survey-left"), 
        an(com.tremorvideo.sdk.android.e.aq.a, "survey-middle"), 
        ao(com.tremorvideo.sdk.android.e.al.a, "survey-button-left"), 
        ap(com.tremorvideo.sdk.android.e.am.a, "survey-button-middle"), 
        aq(com.tremorvideo.sdk.android.e.an.a, "survey-button-press-left"), 
        ar(com.tremorvideo.sdk.android.e.ao.a, "survey-button-press-middle"), 
        as(com.tremorvideo.sdk.android.e.aj.a, "internal-survey-skip"), 
        b(com.tremorvideo.sdk.android.e.ae.a, "icon-web-small-size"), 
        c(com.tremorvideo.sdk.android.e.u.a, "icon-call"), 
        d(com.tremorvideo.sdk.android.e.u.a, "icon-call-small-size"), 
        e(com.tremorvideo.sdk.android.e.z.a, "icon-mp3store"), 
        f(com.tremorvideo.sdk.android.e.z.a, "icon-mp3store-small-size"), 
        g(com.tremorvideo.sdk.android.e.w.a, "icon-market"), 
        h(com.tremorvideo.sdk.android.e.w.a, "icon-market-small-size"), 
        i(com.tremorvideo.sdk.android.e.ad.a, "icon-twitter"), 
        j(com.tremorvideo.sdk.android.e.ad.a, "icon-twitter-small-size"), 
        k(com.tremorvideo.sdk.android.e.x.a, "icon-facebook"), 
        l(com.tremorvideo.sdk.android.e.x.a, "icon-facebook-small-size"), 
        m(com.tremorvideo.sdk.android.e.ac.a, "icon-ticket"), 
        n(com.tremorvideo.sdk.android.e.ac.a, "icon-ticket-small-size"), 
        o(com.tremorvideo.sdk.android.e.af.a, "icon-youtube"), 
        p(com.tremorvideo.sdk.android.e.af.a, "icon-youtube-small-size"), 
        q(com.tremorvideo.sdk.android.e.ab.a, "icon-skip"), 
        r(com.tremorvideo.sdk.android.e.ab.a, "icon-skip-small-size"), 
        s(com.tremorvideo.sdk.android.e.w.a, "icon-survey"), 
        t(com.tremorvideo.sdk.android.e.w.a, "icon-survey-small-size"), 
        u(com.tremorvideo.sdk.android.e.aa.a, "icon-share"), 
        v(com.tremorvideo.sdk.android.e.aa.a, "icon-share-small-size"), 
        w(com.tremorvideo.sdk.android.e.t.a, "icon-calendar"), 
        x(com.tremorvideo.sdk.android.e.t.a, "icon-calendar-small-size"), 
        y(com.tremorvideo.sdk.android.e.y.a, "icon-map"), 
        z(com.tremorvideo.sdk.android.e.y.a, "icon-map-small-size");
        
        public final byte[] at;
        public final String au;
        public boolean av;
        
        private d(final byte[] at, final String au) {
            this.av = false;
            this.at = at;
            this.au = au;
            if (au.indexOf("-small-size") != -1) {
                this.av = true;
            }
        }
    }
}
