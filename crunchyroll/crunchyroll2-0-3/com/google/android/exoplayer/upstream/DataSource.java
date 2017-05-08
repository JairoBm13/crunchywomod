// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import java.io.IOException;

public interface DataSource
{
    void close() throws IOException;
    
    long open(final DataSpec p0) throws IOException;
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException;
}
