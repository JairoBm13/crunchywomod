// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.d.b;
import java.text.ParseException;
import com.google.ads.interactivemedia.v3.a.t;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.f;
import java.text.DateFormat;
import com.google.ads.interactivemedia.v3.a.x;
import java.util.Date;
import com.google.ads.interactivemedia.v3.a.w;

public final class c extends w<Date>
{
    public static final x a;
    private final DateFormat b;
    private final DateFormat c;
    private final DateFormat d;
    
    static {
        a = new x() {
            @Override
            public <T> w<T> a(final f f, final a<T> a) {
                if (a.a() == Date.class) {
                    return (w<T>)new c();
                }
                return null;
            }
        };
    }
    
    public c() {
        this.b = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.c = DateFormat.getDateTimeInstance(2, 2);
        this.d = a();
    }
    
    private static DateFormat a() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }
    
    private Date a(String s) {
        synchronized (this) {
            try {
                s = this.c.parse((String)s);
                return (Date)s;
            }
            catch (ParseException ex2) {
                try {
                    s = this.b.parse((String)s);
                }
                catch (ParseException ex3) {
                    try {
                        s = this.d.parse((String)s);
                    }
                    catch (ParseException ex) {
                        throw new t((String)s, ex);
                    }
                }
            }
        }
    }
    
    public Date a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
            a.j();
            return null;
        }
        return this.a(a.h());
    }
    
    @Override
    public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Date date) throws IOException {
        // monitorenter(this)
        Label_0014: {
            if (date != null) {
                break Label_0014;
            }
            try {
                c.f();
                return;
                c.b(this.b.format(date));
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
