// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.ArrayList;
import android.content.Context;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.f.a;

public class q extends n
{
    public String t;
    public String u;
    public String v;
    public int w;
    public int x;
    private com.tremorvideo.sdk.android.f.a y;
    
    public q(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        super(bs, jsonObject, b);
        this.w = -1;
        this.x = 0;
        this.t = jsonObject.getString("url");
        if (this.t.contains(".zip")) {
            throw new Exception("Invalid Mraid File: " + this.t);
        }
        if (this.t != null && this.t.length() > 0) {
            this.t = this.t.replace("|", "%7C");
            this.t = this.t.replace(" ", "%20");
            this.t = this.t.replace("[", "%5B");
            this.t = this.t.replace("]", "%5D");
        }
        if (this.u() != null || this.s() != null || this.p() != null) {
            (this.d = new bw()).a();
        }
        this.u = jsonObject.getString("asset-url");
        this.a(jsonObject);
    }
    
    public void G() {
        this.y.a();
    }
    
    public String H() {
        return this.y.a;
    }
    
    public com.tremorvideo.sdk.android.f.a I() {
        return this.y;
    }
    
    public String J() {
        return this.u;
    }
    
    public boolean K() {
        return this.y.b();
    }
    
    @Override
    public List<a> a() {
        final List<a> a = super.a();
        if (this.c != null) {
            final HashMap<String, String> hashMap = new HashMap<String, String>(2);
            hashMap.put("url", this.c.c());
            hashMap.put("crc", (String)this.c.d());
            hashMap.put("checkCache", (String)false);
            a.add(new a(bf.d.a, "coupon", (Map<String, Object>)hashMap));
        }
        if (this.e != null) {
            a.add(new a(this, bf.d.c, "survey"));
        }
        return a;
    }
    
    @Override
    public void a(final Context context) {
        super.a(context);
        try {
            this.y = new com.tremorvideo.sdk.android.f.a(context);
            this.v = this.y.a(this.t);
            if (this.v == null) {
                ac.e("TremorLog_error::MRAID::Failed to download or store the MRAID tag");
                this.k = false;
            }
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    @Override
    public String i() {
        return null;
    }
    
    @Override
    public String[] k() {
        final ArrayList<String> list = new ArrayList<String>();
        if (this.c != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.c.c()));
        }
        if (this.e != null) {
            list.add(com.tremorvideo.sdk.android.videoad.n.b(this.e.c()));
        }
        if (list.size() > 0) {
            return list.toArray(new String[list.size()]);
        }
        return super.k();
    }
}
