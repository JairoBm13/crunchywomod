// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.json.JSONArray;
import android.content.Context;
import java.util.Iterator;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import com.tremorvideo.sdk.android.videoad.a.a;
import com.tremorvideo.sdk.android.videoad.a.b;
import java.util.List;

public class s extends t
{
    List<n.a> t;
    List<com.tremorvideo.sdk.android.videoad.a.b> u;
    String v;
    boolean w;
    boolean x;
    
    public s(final com.tremorvideo.sdk.android.videoad.a.a a, final int n, final boolean b) throws Exception {
        super(n, b);
        this.u = new ArrayList<com.tremorvideo.sdk.android.videoad.a.b>();
        this.w = true;
        this.x = true;
        this.x = false;
        this.a = com.tremorvideo.sdk.android.videoad.n.b.e;
        this.r = true;
        this.u.addAll(a.a());
        if (!a.b()) {
            this.C = a.c();
            this.E = "H264-" + a.e() + "x" + a.f() + "-4x3";
            this.v = a.g();
            this.G = a.h();
            this.W();
            if (!this.r) {
                final HashMap<String, s> hashMap = new HashMap<String, s>(4);
                hashMap.put("ad", this);
                hashMap.put("url", (s)this.C.replace("|", "%7C"));
                hashMap.put("index", (s)0);
                this.t.add(new n.a(bf.d.b, "video", (Map<String, Object>)hashMap));
            }
        }
        if (this.b == null) {
            this.b = new ArrayList<aw>();
        }
    }
    
    public s(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        super(bs, jsonObject);
        this.u = new ArrayList<com.tremorvideo.sdk.android.videoad.a.b>();
        this.w = true;
        this.x = true;
        this.x = true;
        this.y = 0;
        if (jsonObject.has("skip")) {
            this.z = jsonObject.getBoolean("skip");
        }
        else {
            this.z = false;
        }
        if (jsonObject.has("skip-delay")) {
            this.A = jsonObject.getInt("skip-delay");
        }
        else {
            this.A = 0;
        }
        if (jsonObject.has("is-select-view")) {
            this.B = jsonObject.getBoolean("is-select-view");
        }
        else {
            this.B = false;
        }
        this.H = a.a;
        this.e = null;
        this.I = null;
        if (jsonObject.has("watermark")) {
            this.Q = jsonObject.getBoolean("watermark");
        }
        else {
            this.Q = true;
        }
        this.t = new ArrayList<n.a>();
        if (jsonObject.has("url")) {
            final HashMap<String, Object> hashMap = new HashMap<String, Object>(2);
            hashMap.put("url", jsonObject.get("url"));
            this.t.add(new n.a(bf.d.e, "vast", hashMap));
        }
        else if (jsonObject.has("vastdoc")) {
            this.a("vast", new com.tremorvideo.sdk.android.videoad.a.a(jsonObject.getString("vastdoc")));
        }
        this.a(jsonObject);
        for (final aw p3 : this.b) {
            if (p3.a() == aw.b.H) {
                this.p = p3;
                this.t.add(new n.a(this, bf.d.i, "adChoices"));
                break;
            }
        }
    }
    
    private void W() {
        if (this.b == null) {
            this.b = new ArrayList<aw>();
        }
        if (this.v != null && this.v.length() > 0) {
            for (int i = 0; i < this.b.size(); ++i) {
                final aw aw = this.b.get(i);
                if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.b) {
                    this.b.remove(aw);
                    break;
                }
            }
            if (this.x) {
                this.b.add(new aw(aw.b.b, this.v, this.d("click")));
            }
            else {
                this.b.add(new aw(aw.b.b, this.v, this.d("click"), 100));
            }
        }
        if (this.x) {
            final by[] d = this.d("close");
            if (d.length > 0) {
                final aw n = this.n();
                if (n == null) {
                    this.b.add(new aw(aw.b.v, 0, d, "close"));
                    return;
                }
                n.a(d);
            }
            return;
        }
        this.b.add(new aw(aw.b.v, 0, this.d("close"), 100, "close"));
    }
    
    private void X() {
        if (this.w) {
            this.w = false;
            this.a(aw.b.j, 0, "impression");
            this.a(aw.b.j, 0, "creativeView");
            this.a(aw.b.j, 0, "start");
            this.a(aw.b.j, this.G / 4, "firstQuartile");
            this.a(aw.b.j, this.G / 2, "midPoint");
            this.a(aw.b.j, this.G / 4 * 3, "thirdQuartile");
            this.a(aw.b.j, this.G, "complete");
        }
    }
    
    private void a(final aw.b b, final int n, final String s) {
        final by[] d = this.d(s);
        if (d.length > 0) {
            if (!this.x) {
                this.b.add(new aw(b, n, d, 100, s));
                return;
            }
            this.b.add(new aw(b, n, d, s));
        }
    }
    
    @Override
    public int A() {
        return this.G;
    }
    
    @Override
    public int G() {
        return Math.round(this.G / 1000.0f);
    }
    
    @Override
    public List<n.a> a() {
        return this.t;
    }
    
    @Override
    public void a(final Context context) {
        this.k = true;
        (this.d = new bw()).a();
    }
    
    @Override
    public void a(final String s, final Object o) throws Exception {
        if (s.equalsIgnoreCase("vast")) {
            final com.tremorvideo.sdk.android.videoad.a.a a = (com.tremorvideo.sdk.android.videoad.a.a)o;
            this.u.addAll(a.a());
            if (a.b()) {
                if (this.x) {
                    final HashMap<String, String> hashMap = new HashMap<String, String>(2);
                    hashMap.put("url", a.d());
                    this.t.add(new n.a(bf.d.e, "vast", (Map<String, Object>)hashMap));
                    if (this.t.size() > 100) {
                        throw new Exception("Too many VAST hops.");
                    }
                }
            }
            else {
                this.C = a.c();
                this.E = "H264-" + a.e() + "x" + a.f() + "-16x9";
                this.v = a.g();
                this.G = a.h();
                this.W();
                if (a.i()) {
                    this.r = true;
                }
                if (!this.r) {
                    final HashMap<String, s> hashMap2 = new HashMap<String, s>(4);
                    hashMap2.put("ad", this);
                    hashMap2.put("url", (s)this.C.replace("|", "%7C"));
                    hashMap2.put("index", (s)0);
                    this.t.add(new n.a(bf.d.b, "video", (Map<String, Object>)hashMap2));
                }
            }
        }
        super.a(s, o);
    }
    
    @Override
    protected void a(final JSONObject jsonObject) throws Exception {
        if (this.b == null) {
            this.b = new ArrayList<aw>();
        }
        if (jsonObject.has("event")) {
            final JSONArray jsonArray = jsonObject.getJSONArray("event");
            for (int i = 0; i < jsonArray.length(); ++i) {
                final aw o = new aw(jsonArray.getJSONObject(i));
                this.b.add(o);
                if (o.a() == aw.b.av) {
                    this.o = o;
                }
            }
            if (this.o()) {
                com.tremorvideo.sdk.android.videoad.n.a(this.b);
            }
            else {
                com.tremorvideo.sdk.android.videoad.n.b(this.b);
            }
            com.tremorvideo.sdk.android.videoad.n.d(this.b);
        }
    }
    
    by[] d(final String s) {
        final ArrayList<by> list = new ArrayList<by>();
        for (final com.tremorvideo.sdk.android.videoad.a.b b : this.u) {
            if (b.a.equalsIgnoreCase(s)) {
                list.add(new by(b.b));
            }
        }
        return list.toArray(new by[list.size()]);
    }
    
    @Override
    public boolean g(final int g) {
        if (this.w && g > 0) {
            ac.e("Building timer events with a duration of: " + g + "ms");
            this.G = g;
            this.X();
            return true;
        }
        return false;
    }
    
    @Override
    public String i() {
        return com.tremorvideo.sdk.android.videoad.n.b(this.C);
    }
    
    @Override
    public String[] k() {
        final ArrayList<String> list = new ArrayList<String>();
        if (!this.r) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.C));
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
    public boolean m() {
        return this.C != null;
    }
}
