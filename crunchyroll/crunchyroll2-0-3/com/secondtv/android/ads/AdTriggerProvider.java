// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

public interface AdTriggerProvider
{
    int getPlayhead();
    
    boolean isPlaying();
    
    boolean isReady();
    
    void onAdStart();
}
