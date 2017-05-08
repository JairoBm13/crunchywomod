// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.AdError;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;

public class b implements AdErrorEvent
{
    private final AdError a;
    private final Object b;
    
    b(final AdError a) {
        this.a = a;
        this.b = null;
    }
    
    b(final AdError a, final Object b) {
        this.a = a;
        this.b = b;
    }
}
