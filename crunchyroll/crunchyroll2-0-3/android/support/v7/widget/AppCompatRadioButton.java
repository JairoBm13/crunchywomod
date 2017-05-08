// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.annotation.DrawableRes;
import android.os.Build$VERSION;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.widget.TintManager;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

public class AppCompatRadioButton extends RadioButton
{
    private static final int[] TINT_ATTRS;
    private Drawable mButtonDrawable;
    private TintManager mTintManager;
    
    static {
        TINT_ATTRS = new int[] { 16843015 };
    }
    
    public AppCompatRadioButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatRadioButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.radioButtonStyle);
    }
    
    public AppCompatRadioButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        if (TintManager.SHOULD_BE_USED) {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, AppCompatRadioButton.TINT_ATTRS, n, 0);
            this.setButtonDrawable(obtainStyledAttributes.getDrawable(0));
            obtainStyledAttributes.recycle();
            this.mTintManager = obtainStyledAttributes.getTintManager();
        }
    }
    
    public int getCompoundPaddingLeft() {
        int compoundPaddingLeft;
        final int n = compoundPaddingLeft = super.getCompoundPaddingLeft();
        if (Build$VERSION.SDK_INT < 17) {
            compoundPaddingLeft = n;
            if (this.mButtonDrawable != null) {
                compoundPaddingLeft = n + this.mButtonDrawable.getIntrinsicWidth();
            }
        }
        return compoundPaddingLeft;
    }
    
    public void setButtonDrawable(@DrawableRes final int buttonDrawable) {
        if (this.mTintManager != null) {
            this.setButtonDrawable(this.mTintManager.getDrawable(buttonDrawable));
            return;
        }
        super.setButtonDrawable(buttonDrawable);
    }
    
    public void setButtonDrawable(final Drawable drawable) {
        super.setButtonDrawable(drawable);
        this.mButtonDrawable = drawable;
    }
}
