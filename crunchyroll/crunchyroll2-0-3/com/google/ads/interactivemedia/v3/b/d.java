// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;

public class d implements VideoAdPlayerCallback, a
{
    private s a;
    private String b;
    private boolean c;
    private boolean d;
    private v e;
    
    public d(final s a, final String b, final v e) {
        this.c = false;
        this.d = false;
        this.a = a;
        this.b = b;
        this.e = e;
    }
    
    @Override
    public void a(final VideoProgressUpdate videoProgressUpdate) {
        if (videoProgressUpdate != null && videoProgressUpdate.getDuration() > 0.0f) {
            if (!this.d && videoProgressUpdate.getCurrentTime() > 0.0f) {
                this.a(r.c.start);
                this.d = true;
            }
            this.a(r.c.timeupdate, videoProgressUpdate);
        }
    }
    
    void a(final r.c c) {
        this.a(c, null);
    }
    
    void a(final r.c c, final VideoProgressUpdate videoProgressUpdate) {
        this.a.b(new r(r.b.videoDisplay, c, this.b, videoProgressUpdate));
    }
    
    @Override
    public void onEnded() {
        this.e.c();
        this.a(r.c.end);
    }
    
    @Override
    public void onError() {
        this.e.c();
        this.a(r.c.error);
    }
    
    @Override
    public void onPause() {
        this.e.c();
        this.a(r.c.pause);
    }
    
    @Override
    public void onPlay() {
        this.e.b();
        this.d = false;
    }
    
    @Override
    public void onResume() {
        this.e.b();
        this.a(r.c.play);
    }
}
