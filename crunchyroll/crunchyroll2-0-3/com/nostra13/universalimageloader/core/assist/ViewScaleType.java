// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist;

import android.widget.ImageView$ScaleType;
import android.widget.ImageView;

public enum ViewScaleType
{
    CROP, 
    FIT_INSIDE;
    
    public static ViewScaleType fromImageView(final ImageView imageView) {
        switch (imageView.getScaleType()) {
            default: {
                return ViewScaleType.CROP;
            }
            case FIT_CENTER:
            case FIT_XY:
            case FIT_START:
            case FIT_END:
            case CENTER_INSIDE: {
                return ViewScaleType.FIT_INSIDE;
            }
        }
    }
}
