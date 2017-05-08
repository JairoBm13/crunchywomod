// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.b.j;
import java.io.Writer;
import com.google.ads.interactivemedia.v3.a.d.c;
import java.io.StringWriter;

public abstract class l
{
    public Number a() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public String b() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public double c() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public long d() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public int e() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public boolean f() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    public boolean g() {
        return this instanceof i;
    }
    
    public boolean h() {
        return this instanceof o;
    }
    
    public boolean i() {
        return this instanceof q;
    }
    
    public boolean j() {
        return this instanceof n;
    }
    
    public o k() {
        if (this.h()) {
            return (o)this;
        }
        throw new IllegalStateException("Not a JSON Object: " + this);
    }
    
    public i l() {
        if (this.g()) {
            return (i)this;
        }
        throw new IllegalStateException("This is not a JSON Array.");
    }
    
    public q m() {
        if (this.i()) {
            return (q)this;
        }
        throw new IllegalStateException("This is not a JSON Primitive.");
    }
    
    Boolean n() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName());
    }
    
    @Override
    public String toString() {
        try {
            final StringWriter stringWriter = new StringWriter();
            final c c = new c(stringWriter);
            c.b(true);
            j.a(this, c);
            return stringWriter.toString();
        }
        catch (IOException ex) {
            throw new AssertionError((Object)ex);
        }
    }
}
