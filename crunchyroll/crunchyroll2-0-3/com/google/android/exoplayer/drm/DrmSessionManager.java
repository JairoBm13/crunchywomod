// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.drm;

import android.media.MediaCrypto;
import android.annotation.TargetApi;

@TargetApi(16)
public interface DrmSessionManager
{
    void close();
    
    Exception getError();
    
    MediaCrypto getMediaCrypto();
    
    int getState();
    
    void open(final DrmInitData p0);
    
    boolean requiresSecureDecoderComponent(final String p0);
}
