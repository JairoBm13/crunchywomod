// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import com.google.ads.interactivemedia.v3.a.d.a;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.d.c;
import com.google.ads.interactivemedia.v3.a.b.a.e;

public abstract class w<T>
{
    public final l a(final T t) {
        try {
            final e e = new e();
            this.a(e, t);
            return e.a();
        }
        catch (IOException ex) {
            throw new m(ex);
        }
    }
    
    public abstract void a(final c p0, final T p1) throws IOException;
    
    public abstract T b(final a p0) throws IOException;
}
