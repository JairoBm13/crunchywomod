// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

public interface TremorAdStateListener
{
    void adComplete(final boolean p0, final int p1);
    
    void adReady(final boolean p0);
    
    void adStart();
    
    void leftApp();
}
