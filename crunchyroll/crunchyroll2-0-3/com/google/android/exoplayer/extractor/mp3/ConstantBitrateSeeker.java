// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.mp3;

final class ConstantBitrateSeeker implements Seeker
{
    private final int bitrate;
    private final long durationUs;
    private final long firstFramePosition;
    
    public ConstantBitrateSeeker(long timeUs, final int bitrate, final long n) {
        final long n2 = -1L;
        this.firstFramePosition = timeUs;
        this.bitrate = bitrate;
        if (n == -1L) {
            timeUs = n2;
        }
        else {
            timeUs = this.getTimeUs(n);
        }
        this.durationUs = timeUs;
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public long getTimeUs(final long n) {
        return (n - this.firstFramePosition) * 1000000L * 8L / this.bitrate;
    }
}
