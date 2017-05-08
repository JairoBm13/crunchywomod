// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap$Config;
import android.content.res.Resources;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import android.graphics.BitmapFactory$Options;

public final class DisplayImageOptions
{
    private final boolean cacheInMemory;
    private final boolean cacheOnDisk;
    private final boolean considerExifParams;
    private final BitmapFactory$Options decodingOptions;
    private final int delayBeforeLoading;
    private final BitmapDisplayer displayer;
    private final Object extraForDownloader;
    private final Handler handler;
    private final Drawable imageForEmptyUri;
    private final Drawable imageOnFail;
    private final Drawable imageOnLoading;
    private final int imageResForEmptyUri;
    private final int imageResOnFail;
    private final int imageResOnLoading;
    private final ImageScaleType imageScaleType;
    private final boolean isSyncLoading;
    private final BitmapProcessor postProcessor;
    private final BitmapProcessor preProcessor;
    private final boolean resetViewBeforeLoading;
    
    private DisplayImageOptions(final Builder builder) {
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResOnFail = builder.imageResOnFail;
        this.imageOnLoading = builder.imageOnLoading;
        this.imageForEmptyUri = builder.imageForEmptyUri;
        this.imageOnFail = builder.imageOnFail;
        this.resetViewBeforeLoading = builder.resetViewBeforeLoading;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheOnDisk = builder.cacheOnDisk;
        this.imageScaleType = builder.imageScaleType;
        this.decodingOptions = builder.decodingOptions;
        this.delayBeforeLoading = builder.delayBeforeLoading;
        this.considerExifParams = builder.considerExifParams;
        this.extraForDownloader = builder.extraForDownloader;
        this.preProcessor = builder.preProcessor;
        this.postProcessor = builder.postProcessor;
        this.displayer = builder.displayer;
        this.handler = builder.handler;
        this.isSyncLoading = builder.isSyncLoading;
    }
    
    public static DisplayImageOptions createSimple() {
        return new Builder().build();
    }
    
    public BitmapFactory$Options getDecodingOptions() {
        return this.decodingOptions;
    }
    
    public int getDelayBeforeLoading() {
        return this.delayBeforeLoading;
    }
    
    public BitmapDisplayer getDisplayer() {
        return this.displayer;
    }
    
    public Object getExtraForDownloader() {
        return this.extraForDownloader;
    }
    
    public Handler getHandler() {
        return this.handler;
    }
    
    public Drawable getImageForEmptyUri(final Resources resources) {
        if (this.imageResForEmptyUri != 0) {
            return resources.getDrawable(this.imageResForEmptyUri);
        }
        return this.imageForEmptyUri;
    }
    
    public Drawable getImageOnFail(final Resources resources) {
        if (this.imageResOnFail != 0) {
            return resources.getDrawable(this.imageResOnFail);
        }
        return this.imageOnFail;
    }
    
    public Drawable getImageOnLoading(final Resources resources) {
        if (this.imageResOnLoading != 0) {
            return resources.getDrawable(this.imageResOnLoading);
        }
        return this.imageOnLoading;
    }
    
    public ImageScaleType getImageScaleType() {
        return this.imageScaleType;
    }
    
    public BitmapProcessor getPostProcessor() {
        return this.postProcessor;
    }
    
    public BitmapProcessor getPreProcessor() {
        return this.preProcessor;
    }
    
    public boolean isCacheInMemory() {
        return this.cacheInMemory;
    }
    
    public boolean isCacheOnDisk() {
        return this.cacheOnDisk;
    }
    
    public boolean isConsiderExifParams() {
        return this.considerExifParams;
    }
    
    public boolean isResetViewBeforeLoading() {
        return this.resetViewBeforeLoading;
    }
    
    boolean isSyncLoading() {
        return this.isSyncLoading;
    }
    
    public boolean shouldDelayBeforeLoading() {
        return this.delayBeforeLoading > 0;
    }
    
    public boolean shouldPostProcess() {
        return this.postProcessor != null;
    }
    
    public boolean shouldPreProcess() {
        return this.preProcessor != null;
    }
    
    public boolean shouldShowImageForEmptyUri() {
        return this.imageForEmptyUri != null || this.imageResForEmptyUri != 0;
    }
    
    public boolean shouldShowImageOnFail() {
        return this.imageOnFail != null || this.imageResOnFail != 0;
    }
    
    public boolean shouldShowImageOnLoading() {
        return this.imageOnLoading != null || this.imageResOnLoading != 0;
    }
    
    public static class Builder
    {
        private boolean cacheInMemory;
        private boolean cacheOnDisk;
        private boolean considerExifParams;
        private BitmapFactory$Options decodingOptions;
        private int delayBeforeLoading;
        private BitmapDisplayer displayer;
        private Object extraForDownloader;
        private Handler handler;
        private Drawable imageForEmptyUri;
        private Drawable imageOnFail;
        private Drawable imageOnLoading;
        private int imageResForEmptyUri;
        private int imageResOnFail;
        private int imageResOnLoading;
        private ImageScaleType imageScaleType;
        private boolean isSyncLoading;
        private BitmapProcessor postProcessor;
        private BitmapProcessor preProcessor;
        private boolean resetViewBeforeLoading;
        
        public Builder() {
            this.imageResOnLoading = 0;
            this.imageResForEmptyUri = 0;
            this.imageResOnFail = 0;
            this.imageOnLoading = null;
            this.imageForEmptyUri = null;
            this.imageOnFail = null;
            this.resetViewBeforeLoading = false;
            this.cacheInMemory = false;
            this.cacheOnDisk = false;
            this.imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
            this.decodingOptions = new BitmapFactory$Options();
            this.delayBeforeLoading = 0;
            this.considerExifParams = false;
            this.extraForDownloader = null;
            this.preProcessor = null;
            this.postProcessor = null;
            this.displayer = DefaultConfigurationFactory.createBitmapDisplayer();
            this.handler = null;
            this.isSyncLoading = false;
            this.decodingOptions.inPurgeable = true;
            this.decodingOptions.inInputShareable = true;
        }
        
        public Builder bitmapConfig(final Bitmap$Config inPreferredConfig) {
            if (inPreferredConfig == null) {
                throw new IllegalArgumentException("bitmapConfig can't be null");
            }
            this.decodingOptions.inPreferredConfig = inPreferredConfig;
            return this;
        }
        
        public DisplayImageOptions build() {
            return new DisplayImageOptions(this, null);
        }
        
        @Deprecated
        public Builder cacheInMemory() {
            this.cacheInMemory = true;
            return this;
        }
        
        public Builder cacheInMemory(final boolean cacheInMemory) {
            this.cacheInMemory = cacheInMemory;
            return this;
        }
        
        @Deprecated
        public Builder cacheOnDisc() {
            return this.cacheOnDisk(true);
        }
        
        @Deprecated
        public Builder cacheOnDisc(final boolean b) {
            return this.cacheOnDisk(b);
        }
        
        public Builder cacheOnDisk(final boolean cacheOnDisk) {
            this.cacheOnDisk = cacheOnDisk;
            return this;
        }
        
        public Builder cloneFrom(final DisplayImageOptions displayImageOptions) {
            this.imageResOnLoading = displayImageOptions.imageResOnLoading;
            this.imageResForEmptyUri = displayImageOptions.imageResForEmptyUri;
            this.imageResOnFail = displayImageOptions.imageResOnFail;
            this.imageOnLoading = displayImageOptions.imageOnLoading;
            this.imageForEmptyUri = displayImageOptions.imageForEmptyUri;
            this.imageOnFail = displayImageOptions.imageOnFail;
            this.resetViewBeforeLoading = displayImageOptions.resetViewBeforeLoading;
            this.cacheInMemory = displayImageOptions.cacheInMemory;
            this.cacheOnDisk = displayImageOptions.cacheOnDisk;
            this.imageScaleType = displayImageOptions.imageScaleType;
            this.decodingOptions = displayImageOptions.decodingOptions;
            this.delayBeforeLoading = displayImageOptions.delayBeforeLoading;
            this.considerExifParams = displayImageOptions.considerExifParams;
            this.extraForDownloader = displayImageOptions.extraForDownloader;
            this.preProcessor = displayImageOptions.preProcessor;
            this.postProcessor = displayImageOptions.postProcessor;
            this.displayer = displayImageOptions.displayer;
            this.handler = displayImageOptions.handler;
            this.isSyncLoading = displayImageOptions.isSyncLoading;
            return this;
        }
        
        public Builder considerExifParams(final boolean considerExifParams) {
            this.considerExifParams = considerExifParams;
            return this;
        }
        
        public Builder decodingOptions(final BitmapFactory$Options decodingOptions) {
            if (decodingOptions == null) {
                throw new IllegalArgumentException("decodingOptions can't be null");
            }
            this.decodingOptions = decodingOptions;
            return this;
        }
        
        public Builder delayBeforeLoading(final int delayBeforeLoading) {
            this.delayBeforeLoading = delayBeforeLoading;
            return this;
        }
        
        public Builder displayer(final BitmapDisplayer displayer) {
            if (displayer == null) {
                throw new IllegalArgumentException("displayer can't be null");
            }
            this.displayer = displayer;
            return this;
        }
        
        public Builder extraForDownloader(final Object extraForDownloader) {
            this.extraForDownloader = extraForDownloader;
            return this;
        }
        
        public Builder handler(final Handler handler) {
            this.handler = handler;
            return this;
        }
        
        public Builder imageScaleType(final ImageScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
            return this;
        }
        
        public Builder postProcessor(final BitmapProcessor postProcessor) {
            this.postProcessor = postProcessor;
            return this;
        }
        
        public Builder preProcessor(final BitmapProcessor preProcessor) {
            this.preProcessor = preProcessor;
            return this;
        }
        
        public Builder resetViewBeforeLoading() {
            this.resetViewBeforeLoading = true;
            return this;
        }
        
        public Builder resetViewBeforeLoading(final boolean resetViewBeforeLoading) {
            this.resetViewBeforeLoading = resetViewBeforeLoading;
            return this;
        }
        
        public Builder showImageForEmptyUri(final int imageResForEmptyUri) {
            this.imageResForEmptyUri = imageResForEmptyUri;
            return this;
        }
        
        public Builder showImageForEmptyUri(final Drawable imageForEmptyUri) {
            this.imageForEmptyUri = imageForEmptyUri;
            return this;
        }
        
        public Builder showImageOnFail(final int imageResOnFail) {
            this.imageResOnFail = imageResOnFail;
            return this;
        }
        
        public Builder showImageOnFail(final Drawable imageOnFail) {
            this.imageOnFail = imageOnFail;
            return this;
        }
        
        public Builder showImageOnLoading(final int imageResOnLoading) {
            this.imageResOnLoading = imageResOnLoading;
            return this;
        }
        
        public Builder showImageOnLoading(final Drawable imageOnLoading) {
            this.imageOnLoading = imageOnLoading;
            return this;
        }
        
        @Deprecated
        public Builder showStubImage(final int imageResOnLoading) {
            this.imageResOnLoading = imageResOnLoading;
            return this;
        }
        
        Builder syncLoading(final boolean isSyncLoading) {
            this.isSyncLoading = isSyncLoading;
            return this;
        }
    }
}
