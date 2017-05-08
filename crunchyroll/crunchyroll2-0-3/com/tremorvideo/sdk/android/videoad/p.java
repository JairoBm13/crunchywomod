// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class p extends n
{
    private String t;
    private aq u;
    
    public p(final bs bs, final JSONObject jsonObject, final boolean b) throws Exception {
        super(bs, jsonObject, b);
        this.u = null;
        this.t = jsonObject.getString("asset-url");
        this.a(jsonObject);
    }
    
    public String G() {
        return this.t;
    }
    
    @Override
    public List<a> a() {
        final ArrayList<a> list = new ArrayList<a>();
        if (this.c != null) {
            final HashMap<String, String> hashMap = new HashMap<String, String>(2);
            hashMap.put("url", this.c.c());
            hashMap.put("crc", (String)this.c.d());
            hashMap.put("checkCache", (String)false);
            list.add(new a(bf.d.a, "coupon", (Map<String, Object>)hashMap));
        }
        if (this.e != null) {
            list.add(new a(this, bf.d.c, "survey"));
        }
        if (this.p != null) {
            list.add(new a(this, bf.d.i, "adChoices"));
        }
        return list;
    }
    
    public void a(final bw d) {
        this.d = d;
    }
    
    @Override
    protected boolean o() {
        return true;
    }
}
