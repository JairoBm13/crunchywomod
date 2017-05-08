// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

public class av
{
    private bs a;
    private int b;
    private boolean c;
    private String d;
    private int e;
    
    public av(final bs a, final String d) {
        this.a = a;
        this.d = d;
        this.b = 0;
        this.e = 0;
        this.c = false;
    }
    
    public void a() {
        ++this.b;
        this.c = false;
    }
    
    public boolean b() {
        return this.e > this.b;
    }
    
    public boolean c() {
        ac.e(this.e + " out of " + this.a.b().size());
        return this.e >= this.a.b().size();
    }
    
    public n d() {
        final n n = this.a.b().get(this.e);
        if (this.e < this.a.b().size()) {
            ++this.e;
            if ((this.e == 1 && this.a.b().size() > 1) || this.a.b().size() == 1) {
                bx.a(bx.a.c, true);
            }
        }
        this.c = true;
        return n;
    }
    
    public boolean e() {
        return this.b >= this.a.b().size();
    }
    
    public n f() {
        return this.a.b().get(this.b);
    }
    
    public n g() {
        if (this.e < this.a.b().size()) {
            return this.a.b().get(this.e);
        }
        return null;
    }
    
    public String h() {
        return this.d;
    }
    
    public bs i() {
        return this.a;
    }
    
    public void j() {
        this.a.b().remove(this.e);
    }
}
