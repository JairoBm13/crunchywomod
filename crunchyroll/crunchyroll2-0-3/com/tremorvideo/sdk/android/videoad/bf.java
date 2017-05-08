// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

public abstract class bf
{
    private c a;
    private b b;
    private a c;
    
    public bf(final a c) {
        this.c = c;
        this.b = bf.b.a;
        this.a = bf.c.a;
    }
    
    public b a() {
        return this.b;
    }
    
    protected void a(final b b) {
        this.a = bf.c.b;
        this.b = b;
        this.c.a(this);
    }
    
    public void a(final String s, final n n) throws Exception {
    }
    
    public void b() {
        if (this.a == bf.c.a) {
            this.a = bf.c.c;
            this.b = bf.b.a;
            this.e();
        }
    }
    
    public void c() {
        if (this.a == bf.c.c) {
            this.f();
        }
    }
    
    public void d() {
        if (this.a == bf.c.b) {
            this.a = bf.c.c;
            this.g();
        }
    }
    
    protected abstract void e();
    
    protected abstract void f();
    
    protected abstract void g();
    
    public interface a
    {
        void a(final bf p0);
    }
    
    public enum b
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f;
    }
    
    public enum c
    {
        a, 
        b, 
        c;
    }
    
    public enum d
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i, 
        j;
    }
}
