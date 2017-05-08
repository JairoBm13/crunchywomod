// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import java.util.SortedSet;

public class n implements a
{
    private final SortedSet<Float> a;
    private s b;
    private String c;
    private float d;
    
    public n(final s b, final SortedSet<Float> a, final String c) {
        this.d = 0.0f;
        this.b = b;
        this.c = c;
        this.a = a;
    }
    
    private SortedSet<Float> a(final float n) {
        if (this.d < n) {
            return this.a.subSet(this.d, n);
        }
        return this.a.subSet(n, this.d);
    }
    
    @Override
    public void a(final VideoProgressUpdate videoProgressUpdate) {
        if (videoProgressUpdate != null && videoProgressUpdate.getDuration() >= 0.0f) {
            int n;
            if (!this.a(videoProgressUpdate.getCurrentTime()).isEmpty()) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.d = videoProgressUpdate.getCurrentTime();
            if (n != 0) {
                this.b.b(new r(r.b.contentTimeUpdate, r.c.contentTimeUpdate, this.c, videoProgressUpdate));
            }
        }
    }
}
