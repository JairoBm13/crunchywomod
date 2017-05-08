// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.impl.ext;

import java.io.InputStream;
import java.io.Closeable;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.utils.L;
import java.io.IOException;
import java.io.File;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import android.graphics.Bitmap$CompressFormat;
import com.nostra13.universalimageloader.cache.disc.DiskCache;

public class LruDiskCache implements DiskCache
{
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final Bitmap$CompressFormat DEFAULT_COMPRESS_FORMAT;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";
    private static final String ERROR_ARG_NULL = " argument must be not null";
    protected int bufferSize;
    protected DiskLruCache cache;
    protected Bitmap$CompressFormat compressFormat;
    protected int compressQuality;
    protected final FileNameGenerator fileNameGenerator;
    private File reserveCacheDir;
    
    static {
        DEFAULT_COMPRESS_FORMAT = Bitmap$CompressFormat.PNG;
    }
    
    public LruDiskCache(final File file, final FileNameGenerator fileNameGenerator, final long n) throws IOException {
        this(file, null, fileNameGenerator, n, 0);
    }
    
    public LruDiskCache(final File file, final File reserveCacheDir, final FileNameGenerator fileNameGenerator, final long n, final int n2) throws IOException {
        this.bufferSize = 32768;
        this.compressFormat = LruDiskCache.DEFAULT_COMPRESS_FORMAT;
        this.compressQuality = 100;
        if (file == null) {
            throw new IllegalArgumentException("cacheDir argument must be not null");
        }
        if (n < 0L) {
            throw new IllegalArgumentException("cacheMaxSize argument must be positive number");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("cacheMaxFileCount argument must be positive number");
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator argument must be not null");
        }
        long n3 = n;
        if (n == 0L) {
            n3 = Long.MAX_VALUE;
        }
        int n4;
        if ((n4 = n2) == 0) {
            n4 = Integer.MAX_VALUE;
        }
        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
        this.initCache(file, reserveCacheDir, n3, n4);
    }
    
    private String getKey(final String s) {
        return this.fileNameGenerator.generate(s);
    }
    
    private void initCache(final File file, final File file2, final long n, final int n2) throws IOException {
        try {
            this.cache = DiskLruCache.open(file, 1, 1, n, n2);
        }
        catch (IOException ex) {
            L.e(ex);
            if (file2 != null) {
                this.initCache(file2, null, n, n2);
            }
            if (this.cache == null) {
                throw ex;
            }
        }
    }
    
    @Override
    public void clear() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.cache:Lcom/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache;
        //     4: invokevirtual   com/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache.delete:()V
        //     7: aload_0        
        //     8: aload_0        
        //     9: getfield        com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.cache:Lcom/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache;
        //    12: invokevirtual   com/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache.getDirectory:()Ljava/io/File;
        //    15: aload_0        
        //    16: getfield        com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.reserveCacheDir:Ljava/io/File;
        //    19: aload_0        
        //    20: getfield        com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.cache:Lcom/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache;
        //    23: invokevirtual   com/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache.getMaxSize:()J
        //    26: aload_0        
        //    27: getfield        com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.cache:Lcom/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache;
        //    30: invokevirtual   com/nostra13/universalimageloader/cache/disc/impl/ext/DiskLruCache.getMaxFileCount:()I
        //    33: invokespecial   com/nostra13/universalimageloader/cache/disc/impl/ext/LruDiskCache.initCache:(Ljava/io/File;Ljava/io/File;JI)V
        //    36: return         
        //    37: astore_1       
        //    38: aload_1        
        //    39: invokestatic    com/nostra13/universalimageloader/utils/L.e:(Ljava/lang/Throwable;)V
        //    42: goto            7
        //    45: astore_1       
        //    46: aload_1        
        //    47: invokestatic    com/nostra13/universalimageloader/utils/L.e:(Ljava/lang/Throwable;)V
        //    50: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      7      37     45     Ljava/io/IOException;
        //  7      36     45     51     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0007:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void close() {
        while (true) {
            try {
                this.cache.close();
                this.cache = null;
            }
            catch (IOException ex) {
                L.e(ex);
                continue;
            }
            break;
        }
    }
    
    @Override
    public File get(final String s) {
        final DiskLruCache.Snapshot snapshot = null;
        final DiskLruCache.Snapshot snapshot2 = null;
        Object o = null;
        Object file = null;
        try {
            final DiskLruCache.Snapshot value = this.cache.get(this.getKey(s));
            if (value == null) {
                file = snapshot2;
            }
            else {
                file = value;
                o = value;
                file = value.getFile(0);
            }
            o = file;
            if (value != null) {
                value.close();
                o = file;
            }
            return (File)o;
        }
        catch (IOException ex) {
            o = file;
            L.e(ex);
            o = snapshot;
            return null;
        }
        finally {
            if (o != null) {
                ((DiskLruCache.Snapshot)o).close();
            }
        }
    }
    
    @Override
    public File getDirectory() {
        return this.cache.getDirectory();
    }
    
    @Override
    public boolean remove(final String s) {
        try {
            return this.cache.remove(this.getKey(s));
        }
        catch (IOException ex) {
            L.e(ex);
            return false;
        }
    }
    
    @Override
    public boolean save(String s, final Bitmap bitmap) throws IOException {
        final DiskLruCache.Editor edit = this.cache.edit(this.getKey(s));
        if (edit == null) {
            return false;
        }
        s = (String)new BufferedOutputStream(edit.newOutputStream(0), this.bufferSize);
        boolean compress;
        try {
            compress = bitmap.compress(this.compressFormat, this.compressQuality, (OutputStream)s);
            IoUtils.closeSilently((Closeable)s);
            if (compress) {
                edit.commit();
                return compress;
            }
        }
        finally {
            IoUtils.closeSilently((Closeable)s);
        }
        edit.abort();
        return compress;
    }
    
    @Override
    public boolean save(String edit, final InputStream inputStream, final IoUtils.CopyListener copyListener) throws IOException {
        edit = (String)this.cache.edit(this.getKey(edit));
        if (edit == null) {
            return false;
        }
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(((DiskLruCache.Editor)edit).newOutputStream(0), this.bufferSize);
        try {
            final boolean copyStream = IoUtils.copyStream(inputStream, bufferedOutputStream, copyListener, this.bufferSize);
            IoUtils.closeSilently(bufferedOutputStream);
            if (copyStream) {
                ((DiskLruCache.Editor)edit).commit();
                return copyStream;
            }
            ((DiskLruCache.Editor)edit).abort();
            return copyStream;
        }
        finally {
            IoUtils.closeSilently(bufferedOutputStream);
            while (true) {
                if (false) {
                    ((DiskLruCache.Editor)edit).commit();
                    break Label_0088;
                }
                ((DiskLruCache.Editor)edit).abort();
                break Label_0088;
                continue;
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
