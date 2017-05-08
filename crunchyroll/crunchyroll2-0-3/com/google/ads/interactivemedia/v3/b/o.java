// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import java.util.List;

public class o
{
    private final List<AdErrorEvent.AdErrorListener> a;
    
    public o() {
        this.a = new ArrayList<AdErrorEvent.AdErrorListener>(1);
    }
    
    public void a(final AdErrorEvent.AdErrorListener adErrorListener) {
        this.a.add(adErrorListener);
    }
    
    public void a(final AdErrorEvent adErrorEvent) {
        final Iterator<AdErrorEvent.AdErrorListener> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAdError(adErrorEvent);
        }
    }
}
