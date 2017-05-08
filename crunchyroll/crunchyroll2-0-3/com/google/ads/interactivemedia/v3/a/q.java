// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import com.google.ads.interactivemedia.v3.a.b.a;
import com.google.ads.interactivemedia.v3.a.b.f;
import java.math.BigInteger;

public final class q extends l
{
    private static final Class<?>[] a;
    private Object b;
    
    static {
        a = new Class[] { Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
    }
    
    public q(final Boolean b) {
        this.a(b);
    }
    
    public q(final Number n) {
        this.a(n);
    }
    
    public q(final String s) {
        this.a(s);
    }
    
    private static boolean a(final q q) {
        if (q.b instanceof Number) {
            final Number n = (Number)q.b;
            return n instanceof BigInteger || n instanceof Long || n instanceof Integer || n instanceof Short || n instanceof Byte;
        }
        return false;
    }
    
    private static boolean b(final Object o) {
        if (!(o instanceof String)) {
            final Class<?> class1 = o.getClass();
            final Class<?>[] a = q.a;
            for (int length = a.length, i = 0; i < length; ++i) {
                if (a[i].isAssignableFrom(class1)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public Number a() {
        if (this.b instanceof String) {
            return new f((String)this.b);
        }
        return (Number)this.b;
    }
    
    void a(final Object b) {
        if (b instanceof Character) {
            this.b = String.valueOf((char)b);
            return;
        }
        com.google.ads.interactivemedia.v3.a.b.a.a(b instanceof Number || b(b));
        this.b = b;
    }
    
    @Override
    public String b() {
        if (this.p()) {
            return this.a().toString();
        }
        if (this.o()) {
            return this.n().toString();
        }
        return (String)this.b;
    }
    
    @Override
    public double c() {
        if (this.p()) {
            return this.a().doubleValue();
        }
        return Double.parseDouble(this.b());
    }
    
    @Override
    public long d() {
        if (this.p()) {
            return this.a().longValue();
        }
        return Long.parseLong(this.b());
    }
    
    @Override
    public int e() {
        if (this.p()) {
            return this.a().intValue();
        }
        return Integer.parseInt(this.b());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final q q = (q)o;
            if (this.b == null) {
                if (q.b != null) {
                    return false;
                }
            }
            else if (a(this) && a(q)) {
                if (this.a().longValue() != q.a().longValue()) {
                    return false;
                }
            }
            else {
                if (this.b instanceof Number && q.b instanceof Number) {
                    final double doubleValue = this.a().doubleValue();
                    final double doubleValue2 = q.a().doubleValue();
                    if (doubleValue != doubleValue2) {
                        boolean b2 = b;
                        if (!Double.isNaN(doubleValue)) {
                            return b2;
                        }
                        b2 = b;
                        if (!Double.isNaN(doubleValue2)) {
                            return b2;
                        }
                    }
                    return true;
                }
                return this.b.equals(q.b);
            }
        }
        return true;
    }
    
    @Override
    public boolean f() {
        if (this.o()) {
            return this.n();
        }
        return Boolean.parseBoolean(this.b());
    }
    
    @Override
    public int hashCode() {
        if (this.b == null) {
            return 31;
        }
        if (a(this)) {
            final long longValue = this.a().longValue();
            return (int)(longValue ^ longValue >>> 32);
        }
        if (this.b instanceof Number) {
            final long doubleToLongBits = Double.doubleToLongBits(this.a().doubleValue());
            return (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        }
        return this.b.hashCode();
    }
    
    @Override
    Boolean n() {
        return (Boolean)this.b;
    }
    
    public boolean o() {
        return this.b instanceof Boolean;
    }
    
    public boolean p() {
        return this.b instanceof Number;
    }
    
    public boolean q() {
        return this.b instanceof String;
    }
}
