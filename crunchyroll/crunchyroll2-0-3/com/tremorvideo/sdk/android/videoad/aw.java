// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.HashMap;
import java.util.Hashtable;
import org.json.JSONArray;
import android.webkit.URLUtil;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Map;
import java.util.Dictionary;

public class aw
{
    private b a;
    private String b;
    private String c;
    private by[] d;
    private String e;
    private String f;
    private boolean g;
    private int h;
    private int i;
    private boolean j;
    private Dictionary<String, String> k;
    private int l;
    private String m;
    private a n;
    private Map<String, String> o;
    private int p;
    
    public aw(final b a, final int i, final by[] d, final int p5, final String b) {
        this.a = a;
        if (b != null) {
            this.b = b;
        }
        else {
            this.b = a.c();
        }
        this.c = "";
        this.d = d;
        this.e = "";
        this.f = "0";
        this.g = true;
        this.h = 0;
        this.i = i;
        this.j = true;
        this.l = -1;
        this.m = "";
        this.n = aw.a.d;
        this.p = p5;
    }
    
    public aw(final b a, final int i, final by[] d, final String b) {
        this.a = a;
        if (b != null) {
            this.b = b;
        }
        else {
            this.b = a.c();
        }
        this.c = "";
        this.d = d;
        this.e = "";
        this.f = "0";
        this.g = true;
        this.h = 0;
        this.i = i;
        this.j = true;
        this.l = -1;
        this.m = "";
        this.n = aw.a.d;
        this.p = -1;
    }
    
    public aw(final b b, final String s, final by[] array) {
        this(b, s, array, -1);
    }
    
    public aw(final b a, final String e, final by[] d, final int p4) {
        this.a = a;
        this.b = a.c();
        this.c = "";
        this.d = d;
        this.e = e;
        this.f = "0";
        this.g = true;
        this.h = 0;
        this.i = 0;
        this.j = true;
        this.l = -1;
        this.m = "";
        this.n = aw.a.d;
        this.p = p4;
    }
    
    public aw(final JSONObject jsonObject) throws Exception {
        this.l = -1;
        String string = null;
        ArrayList<by> list = null;
        Label_0242: {
            if (!jsonObject.has("id")) {
                break Label_0242;
            }
            this.f = jsonObject.getString("id");
        Label_0079_Outer:
            while (true) {
                if (jsonObject.has("billable")) {
                    this.g = jsonObject.getBoolean("billable");
                }
                Label_0251: {
                    if (!jsonObject.has("type")) {
                        break Label_0251;
                    }
                    string = jsonObject.getString("type");
                    this.a = aw.b.a(string);
                    this.n = aw.a.a(string);
                Label_0098_Outer:
                    while (true) {
                        Label_0270: {
                            if (!jsonObject.has("order")) {
                                break Label_0270;
                            }
                            this.h = jsonObject.getInt("order");
                        Label_0117_Outer:
                            while (true) {
                                Label_0278: {
                                    if (!jsonObject.has("text")) {
                                        break Label_0278;
                                    }
                                    this.b = jsonObject.getString("text");
                                Label_0169_Outer:
                                    while (true) {
                                        list = new ArrayList<by>();
                                        while (true) {
                                            if (!jsonObject.has("tracking-url")) {
                                                break Label_0169;
                                            }
                                            try {
                                                final String a = a(jsonObject, "tracking-url");
                                                if (URLUtil.isValidUrl(a)) {
                                                    list.add(new by(a));
                                                }
                                                if (jsonObject.has("tracking")) {
                                                    final JSONArray jsonArray = jsonObject.getJSONArray("tracking");
                                                    int i = 0;
                                                    by a2 = null;
                                                    while (i < jsonArray.length()) {
                                                        final JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                        if (jsonObject2 != null) {
                                                            a2 = by.a(jsonObject2);
                                                        }
                                                        if (a2 != null) {
                                                            list.add(a2);
                                                        }
                                                        ++i;
                                                    }
                                                }
                                                break Label_0242;
                                                this.h = 0;
                                                continue Label_0117_Outer;
                                                this.a = aw.b.ar;
                                                this.n = aw.a.d;
                                                string = null;
                                                continue Label_0098_Outer;
                                                this.f = "0";
                                                continue Label_0079_Outer;
                                                this.b = "";
                                                continue Label_0169_Outer;
                                            }
                                            catch (Exception ex) {
                                                ac.a(ex);
                                                continue;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        this.a(list.toArray(new by[list.size()]));
        this.e = a(jsonObject, "click-url");
        if (jsonObject.has("tag")) {
            this.m = jsonObject.getString("tag");
        }
        else {
            this.m = null;
        }
        if (jsonObject.has("icon")) {
            this.c = jsonObject.getString("icon");
        }
        else {
            this.c = null;
        }
        if (jsonObject.has("event-trigger-secs")) {
            this.i = (int)Math.round(jsonObject.getDouble("event-trigger-secs") * 1000.0);
        }
        else {
            this.i = 0;
        }
        if (jsonObject.has("pre-event-trigger")) {
            this.j = jsonObject.getBoolean("pre-event-trigger");
        }
        else {
            this.j = false;
        }
        if (jsonObject.has("activity-trigger-secs")) {
            this.l = (int)Math.round(jsonObject.getDouble("activity-trigger-secs") * 1000.0);
        }
        if (this.a == aw.b.q) {
            this.g(jsonObject);
        }
        else {
            if (this.a == aw.b.u) {
                this.f(jsonObject);
                return;
            }
            if (this.a == aw.b.x) {
                this.h(jsonObject);
                return;
            }
            if (this.a == aw.b.w) {
                this.i(jsonObject);
                return;
            }
            if (this.a == aw.b.r) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.s) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.t) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.O) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.N) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.M) {
                this.j(jsonObject);
                return;
            }
            if (this.a == aw.b.p) {
                this.e(jsonObject);
                return;
            }
            if (this.a == aw.b.H) {
                this.b(jsonObject);
                return;
            }
            if (this.a == aw.b.I) {
                this.c(jsonObject);
                return;
            }
            if (this.a == aw.b.L) {
                this.d(jsonObject);
                return;
            }
            if (this.a == aw.b.am) {
                this.k(jsonObject);
                return;
            }
            if (this.a == aw.b.ar) {
                if (string == null) {
                    string = "";
                }
                this.b = string;
                return;
            }
            if (this.a == aw.b.v) {
                this.a(jsonObject);
            }
        }
    }
    
    private static String a(final JSONObject jsonObject, final String s) {
        try {
            return jsonObject.getString(s);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static void a(final Dictionary<String, String> dictionary, final JSONObject jsonObject, final String s) {
        final String a = a(jsonObject, s);
        if (a != null) {
            dictionary.put(s, a);
        }
    }
    
    private void a(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "countdown-text");
        a(this.k, jsonObject, "text");
    }
    
    public static String b(final b b) {
        return b.c();
    }
    
    private void b(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "location");
    }
    
    private void c(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "zip");
        a(this.k, jsonObject, "movie");
        a(this.k, jsonObject, "movie-date");
    }
    
    private void d(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "age");
    }
    
    private void e(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "message");
    }
    
    private void f(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "description");
        a(this.k, jsonObject, "image");
        a(this.k, jsonObject, "message");
        a(this.k, jsonObject, "link");
        a(this.k, jsonObject, "name");
        a(this.k, jsonObject, "video");
    }
    
    private void g(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "map-type");
        a(this.k, jsonObject, "latitude");
        a(this.k, jsonObject, "longitude");
        a(this.k, jsonObject, "pinpoint-name");
        a(this.k, jsonObject, "query");
        a(this.k, jsonObject, "zoom");
        a(this.k, jsonObject, "retailigence-endpoint");
        a(this.k, jsonObject, "html-template-path");
    }
    
    private void h(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "duration");
    }
    
    private void i(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "auto-skip");
    }
    
    private void j(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "message");
        a(this.k, jsonObject, "subject");
    }
    
    private void k(final JSONObject jsonObject) {
        a(this.k = new Hashtable<String, String>(), jsonObject, "query-url");
    }
    
    public int a(final String s, final int n) {
        try {
            final String s2 = this.k.get(s);
            int int1 = n;
            if (s2 != null) {
                int1 = Integer.parseInt(s2);
            }
            return int1;
        }
        catch (Exception ex) {
            ac.a("Could not get integer parameter: " + s, ex);
            return n;
        }
    }
    
    public b a() {
        return this.a;
    }
    
    public String a(final String s, final String s2) {
        try {
            final String s3 = this.k.get(s);
            if (s3 != null) {
                return s3;
            }
        }
        catch (Exception ex) {
            ac.a("Could not get string parameter: " + s, ex);
        }
        return s2;
    }
    
    public void a(final b a) {
        this.a = a;
    }
    
    public void a(final ay.a a) {
        if (this.d != null) {
            final by[] d = this.d;
            for (int length = d.length, i = 0; i < length; ++i) {
                d[i].a(a);
            }
        }
    }
    
    public void a(final String e) {
        this.e = e;
    }
    
    public void a(final by[] d) {
        if (this.d == null) {
            this.d = d;
            return;
        }
        final by[] d2 = new by[this.d.length + d.length];
        System.arraycopy(this.d, 0, d2, 0, this.d.length);
        System.arraycopy(d, 0, d2, this.d.length, d.length);
        this.d = d2;
    }
    
    public int b() {
        return this.l;
    }
    
    public void b(final String s, final String s2) {
        if (this.o == null) {
            this.o = new HashMap<String, String>();
        }
        this.o.put(s, s2);
    }
    
    public String c() {
        return this.f;
    }
    
    public String d() {
        return this.m;
    }
    
    public int e() {
        return this.i;
    }
    
    public Dictionary<String, String> f() {
        return this.k;
    }
    
    public boolean g() {
        return this.j;
    }
    
    public String h() {
        return this.b;
    }
    
    public int i() {
        return this.h;
    }
    
    public String j() {
        return this.e;
    }
    
    public int k() {
        return this.p;
    }
    
    public boolean l() {
        return this.a.a() && this.m();
    }
    
    public boolean m() {
        final boolean b = false;
        boolean t;
        if (this.a == aw.b.c) {
            t = ac.t();
        }
        else {
            t = b;
            if (!this.n()) {
                t = b;
                if (ac.a(this.a)) {
                    if (this.j() != null && this.j().startsWith("market://")) {
                        t = b;
                        if (!ac.R()) {
                            return t;
                        }
                    }
                    return true;
                }
            }
        }
        return t;
    }
    
    public boolean n() {
        return this.n != aw.a.d;
    }
    
    public String o() {
        if (this.c != null) {
            return this.c;
        }
        return this.a.b().au;
    }
    
    public boolean p() {
        return this.a.equals(aw.b.ah) || this.a.equals(aw.b.ag) || this.a.equals(aw.b.ai) || this.a.equals(aw.b.aj) || this.a.equals(aw.b.ak) || this.a.equals(aw.b.al) || this.a.equals(aw.b.aq);
    }
    
    public enum a
    {
        a("click-to-appstore"), 
        b("click-to-blackberrymarket"), 
        c("click-to-itunes"), 
        d("none");
        
        private String e;
        
        private a(final String e) {
            this.e = e;
        }
        
        public static a a(final String s) {
            final a[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final a a = values[i];
                if (a.toString().equalsIgnoreCase(s)) {
                    return a;
                }
            }
            return a.d;
        }
        
        @Override
        public String toString() {
            return this.e;
        }
    }
    
    public enum b
    {
        A("save-coupon", true, "Save", bw.d.g), 
        B("show-coupon", true, "Coupon", bw.d.A), 
        C("coupon-auto-display", false, "", (bw.d)null), 
        D("coupon-skip", true, "Done", bw.d.q), 
        E("coupon-back", true, "Done", bw.d.q), 
        F("show-date", true, "Date", bw.d.w), 
        G("show-zip", false, "Date", (bw.d)null), 
        H("ad-choices", false, "", (bw.d)null), 
        I("click-to-tms", false, "", (bw.d)null), 
        J("tms-zip-click", false, "", (bw.d)null), 
        K("tms-date-click", false, "", (bw.d)null), 
        L("age-gate", false, "", (bw.d)null), 
        M("coupon-share", true, "Share", bw.d.u), 
        N("coupon-share-sms", true, "Share SMS", bw.d.u), 
        O("coupon-share-email", true, "Share Email", bw.d.u), 
        P("internal-survey-start", false, "", (bw.d)null), 
        Q("internal-survey-skip", false, "", (bw.d)null), 
        R("internal-survey-submit", false, "", (bw.d)null), 
        S("buy-now", true, "", bw.d.A), 
        T("buy-now-start", false, "", (bw.d)null), 
        U("buy-now-click", false, "", (bw.d)null), 
        V("buy-now-skip", false, "", (bw.d)null), 
        W("buy-now-end", false, "", (bw.d)null), 
        X("tms-movie-board", true, "", bw.d.A), 
        Y("movie-board-start", false, "", (bw.d)null), 
        Z("movie-board-end", false, "", (bw.d)null), 
        a("", false, "", (bw.d)null), 
        aa("movie-board-skip", false, "", (bw.d)null), 
        ab("movie-board-click-date", false, "", (bw.d)null), 
        ac("movie-board-click-zip", false, "", (bw.d)null), 
        ad("movie-board-click-ShowTime", false, "", (bw.d)null), 
        ae("movie-board-click-map", false, "", (bw.d)null), 
        af("movie-board-click-logo", false, "", (bw.d)null), 
        ag("video-impression", false, "", (bw.d)null), 
        ah("video-start", false, "", (bw.d)null), 
        ai("video-first-quarter", false, "", (bw.d)null), 
        aj("video-midpoint", false, "", (bw.d)null), 
        ak("video-third-quarter", false, "", (bw.d)null), 
        al("video-end", false, "", (bw.d)null), 
        am("twitter-feed", false, "", (bw.d)null), 
        an("twitter-feed-click-icon", false, "", (bw.d)null), 
        ao("twitter-feed-click-body", false, "", (bw.d)null), 
        ap("twitter-feed-click-retweet", false, "", (bw.d)null), 
        aq("view-complete", false, "", (bw.d)null), 
        ar("custom", false, "", (bw.d)null), 
        as("show-generic-component", false, "", (bw.d)null), 
        at("show-provider", false, "", (bw.d)null), 
        au("show-zip-callback", false, "", (bw.d)null), 
        av("touch", false, "", (bw.d)null), 
        aw("email", false, "", (bw.d)null), 
        ax("email-success", false, "", (bw.d)null), 
        ay("email-failure", false, "", (bw.d)null), 
        az("email-box-click", false, "", (bw.d)null), 
        b("click-to-web", true, "Web", bw.d.a), 
        c("click-to-call", true, "Call", bw.d.c), 
        d("click-to-mp3store", true, "MP3", bw.d.e), 
        e("click-to-twitter", true, "Twitter", bw.d.i), 
        f("click-to-market", true, "Market", bw.d.g), 
        g("click-to-facebook", true, "Facebook", bw.d.k), 
        h("click-to-tickets", true, "Tickets", bw.d.m), 
        i("click-to-youtube", true, "Youtube", bw.d.o), 
        j("timer", false, "", (bw.d)null), 
        k("external-survey-init", false, "", (bw.d)null), 
        l("external-survey-start", false, "", bw.d.s), 
        m("external-survey-end", false, "", (bw.d)null), 
        n("external-survey-skip", false, "", (bw.d)null), 
        o("survey", false, "Survey", bw.d.g), 
        p("twitter-tweet", true, "Tweet", bw.d.i), 
        q("click-to-map", true, "Map", bw.d.y), 
        r("click-to-share", true, "Share", bw.d.u), 
        s("share-email", true, "Share", bw.d.u), 
        t("share-sms", true, "Share", bw.d.u), 
        u("post-to-facebook", true, "Post", bw.d.k), 
        v("skip", false, "Skip", bw.d.q), 
        w("replay", false, "Replay", bw.d.Q), 
        x("timer-vibration", false, "Vibrate", (bw.d)null), 
        y("ad-start", false, "Ad Start", (bw.d)null), 
        z("ad-complete", false, "Ad Complete", (bw.d)null);
        
        private String aA;
        private boolean aB;
        private String aC;
        private bw.d aD;
        
        private b(final String aa, final boolean ab, final String ac, final bw.d ad) {
            this.aA = aa;
            this.aB = ab;
            this.aC = ac;
            this.aD = ad;
        }
        
        public static b a(final String s) {
            final b[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final b b = values[i];
                if (b.toString().equalsIgnoreCase(s)) {
                    return b;
                }
            }
            return b.ar;
        }
        
        public boolean a() {
            return this.aB;
        }
        
        public bw.d b() {
            return this.aD;
        }
        
        public String c() {
            return this.aC;
        }
        
        @Override
        public String toString() {
            return this.aA;
        }
    }
}
