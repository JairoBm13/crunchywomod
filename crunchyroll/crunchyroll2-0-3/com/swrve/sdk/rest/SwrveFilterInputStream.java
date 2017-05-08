// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public final class SwrveFilterInputStream extends FilterInputStream
{
    private boolean hasMoreToRead;
    
    public SwrveFilterInputStream(final InputStream inputStream) {
        super(inputStream);
        this.hasMoreToRead = true;
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        if (this.hasMoreToRead) {
            read = super.read(array, read, n);
            if (read != -1) {
                return read;
            }
        }
        this.hasMoreToRead = false;
        return -1;
    }
}
