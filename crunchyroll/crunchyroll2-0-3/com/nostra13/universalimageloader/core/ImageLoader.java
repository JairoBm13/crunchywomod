// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import android.view.View;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import android.widget.ImageView;
import android.os.Looper;
import android.os.Handler;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoader
{
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    static final String LOG_DESTROY = "Destroy ImageLoader";
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    public static final String TAG;
    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private ImageLoadingListener defaultListener;
    private ImageLoaderEngine engine;
    
    static {
        TAG = ImageLoader.class.getSimpleName();
    }
    
    protected ImageLoader() {
        this.defaultListener = new SimpleImageLoadingListener();
    }
    
    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException("ImageLoader must be init with configuration before using");
        }
    }
    
    private static Handler defineHandler(final DisplayImageOptions displayImageOptions) {
        final Handler handler = displayImageOptions.getHandler();
        Handler handler2;
        if (displayImageOptions.isSyncLoading()) {
            handler2 = null;
        }
        else if ((handler2 = handler) == null) {
            handler2 = handler;
            if (Looper.myLooper() == Looper.getMainLooper()) {
                return new Handler();
            }
        }
        return handler2;
    }
    
    public static ImageLoader getInstance() {
        Label_0028: {
            if (ImageLoader.instance != null) {
                break Label_0028;
            }
            synchronized (ImageLoader.class) {
                if (ImageLoader.instance == null) {
                    ImageLoader.instance = new ImageLoader();
                }
                return ImageLoader.instance;
            }
        }
    }
    
    public void cancelDisplayTask(final ImageView imageView) {
        this.engine.cancelDisplayTaskFor(new ImageViewAware(imageView));
    }
    
    public void cancelDisplayTask(final ImageAware imageAware) {
        this.engine.cancelDisplayTaskFor(imageAware);
    }
    
    @Deprecated
    public void clearDiscCache() {
        this.clearDiskCache();
    }
    
    public void clearDiskCache() {
        this.checkConfiguration();
        this.configuration.diskCache.clear();
    }
    
    public void clearMemoryCache() {
        this.checkConfiguration();
        this.configuration.memoryCache.clear();
    }
    
    public void denyNetworkDownloads(final boolean b) {
        this.engine.denyNetworkDownloads(b);
    }
    
    public void destroy() {
        if (this.configuration != null) {
            L.d("Destroy ImageLoader", new Object[0]);
        }
        this.stop();
        this.configuration.diskCache.close();
        this.engine = null;
        this.configuration = null;
    }
    
    public void displayImage(final String s, final ImageView imageView) {
        this.displayImage(s, new ImageViewAware(imageView), null, null, null);
    }
    
    public void displayImage(final String s, final ImageView imageView, final DisplayImageOptions displayImageOptions) {
        this.displayImage(s, new ImageViewAware(imageView), displayImageOptions, null, null);
    }
    
    public void displayImage(final String s, final ImageView imageView, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener) {
        this.displayImage(s, imageView, displayImageOptions, imageLoadingListener, null);
    }
    
    public void displayImage(final String s, final ImageView imageView, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener, final ImageLoadingProgressListener imageLoadingProgressListener) {
        this.displayImage(s, new ImageViewAware(imageView), displayImageOptions, imageLoadingListener, imageLoadingProgressListener);
    }
    
    public void displayImage(final String s, final ImageView imageView, final ImageLoadingListener imageLoadingListener) {
        this.displayImage(s, new ImageViewAware(imageView), null, imageLoadingListener, null);
    }
    
    public void displayImage(final String s, final ImageAware imageAware) {
        this.displayImage(s, imageAware, null, null, null);
    }
    
    public void displayImage(final String s, final ImageAware imageAware, final DisplayImageOptions displayImageOptions) {
        this.displayImage(s, imageAware, displayImageOptions, null, null);
    }
    
    public void displayImage(final String s, final ImageAware imageAware, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener) {
        this.displayImage(s, imageAware, displayImageOptions, imageLoadingListener, null);
    }
    
    public void displayImage(final String s, final ImageAware imageAware, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener, final ImageLoadingProgressListener imageLoadingProgressListener) {
        this.checkConfiguration();
        if (imageAware == null) {
            throw new IllegalArgumentException("Wrong arguments were passed to displayImage() method (ImageView reference must not be null)");
        }
        ImageLoadingListener defaultListener;
        if ((defaultListener = imageLoadingListener) == null) {
            defaultListener = this.defaultListener;
        }
        DisplayImageOptions defaultDisplayImageOptions;
        if ((defaultDisplayImageOptions = displayImageOptions) == null) {
            defaultDisplayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            this.engine.cancelDisplayTaskFor(imageAware);
            defaultListener.onLoadingStarted(s, imageAware.getWrappedView());
            if (defaultDisplayImageOptions.shouldShowImageForEmptyUri()) {
                imageAware.setImageDrawable(defaultDisplayImageOptions.getImageForEmptyUri(this.configuration.resources));
            }
            else {
                imageAware.setImageDrawable(null);
            }
            defaultListener.onLoadingComplete(s, imageAware.getWrappedView(), null);
            return;
        }
        final ImageSize defineTargetSizeForView = ImageSizeUtils.defineTargetSizeForView(imageAware, this.configuration.getMaxImageSize());
        final String generateKey = MemoryCacheUtils.generateKey(s, defineTargetSizeForView);
        this.engine.prepareDisplayTaskFor(imageAware, generateKey);
        defaultListener.onLoadingStarted(s, imageAware.getWrappedView());
        final Bitmap value = this.configuration.memoryCache.get(generateKey);
        if (value != null && !value.isRecycled()) {
            L.d("Load image from memory cache [%s]", generateKey);
            if (!defaultDisplayImageOptions.shouldPostProcess()) {
                defaultDisplayImageOptions.getDisplayer().display(value, imageAware, LoadedFrom.MEMORY_CACHE);
                defaultListener.onLoadingComplete(s, imageAware.getWrappedView(), value);
                return;
            }
            final ProcessAndDisplayImageTask processAndDisplayImageTask = new ProcessAndDisplayImageTask(this.engine, value, new ImageLoadingInfo(s, imageAware, defineTargetSizeForView, generateKey, defaultDisplayImageOptions, defaultListener, imageLoadingProgressListener, this.engine.getLockForUri(s)), defineHandler(defaultDisplayImageOptions));
            if (defaultDisplayImageOptions.isSyncLoading()) {
                processAndDisplayImageTask.run();
                return;
            }
            this.engine.submit(processAndDisplayImageTask);
        }
        else {
            if (defaultDisplayImageOptions.shouldShowImageOnLoading()) {
                imageAware.setImageDrawable(defaultDisplayImageOptions.getImageOnLoading(this.configuration.resources));
            }
            else if (defaultDisplayImageOptions.isResetViewBeforeLoading()) {
                imageAware.setImageDrawable(null);
            }
            final LoadAndDisplayImageTask loadAndDisplayImageTask = new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(s, imageAware, defineTargetSizeForView, generateKey, defaultDisplayImageOptions, defaultListener, imageLoadingProgressListener, this.engine.getLockForUri(s)), defineHandler(defaultDisplayImageOptions));
            if (defaultDisplayImageOptions.isSyncLoading()) {
                loadAndDisplayImageTask.run();
                return;
            }
            this.engine.submit(loadAndDisplayImageTask);
        }
    }
    
    public void displayImage(final String s, final ImageAware imageAware, final ImageLoadingListener imageLoadingListener) {
        this.displayImage(s, imageAware, null, imageLoadingListener, null);
    }
    
    @Deprecated
    public DiskCache getDiscCache() {
        return this.getDiskCache();
    }
    
    public DiskCache getDiskCache() {
        this.checkConfiguration();
        return this.configuration.diskCache;
    }
    
    public String getLoadingUriForView(final ImageView imageView) {
        return this.engine.getLoadingUriForView(new ImageViewAware(imageView));
    }
    
    public String getLoadingUriForView(final ImageAware imageAware) {
        return this.engine.getLoadingUriForView(imageAware);
    }
    
    public MemoryCache getMemoryCache() {
        this.checkConfiguration();
        return this.configuration.memoryCache;
    }
    
    public void handleSlowNetwork(final boolean b) {
        this.engine.handleSlowNetwork(b);
    }
    
    public void init(final ImageLoaderConfiguration configuration) {
        // monitorenter(this)
        if (configuration == null) {
            try {
                throw new IllegalArgumentException("ImageLoader configuration can not be initialized with null");
            }
            finally {
            }
            // monitorexit(this)
        }
        if (this.configuration == null) {
            L.d("Initialize ImageLoader with configuration", new Object[0]);
            this.engine = new ImageLoaderEngine(configuration);
            this.configuration = configuration;
        }
        else {
            L.w("Try to initialize ImageLoader which had already been initialized before. To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.", new Object[0]);
        }
    }
    // monitorexit(this)
    
    public boolean isInited() {
        return this.configuration != null;
    }
    
    public void loadImage(final String s, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener) {
        this.loadImage(s, null, displayImageOptions, imageLoadingListener, null);
    }
    
    public void loadImage(final String s, final ImageSize imageSize, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener) {
        this.loadImage(s, imageSize, displayImageOptions, imageLoadingListener, null);
    }
    
    public void loadImage(final String s, final ImageSize imageSize, final DisplayImageOptions displayImageOptions, final ImageLoadingListener imageLoadingListener, final ImageLoadingProgressListener imageLoadingProgressListener) {
        this.checkConfiguration();
        ImageSize maxImageSize = imageSize;
        if (imageSize == null) {
            maxImageSize = this.configuration.getMaxImageSize();
        }
        DisplayImageOptions defaultDisplayImageOptions;
        if ((defaultDisplayImageOptions = displayImageOptions) == null) {
            defaultDisplayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        this.displayImage(s, new NonViewAware(s, maxImageSize, ViewScaleType.CROP), defaultDisplayImageOptions, imageLoadingListener, imageLoadingProgressListener);
    }
    
    public void loadImage(final String s, final ImageSize imageSize, final ImageLoadingListener imageLoadingListener) {
        this.loadImage(s, imageSize, null, imageLoadingListener, null);
    }
    
    public void loadImage(final String s, final ImageLoadingListener imageLoadingListener) {
        this.loadImage(s, null, null, imageLoadingListener, null);
    }
    
    public Bitmap loadImageSync(final String s) {
        return this.loadImageSync(s, null, null);
    }
    
    public Bitmap loadImageSync(final String s, final DisplayImageOptions displayImageOptions) {
        return this.loadImageSync(s, null, displayImageOptions);
    }
    
    public Bitmap loadImageSync(final String s, final ImageSize imageSize) {
        return this.loadImageSync(s, imageSize, null);
    }
    
    public Bitmap loadImageSync(final String s, final ImageSize imageSize, DisplayImageOptions build) {
        DisplayImageOptions defaultDisplayImageOptions = build;
        if (build == null) {
            defaultDisplayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        build = new DisplayImageOptions.Builder().cloneFrom(defaultDisplayImageOptions).syncLoading(true).build();
        final SyncImageLoadingListener syncImageLoadingListener = new SyncImageLoadingListener();
        this.loadImage(s, imageSize, build, syncImageLoadingListener);
        return syncImageLoadingListener.getLoadedBitmap();
    }
    
    public void pause() {
        this.engine.pause();
    }
    
    public void resume() {
        this.engine.resume();
    }
    
    public void setDefaultLoadingListener(final ImageLoadingListener imageLoadingListener) {
        ImageLoadingListener defaultListener = imageLoadingListener;
        if (imageLoadingListener == null) {
            defaultListener = new SimpleImageLoadingListener();
        }
        this.defaultListener = defaultListener;
    }
    
    public void stop() {
        this.engine.stop();
    }
    
    private static class SyncImageLoadingListener extends SimpleImageLoadingListener
    {
        private Bitmap loadedImage;
        
        public Bitmap getLoadedBitmap() {
            return this.loadedImage;
        }
        
        @Override
        public void onLoadingComplete(final String s, final View view, final Bitmap loadedImage) {
            this.loadedImage = loadedImage;
        }
    }
}
