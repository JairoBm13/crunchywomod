// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class FlushedInputStream extends FilterInputStream
{
    public FlushedInputStream(final InputStream inputStream) {
        super(inputStream);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        long n2;
        long skip;
        for (n2 = 0L; n2 < n; n2 += skip) {
            if ((skip = this.in.skip(n - n2)) == 0L) {
                if (this.read() < 0) {
                    break;
                }
                skip = 1L;
            }
        }
        return n2;
    }
}
