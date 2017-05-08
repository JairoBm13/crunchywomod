// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import android.view.ViewGroup;

public interface AdDisplayContainer
{
    ViewGroup getAdContainer();
    
    VideoAdPlayer getPlayer();
    
    void setAdContainer(final ViewGroup p0);
    
    void setPlayer(final VideoAdPlayer p0);
}
