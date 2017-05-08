// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.tremorvideo.sdk.android.richmedia.i;
import java.util.zip.ZipFile;
import java.io.File;
import android.content.Context;
import java.util.Map;
import com.tremorvideo.sdk.android.richmedia.ae;
import com.tremorvideo.sdk.android.richmedia.l;
import java.util.Iterator;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;
import com.tremorvideo.sdk.android.richmedia.a.f;
import com.tremorvideo.sdk.android.richmedia.b.c;
import com.tremorvideo.sdk.android.richmedia.a;

public class r extends n
{
    private boolean A;
    private String B;
    private b C;
    private com.tremorvideo.sdk.android.richmedia.a D;
    private int E;
    protected com.tremorvideo.sdk.android.richmedia.b.c t;
    protected f u;
    protected boolean v;
    private String w;
    private long x;
    private List<c> y;
    private int z;
    
    public r(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        int i = 0;
        super(bs, jsonObject, b);
        this.x = 0L;
        this.y = new ArrayList<c>();
        this.A = false;
        this.E = -1;
        this.w = jsonObject.getString("asset-url");
        if (jsonObject.has("skip")) {
            this.A = Boolean.parseBoolean(jsonObject.getString("skip"));
        }
        if (jsonObject.has("skip-delay")) {
            this.z = jsonObject.getInt("skip-delay");
        }
        else {
            this.z = 0;
        }
        if (jsonObject.has("asset-crc-32")) {
            this.x = jsonObject.getLong("asset-crc-32");
        }
        else if (jsonObject.has("asset-crc32")) {
            this.x = jsonObject.getLong("asset-crc32");
        }
        for (JSONArray jsonArray = jsonObject.getJSONArray("video"); i < jsonArray.length(); ++i) {
            this.y.add(new c(jsonArray.getJSONObject(i)));
        }
        this.s = new HashMap<Integer, a>();
        if (jsonObject.has("watermark")) {
            this.v = jsonObject.getBoolean("watermark");
        }
        else {
            this.v = true;
        }
        if (jsonObject.has("geo-info")) {
            this.C = new b(jsonObject.getJSONObject("geo-info"));
        }
        else {
            this.C = new b();
        }
        if (jsonObject.has("tms-movie-board")) {
            this.h = new com.tremorvideo.sdk.android.b.c(jsonObject.getJSONObject("tms-movie-board"), this.C);
        }
        else {
            this.h = null;
        }
        if (jsonObject.has("duration")) {
            this.E = jsonObject.getInt("duration") * 1000;
        }
        this.a(jsonObject);
    }
    
    private aw[] d(final String s) {
        final int n = 0;
        final Iterator<aw> iterator = this.b.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            if (s.equals(iterator.next().d())) {
                ++n2;
            }
        }
        final aw[] array = new aw[n2];
        final Iterator<aw> iterator2 = this.b.iterator();
        int n3 = n;
        while (iterator2.hasNext()) {
            final aw aw = iterator2.next();
            if (s.equals(aw.d())) {
                array[n3] = aw;
                ++n3;
            }
        }
        return array;
    }
    
    @Override
    public int A() {
        return this.E;
    }
    
    public String G() {
        return this.D.b();
    }
    
    public int H() {
        return this.z;
    }
    
    public boolean I() {
        return this.A;
    }
    
    public com.tremorvideo.sdk.android.richmedia.a J() {
        return this.D;
    }
    
    public l[] K() {
        final l[] array = new l[this.y.size()];
        for (int i = 0; i < this.y.size(); ++i) {
            final c c = this.y.get(i);
            final aw[] d = this.d(c.e());
            a h = null;
            if (c.k != null) {
                h = this.h(ae.a(c.k));
            }
            array[i] = new l(c.e(), c.a(), c.b(), c.c(), d, c.j, h);
        }
        return array;
    }
    
    @Override
    public String a(final int n) {
        return this.y.get(n).d();
    }
    
    @Override
    public List<n.a> a() {
        final ArrayList<n.a> list = new ArrayList<n.a>();
        for (final c c : this.y) {
            if (!c.j) {
                final HashMap<String, Integer> hashMap = new HashMap<String, Integer>(4);
                hashMap.put("ad", this);
                hashMap.put("url", (Integer)c.d());
                hashMap.put("crc", (Integer)c.h());
                hashMap.put("index", this.y.indexOf(c));
                list.add(new n.a(bf.d.b, "video", (Map<String, Object>)hashMap));
            }
            else {
                if (c.k == null || c.k.length() <= 0) {
                    continue;
                }
                final int a = ae.a(c.k);
                if (this.s.containsKey(a)) {
                    continue;
                }
                this.s.put(a, new a(c.k));
                final HashMap<String, Boolean> hashMap2 = new HashMap<String, Boolean>(3);
                hashMap2.put("url", (Boolean)c.k);
                hashMap2.put("crc", (Boolean)(Object)c.l);
                hashMap2.put("checkCache", true);
                list.add(new n.a(bf.d.a, "embedPlayer_" + a, (Map<String, Object>)hashMap2));
                final HashMap<String, Integer> hashMap3 = new HashMap<String, Integer>(2);
                hashMap3.put("hashName", a);
                list.add(new n.a(bf.d.g, "processPlayer", (Map<String, Object>)hashMap3));
            }
        }
        final HashMap<String, Long> hashMap4 = new HashMap<String, Long>(2);
        hashMap4.put("url", (Long)this.w);
        hashMap4.put("crc", this.x);
        list.add(new n.a(bf.d.a, "asset", (Map<String, Object>)hashMap4));
        if (this.c != null) {
            final HashMap<String, Boolean> hashMap5 = new HashMap<String, Boolean>(2);
            hashMap5.put("url", (Boolean)this.c.c());
            hashMap5.put("crc", (Boolean)(Object)this.c.d());
            hashMap5.put("checkCache", false);
            list.add(new n.a(bf.d.a, "coupon", (Map<String, Object>)hashMap5));
        }
        if (this.e != null) {
            list.add(new n.a(this, bf.d.c, "survey"));
        }
        if (this.g != null) {
            final HashMap<String, Boolean> hashMap6 = new HashMap<String, Boolean>(2);
            hashMap6.put("url", (Boolean)this.g.b());
            hashMap6.put("checkCache", false);
            list.add(new n.a(bf.d.a, "buyItNowDealsXml", (Map<String, Object>)hashMap6));
            final HashMap<String, Boolean> hashMap7 = new HashMap<String, Boolean>(3);
            hashMap7.put("url", (Boolean)this.g.c());
            hashMap7.put("crc", (Boolean)(Object)this.g.d());
            hashMap7.put("checkCache", true);
            list.add(new n.a(bf.d.a, "buyItNowTemplate", (Map<String, Object>)hashMap7));
            list.add(new n.a(this, bf.d.f, "processBIN"));
        }
        if (this.h != null) {
            final HashMap<String, Boolean> hashMap8 = new HashMap<String, Boolean>(3);
            hashMap8.put("url", (Boolean)this.h.c());
            hashMap8.put("crc", (Boolean)(Object)this.h.d());
            hashMap8.put("checkCache", true);
            list.add(new n.a(bf.d.a, "movieBoardTemplate", (Map<String, Object>)hashMap8));
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
        try {
            (this.D = new com.tremorvideo.sdk.android.richmedia.a()).a(new ZipFile(new File(this.B)), new com.tremorvideo.sdk.android.richmedia.f(context), this.C, this.v);
            (this.d = new bw()).a();
        }
        catch (Exception ex) {
            this.D = null;
            this.k = false;
            ac.a(ex);
        }
    }
    
    @Override
    public void a(final String s, final Object o) throws Exception {
        if (s.compareTo("asset") == 0) {
            this.B = (String)o;
            return;
        }
        super.a(s, o);
    }
    
    @Override
    public int b(final int n) {
        return this.y.get(n).g();
    }
    
    @Override
    public String c(final int n) {
        return this.y.get(n).f();
    }
    
    @Override
    public void c() {
        super.c();
        if (this.D != null) {
            this.D.f();
        }
    }
    
    @Override
    public int d(final int n) {
        if (n < this.y.size()) {
            return this.y.get(n).g();
        }
        return 0;
    }
    
    @Override
    public void d() {
        if (this.t != null) {
            this.D.x();
            this.t.c();
        }
        if (this.u != null) {
            this.D.y();
            this.u.b();
        }
        final String t = this.t();
        if (t != null) {
            final File file = new File(this.t() + "/" + t);
            if (file.exists()) {
                ae.a(file);
            }
        }
        super.d();
    }
    
    @Override
    public long f(final int n) {
        return this.y.get(n).h();
    }
    
    @Override
    public String i() {
        return com.tremorvideo.sdk.android.videoad.n.b(this.w);
    }
    
    @Override
    public int j() {
        int n2;
        final int n = n2 = this.y.size() + 1;
        if (this.g != null) {
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
        for (int i = 0; i < this.y.size(); ++i) {
            list.add(this.y.get(i).a());
        }
        list.add(this.i());
        if (this.g != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.g.c()));
        }
        if (this.h != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.h.c()));
        }
        if (this.s != null) {
            final Iterator<Map.Entry<Integer, a>> iterator = this.s.entrySet().iterator();
            while (iterator.hasNext()) {
                list.add(com.tremorvideo.sdk.android.videoad.n.b(this.s.get(iterator.next().getKey()).b));
            }
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
    protected boolean o() {
        return true;
    }
    
    @Override
    public b r() {
        return this.C;
    }
    
    @Override
    public String t() {
        return this.D.a();
    }
    
    public class a
    {
        private String b;
        private String c;
        private String d;
        
        public a(final String b) {
            this.b = b;
        }
        
        public String a() {
            return this.d;
        }
        
        public void a(final String d) {
            this.d = d;
        }
        
        public String b() {
            return this.c;
        }
        
        public void b(final String c) {
            this.c = c;
        }
    }
    
    public class b
    {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
        public JSONObject f;
        
        public b() {
            this.a = "";
            this.b = "";
            this.c = "";
            this.d = "";
            this.e = "";
        }
        
        public b(final JSONObject f) throws Exception {
            this.f = f;
            if (f.has("city")) {
                this.a = f.getString("city");
            }
            else {
                this.a = "";
            }
            if (f.has("country")) {
                this.b = f.getString("country");
            }
            else {
                this.b = "";
            }
            if (f.has("state")) {
                this.c = f.getString("state");
            }
            else {
                this.c = "";
            }
            if (f.has("zip")) {
                this.d = f.getString("zip");
            }
            else {
                this.d = "";
            }
            if (f.has("dma")) {
                this.e = f.getString("dma");
                return;
            }
            this.e = "";
        }
    }
    
    public class c
    {
        private String b;
        private String c;
        private int d;
        private String e;
        private long f;
        private String g;
        private int h;
        private int i;
        private boolean j;
        private String k;
        private long l;
        
        public c(final JSONObject jsonObject) throws Exception {
            this.e = "";
            this.f = 0L;
            this.g = "";
            this.h = 0;
            this.i = 0;
            this.j = false;
            this.b = jsonObject.getString("video-url");
            this.c = jsonObject.getString("format");
            this.g = jsonObject.getString("tag");
            if (jsonObject.has("streaming")) {
                this.j = jsonObject.getBoolean("streaming");
            }
            if (this.j) {
                if (jsonObject.has("ad-size")) {
                    this.d = jsonObject.getInt("ad-size");
                }
            }
            else {
                this.d = jsonObject.getInt("ad-size");
            }
            if (jsonObject.has("video-crc-32")) {
                this.f = jsonObject.getLong("video-crc-32");
            }
            else if (jsonObject.has("video-crc32")) {
                this.f = jsonObject.getLong("video-crc32");
            }
            if (jsonObject.has("streaming")) {
                this.j = jsonObject.getBoolean("streaming");
            }
            if (jsonObject.has("embed-video-player")) {
                this.k = jsonObject.getString("embed-video-player");
                if (jsonObject.has("embed-video-player-crc32")) {
                    this.l = jsonObject.getLong("embed-video-player-crc32");
                }
            }
            else {
                this.k = null;
            }
            final String[] split = this.c.split("-")[1].split("x");
            this.h = Integer.parseInt(split[0]);
            this.i = Integer.parseInt(split[1]);
        }
        
        public String a() {
            if (this.j) {
                return this.b;
            }
            return com.tremorvideo.sdk.android.videoad.n.b(this.b);
        }
        
        public int b() {
            return this.h;
        }
        
        public int c() {
            return this.i;
        }
        
        public String d() {
            return this.b;
        }
        
        public String e() {
            return this.g;
        }
        
        public String f() {
            return this.c;
        }
        
        public int g() {
            return this.d;
        }
        
        public long h() {
            return this.f;
        }
    }
}
