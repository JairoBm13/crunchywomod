// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.chunk;

public final class ChunkOperationHolder
{
    public Chunk chunk;
    public boolean endOfStream;
    public int queueSize;
    
    public void clear() {
        this.queueSize = 0;
        this.chunk = null;
        this.endOfStream = false;
    }
}
