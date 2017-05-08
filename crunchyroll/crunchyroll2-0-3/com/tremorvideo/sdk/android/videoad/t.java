// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.Context;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;
import java.io.File;
import org.json.JSONObject;

public class t extends n
{
    protected int A;
    protected boolean B;
    protected String C;
    protected long D;
    protected String E;
    protected int F;
    protected int G;
    protected a H;
    protected String I;
    protected long J;
    protected String K;
    protected String L;
    protected String M;
    protected String N;
    protected bz O;
    protected boolean P;
    protected boolean Q;
    protected boolean R;
    protected b S;
    protected int y;
    protected boolean z;
    
    protected t(final int a, final boolean q) throws Exception {
        this.R = false;
        this.S = null;
        this.Q = q;
        if (a > 0) {
            this.z = true;
            this.A = a;
        }
        else {
            this.z = false;
            this.A = 0;
        }
        this.d = new bw();
    }
    
    protected t(final bs bs, final JSONObject jsonObject) throws Exception {
        super(bs, jsonObject, false);
        this.R = false;
        this.S = null;
    }
    
    public t(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        boolean boolean1 = false;
        super(bs, jsonObject, b);
        this.R = false;
        this.S = null;
        this.d = new bw();
        this.y = jsonObject.getInt("priority");
        this.z = jsonObject.getBoolean("skip");
        int int1;
        if (jsonObject.has("skip-delay")) {
            int1 = jsonObject.getInt("skip-delay");
        }
        else {
            int1 = 0;
        }
        this.A = int1;
        if (jsonObject.has("is-select-view")) {
            boolean1 = jsonObject.getBoolean("is-select-view");
        }
        this.B = boolean1;
        this.G = jsonObject.getInt("duration");
        this.C = jsonObject.getString("url");
        this.E = jsonObject.getString("format");
        this.F = jsonObject.getInt("ad-size");
        this.H = a.a(jsonObject.getString("placement"));
        if (jsonObject.has("video-crc-32")) {
            this.D = jsonObject.getLong("video-crc-32");
        }
        else if (jsonObject.has("video-crc32")) {
            this.D = jsonObject.getLong("video-crc32");
        }
        if (jsonObject.has("theme-crc-32")) {
            this.J = jsonObject.getLong("theme-crc-32");
        }
        else if (jsonObject.has("theme-crc32")) {
            this.J = jsonObject.getLong("theme-crc32");
        }
        if (jsonObject.has("watermark")) {
            this.Q = jsonObject.getBoolean("watermark");
        }
        else {
            this.Q = true;
        }
        if (jsonObject.has("twitter-feed") && b) {
            this.d(jsonObject.getString("twitter-feed"));
        }
        if (jsonObject.has("theme-url")) {
            this.I = jsonObject.getString("theme-url");
        }
        else {
            this.I = null;
        }
        if (jsonObject.has("html5")) {
            this.S = new b(jsonObject.getJSONObject("html5"));
        }
        this.a(jsonObject);
    }
    
    private void d(final String s) {
        (this.O = new bz(this)).a(s, (bz.c)new bz.c() {
            @Override
            public void a(final bz bz, final boolean p2) {
                t.this.P = p2;
            }
        });
    }
    
    private void e(final String s) {
        try {
            final File file = new File(s);
            if (!file.exists()) {
                ac.a(ac.c.b, "Can't find theme: " + s + ", using default...");
                return;
            }
            this.d.a(new ZipFile(file));
        }
        catch (Exception ex) {
            ac.a(ex);
            this.d.a();
        }
    }
    
    @Override
    public int A() {
        return this.G * 1000;
    }
    
    public int G() {
        return this.G;
    }
    
    public boolean H() {
        return this.P;
    }
    
    public bz I() {
        return this.O;
    }
    
    public boolean J() {
        return this.K != null;
    }
    
    public String K() {
        return this.K;
    }
    
    public String L() {
        return this.L;
    }
    
    public String M() {
        return this.M;
    }
    
    public boolean N() {
        return this.Q;
    }
    
    public boolean O() {
        return this.S == null && this.z;
    }
    
    public boolean P() {
        return this.z;
    }
    
    public int Q() {
        return this.A * 1000;
    }
    
    public boolean R() {
        return this.T() != null;
    }
    
    public List<aw> S() {
        final ArrayList<aw> list = new ArrayList<aw>();
        if (this.S == null) {
            for (final aw aw : this.b) {
                if (aw.l()) {
                    list.add(aw);
                }
            }
        }
        return list;
    }
    
    public aw T() {
        if (this.z) {
            for (final aw aw : this.b) {
                if (aw.a() == aw.b.w) {
                    return aw;
                }
            }
        }
        return null;
    }
    
    public b U() {
        return this.S;
    }
    
    public boolean V() {
        return this.z && this.B;
    }
    
    @Override
    public String a(final int n) {
        return this.C;
    }
    
    @Override
    public List<n.a> a() {
        final ArrayList<n.a> list = new ArrayList<n.a>();
        if (!this.r) {
            final HashMap<String, t> hashMap = new HashMap<String, t>(4);
            hashMap.put("ad", this);
            hashMap.put("url", (t)this.C);
            hashMap.put("crc", (t)this.D);
            hashMap.put("index", (t)0);
            list.add(new n.a(bf.d.b, "video", (Map<String, Object>)hashMap));
        }
        if (this.c != null) {
            final HashMap<String, Boolean> hashMap2 = new HashMap<String, Boolean>(2);
            hashMap2.put("url", (Boolean)this.c.c());
            hashMap2.put("crc", (Boolean)(Object)this.c.d());
            hashMap2.put("checkCache", false);
            list.add(new n.a(bf.d.a, "coupon", (Map<String, Object>)hashMap2));
        }
        if (this.I != null) {
            final HashMap<String, Long> hashMap3 = new HashMap<String, Long>(2);
            hashMap3.put("url", (Long)this.I);
            hashMap3.put("crc", this.J);
            list.add(new n.a(bf.d.a, "theme", (Map<String, Object>)hashMap3));
        }
        if (this.e != null) {
            list.add(new n.a(this, bf.d.c, "survey"));
        }
        if (this.g != null) {
            final HashMap<String, Boolean> hashMap4 = new HashMap<String, Boolean>(2);
            hashMap4.put("url", (Boolean)this.g.b());
            hashMap4.put("checkCache", false);
            list.add(new n.a(bf.d.a, "buyItNowDealsXml", (Map<String, Object>)hashMap4));
            final HashMap<String, Boolean> hashMap5 = new HashMap<String, Boolean>(3);
            hashMap5.put("url", (Boolean)this.g.c());
            hashMap5.put("crc", (Boolean)(Object)this.g.d());
            hashMap5.put("checkCache", true);
            list.add(new n.a(bf.d.a, "buyItNowTemplate", (Map<String, Object>)hashMap5));
            list.add(new n.a(this, bf.d.f, "processBIN"));
        }
        if (this.h != null) {
            final HashMap<String, Boolean> hashMap6 = new HashMap<String, Boolean>(3);
            hashMap6.put("url", (Boolean)this.h.c());
            hashMap6.put("crc", (Boolean)(Object)this.h.d());
            hashMap6.put("checkCache", true);
            list.add(new n.a(bf.d.a, "movieBoardTemplate", (Map<String, Object>)hashMap6));
            list.add(new n.a(this, bf.d.h, "processMovieBoard"));
        }
        if (this.p != null) {
            list.add(new n.a(this, bf.d.i, "adChoices"));
        }
        return list;
    }
    
    @Override
    public void a(final Context context) {
        super.a(context);
        if (this.N == null) {
            this.d.a();
            return;
        }
        this.e(this.N);
    }
    
    @Override
    public void a(final String s, final Object o) throws Exception {
        if (s.equals("theme")) {
            this.N = (String)o;
        }
        super.a(s, o);
    }
    
    @Override
    public int b(final int n) {
        return this.F;
    }
    
    @Override
    public String c(final int n) {
        return this.E;
    }
    
    @Override
    public int d(final int n) {
        if (n != 0 || this.r) {
            return 0;
        }
        return this.F;
    }
    
    @Override
    public long f(final int n) {
        return this.D;
    }
    
    @Override
    public String i() {
        return com.tremorvideo.sdk.android.videoad.n.b(this.C);
    }
    
    @Override
    public int j() {
        int n;
        if (this.r) {
            n = 0;
        }
        else {
            n = 1;
        }
        int n2 = n;
        if (this.I != null) {
            n2 = n + 1;
        }
        int n3 = n2;
        if (this.c != null) {
            n3 = n2 + 1;
        }
        return n3;
    }
    
    @Override
    public String[] k() {
        final ArrayList<String> list = new ArrayList<String>();
        if (!this.r) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.C));
        }
        if (this.I != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.C) + ".theme");
        }
        if (this.c != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.c.c()));
        }
        if (this.e != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.e.c()));
        }
        return list.toArray(new String[list.size()]);
    }
    
    @Override
    public bw q() {
        return this.d;
    }
    
    public enum a
    {
        a("default"), 
        b("appstart"), 
        c("preroll");
        
        private String d;
        
        private a(final String d) {
            this.d = d;
        }
        
        public static a a(final String s) {
            final a[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final a a = values[i];
                if (a.toString().equals(s)) {
                    return a;
                }
            }
            return a.a;
        }
        
        @Override
        public String toString() {
            return this.d;
        }
    }
    
    public static class b
    {
        private String a;
        private int b;
        private int c;
        private int d;
        private int e;
        private c f;
        private int g;
        private boolean h;
        
        public b(final JSONObject jsonObject) throws Exception {
            this.a = jsonObject.getString("url");
            this.d = jsonObject.getInt("layout-width");
            this.e = jsonObject.getInt("layout-height");
            this.f = c.values()[jsonObject.getInt("layout")];
            this.g = (int)(Object)Long.decode(jsonObject.getString("background-color"));
            this.h = jsonObject.getBoolean("opaque");
            if (jsonObject.has("layout-x")) {
                this.b = jsonObject.getInt("layout-x");
            }
            if (jsonObject.has("layout-y")) {
                this.c = jsonObject.getInt("layout-y");
            }
        }
        
        public int a() {
            if (this.f.b()) {
                return this.d;
            }
            return 0;
        }
        
        public String b() {
            return this.a;
        }
        
        public int c() {
            return this.b;
        }
        
        public int d() {
            return this.c;
        }
        
        public int e() {
            return this.d;
        }
        
        public int f() {
            return this.e;
        }
        
        public c g() {
            return this.f;
        }
    }
    
    public enum c
    {
        a("top"), 
        b("bottom"), 
        c("left"), 
        d("right"), 
        e("absolute");
        
        private String f;
        
        private c(final String f) {
            this.f = f;
        }
        
        public boolean a() {
            return this.ordinal() == c.a.ordinal() || this.ordinal() == c.b.ordinal();
        }
        
        public boolean b() {
            return this.ordinal() == c.c.ordinal() || this.ordinal() == c.d.ordinal();
        }
        
        @Override
        public String toString() {
            return this.f;
        }
    }
}
