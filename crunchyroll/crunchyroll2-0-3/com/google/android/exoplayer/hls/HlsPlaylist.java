// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

public abstract class HlsPlaylist
{
    public final String baseUri;
    public final int type;
    
    protected HlsPlaylist(final String baseUri, final int type) {
        this.baseUri = baseUri;
        this.type = type;
    }
}
