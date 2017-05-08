// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import java.io.IOException;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.DefaultExtractorInput;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.chunk.MediaChunk;

public final class TsChunk extends MediaChunk
{
    private int bytesLoaded;
    public final HlsExtractorWrapper extractorWrapper;
    private final boolean isEncrypted;
    private volatile boolean loadCanceled;
    
    public TsChunk(final DataSource dataSource, final DataSpec dataSpec, final int n, final Format format, final long n2, final long n3, final int n4, final HlsExtractorWrapper extractorWrapper, final byte[] array, final byte[] array2) {
        super(buildDataSource(dataSource, array, array2), dataSpec, n, format, n2, n3, n4);
        this.extractorWrapper = extractorWrapper;
        this.isEncrypted = (this.dataSource instanceof Aes128DataSource);
    }
    
    private static DataSource buildDataSource(final DataSource dataSource, final byte[] array, final byte[] array2) {
        if (array == null || array2 == null) {
            return dataSource;
        }
        return new Aes128DataSource(dataSource, array, array2);
    }
    
    @Override
    public long bytesLoaded() {
        return this.bytesLoaded;
    }
    
    @Override
    public void cancelLoad() {
        this.loadCanceled = true;
    }
    
    @Override
    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }
    
    @Override
    public void load() throws IOException, InterruptedException {
        Label_0091: {
            if (!this.isEncrypted) {
                break Label_0091;
            }
            Object o = this.dataSpec;
            Label_0086: {
                if (this.bytesLoaded == 0) {
                    break Label_0086;
                }
                int read = 1;
                try {
                    while (true) {
                        o = new DefaultExtractorInput(this.dataSource, ((DataSpec)o).absoluteStreamPosition, this.dataSource.open((DataSpec)o));
                        if (read != 0) {
                            ((ExtractorInput)o).skipFully(this.bytesLoaded);
                        }
                        read = 0;
                        while (true) {
                            if (read != 0) {
                                return;
                            }
                            try {
                                if (!this.loadCanceled) {
                                    read = this.extractorWrapper.read((ExtractorInput)o);
                                    continue;
                                }
                                return;
                                o = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
                                read = 0;
                                break;
                                read = 0;
                            }
                            finally {
                                this.bytesLoaded = (int)(((ExtractorInput)o).getPosition() - this.dataSpec.absoluteStreamPosition);
                            }
                        }
                    }
                }
                finally {
                    this.dataSource.close();
                }
            }
        }
    }
}
