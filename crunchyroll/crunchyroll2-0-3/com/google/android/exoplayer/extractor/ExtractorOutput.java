// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor;

public interface ExtractorOutput
{
    void endTracks();
    
    void seekMap(final SeekMap p0);
    
    TrackOutput track(final int p0);
}
