// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api.player;

public interface VideoAdPlayer
{
    void addCallback(final VideoAdPlayerCallback p0);
    
    VideoProgressUpdate getProgress();
    
    void loadAd(final String p0);
    
    void pauseAd();
    
    void playAd();
    
    void removeCallback(final VideoAdPlayerCallback p0);
    
    void stopAd();
    
    public interface VideoAdPlayerCallback
    {
        void onEnded();
        
        void onError();
        
        void onPause();
        
        void onPlay();
        
        void onResume();
    }
}
