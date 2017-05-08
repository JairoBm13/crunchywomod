// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.chunk;

import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.DataSource;

public abstract class DataChunk extends Chunk
{
    private byte[] data;
    private int limit;
    private volatile boolean loadCanceled;
    
    public DataChunk(final DataSource dataSource, final DataSpec dataSpec, final int n, final int n2, final Format format, final int n3, final byte[] data) {
        super(dataSource, dataSpec, n, n2, format, n3);
        this.data = data;
    }
    
    private void maybeExpandData() {
        if (this.data == null) {
            this.data = new byte[16384];
        }
        else if (this.data.length < this.limit + 16384) {
            this.data = Arrays.copyOf(this.data, this.data.length + 16384);
        }
    }
    
    @Override
    public long bytesLoaded() {
        return this.limit;
    }
    
    @Override
    public final void cancelLoad() {
        this.loadCanceled = true;
    }
    
    protected abstract void consume(final byte[] p0, final int p1) throws IOException;
    
    public byte[] getDataHolder() {
        return this.data;
    }
    
    @Override
    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }
    
    @Override
    public final void load() throws IOException, InterruptedException {
        try {
            this.dataSource.open(this.dataSpec);
            this.limit = 0;
            int read;
            for (int n = 0; n != -1 && !this.loadCanceled; n = read) {
                this.maybeExpandData();
                read = this.dataSource.read(this.data, this.limit, 16384);
                if ((n = read) != -1) {
                    this.limit += read;
                }
            }
        }
        finally {
            this.dataSource.close();
        }
        if (!this.loadCanceled) {
            this.consume(this.data, this.limit);
        }
        this.dataSource.close();
    }
}
