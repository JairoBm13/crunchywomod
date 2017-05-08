// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatWrapper;

public final class Variant implements FormatWrapper
{
    public final Format format;
    public final String url;
    
    public Variant(final int n, final String url, final int n2, final String s, final int n3, final int n4) {
        this.url = url;
        this.format = new Format(Integer.toString(n), "application/x-mpegURL", n3, n4, -1.0f, -1, -1, n2, null, s);
    }
    
    @Override
    public Format getFormat() {
        return this.format;
    }
}
