// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

public final class PtsTimestampAdjuster
{
    private final long firstSampleTimestampUs;
    private long lastPts;
    private long timestampOffsetUs;
    
    public PtsTimestampAdjuster(final long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        this.lastPts = Long.MIN_VALUE;
    }
    
    public long adjustTimestamp(long n) {
        long lastPts = n;
        if (this.lastPts != Long.MIN_VALUE) {
            final long n2 = (this.lastPts + 4294967296L) / 8589934592L;
            lastPts = n + 8589934592L * (n2 - 1L);
            n += 8589934592L * n2;
            if (Math.abs(lastPts - this.lastPts) >= Math.abs(n - this.lastPts)) {
                lastPts = n;
            }
        }
        n = 1000000L * lastPts / 90000L;
        if (this.lastPts == Long.MIN_VALUE) {
            this.timestampOffsetUs = this.firstSampleTimestampUs - n;
        }
        this.lastPts = lastPts;
        return this.timestampOffsetUs + n;
    }
}
