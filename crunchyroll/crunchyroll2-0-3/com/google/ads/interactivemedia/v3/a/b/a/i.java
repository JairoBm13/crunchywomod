// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import com.google.ads.interactivemedia.v3.a.d.c;
import java.io.IOException;
import java.text.ParseException;
import com.google.ads.interactivemedia.v3.a.t;
import com.google.ads.interactivemedia.v3.a.d.b;
import java.text.SimpleDateFormat;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.f;
import java.text.DateFormat;
import com.google.ads.interactivemedia.v3.a.x;
import java.sql.Date;
import com.google.ads.interactivemedia.v3.a.w;

public final class i extends w<Date>
{
    public static final x a;
    private final DateFormat b;
    
    static {
        a = new x() {
            @Override
            public <T> w<T> a(final f f, final a<T> a) {
                if (a.a() == Date.class) {
                    return (w<T>)new i();
                }
                return null;
            }
        };
    }
    
    public i() {
        this.b = new SimpleDateFormat("MMM d, yyyy");
    }
    
    public Date a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        synchronized (this) {
            Date date;
            if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                date = null;
            }
            else {
                try {
                    date = new Date(this.b.parse(a.h()).getTime());
                }
                catch (ParseException ex) {
                    throw new t(ex);
                }
            }
            return date;
        }
    }
    
    @Override
    public void a(final c c, final Date date) throws IOException {
        // monitorenter(this)
        Label_0017: {
            if (date != null) {
                break Label_0017;
            }
            String format = null;
            try {
                while (true) {
                    c.b(format);
                    return;
                    format = this.b.format(date);
                    continue;
                }
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
