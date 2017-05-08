// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import java.util.List;

public final class HlsMasterPlaylist extends HlsPlaylist
{
    public final List<Subtitle> subtitles;
    public final List<Variant> variants;
    
    public HlsMasterPlaylist(final String s, final List<Variant> variants, final List<Subtitle> subtitles) {
        super(s, 0);
        this.variants = variants;
        this.subtitles = subtitles;
    }
}
