// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import com.google.ads.interactivemedia.v3.a.d.c;
import java.io.Writer;
import com.google.ads.interactivemedia.v3.a.p;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.m;
import com.google.ads.interactivemedia.v3.a.d.d;
import java.io.EOFException;
import com.google.ads.interactivemedia.v3.a.t;
import com.google.ads.interactivemedia.v3.a.n;
import com.google.ads.interactivemedia.v3.a.l;
import com.google.ads.interactivemedia.v3.a.d.a;

public final class j
{
    public static l a(final com.google.ads.interactivemedia.v3.a.d.a a) throws p {
        boolean b = true;
        try {
            a.f();
            b = false;
            return com.google.ads.interactivemedia.v3.a.b.a.l.P.b(a);
        }
        catch (EOFException ex) {
            if (b) {
                return n.a;
            }
            throw new t(ex);
        }
        catch (d d) {
            throw new t(d);
        }
        catch (IOException ex2) {
            throw new m(ex2);
        }
        catch (NumberFormatException ex3) {
            throw new t(ex3);
        }
    }
    
    public static Writer a(final Appendable appendable) {
        if (appendable instanceof Writer) {
            return (Writer)appendable;
        }
        return new a(appendable);
    }
    
    public static void a(final l l, final c c) throws IOException {
        com.google.ads.interactivemedia.v3.a.b.a.l.P.a(c, l);
    }
    
    private static final class a extends Writer
    {
        private final Appendable a;
        private final j.a.a b;
        
        private a(final Appendable a) {
            this.b = new j.a.a();
            this.a = a;
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void write(final int n) throws IOException {
            this.a.append((char)n);
        }
        
        @Override
        public void write(final char[] a, final int n, final int n2) throws IOException {
            this.b.a = a;
            this.a.append(this.b, n, n + n2);
        }
        
        static class a implements CharSequence
        {
            char[] a;
            
            @Override
            public char charAt(final int n) {
                return this.a[n];
            }
            
            @Override
            public int length() {
                return this.a.length;
            }
            
            @Override
            public CharSequence subSequence(final int n, final int n2) {
                return new String(this.a, n, n2 - n);
            }
        }
    }
}
