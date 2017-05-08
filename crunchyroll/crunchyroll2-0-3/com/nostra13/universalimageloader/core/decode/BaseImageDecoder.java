// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.decode;

import android.graphics.BitmapFactory$Options;
import android.media.ExifInterface;
import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;
import com.nostra13.universalimageloader.utils.IoUtils;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

public class BaseImageDecoder implements ImageDecoder
{
    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";
    protected static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
    protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
    protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d° [%2$s]";
    protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
    protected static final String LOG_SUBSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
    protected final boolean loggingEnabled;
    
    public BaseImageDecoder(final boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
    
    private boolean canDefineExifParams(final String s, final String s2) {
        return "image/jpeg".equalsIgnoreCase(s2) && ImageDownloader.Scheme.ofUri(s) == ImageDownloader.Scheme.FILE;
    }
    
    protected Bitmap considerExactScaleAndOrientatiton(final Bitmap bitmap, final ImageDecodingInfo imageDecodingInfo, final int n, final boolean b) {
        final Matrix matrix = new Matrix();
        final ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        if (imageScaleType == ImageScaleType.EXACTLY || imageScaleType == ImageScaleType.EXACTLY_STRETCHED) {
            final ImageSize imageSize = new ImageSize(bitmap.getWidth(), bitmap.getHeight(), n);
            final float computeImageScale = ImageSizeUtils.computeImageScale(imageSize, imageDecodingInfo.getTargetSize(), imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.EXACTLY_STRETCHED);
            if (Float.compare(computeImageScale, 1.0f) != 0) {
                matrix.setScale(computeImageScale, computeImageScale);
                if (this.loggingEnabled) {
                    L.d("Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]", imageSize, imageSize.scale(computeImageScale), computeImageScale, imageDecodingInfo.getImageKey());
                }
            }
        }
        if (b) {
            matrix.postScale(-1.0f, 1.0f);
            if (this.loggingEnabled) {
                L.d("Flip image horizontally [%s]", imageDecodingInfo.getImageKey());
            }
        }
        if (n != 0) {
            matrix.postRotate((float)n);
            if (this.loggingEnabled) {
                L.d("Rotate image on %1$d° [%2$s]", n, imageDecodingInfo.getImageKey());
            }
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap2 != bitmap) {
            bitmap.recycle();
        }
        return bitmap2;
    }
    
    @Override
    public Bitmap decode(final ImageDecodingInfo imageDecodingInfo) throws IOException {
        final InputStream imageStream = this.getImageStream(imageDecodingInfo);
        if (imageStream == null) {
            L.e("No stream for image [%s]", imageDecodingInfo.getImageKey());
            return null;
        }
        InputStream resetStream = imageStream;
        ImageFileInfo defineImageSizeAndRotation;
        Bitmap decodeStream;
        try {
            defineImageSizeAndRotation = this.defineImageSizeAndRotation(imageStream, imageDecodingInfo);
            resetStream = imageStream;
            final InputStream inputStream = resetStream = this.resetStream(imageStream, imageDecodingInfo);
            decodeStream = BitmapFactory.decodeStream(inputStream, (Rect)null, this.prepareDecodingOptions(defineImageSizeAndRotation.imageSize, imageDecodingInfo));
            IoUtils.closeSilently(inputStream);
            if (decodeStream == null) {
                L.e("Image can't be decoded [%s]", imageDecodingInfo.getImageKey());
                return decodeStream;
            }
        }
        finally {
            IoUtils.closeSilently(resetStream);
        }
        final ImageDecodingInfo imageDecodingInfo2;
        return this.considerExactScaleAndOrientatiton(decodeStream, imageDecodingInfo2, defineImageSizeAndRotation.exif.rotation, defineImageSizeAndRotation.exif.flipHorizontal);
    }
    
    protected ExifInfo defineExifOrientation(final String s) {
        int n = 0;
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 = false;
        final boolean b5 = false;
        while (true) {
            try {
                switch (new ExifInterface(ImageDownloader.Scheme.FILE.crop(s)).getAttributeInt("Orientation", 1)) {
                    default: {
                        b = b5;
                        break;
                    }
                    case 2: {
                        b = true;
                    }
                    case 1: {
                        n = 0;
                        break;
                    }
                    case 7: {
                        b2 = true;
                    }
                    case 6: {
                        n = 90;
                        b = b2;
                        break;
                    }
                    case 4: {
                        b3 = true;
                    }
                    case 3: {
                        n = 180;
                        b = b3;
                        break;
                    }
                    case 5: {
                        b4 = true;
                    }
                    case 8: {
                        n = 270;
                        b = b4;
                        break;
                    }
                }
                return new ExifInfo(n, b);
            }
            catch (IOException ex) {
                L.w("Can't read EXIF tags from file [%s]", s);
                b = b5;
                return new ExifInfo(n, b);
            }
            continue;
        }
    }
    
    protected ImageFileInfo defineImageSizeAndRotation(final InputStream inputStream, final ImageDecodingInfo imageDecodingInfo) throws IOException {
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options);
        final String imageUri = imageDecodingInfo.getImageUri();
        ExifInfo defineExifOrientation;
        if (imageDecodingInfo.shouldConsiderExifParams() && this.canDefineExifParams(imageUri, bitmapFactory$Options.outMimeType)) {
            defineExifOrientation = this.defineExifOrientation(imageUri);
        }
        else {
            defineExifOrientation = new ExifInfo();
        }
        return new ImageFileInfo(new ImageSize(bitmapFactory$Options.outWidth, bitmapFactory$Options.outHeight, defineExifOrientation.rotation), defineExifOrientation);
    }
    
    protected InputStream getImageStream(final ImageDecodingInfo imageDecodingInfo) throws IOException {
        return imageDecodingInfo.getDownloader().getStream(imageDecodingInfo.getImageUri(), imageDecodingInfo.getExtraForDownloader());
    }
    
    protected BitmapFactory$Options prepareDecodingOptions(final ImageSize imageSize, final ImageDecodingInfo imageDecodingInfo) {
        final ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        int inSampleSize;
        if (imageScaleType == ImageScaleType.NONE) {
            inSampleSize = 1;
        }
        else if (imageScaleType == ImageScaleType.NONE_SAFE) {
            inSampleSize = ImageSizeUtils.computeMinImageSampleSize(imageSize);
        }
        else {
            inSampleSize = ImageSizeUtils.computeImageSampleSize(imageSize, imageDecodingInfo.getTargetSize(), imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2);
        }
        if (inSampleSize > 1 && this.loggingEnabled) {
            L.d("Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]", imageSize, imageSize.scaleDown(inSampleSize), inSampleSize, imageDecodingInfo.getImageKey());
        }
        final BitmapFactory$Options decodingOptions = imageDecodingInfo.getDecodingOptions();
        decodingOptions.inSampleSize = inSampleSize;
        return decodingOptions;
    }
    
    protected InputStream resetStream(final InputStream inputStream, final ImageDecodingInfo imageDecodingInfo) throws IOException {
        try {
            inputStream.reset();
            return inputStream;
        }
        catch (IOException ex) {
            IoUtils.closeSilently(inputStream);
            return this.getImageStream(imageDecodingInfo);
        }
    }
    
    protected static class ExifInfo
    {
        public final boolean flipHorizontal;
        public final int rotation;
        
        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }
        
        protected ExifInfo(final int rotation, final boolean flipHorizontal) {
            this.rotation = rotation;
            this.flipHorizontal = flipHorizontal;
        }
    }
    
    protected static class ImageFileInfo
    {
        public final ExifInfo exif;
        public final ImageSize imageSize;
        
        protected ImageFileInfo(final ImageSize imageSize, final ExifInfo exif) {
            this.imageSize = imageSize;
            this.exif = exif;
        }
    }
}
