// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import android.opengl.GLES10;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public final class ImageSizeUtils
{
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static ImageSize maxBitmapSize;
    
    static {
        final int[] array = { 0 };
        GLES10.glGetIntegerv(3379, array, 0);
        final int max = Math.max(array[0], 2048);
        ImageSizeUtils.maxBitmapSize = new ImageSize(max, max);
    }
    
    public static int computeImageSampleSize(final ImageSize imageSize, final ImageSize imageSize2, final ViewScaleType viewScaleType, final boolean b) {
        final int width = imageSize.getWidth();
        final int height = imageSize.getHeight();
        final int width2 = imageSize2.getWidth();
        final int height2 = imageSize2.getHeight();
        int n = 1;
        final int n2 = 1;
        final boolean b2 = true;
        int n3 = 0;
        Label_0068: {
            switch (viewScaleType) {
                default: {
                    n3 = (b2 ? 1 : 0);
                    break;
                }
                case FIT_INSIDE: {
                    if (!b) {
                        n3 = Math.max(width / width2, height / height2);
                        break;
                    }
                    final int n4 = width / 2;
                    final int n5 = height / 2;
                    while (true) {
                        if (n4 / n <= width2) {
                            n3 = n;
                            if (n5 / n <= height2) {
                                break Label_0068;
                            }
                        }
                        n *= 2;
                    }
                    break;
                }
                case CROP: {
                    if (!b) {
                        n3 = Math.min(width / width2, height / height2);
                        break;
                    }
                    final int n6 = width / 2;
                    final int n7 = height / 2;
                    int n8 = n2;
                    while (true) {
                        n3 = n8;
                        if (n6 / n8 <= width2) {
                            break Label_0068;
                        }
                        n3 = n8;
                        if (n7 / n8 <= height2) {
                            break Label_0068;
                        }
                        n8 *= 2;
                    }
                    break;
                }
            }
        }
        int n9;
        if ((n9 = n3) < 1) {
            n9 = 1;
        }
        return considerMaxTextureSize(width, height, n9, b);
    }
    
    public static float computeImageScale(final ImageSize imageSize, final ImageSize imageSize2, final ViewScaleType viewScaleType, final boolean b) {
        final int width = imageSize.getWidth();
        final int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        final float n = width / width2;
        final float n2 = height / height2;
        if ((viewScaleType == ViewScaleType.FIT_INSIDE && n >= n2) || (viewScaleType == ViewScaleType.CROP && n < n2)) {
            height2 = (int)(height / n);
        }
        else {
            width2 = (int)(width / n2);
        }
        final float n3 = 1.0f;
        if (b || width2 >= width || height2 >= height) {
            float n4 = n3;
            if (!b) {
                return n4;
            }
            n4 = n3;
            if (width2 == width) {
                return n4;
            }
            n4 = n3;
            if (height2 == height) {
                return n4;
            }
        }
        return width2 / width;
    }
    
    public static int computeMinImageSampleSize(final ImageSize imageSize) {
        return Math.max((int)Math.ceil(imageSize.getWidth() / ImageSizeUtils.maxBitmapSize.getWidth()), (int)Math.ceil(imageSize.getHeight() / ImageSizeUtils.maxBitmapSize.getHeight()));
    }
    
    private static int considerMaxTextureSize(final int n, final int n2, int n3, final boolean b) {
        final int width = ImageSizeUtils.maxBitmapSize.getWidth();
        final int height = ImageSizeUtils.maxBitmapSize.getHeight();
        while (n / n3 > width || n2 / n3 > height) {
            if (b) {
                n3 *= 2;
            }
            else {
                ++n3;
            }
        }
        return n3;
    }
    
    public static ImageSize defineTargetSizeForView(final ImageAware imageAware, final ImageSize imageSize) {
        int n;
        if ((n = imageAware.getWidth()) <= 0) {
            n = imageSize.getWidth();
        }
        int n2;
        if ((n2 = imageAware.getHeight()) <= 0) {
            n2 = imageSize.getHeight();
        }
        return new ImageSize(n, n2);
    }
}
