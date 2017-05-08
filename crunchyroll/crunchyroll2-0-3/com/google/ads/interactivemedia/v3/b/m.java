// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;

class m implements VideoAdPlayerCallback
{
    private v a;
    
    public m(final v a) {
        this.a = a;
    }
    
    @Override
    public void onEnded() {
        this.a.c();
    }
    
    @Override
    public void onError() {
        this.a.c();
    }
    
    @Override
    public void onPause() {
        this.a.c();
    }
    
    @Override
    public void onPlay() {
        this.a.a();
    }
    
    @Override
    public void onResume() {
        this.a.a();
    }
}
