// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.Context;
import org.apache.http.NameValuePair;
import java.util.List;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.content.Intent;
import android.app.Activity;

public abstract class a
{
    protected Activity c;
    protected a d;
    
    public a(final a d, final Activity c) {
        this.d = d;
        this.c = c;
    }
    
    public void a() {
    }
    
    public void a(final int n, final int n2, final Intent intent) {
    }
    
    public void a(final Configuration configuration) {
    }
    
    public boolean a(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    public void b() {
    }
    
    public void c() {
    }
    
    public void d() {
    }
    
    public boolean j() {
        return true;
    }
    
    public void k() {
    }
    
    public void l() {
    }
    
    public boolean m() {
        return false;
    }
    
    public b n() {
        return b.d;
    }
    
    public void o() {
    }
    
    public void p() {
    }
    
    public void q() {
    }
    
    public interface a
    {
        int a(final aw p0);
        
        int a(final aw p0, final int p1);
        
        int a(final aw p0, final int p1, final List<NameValuePair> p2);
        
        void a(final int p0);
        
        void a(final com.tremorvideo.sdk.android.videoad.a p0);
        
        void b(final int p0);
        
        n g();
        
        Context h();
        
        int i();
        
        int j();
        
        void l();
    }
    
    public enum b
    {
        a, 
        b, 
        c, 
        d;
    }
}
