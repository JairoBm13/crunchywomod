// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import android.widget.ImageView$ScaleType;
import com.crunchyroll.crunchyroid.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Matrix;
import android.widget.ImageView;

public class SmartFillImageView extends ImageView
{
    public static final int BOTTOM = 5;
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_RIGHT = 2;
    public static final int LEFT = 6;
    public static final int RIGHT = 7;
    public static final int TOP = 4;
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    private final int mAlign;
    private Matrix mMatrix;
    
    public SmartFillImageView(final Context context) {
        super(context);
        this.mAlign = 0;
        this.init();
    }
    
    public SmartFillImageView(Context obtainStyledAttributes, final AttributeSet set) {
        super(obtainStyledAttributes, set);
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.SmartFillImageView, 0, 0);
        try {
            this.mAlign = ((TypedArray)obtainStyledAttributes).getInt(0, 0);
            ((TypedArray)obtainStyledAttributes).recycle();
            this.init();
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    private void init() {
        this.setImageMatrix(this.mMatrix = new Matrix());
        this.setScaleType(ImageView$ScaleType.MATRIX);
        this.mMatrix.setScale(1.0f, 1.0f);
        this.setImageMatrix(this.mMatrix);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final Drawable drawable = this.getDrawable();
        if (drawable != null) {
            final float n3 = drawable.getIntrinsicWidth();
            final float n4 = drawable.getIntrinsicHeight();
            final float n5 = View$MeasureSpec.getSize(n);
            final float n6 = View$MeasureSpec.getSize(n2);
            float n7 = n5 / n3;
            final float n8 = n6 / n4;
            if (n7 <= n8) {
                n7 = n8;
            }
            final float n9 = n3 * n7;
            final float n10 = n4 * n7;
            final float n11 = 0.0f;
            final float n12 = 0.0f;
            float n13 = n11;
            float n14 = n12;
            Label_0151: {
                switch (this.mAlign) {
                    default: {
                        n14 = n12;
                        n13 = n11;
                        break Label_0151;
                    }
                    case 7: {
                        n13 = n5 - n9;
                        n14 = (n6 - n10) / 2.0f;
                        break Label_0151;
                    }
                    case 6: {
                        n14 = (n6 - n10) / 2.0f;
                        n13 = n11;
                        break Label_0151;
                    }
                    case 5: {
                        n13 = (n5 - n9) / 2.0f;
                        n14 = n6 - n10;
                        break Label_0151;
                    }
                    case 4: {
                        n13 = (n5 - n9) / 2.0f;
                        n14 = n12;
                        break Label_0151;
                    }
                    case 3: {
                        n14 = n6 - n10;
                        n13 = n11;
                        break Label_0151;
                    }
                    case 2: {
                        n13 = n5 - n9;
                        n14 = n6 - n10;
                        break Label_0151;
                    }
                    case 1: {
                        n13 = n5 - n9;
                        n14 = n12;
                    }
                    case 0: {
                        this.mMatrix.setScale(n7, n7);
                        this.setImageMatrix(this.mMatrix);
                        this.mMatrix.postTranslate(n13, n14);
                        this.setImageMatrix(this.mMatrix);
                        break;
                    }
                }
            }
        }
    }
}
