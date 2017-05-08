// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import java.util.Iterator;
import org.apache.http.NameValuePair;
import java.util.List;
import android.content.Context;
import org.json.JSONObject;
import android.widget.RelativeLayout$LayoutParams;
import java.util.ArrayList;
import android.app.Activity;
import com.tremorvideo.sdk.android.videoad.h;

public class f implements e
{
    private h a;
    private Activity b;
    private ArrayList<com.tremorvideo.sdk.android.richmedia.a.h> c;
    
    public f(final Activity b, final h a) {
        this.c = new ArrayList<com.tremorvideo.sdk.android.richmedia.a.h>();
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void a() {
        this.a.a(this.c);
    }
    
    @Override
    public void a(final float n, final int n2) {
        final com.tremorvideo.sdk.android.richmedia.a.h b = this.b(n2);
        if (b != null) {
            b.a(n);
        }
    }
    
    @Override
    public void a(final int n) {
        this.a.a(this.b(n));
    }
    
    @Override
    public void a(final RelativeLayout$LayoutParams relativeLayout$LayoutParams, final int n) {
        final com.tremorvideo.sdk.android.richmedia.a.h b = this.b(n);
        if (b != null && this.a != null) {
            this.a.a(relativeLayout$LayoutParams, b);
        }
    }
    
    @Override
    public void a(final com.tremorvideo.sdk.android.richmedia.a.h h) {
        this.a.c(h);
    }
    
    @Override
    public void a(final String s, final String s2, final int n) {
        try {
            final com.tremorvideo.sdk.android.richmedia.a.h h = new com.tremorvideo.sdk.android.richmedia.a.h((Context)this.b, n, s, new JSONObject(s2), this.a.t().r().f);
            h.a(this);
            this.c.add(h);
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void a(final String s, final List<NameValuePair> list) {
        this.a.b(s, list);
    }
    
    public com.tremorvideo.sdk.android.richmedia.a.h b(final int n) {
        if (this.c != null) {
            for (final com.tremorvideo.sdk.android.richmedia.a.h h : this.c) {
                if (n == h.a()) {
                    return h;
                }
            }
        }
        return null;
    }
    
    public void b() {
        if (this.c != null) {
            this.c.clear();
            this.c = null;
            System.gc();
        }
    }
}
