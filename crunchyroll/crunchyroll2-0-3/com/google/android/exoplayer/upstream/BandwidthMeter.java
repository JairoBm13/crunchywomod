// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

public interface BandwidthMeter extends TransferListener
{
    long getBitrateEstimate();
    
    public interface EventListener
    {
        void onBandwidthSample(final int p0, final long p1, final long p2);
    }
}
