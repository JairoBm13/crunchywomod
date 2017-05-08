// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.impl;

import java.io.InputStream;
import java.io.IOException;
import java.io.Closeable;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import java.io.File;
import android.graphics.Bitmap$CompressFormat;
import com.nostra13.universalimageloader.cache.disc.DiskCache;

public abstract class BaseDiskCache implements DiskCache
{
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final Bitmap$CompressFormat DEFAULT_COMPRESS_FORMAT;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String TEMP_IMAGE_POSTFIX = ".tmp";
    protected int bufferSize;
    protected final File cacheDir;
    protected Bitmap$CompressFormat compressFormat;
    protected int compressQuality;
    protected final FileNameGenerator fileNameGenerator;
    protected final File reserveCacheDir;
    
    static {
        DEFAULT_COMPRESS_FORMAT = Bitmap$CompressFormat.PNG;
    }
    
    public BaseDiskCache(final File file) {
        this(file, null);
    }
    
    public BaseDiskCache(final File file, final File file2) {
        this(file, file2, DefaultConfigurationFactory.createFileNameGenerator());
    }
    
    public BaseDiskCache(final File cacheDir, final File reserveCacheDir, final FileNameGenerator fileNameGenerator) {
        this.bufferSize = 32768;
        this.compressFormat = BaseDiskCache.DEFAULT_COMPRESS_FORMAT;
        this.compressQuality = 100;
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir argument must be not null");
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator argument must be not null");
        }
        this.cacheDir = cacheDir;
        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
    }
    
    @Override
    public void clear() {
        final File[] listFiles = this.cacheDir.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                listFiles[i].delete();
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public File get(final String s) {
        return this.getFile(s);
    }
    
    @Override
    public File getDirectory() {
        return this.cacheDir;
    }
    
    protected File getFile(final String s) {
        final String generate = this.fileNameGenerator.generate(s);
        File file2;
        final File file = file2 = this.cacheDir;
        if (!this.cacheDir.exists()) {
            file2 = file;
            if (!this.cacheDir.mkdirs()) {
                file2 = file;
                if (this.reserveCacheDir != null) {
                    if (!this.reserveCacheDir.exists()) {
                        file2 = file;
                        if (!this.reserveCacheDir.mkdirs()) {
                            return new File(file2, generate);
                        }
                    }
                    file2 = this.reserveCacheDir;
                }
            }
        }
        return new File(file2, generate);
    }
    
    @Override
    public boolean remove(final String s) {
        return this.getFile(s).delete();
    }
    
    @Override
    public boolean save(String file, final Bitmap bitmap) throws IOException {
        file = (String)this.getFile(file);
        final File file2 = new File(((File)file).getAbsolutePath() + ".tmp");
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2), this.bufferSize);
        try {
            final boolean compress = bitmap.compress(this.compressFormat, this.compressQuality, (OutputStream)bufferedOutputStream);
            IoUtils.closeSilently(bufferedOutputStream);
            boolean b = compress;
            if (compress) {
                b = compress;
                if (!file2.renameTo((File)file)) {
                    b = false;
                }
            }
            if (!b) {
                file2.delete();
            }
            bitmap.recycle();
            return b;
        }
        finally {
            IoUtils.closeSilently(bufferedOutputStream);
            if (!false || !file2.renameTo((File)file)) {}
            if (!false) {
                file2.delete();
            }
        }
    }
    
    @Override
    public boolean save(String file, final InputStream inputStream, final IoUtils.CopyListener copyListener) throws IOException {
        file = (String)this.getFile(file);
        final File file2 = new File(((File)file).getAbsolutePath() + ".tmp");
        boolean copyStream;
        boolean b = copyStream = false;
        try {
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2), this.bufferSize);
            try {
                copyStream = IoUtils.copyStream(inputStream, bufferedOutputStream, copyListener, this.bufferSize);
                b = (copyStream = copyStream);
                IoUtils.closeSilently(bufferedOutputStream);
                return copyStream;
            }
            finally {
                copyStream = b;
                IoUtils.closeSilently(bufferedOutputStream);
                copyStream = b;
            }
        }
        finally {
            boolean b2 = copyStream;
            if (copyStream) {
                b2 = copyStream;
                if (!file2.renameTo((File)file)) {
                    b2 = false;
                }
            }
            if (!b2) {
                file2.delete();
            }
        }
    }
    
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public void setCompressFormat(final Bitmap$CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }
    
    public void setCompressQuality(final int compressQuality) {
        this.compressQuality = compressQuality;
    }
}
