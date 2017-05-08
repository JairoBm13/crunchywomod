// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.res.Resources;
import android.content.Context;
import android.view.KeyEvent;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.Intent;
import org.apache.http.NameValuePair;
import java.util.List;
import android.app.Activity;

public class k implements a, l
{
    public static n a;
    public static a b;
    com.tremorvideo.sdk.android.videoad.a c;
    Activity d;
    a e;
    n f;
    
    @Override
    public int a(final aw aw) {
        return this.e.a(aw);
    }
    
    @Override
    public int a(final aw aw, final int n) {
        return this.e.a(aw, n);
    }
    
    @Override
    public int a(final aw aw, final int n, final List<NameValuePair> list) {
        return this.e.a(aw, n, list);
    }
    
    @Override
    public void a() {
        this.c.a();
    }
    
    @Override
    public void a(final float n, final float n2, final int n3) {
    }
    
    @Override
    public void a(final int n) {
        this.e.a(n);
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
    }
    
    @Override
    public void a(final Configuration configuration) {
        this.c.a(configuration);
    }
    
    @Override
    public void a(final Bundle bundle, final Activity d) {
        this.f = k.a;
        this.e = k.b;
        k.a = null;
        k.b = null;
        (this.d = d).requestWindowFeature(1);
        this.d.getWindow().setFlags(1024, 1024);
        this.c = new com.tremorvideo.sdk.android.videoad.b(this, this.d, this.f, false);
    }
    
    @Override
    public void a(final com.tremorvideo.sdk.android.videoad.a a) {
        final int intExtra = this.d.getIntent().getIntExtra("curEventID", -1);
        final Intent intent = new Intent();
        if (intExtra > -1) {
            intent.putExtra("endEventId", intExtra);
        }
        this.d.setResult(100, intent);
        this.d.finish();
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        return this.c.a(n, keyEvent);
    }
    
    @Override
    public void b() {
        this.c.b();
    }
    
    @Override
    public void b(final int n) {
    }
    
    @Override
    public void c() {
        this.c.c();
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public void e() {
    }
    
    @Override
    public void f() {
    }
    
    @Override
    public n g() {
        return this.f;
    }
    
    @Override
    public Context h() {
        return (Context)this.d;
    }
    
    @Override
    public int i() {
        if (Resources.getSystem().getConfiguration().orientation == 2) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public int j() {
        return -1;
    }
    
    @Override
    public void l() {
    }
}
