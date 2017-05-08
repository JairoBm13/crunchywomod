// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor;

import java.io.IOException;

public interface ExtractorInput
{
    long getLength();
    
    long getPosition();
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException, InterruptedException;
    
    void readFully(final byte[] p0, final int p1, final int p2) throws IOException, InterruptedException;
    
    boolean readFully(final byte[] p0, final int p1, final int p2, final boolean p3) throws IOException, InterruptedException;
    
    void skipFully(final int p0) throws IOException, InterruptedException;
}
