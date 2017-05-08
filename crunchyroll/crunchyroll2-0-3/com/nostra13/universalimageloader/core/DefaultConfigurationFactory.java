// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import java.util.concurrent.atomic.AtomicInteger;
import android.os.Build$VERSION;
import android.annotation.TargetApi;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import android.app.ActivityManager;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import java.io.Serializable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import java.util.concurrent.Executor;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.io.File;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import java.io.IOException;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import android.content.Context;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

public class DefaultConfigurationFactory
{
    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }
    
    public static DiskCache createDiskCache(final Context context, final FileNameGenerator fileNameGenerator, final long n, final int n2) {
        final File reserveDiskCacheDir = createReserveDiskCacheDir(context);
        if (n > 0L || n2 > 0) {
            final File individualCacheDirectory = StorageUtils.getIndividualCacheDirectory(context);
            try {
                return new LruDiskCache(individualCacheDirectory, reserveDiskCacheDir, fileNameGenerator, n, n2);
            }
            catch (IOException ex) {
                L.e(ex);
            }
        }
        return new UnlimitedDiskCache(StorageUtils.getCacheDirectory(context), reserveDiskCacheDir, fileNameGenerator);
    }
    
    public static Executor createExecutor(final int n, final int n2, final QueueProcessingType queueProcessingType) {
        int n3;
        if (queueProcessingType == QueueProcessingType.LIFO) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        Serializable s;
        if (n3 != 0) {
            s = new LIFOLinkedBlockingDeque<Object>();
        }
        else {
            s = new LinkedBlockingQueue<Object>();
        }
        return new ThreadPoolExecutor(n, n, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>)s, createThreadFactory(n2, "uil-pool-"));
    }
    
    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }
    
    public static ImageDecoder createImageDecoder(final boolean b) {
        return new BaseImageDecoder(b);
    }
    
    public static ImageDownloader createImageDownloader(final Context context) {
        return new BaseImageDownloader(context);
    }
    
    public static MemoryCache createMemoryCache(final Context context, int n) {
        int n2 = n;
        if (n == 0) {
            final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
            final int n3 = n = activityManager.getMemoryClass();
            if (hasHoneycomb()) {
                n = n3;
                if (isLargeHeap(context)) {
                    n = getLargeMemoryClass(activityManager);
                }
            }
            n2 = 1048576 * n / 8;
        }
        return new LruMemoryCache(n2);
    }
    
    private static File createReserveDiskCacheDir(final Context context) {
        File cacheDirectory = StorageUtils.getCacheDirectory(context, false);
        final File file = new File(cacheDirectory, "uil-images");
        if (file.exists() || file.mkdir()) {
            cacheDirectory = file;
        }
        return cacheDirectory;
    }
    
    public static Executor createTaskDistributor() {
        return Executors.newCachedThreadPool(createThreadFactory(5, "uil-pool-d-"));
    }
    
    private static ThreadFactory createThreadFactory(final int n, final String s) {
        return new DefaultThreadFactory(n, s);
    }
    
    @TargetApi(11)
    private static int getLargeMemoryClass(final ActivityManager activityManager) {
        return activityManager.getLargeMemoryClass();
    }
    
    private static boolean hasHoneycomb() {
        return Build$VERSION.SDK_INT >= 11;
    }
    
    @TargetApi(11)
    private static boolean isLargeHeap(final Context context) {
        return (context.getApplicationInfo().flags & 0x100000) != 0x0;
    }
    
    private static class DefaultThreadFactory implements ThreadFactory
    {
        private static final AtomicInteger poolNumber;
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber;
        private final int threadPriority;
        
        static {
            poolNumber = new AtomicInteger(1);
        }
        
        DefaultThreadFactory(final int threadPriority, final String s) {
            this.threadNumber = new AtomicInteger(1);
            this.threadPriority = threadPriority;
            this.group = Thread.currentThread().getThreadGroup();
            this.namePrefix = s + DefaultThreadFactory.poolNumber.getAndIncrement() + "-thread-";
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(this.threadPriority);
            return thread;
        }
    }
}
