// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.Date;
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
import java.sql.Time;
import com.google.ads.interactivemedia.v3.a.w;

public final class j extends w<Time>
{
    public static final x a;
    private final DateFormat b;
    
    static {
        a = new x() {
            @Override
            public <T> w<T> a(final f f, final a<T> a) {
                if (a.a() == Time.class) {
                    return (w<T>)new j();
                }
                return null;
            }
        };
    }
    
    public j() {
        this.b = new SimpleDateFormat("hh:mm:ss a");
    }
    
    public Time a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        synchronized (this) {
            Time time;
            if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                time = null;
            }
            else {
                try {
                    time = new Time(this.b.parse(a.h()).getTime());
                }
                catch (ParseException ex) {
                    throw new t(ex);
                }
            }
            return time;
        }
    }
    
    @Override
    public void a(final c c, final Time time) throws IOException {
        // monitorenter(this)
        Label_0017: {
            if (time != null) {
                break Label_0017;
            }
            String format = null;
            try {
                while (true) {
                    c.b(format);
                    return;
                    format = this.b.format(time);
                    continue;
                }
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
