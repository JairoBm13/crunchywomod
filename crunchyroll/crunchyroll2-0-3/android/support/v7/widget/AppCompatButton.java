// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.text.method.TransformationMethod;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.view.View;
import android.content.res.TypedArray;
import android.content.res.ColorStateList;
import android.support.v7.internal.widget.ThemeUtils;
import android.os.Build$VERSION;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintInfo;
import android.support.v4.view.TintableBackgroundView;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView
{
    private static final int[] TINT_ATTRS;
    private TintInfo mBackgroundTint;
    private TintInfo mInternalBackgroundTint;
    private TintManager mTintManager;
    
    static {
        TINT_ATTRS = new int[] { 16842964 };
    }
    
    public AppCompatButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.buttonStyle);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        if (TintManager.SHOULD_BE_USED) {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, AppCompatButton.TINT_ATTRS, n, 0);
            if (obtainStyledAttributes.hasValue(0)) {
                final ColorStateList tintList = obtainStyledAttributes.getTintManager().getTintList(obtainStyledAttributes.getResourceId(0, -1));
                if (tintList != null) {
                    this.setInternalBackgroundTint(tintList);
                }
            }
            this.mTintManager = obtainStyledAttributes.getTintManager();
            obtainStyledAttributes.recycle();
        }
        final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, R.styleable.AppCompatTextView, n, 0);
        final int resourceId = obtainStyledAttributes2.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, -1);
        obtainStyledAttributes2.recycle();
        if (resourceId != -1) {
            final TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(resourceId, R.styleable.TextAppearance);
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                this.setAllCaps(obtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
            }
            obtainStyledAttributes3.recycle();
        }
        final TypedArray obtainStyledAttributes4 = context.obtainStyledAttributes(set, R.styleable.AppCompatTextView, n, 0);
        if (obtainStyledAttributes4.hasValue(R.styleable.AppCompatTextView_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes4.getBoolean(R.styleable.AppCompatTextView_textAllCaps, false));
        }
        obtainStyledAttributes4.recycle();
        final ColorStateList textColors = this.getTextColors();
        if (textColors != null && !textColors.isStateful()) {
            if (Build$VERSION.SDK_INT < 21) {
                n = ThemeUtils.getDisabledThemeAttrColor(context, 16842808);
            }
            else {
                n = ThemeUtils.getThemeAttrColor(context, 16842808);
            }
            this.setTextColor(ThemeUtils.createDisabledStateList(textColors.getDefaultColor(), n));
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
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)Button.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    public void setAllCaps(final boolean b) {
        Object transformationMethod;
        if (b) {
            transformationMethod = new AllCapsTransformationMethod(this.getContext());
        }
        else {
            transformationMethod = null;
        }
        this.setTransformationMethod((TransformationMethod)transformationMethod);
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
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        obtainStyledAttributes.recycle();
    }
}
