// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.provider.MediaStore$Images$Media;
import android.content.Context;
import java.util.Iterator;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

public class ae
{
    private String a;
    private long b;
    private String c;
    private String d;
    private int e;
    private List<aw> f;
    private File g;
    
    public ae(final JSONObject jsonObject) throws Exception {
        int i = 0;
        this.b = 0L;
        this.e = 0;
        this.f = new ArrayList<aw>();
        this.a = jsonObject.getString("coupon-image");
        this.d = jsonObject.getString("coupon-id");
        if (jsonObject.has("coupon-skip-seconds")) {
            this.e = jsonObject.getInt("coupon-skip-seconds") * 1000;
        }
        if (jsonObject.has("coupon-code")) {
            this.c = jsonObject.getString("coupon-code");
        }
        for (JSONArray jsonArray = jsonObject.getJSONArray("events"); i < jsonArray.length(); ++i) {
            this.f.add(new aw(jsonArray.getJSONObject(i)));
        }
        n.c(this.f);
    }
    
    public int a() {
        return this.e;
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.f) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
    
    public String a(final Context context) {
        try {
            return MediaStore$Images$Media.insertImage(context.getContentResolver(), this.g.getAbsolutePath(), "Coupon", "Coupon");
        }
        catch (Exception ex) {
            ac.a(ex);
            return null;
        }
    }
    
    public boolean a(final File g) {
        try {
            this.g = g;
            return true;
        }
        catch (Exception ex) {
            ac.a(ex);
            return false;
        }
    }
    
    public File b() {
        return this.g;
    }
    
    public String c() {
        return this.a;
    }
    
    public long d() {
        return this.b;
    }
    
    public void e() {
    }
}
