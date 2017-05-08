// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.Iterator;
import java.util.Map;
import com.google.ads.interactivemedia.v3.api.Ad;
import com.google.ads.interactivemedia.v3.api.AdEvent;

public class c implements AdEvent
{
    private AdEventType a;
    private Ad b;
    private Map<String, String> c;
    
    c(final AdEventType a, final Ad b, final Map<String, String> c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    private String a() {
        if (this.c == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder("{");
        final Iterator<Map.Entry<String, String>> iterator = this.c.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof c)) {
                return false;
            }
            final c c = (c)o;
            if (this.b == null) {
                if (c.b != null) {
                    return false;
                }
            }
            else if (!this.b.equals(c.b)) {
                return false;
            }
            if (this.a != c.a) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public AdEventType getType() {
        return this.a;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append(String.format("AdEvent[type=%s, ad=%s", this.a, this.b));
        String format;
        if (this.c == null) {
            format = "]";
        }
        else {
            format = String.format(", adData=%s]", this.a());
        }
        return append.append(format).toString();
    }
}
