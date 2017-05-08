// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.content.res.TypedArray;
import android.view.View$MeasureSpec;
import com.crunchyroll.crunchyroid.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

public class FixedAspectRatioImageView extends ImageView
{
    private final int mFixedAspectHeight;
    private final int mFixedAspectWidth;
    
    public FixedAspectRatioImageView(final Context context) {
        super(context);
        this.mFixedAspectWidth = -1;
        this.mFixedAspectHeight = -1;
    }
    
    public FixedAspectRatioImageView(Context obtainStyledAttributes, final AttributeSet set) {
        super(obtainStyledAttributes, set);
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.FixedAspectRatioImageView, 0, 0);
        try {
            this.mFixedAspectWidth = ((TypedArray)obtainStyledAttributes).getInt(0, -1);
            this.mFixedAspectHeight = ((TypedArray)obtainStyledAttributes).getInt(1, -1);
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    protected void onMeasure(int n, int n2) {
        if (this.mFixedAspectWidth > 0 && this.mFixedAspectHeight > 0) {
            final int mode = View$MeasureSpec.getMode(n);
            final int size = View$MeasureSpec.getSize(n);
            final int mode2 = View$MeasureSpec.getMode(n2);
            final int size2 = View$MeasureSpec.getSize(n2);
            Label_0070: {
                if (mode == 1073741824 && (mode2 == 0 || mode2 == Integer.MIN_VALUE)) {
                    n = this.mFixedAspectHeight * size / this.mFixedAspectWidth;
                    n2 = size;
                }
                else {
                    n = size2;
                    n2 = size;
                    if (mode2 == 1073741824) {
                        if (mode != 0) {
                            n = size2;
                            n2 = size;
                            if (mode != Integer.MIN_VALUE) {
                                break Label_0070;
                            }
                        }
                        n2 = this.mFixedAspectWidth * size2 / this.mFixedAspectHeight;
                        n = size2;
                    }
                }
            }
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(n2, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            return;
        }
        super.onMeasure(n, n2);
    }
}
