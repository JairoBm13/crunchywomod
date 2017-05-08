// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.nostra13.universalimageloader.cache.memory.impl.FuzzyKeyMemoryCache;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import android.util.DisplayMetrics;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import android.content.Context;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.concurrent.Executor;
import android.content.res.Resources;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;

public final class ImageLoaderConfiguration
{
    final boolean customExecutor;
    final boolean customExecutorForCachedImages;
    final ImageDecoder decoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final DiskCache diskCache;
    final ImageDownloader downloader;
    final int maxImageHeightForDiskCache;
    final int maxImageHeightForMemoryCache;
    final int maxImageWidthForDiskCache;
    final int maxImageWidthForMemoryCache;
    final MemoryCache memoryCache;
    final ImageDownloader networkDeniedDownloader;
    final BitmapProcessor processorForDiskCache;
    final Resources resources;
    final ImageDownloader slowNetworkDownloader;
    final Executor taskExecutor;
    final Executor taskExecutorForCachedImages;
    final QueueProcessingType tasksProcessingType;
    final int threadPoolSize;
    final int threadPriority;
    
    private ImageLoaderConfiguration(final Builder builder) {
        this.resources = builder.context.getResources();
        this.maxImageWidthForMemoryCache = builder.maxImageWidthForMemoryCache;
        this.maxImageHeightForMemoryCache = builder.maxImageHeightForMemoryCache;
        this.maxImageWidthForDiskCache = builder.maxImageWidthForDiskCache;
        this.maxImageHeightForDiskCache = builder.maxImageHeightForDiskCache;
        this.processorForDiskCache = builder.processorForDiskCache;
        this.taskExecutor = builder.taskExecutor;
        this.taskExecutorForCachedImages = builder.taskExecutorForCachedImages;
        this.threadPoolSize = builder.threadPoolSize;
        this.threadPriority = builder.threadPriority;
        this.tasksProcessingType = builder.tasksProcessingType;
        this.diskCache = builder.diskCache;
        this.memoryCache = builder.memoryCache;
        this.defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        this.downloader = builder.downloader;
        this.decoder = builder.decoder;
        this.customExecutor = builder.customExecutor;
        this.customExecutorForCachedImages = builder.customExecutorForCachedImages;
        this.networkDeniedDownloader = new NetworkDeniedImageDownloader(this.downloader);
        this.slowNetworkDownloader = new SlowNetworkImageDownloader(this.downloader);
        L.writeDebugLogs(builder.writeLogs);
    }
    
    public static ImageLoaderConfiguration createDefault(final Context context) {
        return new Builder(context).build();
    }
    
    ImageSize getMaxImageSize() {
        final DisplayMetrics displayMetrics = this.resources.getDisplayMetrics();
        int n;
        if ((n = this.maxImageWidthForMemoryCache) <= 0) {
            n = displayMetrics.widthPixels;
        }
        int n2;
        if ((n2 = this.maxImageHeightForMemoryCache) <= 0) {
            n2 = displayMetrics.heightPixels;
        }
        return new ImageSize(n, n2);
    }
    
    public static class Builder
    {
        public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE;
        public static final int DEFAULT_THREAD_POOL_SIZE = 3;
        public static final int DEFAULT_THREAD_PRIORITY = 3;
        private static final String WARNING_OVERLAP_DISK_CACHE_NAME_GENERATOR = "diskCache() and diskCacheFileNameGenerator() calls overlap each other";
        private static final String WARNING_OVERLAP_DISK_CACHE_PARAMS = "diskCache(), diskCacheSize() and diskCacheFileCount calls overlap each other";
        private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.";
        private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
        private Context context;
        private boolean customExecutor;
        private boolean customExecutorForCachedImages;
        private ImageDecoder decoder;
        private DisplayImageOptions defaultDisplayImageOptions;
        private boolean denyCacheImageMultipleSizesInMemory;
        private DiskCache diskCache;
        private int diskCacheFileCount;
        private FileNameGenerator diskCacheFileNameGenerator;
        private long diskCacheSize;
        private ImageDownloader downloader;
        private int maxImageHeightForDiskCache;
        private int maxImageHeightForMemoryCache;
        private int maxImageWidthForDiskCache;
        private int maxImageWidthForMemoryCache;
        private MemoryCache memoryCache;
        private int memoryCacheSize;
        private BitmapProcessor processorForDiskCache;
        private Executor taskExecutor;
        private Executor taskExecutorForCachedImages;
        private QueueProcessingType tasksProcessingType;
        private int threadPoolSize;
        private int threadPriority;
        private boolean writeLogs;
        
        static {
            DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;
        }
        
        public Builder(final Context context) {
            this.maxImageWidthForMemoryCache = 0;
            this.maxImageHeightForMemoryCache = 0;
            this.maxImageWidthForDiskCache = 0;
            this.maxImageHeightForDiskCache = 0;
            this.processorForDiskCache = null;
            this.taskExecutor = null;
            this.taskExecutorForCachedImages = null;
            this.customExecutor = false;
            this.customExecutorForCachedImages = false;
            this.threadPoolSize = 3;
            this.threadPriority = 3;
            this.denyCacheImageMultipleSizesInMemory = false;
            this.tasksProcessingType = Builder.DEFAULT_TASK_PROCESSING_TYPE;
            this.memoryCacheSize = 0;
            this.diskCacheSize = 0L;
            this.diskCacheFileCount = 0;
            this.memoryCache = null;
            this.diskCache = null;
            this.diskCacheFileNameGenerator = null;
            this.downloader = null;
            this.defaultDisplayImageOptions = null;
            this.writeLogs = false;
            this.context = context.getApplicationContext();
        }
        
        private void initEmptyFieldsWithDefaultValues() {
            if (this.taskExecutor == null) {
                this.taskExecutor = DefaultConfigurationFactory.createExecutor(this.threadPoolSize, this.threadPriority, this.tasksProcessingType);
            }
            else {
                this.customExecutor = true;
            }
            if (this.taskExecutorForCachedImages == null) {
                this.taskExecutorForCachedImages = DefaultConfigurationFactory.createExecutor(this.threadPoolSize, this.threadPriority, this.tasksProcessingType);
            }
            else {
                this.customExecutorForCachedImages = true;
            }
            if (this.diskCache == null) {
                if (this.diskCacheFileNameGenerator == null) {
                    this.diskCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
                }
                this.diskCache = DefaultConfigurationFactory.createDiskCache(this.context, this.diskCacheFileNameGenerator, this.diskCacheSize, this.diskCacheFileCount);
            }
            if (this.memoryCache == null) {
                this.memoryCache = DefaultConfigurationFactory.createMemoryCache(this.context, this.memoryCacheSize);
            }
            if (this.denyCacheImageMultipleSizesInMemory) {
                this.memoryCache = new FuzzyKeyMemoryCache(this.memoryCache, MemoryCacheUtils.createFuzzyKeyComparator());
            }
            if (this.downloader == null) {
                this.downloader = DefaultConfigurationFactory.createImageDownloader(this.context);
            }
            if (this.decoder == null) {
                this.decoder = DefaultConfigurationFactory.createImageDecoder(this.writeLogs);
            }
            if (this.defaultDisplayImageOptions == null) {
                this.defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }
        }
        
        public ImageLoaderConfiguration build() {
            this.initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(this, null);
        }
        
        public Builder defaultDisplayImageOptions(final DisplayImageOptions defaultDisplayImageOptions) {
            this.defaultDisplayImageOptions = defaultDisplayImageOptions;
            return this;
        }
        
        public Builder denyCacheImageMultipleSizesInMemory() {
            this.denyCacheImageMultipleSizesInMemory = true;
            return this;
        }
        
        @Deprecated
        public Builder discCache(final DiskCache diskCache) {
            return this.diskCache(diskCache);
        }
        
        @Deprecated
        public Builder discCacheExtraOptions(final int n, final int n2, final BitmapProcessor bitmapProcessor) {
            return this.diskCacheExtraOptions(n, n2, bitmapProcessor);
        }
        
        @Deprecated
        public Builder discCacheFileCount(final int n) {
            return this.diskCacheFileCount(n);
        }
        
        @Deprecated
        public Builder discCacheFileNameGenerator(final FileNameGenerator fileNameGenerator) {
            return this.diskCacheFileNameGenerator(fileNameGenerator);
        }
        
        @Deprecated
        public Builder discCacheSize(final int n) {
            return this.diskCacheSize(n);
        }
        
        public Builder diskCache(final DiskCache diskCache) {
            if (this.diskCacheSize > 0L || this.diskCacheFileCount > 0) {
                L.w("diskCache(), diskCacheSize() and diskCacheFileCount calls overlap each other", new Object[0]);
            }
            if (this.diskCacheFileNameGenerator != null) {
                L.w("diskCache() and diskCacheFileNameGenerator() calls overlap each other", new Object[0]);
            }
            this.diskCache = diskCache;
            return this;
        }
        
        public Builder diskCacheExtraOptions(final int maxImageWidthForDiskCache, final int maxImageHeightForDiskCache, final BitmapProcessor processorForDiskCache) {
            this.maxImageWidthForDiskCache = maxImageWidthForDiskCache;
            this.maxImageHeightForDiskCache = maxImageHeightForDiskCache;
            this.processorForDiskCache = processorForDiskCache;
            return this;
        }
        
        public Builder diskCacheFileCount(final int diskCacheFileCount) {
            if (diskCacheFileCount <= 0) {
                throw new IllegalArgumentException("maxFileCount must be a positive number");
            }
            if (this.diskCache != null) {
                L.w("diskCache(), diskCacheSize() and diskCacheFileCount calls overlap each other", new Object[0]);
            }
            this.diskCacheFileCount = diskCacheFileCount;
            return this;
        }
        
        public Builder diskCacheFileNameGenerator(final FileNameGenerator diskCacheFileNameGenerator) {
            if (this.diskCache != null) {
                L.w("diskCache() and diskCacheFileNameGenerator() calls overlap each other", new Object[0]);
            }
            this.diskCacheFileNameGenerator = diskCacheFileNameGenerator;
            return this;
        }
        
        public Builder diskCacheSize(final int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("maxCacheSize must be a positive number");
            }
            if (this.diskCache != null) {
                L.w("diskCache(), diskCacheSize() and diskCacheFileCount calls overlap each other", new Object[0]);
            }
            this.diskCacheSize = n;
            return this;
        }
        
        public Builder imageDecoder(final ImageDecoder decoder) {
            this.decoder = decoder;
            return this;
        }
        
        public Builder imageDownloader(final ImageDownloader downloader) {
            this.downloader = downloader;
            return this;
        }
        
        public Builder memoryCache(final MemoryCache memoryCache) {
            if (this.memoryCacheSize != 0) {
                L.w("memoryCache() and memoryCacheSize() calls overlap each other", new Object[0]);
            }
            this.memoryCache = memoryCache;
            return this;
        }
        
        public Builder memoryCacheExtraOptions(final int maxImageWidthForMemoryCache, final int maxImageHeightForMemoryCache) {
            this.maxImageWidthForMemoryCache = maxImageWidthForMemoryCache;
            this.maxImageHeightForMemoryCache = maxImageHeightForMemoryCache;
            return this;
        }
        
        public Builder memoryCacheSize(final int memoryCacheSize) {
            if (memoryCacheSize <= 0) {
                throw new IllegalArgumentException("memoryCacheSize must be a positive number");
            }
            if (this.memoryCache != null) {
                L.w("memoryCache() and memoryCacheSize() calls overlap each other", new Object[0]);
            }
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }
        
        public Builder memoryCacheSizePercentage(final int n) {
            if (n <= 0 || n >= 100) {
                throw new IllegalArgumentException("availableMemoryPercent must be in range (0 < % < 100)");
            }
            if (this.memoryCache != null) {
                L.w("memoryCache() and memoryCacheSize() calls overlap each other", new Object[0]);
            }
            this.memoryCacheSize = (int)(Runtime.getRuntime().maxMemory() * (n / 100.0f));
            return this;
        }
        
        public Builder taskExecutor(final Executor taskExecutor) {
            if (this.threadPoolSize != 3 || this.threadPriority != 3 || this.tasksProcessingType != Builder.DEFAULT_TASK_PROCESSING_TYPE) {
                L.w("threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.", new Object[0]);
            }
            this.taskExecutor = taskExecutor;
            return this;
        }
        
        public Builder taskExecutorForCachedImages(final Executor taskExecutorForCachedImages) {
            if (this.threadPoolSize != 3 || this.threadPriority != 3 || this.tasksProcessingType != Builder.DEFAULT_TASK_PROCESSING_TYPE) {
                L.w("threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.", new Object[0]);
            }
            this.taskExecutorForCachedImages = taskExecutorForCachedImages;
            return this;
        }
        
        public Builder tasksProcessingOrder(final QueueProcessingType tasksProcessingType) {
            if (this.taskExecutor != null || this.taskExecutorForCachedImages != null) {
                L.w("threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.", new Object[0]);
            }
            this.tasksProcessingType = tasksProcessingType;
            return this;
        }
        
        public Builder threadPoolSize(final int threadPoolSize) {
            if (this.taskExecutor != null || this.taskExecutorForCachedImages != null) {
                L.w("threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.", new Object[0]);
            }
            this.threadPoolSize = threadPoolSize;
            return this;
        }
        
        public Builder threadPriority(final int threadPriority) {
            if (this.taskExecutor != null || this.taskExecutorForCachedImages != null) {
                L.w("threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.", new Object[0]);
            }
            if (threadPriority < 1) {
                this.threadPriority = 1;
                return this;
            }
            if (threadPriority > 10) {
                this.threadPriority = 10;
                return this;
            }
            this.threadPriority = threadPriority;
            return this;
        }
        
        public Builder writeDebugLogs() {
            this.writeLogs = true;
            return this;
        }
    }
    
    private static class NetworkDeniedImageDownloader implements ImageDownloader
    {
        private final ImageDownloader wrappedDownloader;
        
        public NetworkDeniedImageDownloader(final ImageDownloader wrappedDownloader) {
            this.wrappedDownloader = wrappedDownloader;
        }
        
        @Override
        public InputStream getStream(final String s, final Object o) throws IOException {
            switch (Scheme.ofUri(s)) {
                default: {
                    return this.wrappedDownloader.getStream(s, o);
                }
                case HTTP:
                case HTTPS: {
                    throw new IllegalStateException();
                }
            }
        }
    }
    
    private static class SlowNetworkImageDownloader implements ImageDownloader
    {
        private final ImageDownloader wrappedDownloader;
        
        public SlowNetworkImageDownloader(final ImageDownloader wrappedDownloader) {
            this.wrappedDownloader = wrappedDownloader;
        }
        
        @Override
        public InputStream getStream(final String s, final Object o) throws IOException {
            final InputStream stream = this.wrappedDownloader.getStream(s, o);
            switch (Scheme.ofUri(s)) {
                default: {
                    return stream;
                }
                case HTTP:
                case HTTPS: {
                    return new FlushedInputStream(stream);
                }
            }
        }
    }
}
