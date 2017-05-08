// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.sql.Timestamp;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

final class a implements k<Date>, s<Date>
{
    private final DateFormat a;
    private final DateFormat b;
    private final DateFormat c;
    
    a() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }
    
    public a(final int n, final int n2) {
        this(DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }
    
    a(final String s) {
        this(new SimpleDateFormat(s, Locale.US), new SimpleDateFormat(s));
    }
    
    a(final DateFormat a, final DateFormat b) {
        this.a = a;
        this.b = b;
        (this.c = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)).setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private Date a(l l) {
        final DateFormat b = this.b;
        // monitorenter(b)
        try {
            return this.b.parse(l.b());
        }
        catch (ParseException ex2) {
            final a a = this;
            final DateFormat dateFormat = a.a;
            final l i = l;
            final String s = i.b();
            final Date parse = dateFormat.parse(s);
            return parse;
        }
        finally {
            final l j;
            l = j;
        }
        // monitorexit(b)
        try {
            final a a = this;
            final DateFormat dateFormat = a.a;
            final l i = l;
            final String s = i.b();
            final Date parse2;
            final Date parse = parse2 = dateFormat.parse(s);
            return parse2;
        }
        catch (ParseException ex3) {
            try {
                // monitorexit(b)
                return this.c.parse(l.b());
            }
            catch (ParseException ex) {
                throw new t(l.b(), ex);
            }
        }
    }
    
    @Override
    public l a(final Date date, final Type type, final r r) {
        synchronized (this.b) {
            return new q(this.a.format(date));
        }
    }
    
    public Date a(final l l, final Type type, final j j) throws p {
        if (!(l instanceof q)) {
            throw new p("The date should be a string value");
        }
        final Date a = this.a(l);
        if (type == Date.class) {
            return a;
        }
        if (type == Timestamp.class) {
            return new Timestamp(a.getTime());
        }
        if (type == java.sql.Date.class) {
            return new java.sql.Date(a.getTime());
        }
        throw new IllegalArgumentException(this.getClass() + " cannot deserialize to " + type);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(a.class.getSimpleName());
        sb.append('(').append(this.b.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
}
