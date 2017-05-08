// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.extractor.TrackOutput;

abstract class ElementaryStreamReader
{
    protected final TrackOutput output;
    
    protected ElementaryStreamReader(final TrackOutput output) {
        this.output = output;
    }
    
    public abstract void consume(final ParsableByteArray p0, final long p1, final boolean p2);
    
    public abstract void packetFinished();
}
