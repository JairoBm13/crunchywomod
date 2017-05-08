// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.annotation.TargetApi;
import java.lang.reflect.Field;
import android.widget.ListPopupWindow;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintInfo;
import android.support.v4.view.TintableBackgroundView;
import android.widget.Spinner;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView
{
    private static final int[] TINT_ATTRS;
    private TintInfo mBackgroundTint;
    private TintInfo mInternalBackgroundTint;
    private TintManager mTintManager;
    
    static {
        TINT_ATTRS = new int[] { 16842964, 16843126 };
    }
    
    public AppCompatSpinner(final Context context) {
        this(context, null);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set) {
        this(context, set, R.attr.spinnerStyle);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        if (TintManager.SHOULD_BE_USED) {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, AppCompatSpinner.TINT_ATTRS, n, 0);
            if (obtainStyledAttributes.hasValue(0)) {
                final ColorStateList tintList = obtainStyledAttributes.getTintManager().getTintList(obtainStyledAttributes.getResourceId(0, -1));
                if (tintList != null) {
                    this.setInternalBackgroundTint(tintList);
                }
            }
            if (obtainStyledAttributes.hasValue(1)) {
                final Drawable drawable = obtainStyledAttributes.getDrawable(1);
                if (Build$VERSION.SDK_INT >= 16) {
                    this.setPopupBackgroundDrawable(drawable);
                }
                else if (Build$VERSION.SDK_INT >= 11) {
                    setPopupBackgroundDrawableV11(this, drawable);
                }
            }
            this.mTintManager = obtainStyledAttributes.getTintManager();
            obtainStyledAttributes.recycle();
        }
    }
    
    private void applySupportBackgroundTint() {
        if (this.getBackground() != null) {
            if (this.mBackgroundTint != null) {
                TintManager.tintViewBackground((View)this, this.mBackgroundTint);
            }
            else if (this.mInternalBackgroundTint != null) {
                TintManager.tintViewBackground((View)this, this.mInternalBackgroundTint);
            }
        }
    }
    
    private void setInternalBackgroundTint(final ColorStateList mTintList) {
        if (mTintList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            this.mInternalBackgroundTint.mTintList = mTintList;
            this.mInternalBackgroundTint.mHasTintList = true;
        }
        else {
            this.mInternalBackgroundTint = null;
        }
        this.applySupportBackgroundTint();
    }
    
    @TargetApi(11)
    private static void setPopupBackgroundDrawableV11(final Spinner spinner, final Drawable backgroundDrawable) {
        try {
            final Field declaredField = Spinner.class.getDeclaredField("mPopup");
            declaredField.setAccessible(true);
            final Object value = declaredField.get(spinner);
            if (value instanceof ListPopupWindow) {
                ((ListPopupWindow)value).setBackgroundDrawable(backgroundDrawable);
            }
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex2) {
            ex2.printStackTrace();
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.applySupportBackgroundTint();
    }
    
    @Nullable
    public ColorStateList getSupportBackgroundTintList() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintList;
        }
        return null;
    }
    
    @Nullable
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintMode;
        }
        return null;
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        this.setInternalBackgroundTint(null);
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        ColorStateList tintList;
        if (this.mTintManager != null) {
            tintList = this.mTintManager.getTintList(backgroundResource);
        }
        else {
            tintList = null;
        }
        this.setInternalBackgroundTint(tintList);
    }
    
    public void setSupportBackgroundTintList(@Nullable final ColorStateList mTintList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = mTintList;
        this.mBackgroundTint.mHasTintList = true;
        this.applySupportBackgroundTint();
    }
    
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode mTintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = mTintMode;
        this.mBackgroundTint.mHasTintMode = true;
        this.applySupportBackgroundTint();
    }
}
