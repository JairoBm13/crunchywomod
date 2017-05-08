// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.decode;

import android.annotation.TargetApi;
import android.os.Build$VERSION;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import android.graphics.BitmapFactory$Options;

public class ImageDecodingInfo
{
    private final boolean considerExifParams;
    private final BitmapFactory$Options decodingOptions;
    private final ImageDownloader downloader;
    private final Object extraForDownloader;
    private final String imageKey;
    private final ImageScaleType imageScaleType;
    private final String imageUri;
    private final String originalImageUri;
    private final ImageSize targetSize;
    private final ViewScaleType viewScaleType;
    
    public ImageDecodingInfo(final String imageKey, final String imageUri, final String originalImageUri, final ImageSize targetSize, final ViewScaleType viewScaleType, final ImageDownloader downloader, final DisplayImageOptions displayImageOptions) {
        this.imageKey = imageKey;
        this.imageUri = imageUri;
        this.originalImageUri = originalImageUri;
        this.targetSize = targetSize;
        this.imageScaleType = displayImageOptions.getImageScaleType();
        this.viewScaleType = viewScaleType;
        this.downloader = downloader;
        this.extraForDownloader = displayImageOptions.getExtraForDownloader();
        this.considerExifParams = displayImageOptions.isConsiderExifParams();
        this.decodingOptions = new BitmapFactory$Options();
        this.copyOptions(displayImageOptions.getDecodingOptions(), this.decodingOptions);
    }
    
    private void copyOptions(final BitmapFactory$Options bitmapFactory$Options, final BitmapFactory$Options bitmapFactory$Options2) {
        bitmapFactory$Options2.inDensity = bitmapFactory$Options.inDensity;
        bitmapFactory$Options2.inDither = bitmapFactory$Options.inDither;
        bitmapFactory$Options2.inInputShareable = bitmapFactory$Options.inInputShareable;
        bitmapFactory$Options2.inJustDecodeBounds = bitmapFactory$Options.inJustDecodeBounds;
        bitmapFactory$Options2.inPreferredConfig = bitmapFactory$Options.inPreferredConfig;
        bitmapFactory$Options2.inPurgeable = bitmapFactory$Options.inPurgeable;
        bitmapFactory$Options2.inSampleSize = bitmapFactory$Options.inSampleSize;
        bitmapFactory$Options2.inScaled = bitmapFactory$Options.inScaled;
        bitmapFactory$Options2.inScreenDensity = bitmapFactory$Options.inScreenDensity;
        bitmapFactory$Options2.inTargetDensity = bitmapFactory$Options.inTargetDensity;
        bitmapFactory$Options2.inTempStorage = bitmapFactory$Options.inTempStorage;
        if (Build$VERSION.SDK_INT >= 10) {
            this.copyOptions10(bitmapFactory$Options, bitmapFactory$Options2);
        }
        if (Build$VERSION.SDK_INT >= 11) {
            this.copyOptions11(bitmapFactory$Options, bitmapFactory$Options2);
        }
    }
    
    @TargetApi(10)
    private void copyOptions10(final BitmapFactory$Options bitmapFactory$Options, final BitmapFactory$Options bitmapFactory$Options2) {
        bitmapFactory$Options2.inPreferQualityOverSpeed = bitmapFactory$Options.inPreferQualityOverSpeed;
    }
    
    @TargetApi(11)
    private void copyOptions11(final BitmapFactory$Options bitmapFactory$Options, final BitmapFactory$Options bitmapFactory$Options2) {
        bitmapFactory$Options2.inBitmap = bitmapFactory$Options.inBitmap;
        bitmapFactory$Options2.inMutable = bitmapFactory$Options.inMutable;
    }
    
    public BitmapFactory$Options getDecodingOptions() {
        return this.decodingOptions;
    }
    
    public ImageDownloader getDownloader() {
        return this.downloader;
    }
    
    public Object getExtraForDownloader() {
        return this.extraForDownloader;
    }
    
    public String getImageKey() {
        return this.imageKey;
    }
    
    public ImageScaleType getImageScaleType() {
        return this.imageScaleType;
    }
    
    public String getImageUri() {
        return this.imageUri;
    }
    
    public String getOriginalImageUri() {
        return this.originalImageUri;
    }
    
    public ImageSize getTargetSize() {
        return this.targetSize;
    }
    
    public ViewScaleType getViewScaleType() {
        return this.viewScaleType;
    }
    
    public boolean shouldConsiderExifParams() {
        return this.considerExifParams;
    }
}
