// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.json.JSONArray;
import java.io.File;
import android.content.Context;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.GregorianCalendar;
import com.tremorvideo.sdk.android.videoad.a.a;
import java.util.HashMap;
import org.json.JSONObject;
import java.util.Map;
import com.tremorvideo.sdk.android.richmedia.a.d;
import com.tremorvideo.sdk.android.a.c;
import java.util.List;

public class n
{
    protected b a;
    protected List<aw> b;
    protected ae c;
    protected bw d;
    protected az e;
    protected be f;
    protected c g;
    protected com.tremorvideo.sdk.android.b.c h;
    protected d i;
    protected long j;
    protected boolean k;
    protected boolean l;
    protected Map<String, Object> m;
    protected long n;
    public aw o;
    public aw p;
    public int q;
    protected boolean r;
    protected Map<Integer, r.a> s;
    private bs t;
    private String u;
    private String v;
    private boolean w;
    private String x;
    
    public n() {
        this.j = 0L;
        this.k = false;
        this.l = false;
        this.o = null;
        this.p = null;
        this.q = 0;
        this.r = false;
    }
    
    public n(final bs t, final JSONObject jsonObject, final boolean b) throws Exception {
        this.j = 0L;
        this.k = false;
        this.l = false;
        this.o = null;
        this.p = null;
        this.q = 0;
        this.r = false;
        this.m = new HashMap<String, Object>();
        this.a = com.tremorvideo.sdk.android.videoad.n.b.a(jsonObject.getString("adtype"));
        this.t = t;
        this.w = false;
        this.x = jsonObject.toString();
        this.u = jsonObject.getString("id");
        this.n = ac.G();
        if (jsonObject.has("ad-lifetime")) {
            this.j = jsonObject.getLong("ad-lifetime");
        }
        if (jsonObject.has("coupon")) {
            this.c = new ae(jsonObject.getJSONObject("coupon"));
        }
        this.v = jsonObject.getString("cache-expiry-date");
        if (jsonObject.has("ad-survey-external")) {
            this.e = new az(jsonObject.getJSONObject("ad-survey-external"));
        }
        else {
            this.e = null;
        }
        if (jsonObject.has("ad-survey-internal")) {
            this.f = new be(jsonObject.getJSONObject("ad-survey-internal"));
        }
        else {
            this.f = null;
        }
        if (jsonObject.has("buy-now")) {
            this.g = new c(jsonObject.getJSONObject("buy-now"));
        }
        else {
            this.g = null;
        }
        if (jsonObject.has("uatype")) {
            this.q = jsonObject.getInt("uatype");
            if (this.q > 3 || this.q < 0) {
                this.q = 0;
            }
        }
        else {
            this.q = 0;
        }
        com.tremorvideo.sdk.android.richmedia.ae.a = this.q;
        if (jsonObject.has("streaming")) {
            this.r = jsonObject.getBoolean("streaming");
        }
    }
    
    public static n a(final com.tremorvideo.sdk.android.videoad.a.a a, final int n, final boolean b) throws Exception {
        return new s(a, n, b);
    }
    
    public static n a(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        String s;
        if (jsonObject.has("adtype")) {
            final String string = jsonObject.getString("adtype");
            if ((s = string) != null) {
                s = string;
                if (string.length() > 0) {
                    final b a = n.b.a(jsonObject.getString("adtype"));
                    if (a == n.b.b) {
                        return new t(bs, jsonObject, b);
                    }
                    if (a == n.b.c) {
                        return new r(bs, jsonObject, b);
                    }
                    if (a == n.b.d) {
                        return new p(bs, jsonObject, b);
                    }
                    if (a == n.b.e) {
                        return new s(bs, jsonObject, b);
                    }
                    s = string;
                    if (a == n.b.f) {
                        return new q(bs, jsonObject, b);
                    }
                }
            }
        }
        else {
            s = null;
        }
        ac.e("Invalid adtype, adtype=" + s);
        return null;
    }
    
    public static GregorianCalendar a(final String s) {
        try {
            final String[] split = s.split(" ");
            final String[] split2 = split[0].split("-");
            if (split.length == 1) {
                return new GregorianCalendar(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]) - 1, Integer.parseInt(split2[2]));
            }
            final String[] split3 = split[1].split(":");
            return new GregorianCalendar(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]) - 1, Integer.parseInt(split2[2]), Integer.parseInt(split3[0]), Integer.parseInt(split3[1]), Integer.parseInt(split3[2]));
        }
        catch (Exception ex) {
            ac.a(ex);
            return new GregorianCalendar();
        }
    }
    
    protected static void a(final List<aw> list) {
        for (final aw aw : list) {
            if (aw.a() == aw.b.t) {
                aw.a(aw.b.r);
            }
            else if (aw.a() == aw.b.s) {
                aw.a(aw.b.r);
            }
            else if (aw.a() == aw.b.N) {
                aw.a(aw.b.M);
            }
            else {
                if (aw.a() != aw.b.O) {
                    continue;
                }
                aw.a(aw.b.M);
            }
        }
    }
    
    protected static String b(final String s) {
        return "" + com.tremorvideo.sdk.android.richmedia.ae.a(s);
    }
    
    protected static void b(final List<aw> list) {
        int n = 0;
        final ArrayList<aw> list2 = new ArrayList<aw>();
        final Iterator<aw> iterator = list.iterator();
        aw aw = null;
    Label_0078_Outer:
        while (iterator.hasNext()) {
            aw aw2 = iterator.next();
            int n2 = -1;
            if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.t) {
                n2 = 1;
            }
            else if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.s) {
                n2 = 2;
            }
            else if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.r) {
                n2 = 3;
            }
            else {
                aw2 = null;
            }
            while (true) {
                Label_0158: {
                    if (aw2 == null) {
                        break Label_0158;
                    }
                    list2.add(aw2);
                    if (n2 <= n) {
                        break Label_0158;
                    }
                    aw = aw2;
                    n = n2;
                    continue Label_0078_Outer;
                }
                n2 = n;
                aw2 = aw;
                continue;
            }
        }
        if (aw != null) {
            aw.a(com.tremorvideo.sdk.android.videoad.aw.b.r);
            list2.remove(aw);
        }
        if (list2.size() > 0) {
            list.removeAll(list2);
        }
    }
    
    protected static void c(final List<aw> list) {
        int n = 0;
        final ArrayList<aw> list2 = new ArrayList<aw>();
        final Iterator<aw> iterator = list.iterator();
        aw aw = null;
    Label_0078_Outer:
        while (iterator.hasNext()) {
            aw aw2 = iterator.next();
            int n2 = -1;
            if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.N) {
                n2 = 1;
            }
            else if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.O) {
                n2 = 2;
            }
            else if (aw2.a() == com.tremorvideo.sdk.android.videoad.aw.b.M) {
                n2 = 3;
            }
            else {
                aw2 = null;
            }
            while (true) {
                Label_0158: {
                    if (aw2 == null) {
                        break Label_0158;
                    }
                    list2.add(aw2);
                    if (n2 <= n) {
                        break Label_0158;
                    }
                    aw = aw2;
                    n = n2;
                    continue Label_0078_Outer;
                }
                n2 = n;
                aw2 = aw;
                continue;
            }
        }
        if (aw != null) {
            aw.a(com.tremorvideo.sdk.android.videoad.aw.b.M);
            list2.remove(aw);
        }
        if (list2.size() > 0) {
            list.removeAll(list2);
        }
    }
    
    protected static void d(final List<aw> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<aw>() {
            public int a(final aw aw, final aw aw2) {
                if (aw.i() < aw2.i()) {
                    return -1;
                }
                if (aw.i() > aw2.i()) {
                    return 1;
                }
                return 0;
            }
        });
    }
    
    public int A() {
        return -1;
    }
    
    public aw B() {
        for (final aw aw : this.b) {
            if (aw.a() == aw.b.y) {
                return aw;
            }
        }
        return null;
    }
    
    public aw C() {
        for (final aw aw : this.b) {
            if (aw.a() == aw.b.z) {
                return aw;
            }
        }
        return null;
    }
    
    public aw D() {
        for (final aw aw : this.b) {
            if (aw.a() == aw.b.aq) {
                return aw;
            }
        }
        return null;
    }
    
    public void E() {
        this.l = true;
    }
    
    public boolean F() {
        return this.l;
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.b) {
            if (b == aw.a()) {
                return aw;
            }
        }
        return null;
    }
    
    public String a(final int n) {
        return "";
    }
    
    public String a(final Context context, final int n) {
        return com.tremorvideo.sdk.android.videoad.x.a(context.getFilesDir(), this.e(n)).getAbsolutePath();
    }
    
    public List<a> a() {
        return new ArrayList<a>();
    }
    
    public void a(final Context context) {
        this.k = true;
    }
    
    public void a(final String s, final Object o) throws Exception {
        if (s.compareTo("coupon") == 0) {
            this.c.a(new File((String)o));
        }
        else {
            if (s.compareTo("buyItNowDealsXml") == 0) {
                this.g.a(new File((String)o));
                return;
            }
            if (s.compareTo("buyItNowTemplate") == 0) {
                this.g.b(new File((String)o));
                return;
            }
            if (s.compareTo("movieBoardTemplate") == 0) {
                this.h.a(new File((String)o));
                return;
            }
            if (s.compareTo("genericTemplate") == 0) {
                this.i.a(new File((String)o));
                return;
            }
            if (s.startsWith("embedPlayer_")) {
                if (this.s != null) {
                    final int int1 = Integer.parseInt(s.substring(s.lastIndexOf("_") + 1));
                    if (this.s.containsKey(int1)) {
                        this.s.get(int1).a(o.toString());
                    }
                }
            }
            else {
                if (s.compareTo("adChoices") != 0) {
                    if (this.m == null) {
                        this.m = new HashMap<String, Object>();
                    }
                    this.m.put(s, o);
                    return;
                }
                if (this.p != null) {
                    this.p.a((String)o);
                }
            }
        }
    }
    
    protected void a(final JSONObject jsonObject) throws Exception {
        final JSONArray jsonArray = jsonObject.getJSONArray("event");
        this.b = new ArrayList<aw>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            final aw aw = new aw(jsonArray.getJSONObject(i));
            this.b.add(aw);
            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.av) {
                this.o = aw;
            }
            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.H) {
                this.p = aw;
            }
        }
        if (this.o()) {
            a(this.b);
        }
        else {
            b(this.b);
        }
        d(this.b);
    }
    
    public int b(final int n) {
        return 0;
    }
    
    public boolean b() {
        return this.k;
    }
    
    public aw c(final String s) {
        for (final aw aw : this.b) {
            if (s.equalsIgnoreCase(aw.d())) {
                return aw;
            }
        }
        return null;
    }
    
    public String c(final int n) {
        return "";
    }
    
    public void c() {
        ac.e("Freeing AD resources");
        if (this.c != null) {
            this.c.e();
            this.c = null;
        }
        if (this.d != null) {
            this.d.b();
            this.d = null;
        }
    }
    
    public int d(final int n) {
        return 0;
    }
    
    public void d() {
        if (this.g != null) {
            this.g.g();
            this.g = null;
        }
        if (this.h != null) {
            this.h.e();
            this.h = null;
        }
        if (this.s != null) {
            final Iterator<Integer> iterator = this.s.keySet().iterator();
            while (iterator.hasNext()) {
                final r.a a = this.s.get((int)iterator.next());
                if (a != null) {
                    final File file = new File(a.b());
                    if (!file.exists()) {
                        continue;
                    }
                    com.tremorvideo.sdk.android.richmedia.ae.a(file);
                }
            }
        }
    }
    
    public String e(final int n) {
        return "" + com.tremorvideo.sdk.android.richmedia.ae.a(this.a(n));
    }
    
    public GregorianCalendar e() {
        return a(this.v);
    }
    
    public long f(final int n) {
        return 0L;
    }
    
    public bs f() {
        return this.t;
    }
    
    public b g() {
        return this.a;
    }
    
    public boolean g(final int n) {
        return false;
    }
    
    public r.a h(final int n) {
        if (this.s.containsKey(n)) {
            return this.s.get(n);
        }
        return null;
    }
    
    public String h() {
        return this.v;
    }
    
    public String i() {
        return "";
    }
    
    public int j() {
        return 0;
    }
    
    public String[] k() {
        return new String[0];
    }
    
    public List<aw> l() {
        return this.b;
    }
    
    public boolean m() {
        return true;
    }
    
    public aw n() {
        for (final aw aw : this.b) {
            if (aw.a() == aw.b.v) {
                return aw;
            }
        }
        return null;
    }
    
    protected boolean o() {
        return false;
    }
    
    public ae p() {
        return this.c;
    }
    
    public bw q() {
        return this.d;
    }
    
    public r.b r() {
        return null;
    }
    
    public az s() {
        return this.e;
    }
    
    public String t() {
        return null;
    }
    
    public be u() {
        return this.f;
    }
    
    public boolean v() {
        return this.c != null && this.a(aw.b.B) == null;
    }
    
    public long w() {
        return this.j;
    }
    
    public long x() {
        return this.n;
    }
    
    public c y() {
        return this.g;
    }
    
    public com.tremorvideo.sdk.android.b.c z() {
        return this.h;
    }
    
    public class a
    {
        public bf.d a;
        public String b;
        public Map<String, Object> c;
        
        public a(final bf.d a, final String b) {
            this.a = a;
            this.b = b;
            this.c = new HashMap<String, Object>();
        }
        
        public a(final bf.d a, final String b, final Map<String, Object> c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    
    public enum b
    {
        a("unknown"), 
        b("video"), 
        c("richmedia"), 
        d("html5"), 
        e("vast"), 
        f("mraid");
        
        private String g;
        
        private b(final String g) {
            this.g = g;
        }
        
        public static b a(final String s) {
            final b[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final b b = values[i];
                if (b.toString().equals(s)) {
                    return b;
                }
            }
            return b.a;
        }
        
        @Override
        public String toString() {
            return this.g;
        }
    }
}
