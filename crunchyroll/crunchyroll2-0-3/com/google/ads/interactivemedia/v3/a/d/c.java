// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.d;

import java.io.IOException;
import java.io.Writer;
import java.io.Flushable;
import java.io.Closeable;

public class c implements Closeable, Flushable
{
    private static final String[] a;
    private static final String[] b;
    private final Writer c;
    private int[] d;
    private int e;
    private String f;
    private String g;
    private boolean h;
    private boolean i;
    private String j;
    private boolean k;
    
    static {
        a = new String[128];
        for (int i = 0; i <= 31; ++i) {
            c.a[i] = String.format("\\u%04x", i);
        }
        c.a[34] = "\\\"";
        c.a[92] = "\\\\";
        c.a[9] = "\\t";
        c.a[8] = "\\b";
        c.a[10] = "\\n";
        c.a[13] = "\\r";
        c.a[12] = "\\f";
        (b = c.a.clone())[60] = "\\u003c";
        c.b[62] = "\\u003e";
        c.b[38] = "\\u0026";
        c.b[61] = "\\u003d";
        c.b[39] = "\\u0027";
    }
    
    public c(final Writer c) {
        this.d = new int[32];
        this.e = 0;
        this.a(6);
        this.g = ":";
        this.k = true;
        if (c == null) {
            throw new NullPointerException("out == null");
        }
        this.c = c;
    }
    
    private int a() {
        if (this.e == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.d[this.e - 1];
    }
    
    private c a(final int n, final int n2, final String s) throws IOException {
        final int a = this.a();
        if (a != n2 && a != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.j != null) {
            throw new IllegalStateException("Dangling name: " + this.j);
        }
        --this.e;
        if (a == n2) {
            this.k();
        }
        this.c.write(s);
        return this;
    }
    
    private c a(final int n, final String s) throws IOException {
        this.e(true);
        this.a(n);
        this.c.write(s);
        return this;
    }
    
    private void a(final int n) {
        if (this.e == this.d.length) {
            final int[] d = new int[this.e * 2];
            System.arraycopy(this.d, 0, d, 0, this.e);
            this.d = d;
        }
        this.d[this.e++] = n;
    }
    
    private void b(final int n) {
        this.d[this.e - 1] = n;
    }
    
    private void d(final String s) throws IOException {
        int n = 0;
        String[] array;
        if (this.i) {
            array = com.google.ads.interactivemedia.v3.a.d.c.b;
        }
        else {
            array = com.google.ads.interactivemedia.v3.a.d.c.a;
        }
        this.c.write("\"");
        final int length = s.length();
        int i = 0;
    Label_0071_Outer:
        while (i < length) {
            final char char1 = s.charAt(i);
            while (true) {
                String s2 = null;
                Label_0101: {
                    int n2;
                    if (char1 < '\u0080') {
                        if ((s2 = array[char1]) != null) {
                            break Label_0101;
                        }
                        n2 = n;
                    }
                    else {
                        if (char1 == '\u2028') {
                            s2 = "\\u2028";
                            break Label_0101;
                        }
                        n2 = n;
                        if (char1 == '\u2029') {
                            s2 = "\\u2029";
                            break Label_0101;
                        }
                    }
                    ++i;
                    n = n2;
                    continue Label_0071_Outer;
                }
                if (n < i) {
                    this.c.write(s, n, i - n);
                }
                this.c.write(s2);
                int n2 = i + 1;
                continue;
            }
        }
        if (n < length) {
            this.c.write(s, n, length - n);
        }
        this.c.write("\"");
    }
    
    private void e(final boolean b) throws IOException {
        switch (this.a()) {
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
            case 7: {
                if (!this.h) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                if (!this.h && !b) {
                    throw new IllegalStateException("JSON must start with an array or an object.");
                }
                this.b(7);
            }
            case 1: {
                this.b(2);
                this.k();
            }
            case 2: {
                this.c.append(',');
                this.k();
            }
            case 4: {
                this.c.append((CharSequence)this.g);
                this.b(5);
            }
        }
    }
    
    private void j() throws IOException {
        if (this.j != null) {
            this.l();
            this.d(this.j);
            this.j = null;
        }
    }
    
    private void k() throws IOException {
        if (this.f != null) {
            this.c.write("\n");
            for (int i = 1; i < this.e; ++i) {
                this.c.write(this.f);
            }
        }
    }
    
    private void l() throws IOException {
        final int a = this.a();
        if (a == 5) {
            this.c.write(44);
        }
        else if (a != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.k();
        this.b(4);
    }
    
    public c a(final long n) throws IOException {
        this.j();
        this.e(false);
        this.c.write(Long.toString(n));
        return this;
    }
    
    public c a(final Number n) throws IOException {
        if (n == null) {
            return this.f();
        }
        this.j();
        final String string = n.toString();
        if (!this.h && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + n);
        }
        this.e(false);
        this.c.append((CharSequence)string);
        return this;
    }
    
    public c a(final String j) throws IOException {
        if (j == null) {
            throw new NullPointerException("name == null");
        }
        if (this.j != null) {
            throw new IllegalStateException();
        }
        if (this.e == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.j = j;
        return this;
    }
    
    public c a(final boolean b) throws IOException {
        this.j();
        this.e(false);
        final Writer c = this.c;
        String s;
        if (b) {
            s = "true";
        }
        else {
            s = "false";
        }
        c.write(s);
        return this;
    }
    
    public c b() throws IOException {
        this.j();
        return this.a(1, "[");
    }
    
    public c b(final String s) throws IOException {
        if (s == null) {
            return this.f();
        }
        this.j();
        this.e(false);
        this.d(s);
        return this;
    }
    
    public final void b(final boolean h) {
        this.h = h;
    }
    
    public c c() throws IOException {
        return this.a(1, 2, "]");
    }
    
    public final void c(final String f) {
        if (f.length() == 0) {
            this.f = null;
            this.g = ":";
            return;
        }
        this.f = f;
        this.g = ": ";
    }
    
    public final void c(final boolean i) {
        this.i = i;
    }
    
    @Override
    public void close() throws IOException {
        this.c.close();
        final int e = this.e;
        if (e > 1 || (e == 1 && this.d[e - 1] != 7)) {
            throw new IOException("Incomplete document");
        }
        this.e = 0;
    }
    
    public c d() throws IOException {
        this.j();
        return this.a(3, "{");
    }
    
    public final void d(final boolean k) {
        this.k = k;
    }
    
    public c e() throws IOException {
        return this.a(3, 5, "}");
    }
    
    public c f() throws IOException {
        if (this.j != null) {
            if (!this.k) {
                this.j = null;
                return this;
            }
            this.j();
        }
        this.e(false);
        this.c.write("null");
        return this;
    }
    
    @Override
    public void flush() throws IOException {
        if (this.e == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.c.flush();
    }
    
    public boolean g() {
        return this.h;
    }
    
    public final boolean h() {
        return this.i;
    }
    
    public final boolean i() {
        return this.k;
    }
}
