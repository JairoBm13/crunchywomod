// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api.player;

public class VideoProgressUpdate
{
    public static final VideoProgressUpdate VIDEO_TIME_NOT_READY;
    private float currentTime;
    private float duration;
    
    static {
        VIDEO_TIME_NOT_READY = new VideoProgressUpdate(-1L, -1L);
    }
    
    public VideoProgressUpdate() {
        this(-1L, -1L);
    }
    
    public VideoProgressUpdate(final long n, final long n2) {
        this.currentTime = n / 1000.0f;
        this.duration = n2 / 1000.0f;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final VideoProgressUpdate videoProgressUpdate = (VideoProgressUpdate)o;
            if (Float.floatToIntBits(this.currentTime) != Float.floatToIntBits(videoProgressUpdate.currentTime)) {
                return false;
            }
            if (Float.floatToIntBits(this.duration) != Float.floatToIntBits(videoProgressUpdate.duration)) {
                return false;
            }
        }
        return true;
    }
    
    public float getCurrentTime() {
        return this.currentTime;
    }
    
    public float getDuration() {
        return this.duration;
    }
    
    @Override
    public String toString() {
        return "VideoProgressUpdate [currentTime=" + this.currentTime + ", duration=" + this.duration + "]";
    }
}
