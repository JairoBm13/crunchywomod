// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.Closeable;

public final class IoUtils
{
    public static final int CONTINUE_LOADING_PERCENTAGE = 75;
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final int DEFAULT_IMAGE_TOTAL_SIZE = 512000;
    
    public static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (Exception ex) {}
    }
    
    public static boolean copyStream(final InputStream inputStream, final OutputStream outputStream, final CopyListener copyListener) throws IOException {
        return copyStream(inputStream, outputStream, copyListener, 32768);
    }
    
    public static boolean copyStream(final InputStream inputStream, final OutputStream outputStream, final CopyListener copyListener, final int n) throws IOException {
        int n2 = 0;
        int available;
        if ((available = inputStream.available()) <= 0) {
            available = 512000;
        }
        final byte[] array = new byte[n];
        if (shouldStopLoading(copyListener, 0, available)) {
            return false;
        }
        int read;
        do {
            read = inputStream.read(array, 0, n);
            if (read == -1) {
                outputStream.flush();
                return true;
            }
            outputStream.write(array, 0, read);
        } while (!shouldStopLoading(copyListener, n2 += read, available));
        return false;
    }
    
    public static void readAndCloseStream(final InputStream inputStream) {
        final byte[] array = new byte[32768];
        try {
            while (inputStream.read(array, 0, 32768) != -1) {}
        }
        catch (IOException ex) {}
        finally {
            closeSilently(inputStream);
        }
    }
    
    private static boolean shouldStopLoading(final CopyListener copyListener, final int n, final int n2) {
        return copyListener != null && !copyListener.onBytesCopied(n, n2) && n * 100 / n2 < 75;
    }
    
    public interface CopyListener
    {
        boolean onBytesCopied(final int p0, final int p1);
    }
}
