// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import android.content.Context;
import android.content.ContentResolver;
import java.io.InputStream;

public final class ContentDataSource implements UriDataSource
{
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener listener;
    private boolean opened;
    private final ContentResolver resolver;
    private String uriString;
    
    public ContentDataSource(final Context context, final TransferListener listener) {
        this.resolver = context.getContentResolver();
        this.listener = listener;
    }
    
    @Override
    public void close() throws ContentDataSourceException {
        this.uriString = null;
        if (this.inputStream == null) {
            return;
        }
        try {
            this.inputStream.close();
        }
        catch (IOException ex) {
            throw new ContentDataSourceException(ex);
        }
        finally {
            this.inputStream = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd();
                }
            }
        }
    }
    
    @Override
    public String getUri() {
        return this.uriString;
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws ContentDataSourceException {
        try {
            this.uriString = dataSpec.uri.toString();
            this.inputStream = new FileInputStream(this.resolver.openAssetFileDescriptor(dataSpec.uri, "r").getFileDescriptor());
            if (this.inputStream.skip(dataSpec.position) < dataSpec.position) {
                throw new EOFException();
            }
        }
        catch (IOException ex) {
            throw new ContentDataSourceException(ex);
        }
        if (dataSpec.length != -1L) {
            this.bytesRemaining = dataSpec.length;
        }
        else {
            this.bytesRemaining = this.inputStream.available();
            if (this.bytesRemaining == 0L) {
                this.bytesRemaining = -1L;
            }
        }
        this.opened = true;
        if (this.listener != null) {
            this.listener.onTransferStart();
        }
        return this.bytesRemaining;
    }
    
    @Override
    public int read(final byte[] array, int n, int read) throws ContentDataSourceException {
        if (this.bytesRemaining == 0L) {
            n = -1;
        }
        else {
            try {
                if (this.bytesRemaining != -1L) {
                    read = (int)Math.min(this.bytesRemaining, read);
                }
                read = this.inputStream.read(array, n, read);
                if ((n = read) > 0) {
                    if (this.bytesRemaining != -1L) {
                        this.bytesRemaining -= read;
                    }
                    n = read;
                    if (this.listener != null) {
                        this.listener.onBytesTransferred(read);
                        return read;
                    }
                }
            }
            catch (IOException ex) {
                throw new ContentDataSourceException(ex);
            }
        }
        return n;
    }
    
    public static class ContentDataSourceException extends IOException
    {
        public ContentDataSourceException(final IOException ex) {
            super(ex);
        }
    }
}
