// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

public interface CastEventListener
{
    void onMediaChanged(final long p0);
    
    void onPlaybackStop();
    
    void onTeardown();
}
