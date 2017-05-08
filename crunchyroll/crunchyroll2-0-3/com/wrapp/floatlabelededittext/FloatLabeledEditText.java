// 
// Decompiled by Procyon v0.5.30
// 

package com.wrapp.floatlabelededittext;

import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.view.View;
import com.nineoldandroids.view.animation.AnimatorProxy;
import android.util.TypedValue;
import com.crunchyroll.crunchyroid.R;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Context;
import android.widget.FrameLayout;

public class FloatLabeledEditText extends FrameLayout
{
    private Context mContext;
    private EditText mEditText;
    private TextView mHintTextView;
    
    public FloatLabeledEditText(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
    
    public FloatLabeledEditText(final Context mContext, final AttributeSet attributes) {
        super(mContext, attributes);
        this.mContext = mContext;
        this.setAttributes(attributes);
    }
    
    public FloatLabeledEditText(final Context mContext, final AttributeSet attributes, final int n) {
        super(mContext, attributes, n);
        this.mContext = mContext;
        this.setAttributes(attributes);
    }
    
    private void setAttributes(final AttributeSet set) {
        this.mHintTextView = new TextView(this.mContext);
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, R.styleable.FloatLabeledEditText);
        final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        final int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(2, (int)TypedValue.applyDimension(1, 2.0f, this.getResources().getDisplayMetrics()));
        final int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        final int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        final int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(5, 0);
        final Drawable drawable = obtainStyledAttributes.getDrawable(6);
        if (dimensionPixelSize != 0) {
            this.mHintTextView.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        }
        else {
            this.mHintTextView.setPadding(dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize5);
        }
        if (drawable != null) {
            this.setHintBackground(drawable);
        }
        this.mHintTextView.setTextAppearance(this.mContext, obtainStyledAttributes.getResourceId(0, 16973894));
        this.mHintTextView.setVisibility(4);
        AnimatorProxy.wrap((View)this.mHintTextView).setAlpha(0.0f);
        this.addView((View)this.mHintTextView, -2, -2);
        obtainStyledAttributes.recycle();
    }
    
    private void setEditText(final EditText mEditText) {
        (this.mEditText = mEditText).addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                FloatLabeledEditText.this.setShowHint(!TextUtils.isEmpty((CharSequence)editable));
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        if (!TextUtils.isEmpty((CharSequence)this.mEditText.getText())) {
            this.mHintTextView.setVisibility(0);
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void setHintBackground(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mHintTextView.setBackground(drawable);
            return;
        }
        this.mHintTextView.setBackgroundDrawable(drawable);
    }
    
    private void setShowHint(final boolean b) {
        final AnimatorSet set = null;
        AnimatorSet set2;
        if (this.mHintTextView.getVisibility() == 0 && !b) {
            set2 = new AnimatorSet();
            set2.playTogether(ObjectAnimator.ofFloat(this.mHintTextView, "translationY", 0.0f, this.mHintTextView.getHeight() / 8), ObjectAnimator.ofFloat(this.mHintTextView, "alpha", 1.0f, 0.0f));
        }
        else {
            set2 = set;
            if (this.mHintTextView.getVisibility() != 0) {
                set2 = set;
                if (b) {
                    this.mHintTextView.setText(this.mEditText.getHint());
                    set2 = new AnimatorSet();
                    set2.playTogether(ObjectAnimator.ofFloat(this.mHintTextView, "translationY", this.mHintTextView.getHeight() / 8, 0.0f), ObjectAnimator.ofFloat(this.mHintTextView, "alpha", 0.0f, 1.0f));
                }
            }
        }
        if (set2 != null) {
            set2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    final TextView access$100 = FloatLabeledEditText.this.mHintTextView;
                    int visibility;
                    if (b) {
                        visibility = 0;
                    }
                    else {
                        visibility = 4;
                    }
                    access$100.setVisibility(visibility);
                    final AnimatorProxy wrap = AnimatorProxy.wrap((View)FloatLabeledEditText.this.mHintTextView);
                    float alpha;
                    if (b) {
                        alpha = 1.0f;
                    }
                    else {
                        alpha = 0.0f;
                    }
                    wrap.setAlpha(alpha);
                }
                
                @Override
                public void onAnimationStart(final Animator animator) {
                    super.onAnimationStart(animator);
                    FloatLabeledEditText.this.mHintTextView.setVisibility(0);
                }
            });
            set2.start();
        }
    }
    
    public final void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        Object o = viewGroup$LayoutParams;
        if (view instanceof EditText) {
            if (this.mEditText != null) {
                throw new IllegalArgumentException("Can only have one Edittext subview");
            }
            o = new FrameLayout$LayoutParams(viewGroup$LayoutParams);
            ((FrameLayout$LayoutParams)o).gravity = 80;
            ((FrameLayout$LayoutParams)o).topMargin = (int)(this.mHintTextView.getTextSize() + this.mHintTextView.getPaddingBottom() + this.mHintTextView.getPaddingTop());
            this.setEditText((EditText)view);
        }
        super.addView(view, n, (ViewGroup$LayoutParams)o);
    }
    
    public void setHint(final String s) {
        this.mEditText.setHint((CharSequence)s);
        this.mHintTextView.setText((CharSequence)s);
    }
}
