// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.File;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.assist.FailReason;
import java.io.InputStream;
import java.io.Closeable;
import com.nostra13.universalimageloader.utils.L;
import java.io.IOException;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.os.Handler;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.utils.IoUtils;

final class LoadAndDisplayImageTask implements CopyListener, Runnable
{
    private static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
    private static final String ERROR_POST_PROCESSOR_NULL = "Post-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISK_CACHE_NULL = "Bitmap processor for disk cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISK = "Cache image on disk [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISK_CACHE = "Load image from disk cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK = "Process image before cache on disk [%s]";
    private static final String LOG_RESIZE_CACHED_IMAGE_FILE = "Resize image in disk cache [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final boolean syncLoading;
    private final ImageSize targetSize;
    final String uri;
    
    public LoadAndDisplayImageTask(final ImageLoaderEngine engine, final ImageLoadingInfo imageLoadingInfo, final Handler handler) {
        this.loadedFrom = LoadedFrom.NETWORK;
        this.engine = engine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        this.configuration = engine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.uri = imageLoadingInfo.uri;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.imageAware = imageLoadingInfo.imageAware;
        this.targetSize = imageLoadingInfo.targetSize;
        this.options = imageLoadingInfo.options;
        this.listener = imageLoadingInfo.listener;
        this.progressListener = imageLoadingInfo.progressListener;
        this.syncLoading = this.options.isSyncLoading();
    }
    
    private void checkTaskInterrupted() throws TaskCancelledException {
        if (this.isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }
    
    private void checkTaskNotActual() throws TaskCancelledException {
        this.checkViewCollected();
        this.checkViewReused();
    }
    
    private void checkViewCollected() throws TaskCancelledException {
        if (this.isViewCollected()) {
            throw new TaskCancelledException();
        }
    }
    
    private void checkViewReused() throws TaskCancelledException {
        if (this.isViewReused()) {
            throw new TaskCancelledException();
        }
    }
    
    private Bitmap decodeImage(final String s) throws IOException {
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, s, this.uri, this.targetSize, this.imageAware.getScaleType(), this.getDownloader(), this.options));
    }
    
    private boolean delayIfNeed() {
        if (this.options.shouldDelayBeforeLoading()) {
            L.d("Delay %d ms before loading...  [%s]", this.options.getDelayBeforeLoading(), this.memoryCacheKey);
            try {
                Thread.sleep(this.options.getDelayBeforeLoading());
                return this.isTaskNotActual();
            }
            catch (InterruptedException ex) {
                L.e("Task was interrupted [%s]", this.memoryCacheKey);
                return true;
            }
        }
        return false;
    }
    
    private boolean downloadImage() throws IOException {
        final InputStream stream = this.getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        if (stream == null) {
            L.e("No stream for image [%s]", this.memoryCacheKey);
            return false;
        }
        try {
            return this.configuration.diskCache.save(this.uri, stream, this);
        }
        finally {
            IoUtils.closeSilently(stream);
        }
    }
    
    private void fireCancelEvent() {
        if (this.syncLoading || this.isTaskInterrupted()) {
            return;
        }
        runTask(new Runnable() {
            @Override
            public void run() {
                LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
            }
        }, false, this.handler, this.engine);
    }
    
    private void fireFailEvent(final FailReason.FailType failType, final Throwable t) {
        if (this.syncLoading || this.isTaskInterrupted() || this.isTaskNotActual()) {
            return;
        }
        runTask(new Runnable() {
            @Override
            public void run() {
                if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                    LoadAndDisplayImageTask.this.imageAware.setImageDrawable(LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources));
                }
                LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, t));
            }
        }, false, this.handler, this.engine);
    }
    
    private boolean fireProgressEvent(final int n, final int n2) {
        if (this.isTaskInterrupted() || this.isTaskNotActual()) {
            return false;
        }
        if (this.progressListener != null) {
            runTask(new Runnable() {
                @Override
                public void run() {
                    LoadAndDisplayImageTask.this.progressListener.onProgressUpdate(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), n, n2);
                }
            }, false, this.handler, this.engine);
        }
        return true;
    }
    
    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.isSlowNetwork()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }
    
    private boolean isTaskInterrupted() {
        if (Thread.interrupted()) {
            L.d("Task was interrupted [%s]", this.memoryCacheKey);
            return true;
        }
        return false;
    }
    
    private boolean isTaskNotActual() {
        return this.isViewCollected() || this.isViewReused();
    }
    
    private boolean isViewCollected() {
        if (this.imageAware.isCollected()) {
            L.d("ImageAware was collected by GC. Task is cancelled. [%s]", this.memoryCacheKey);
            return true;
        }
        return false;
    }
    
    private boolean isViewReused() {
        int n;
        if (!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware))) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            L.d("ImageAware is reused for another image. Task is cancelled. [%s]", this.memoryCacheKey);
            return true;
        }
        return false;
    }
    
    private boolean resizeAndSaveImage(final int n, final int n2) throws IOException {
        final boolean b = false;
        final File value = this.configuration.diskCache.get(this.uri);
        boolean save = b;
        if (value != null) {
            save = b;
            if (value.exists()) {
                final Bitmap decode = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, ImageDownloader.Scheme.FILE.wrap(value.getAbsolutePath()), this.uri, new ImageSize(n, n2), ViewScaleType.FIT_INSIDE, this.getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
                Bitmap bitmap;
                if ((bitmap = decode) != null) {
                    bitmap = decode;
                    if (this.configuration.processorForDiskCache != null) {
                        L.d("Process image before cache on disk [%s]", this.memoryCacheKey);
                        final Bitmap process = this.configuration.processorForDiskCache.process(decode);
                        if ((bitmap = process) == null) {
                            L.e("Bitmap processor for disk cache returned null [%s]", this.memoryCacheKey);
                            bitmap = process;
                        }
                    }
                }
                save = b;
                if (bitmap != null) {
                    save = this.configuration.diskCache.save(this.uri, bitmap);
                    bitmap.recycle();
                }
            }
        }
        return save;
    }
    
    static void runTask(final Runnable runnable, final boolean b, final Handler handler, final ImageLoaderEngine imageLoaderEngine) {
        if (b) {
            runnable.run();
            return;
        }
        if (handler == null) {
            imageLoaderEngine.fireCallback(runnable);
            return;
        }
        handler.post(runnable);
    }
    
    private boolean tryCacheImageOnDisk() throws TaskCancelledException {
        L.d("Cache image on disk [%s]", this.memoryCacheKey);
        try {
            final boolean downloadImage = this.downloadImage();
            if (downloadImage) {
                final int maxImageWidthForDiskCache = this.configuration.maxImageWidthForDiskCache;
                final int maxImageHeightForDiskCache = this.configuration.maxImageHeightForDiskCache;
                if (maxImageWidthForDiskCache > 0 || maxImageHeightForDiskCache > 0) {
                    L.d("Resize image in disk cache [%s]", this.memoryCacheKey);
                    this.resizeAndSaveImage(maxImageWidthForDiskCache, maxImageHeightForDiskCache);
                }
            }
            return downloadImage;
        }
        catch (IOException ex) {
            L.e(ex);
            return false;
        }
    }
    
    private Bitmap tryLoadBitmap() throws TaskCancelledException {
        final Bitmap bitmap = null;
        final Bitmap bitmap2 = null;
        final Bitmap bitmap3 = null;
        final Bitmap bitmap4 = null;
        final Bitmap bitmap5 = null;
        Bitmap bitmap6 = bitmap;
        Bitmap bitmap7 = bitmap2;
        Bitmap bitmap8 = bitmap3;
        Bitmap bitmap9 = bitmap4;
        try {
            final File value = this.configuration.diskCache.get(this.uri);
            Bitmap decodeImage = bitmap5;
            if (value != null) {
                decodeImage = bitmap5;
                bitmap6 = bitmap;
                bitmap7 = bitmap2;
                bitmap8 = bitmap3;
                bitmap9 = bitmap4;
                if (value.exists()) {
                    decodeImage = bitmap5;
                    bitmap6 = bitmap;
                    bitmap7 = bitmap2;
                    bitmap8 = bitmap3;
                    bitmap9 = bitmap4;
                    if (value.length() > 0L) {
                        bitmap6 = bitmap;
                        bitmap7 = bitmap2;
                        bitmap8 = bitmap3;
                        bitmap9 = bitmap4;
                        L.d("Load image from disk cache [%s]", this.memoryCacheKey);
                        bitmap6 = bitmap;
                        bitmap7 = bitmap2;
                        bitmap8 = bitmap3;
                        bitmap9 = bitmap4;
                        this.loadedFrom = LoadedFrom.DISC_CACHE;
                        bitmap6 = bitmap;
                        bitmap7 = bitmap2;
                        bitmap8 = bitmap3;
                        bitmap9 = bitmap4;
                        this.checkTaskNotActual();
                        bitmap6 = bitmap;
                        bitmap7 = bitmap2;
                        bitmap8 = bitmap3;
                        bitmap9 = bitmap4;
                        decodeImage = this.decodeImage(ImageDownloader.Scheme.FILE.wrap(value.getAbsolutePath()));
                    }
                }
            }
            if (decodeImage != null) {
                bitmap6 = decodeImage;
                bitmap7 = decodeImage;
                bitmap8 = decodeImage;
                bitmap9 = decodeImage;
                if (decodeImage.getWidth() > 0) {
                    final Bitmap bitmap10 = decodeImage;
                    bitmap6 = decodeImage;
                    bitmap7 = decodeImage;
                    bitmap8 = decodeImage;
                    bitmap9 = decodeImage;
                    if (decodeImage.getHeight() > 0) {
                        return bitmap10;
                    }
                }
            }
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            L.d("Load image from network [%s]", this.memoryCacheKey);
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            this.loadedFrom = LoadedFrom.NETWORK;
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            String s2;
            final String s = s2 = this.uri;
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            if (this.options.isCacheOnDisk()) {
                s2 = s;
                bitmap6 = decodeImage;
                bitmap7 = decodeImage;
                bitmap8 = decodeImage;
                bitmap9 = decodeImage;
                if (this.tryCacheImageOnDisk()) {
                    bitmap6 = decodeImage;
                    bitmap7 = decodeImage;
                    bitmap8 = decodeImage;
                    bitmap9 = decodeImage;
                    final File value2 = this.configuration.diskCache.get(this.uri);
                    s2 = s;
                    if (value2 != null) {
                        bitmap6 = decodeImage;
                        bitmap7 = decodeImage;
                        bitmap8 = decodeImage;
                        bitmap9 = decodeImage;
                        s2 = ImageDownloader.Scheme.FILE.wrap(value2.getAbsolutePath());
                    }
                }
            }
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            this.checkTaskNotActual();
            bitmap6 = decodeImage;
            bitmap7 = decodeImage;
            bitmap8 = decodeImage;
            bitmap9 = decodeImage;
            final Bitmap decodeImage2 = this.decodeImage(s2);
            if (decodeImage2 != null) {
                bitmap6 = decodeImage2;
                bitmap7 = decodeImage2;
                bitmap8 = decodeImage2;
                bitmap9 = decodeImage2;
                if (decodeImage2.getWidth() > 0) {
                    final Bitmap bitmap10 = decodeImage2;
                    bitmap6 = decodeImage2;
                    bitmap7 = decodeImage2;
                    bitmap8 = decodeImage2;
                    bitmap9 = decodeImage2;
                    if (decodeImage2.getHeight() > 0) {
                        return bitmap10;
                    }
                }
            }
            bitmap6 = decodeImage2;
            bitmap7 = decodeImage2;
            bitmap8 = decodeImage2;
            bitmap9 = decodeImage2;
            this.fireFailEvent(FailReason.FailType.DECODING_ERROR, null);
            return decodeImage2;
        }
        catch (IllegalStateException ex3) {
            this.fireFailEvent(FailReason.FailType.NETWORK_DENIED, null);
            return bitmap6;
        }
        catch (TaskCancelledException ex) {
            throw ex;
        }
        catch (IOException ex2) {
            L.e(ex2);
            this.fireFailEvent(FailReason.FailType.IO_ERROR, ex2);
            return bitmap7;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            L.e(outOfMemoryError);
            this.fireFailEvent(FailReason.FailType.OUT_OF_MEMORY, outOfMemoryError);
            return bitmap8;
        }
        catch (Throwable t) {
            L.e(t);
            this.fireFailEvent(FailReason.FailType.UNKNOWN, t);
            return bitmap9;
        }
    }
    
    private boolean waitIfPaused() {
        final AtomicBoolean pause = this.engine.getPause();
        Label_0076: {
            if (!pause.get()) {
                break Label_0076;
            }
            synchronized (this.engine.getPauseLock()) {
                Label_0074: {
                    if (!pause.get()) {
                        break Label_0074;
                    }
                    L.d("ImageLoader is paused. Waiting...  [%s]", this.memoryCacheKey);
                    try {
                        this.engine.getPauseLock().wait();
                        L.d(".. Resume loading [%s]", this.memoryCacheKey);
                        // monitorexit(this.engine.getPauseLock())
                        return this.isTaskNotActual();
                    }
                    catch (InterruptedException ex) {
                        L.e("Task was interrupted [%s]", this.memoryCacheKey);
                        return true;
                    }
                }
            }
        }
    }
    
    String getLoadingUri() {
        return this.uri;
    }
    
    @Override
    public boolean onBytesCopied(final int n, final int n2) {
        return this.syncLoading || this.fireProgressEvent(n, n2);
    }
    
    @Override
    public void run() {
        if (this.waitIfPaused() || this.delayIfNeed()) {
            return;
        }
        final ReentrantLock loadFromUriLock = this.imageLoadingInfo.loadFromUriLock;
        L.d("Start display image task [%s]", this.memoryCacheKey);
        if (loadFromUriLock.isLocked()) {
            L.d("Image already is loading. Waiting... [%s]", this.memoryCacheKey);
        }
        loadFromUriLock.lock();
        try {
            this.checkTaskNotActual();
            Bitmap value = this.configuration.memoryCache.get(this.memoryCacheKey);
            if (value == null || value.isRecycled()) {
                final Bitmap tryLoadBitmap = this.tryLoadBitmap();
                if (tryLoadBitmap == null) {
                    return;
                }
                this.checkTaskNotActual();
                this.checkTaskInterrupted();
                Bitmap bitmap = tryLoadBitmap;
                if (this.options.shouldPreProcess()) {
                    L.d("PreProcess image before caching in memory [%s]", this.memoryCacheKey);
                    final Bitmap process = this.options.getPreProcessor().process(tryLoadBitmap);
                    if ((bitmap = process) == null) {
                        L.e("Pre-processor returned null [%s]", this.memoryCacheKey);
                        bitmap = process;
                    }
                }
                if ((value = bitmap) != null) {
                    value = bitmap;
                    if (this.options.isCacheInMemory()) {
                        L.d("Cache image in memory [%s]", this.memoryCacheKey);
                        this.configuration.memoryCache.put(this.memoryCacheKey, bitmap);
                        value = bitmap;
                    }
                }
            }
            else {
                this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                L.d("...Get cached bitmap from memory after waiting. [%s]", this.memoryCacheKey);
            }
            Bitmap bitmap2;
            if ((bitmap2 = value) != null) {
                bitmap2 = value;
                if (this.options.shouldPostProcess()) {
                    L.d("PostProcess image before displaying [%s]", this.memoryCacheKey);
                    final Bitmap process2 = this.options.getPostProcessor().process(value);
                    if ((bitmap2 = process2) == null) {
                        L.e("Post-processor returned null [%s]", this.memoryCacheKey);
                        bitmap2 = process2;
                    }
                }
            }
            this.checkTaskNotActual();
            this.checkTaskInterrupted();
            loadFromUriLock.unlock();
            runTask(new DisplayBitmapTask(bitmap2, this.imageLoadingInfo, this.engine, this.loadedFrom), this.syncLoading, this.handler, this.engine);
        }
        catch (TaskCancelledException ex) {
            this.fireCancelEvent();
        }
        finally {
            loadFromUriLock.unlock();
        }
    }
    
    class TaskCancelledException extends Exception
    {
    }
}
