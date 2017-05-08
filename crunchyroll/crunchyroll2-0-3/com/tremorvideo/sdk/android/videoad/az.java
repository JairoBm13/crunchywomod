// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Iterator;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;
import java.io.File;

public class az
{
    File a;
    private int b;
    private String c;
    private String d;
    private List<aw> e;
    
    public az(final JSONObject jsonObject) throws Exception {
        this.a = null;
        this.b = jsonObject.getInt("survey-skip-seconds") * 1000;
        this.c = jsonObject.getString("survey-link");
        this.d = jsonObject.getString("survey-image");
        if (!jsonObject.has("events")) {
            this.e = new ArrayList<aw>();
        }
        else {
            final JSONArray jsonArray = jsonObject.getJSONArray("events");
            this.e = new ArrayList<aw>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.e.add(new aw(jsonArray.getJSONObject(i)));
            }
        }
    }
    
    public int a() {
        return this.b;
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.e) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
    
    public void a(final File a) {
        this.a = a;
    }
    
    public String b() {
        return this.c;
    }
    
    public String c() {
        return this.d;
    }
    
    public File d() {
        return this.a;
    }
}
