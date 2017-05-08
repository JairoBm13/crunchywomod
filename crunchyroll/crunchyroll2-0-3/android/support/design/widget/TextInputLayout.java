// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View$OnFocusChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.design.R;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateInterpolator;
import android.os.Message;
import android.os.Handler$Callback;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TextInputLayout extends LinearLayout
{
    private static final int ANIMATION_DURATION = 200;
    private static final int MSG_UPDATE_LABEL = 0;
    private ValueAnimatorCompat mAnimator;
    private final CollapsingTextHelper mCollapsingTextHelper;
    private EditText mEditText;
    private boolean mErrorEnabled;
    private int mErrorTextAppearance;
    private TextView mErrorView;
    private final Handler mHandler;
    private CharSequence mHint;
    private ColorStateList mLabelTextColor;
    
    public TextInputLayout(final Context context) {
        this(context, null);
    }
    
    public TextInputLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.mCollapsingTextHelper = new CollapsingTextHelper((View)this);
        this.mHandler = new Handler((Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 0: {
                        TextInputLayout.this.updateLabelVisibility(true);
                        return true;
                    }
                }
            }
        });
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mCollapsingTextHelper.setPositionInterpolator((Interpolator)new AccelerateInterpolator());
        this.mCollapsingTextHelper.setCollapsedTextVerticalGravity(48);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.TextInputLayout, 0, R.style.Widget_Design_TextInputLayout);
        this.mHint = obtainStyledAttributes.getText(R.styleable.TextInputLayout_android_hint);
        final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1);
        if (resourceId != -1) {
            this.mCollapsingTextHelper.setCollapsedTextAppearance(resourceId);
        }
        this.mErrorTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        this.mLabelTextColor = this.createLabelTextColorStateList(this.mCollapsingTextHelper.getCollapsedTextColor());
        this.mCollapsingTextHelper.setCollapsedTextColor(this.mLabelTextColor.getDefaultColor());
        this.mCollapsingTextHelper.setExpandedTextColor(this.mLabelTextColor.getDefaultColor());
        obtainStyledAttributes.recycle();
        if (boolean1) {
            this.setErrorEnabled(true);
        }
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setAccessibilityDelegate((View)this, new TextInputAccessibilityDelegate());
    }
    
    private void animateToExpansionFraction(final float n) {
        if (this.mAnimator == null) {
            (this.mAnimator = ViewUtils.createAnimator()).setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200);
            this.mAnimator.setUpdateListener((ValueAnimatorCompat.AnimatorUpdateListener)new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimatorCompat valueAnimatorCompat) {
                    TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction(valueAnimatorCompat.getAnimatedFloatValue());
                }
            });
        }
        else if (this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        this.mAnimator.setFloatValues(this.mCollapsingTextHelper.getExpansionFraction(), n);
        this.mAnimator.start();
    }
    
    private void collapseHint(final boolean b) {
        if (b) {
            this.animateToExpansionFraction(1.0f);
            return;
        }
        this.mCollapsingTextHelper.setExpansionFraction(1.0f);
    }
    
    private ColorStateList createLabelTextColorStateList(int n) {
        final int[][] array = new int[2][];
        final int[] array2 = new int[2];
        array[0] = TextInputLayout.FOCUSED_STATE_SET;
        array2[0] = n;
        n = 0 + 1;
        array[n] = TextInputLayout.EMPTY_STATE_SET;
        array2[n] = this.getThemeAttrColor(16842906);
        return new ColorStateList(array, array2);
    }
    
    private void expandHint(final boolean b) {
        if (b) {
            this.animateToExpansionFraction(0.0f);
            return;
        }
        this.mCollapsingTextHelper.setExpansionFraction(0.0f);
    }
    
    private int getThemeAttrColor(final int n) {
        final TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(n, typedValue, true)) {
            return typedValue.data;
        }
        return -65281;
    }
    
    private LinearLayout$LayoutParams setEditText(final EditText mEditText, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.mEditText != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }
        this.mEditText = mEditText;
        this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
        this.mEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                TextInputLayout.this.mHandler.sendEmptyMessage(0);
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.mEditText.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean b) {
                TextInputLayout.this.mHandler.sendEmptyMessage(0);
            }
        });
        if (TextUtils.isEmpty(this.mHint)) {
            this.setHint(this.mEditText.getHint());
            this.mEditText.setHint((CharSequence)null);
        }
        if (this.mErrorView != null) {
            ViewCompat.setPaddingRelative((View)this.mErrorView, ViewCompat.getPaddingStart((View)this.mEditText), 0, ViewCompat.getPaddingEnd((View)this.mEditText), this.mEditText.getPaddingBottom());
        }
        this.updateLabelVisibility(false);
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(viewGroup$LayoutParams);
        final Paint paint = new Paint();
        paint.setTextSize(this.mCollapsingTextHelper.getExpandedTextSize());
        linearLayout$LayoutParams.topMargin = (int)(-paint.ascent());
        return linearLayout$LayoutParams;
    }
    
    private void updateLabelVisibility(final boolean b) {
        boolean b2;
        if (!TextUtils.isEmpty((CharSequence)this.mEditText.getText())) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final boolean focused = this.mEditText.isFocused();
        final CollapsingTextHelper mCollapsingTextHelper = this.mCollapsingTextHelper;
        final ColorStateList mLabelTextColor = this.mLabelTextColor;
        int[] array;
        if (focused) {
            array = TextInputLayout.FOCUSED_STATE_SET;
        }
        else {
            array = TextInputLayout.EMPTY_STATE_SET;
        }
        mCollapsingTextHelper.setCollapsedTextColor(mLabelTextColor.getColorForState(array, this.mLabelTextColor.getDefaultColor()));
        if (b2 || focused) {
            this.collapseHint(b);
            return;
        }
        this.expandHint(b);
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (view instanceof EditText) {
            super.addView(view, 0, (ViewGroup$LayoutParams)this.setEditText((EditText)view, viewGroup$LayoutParams));
            return;
        }
        super.addView(view, n, viewGroup$LayoutParams);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        this.mCollapsingTextHelper.draw(canvas);
    }
    
    public EditText getEditText() {
        return this.mEditText;
    }
    
    protected void onLayout(final boolean b, int n, final int n2, int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.mCollapsingTextHelper.onLayout(b, n, n2, n3, n4);
        if (this.mEditText != null) {
            n = this.mEditText.getLeft() + this.mEditText.getPaddingLeft();
            n3 = this.mEditText.getRight() - this.mEditText.getPaddingRight();
            this.mCollapsingTextHelper.setExpandedBounds(n, this.mEditText.getTop() + this.mEditText.getPaddingTop(), n3, this.mEditText.getBottom() - this.mEditText.getPaddingBottom());
            this.mCollapsingTextHelper.setCollapsedBounds(n, this.getPaddingTop(), n3, n4 - n2 - this.getPaddingBottom());
        }
    }
    
    public void setError(final CharSequence text) {
        if (!this.mErrorEnabled) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        if (!TextUtils.isEmpty(text)) {
            this.mErrorView.setText(text);
            this.mErrorView.setVisibility(0);
            ViewCompat.setAlpha((View)this.mErrorView, 0.0f);
            ViewCompat.animate((View)this.mErrorView).alpha(1.0f).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null).start();
        }
        else if (this.mErrorView.getVisibility() == 0) {
            ViewCompat.animate((View)this.mErrorView).alpha(0.0f).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final View view) {
                    TextInputLayout.this.mErrorView.setText((CharSequence)null);
                    TextInputLayout.this.mErrorView.setVisibility(4);
                }
            }).start();
        }
        this.sendAccessibilityEvent(2048);
    }
    
    public void setErrorEnabled(final boolean mErrorEnabled) {
        if (this.mErrorEnabled != mErrorEnabled) {
            if (mErrorEnabled) {
                (this.mErrorView = new TextView(this.getContext())).setTextAppearance(this.getContext(), this.mErrorTextAppearance);
                this.mErrorView.setVisibility(4);
                this.addView((View)this.mErrorView);
                if (this.mEditText != null) {
                    ViewCompat.setPaddingRelative((View)this.mErrorView, ViewCompat.getPaddingStart((View)this.mEditText), 0, ViewCompat.getPaddingEnd((View)this.mEditText), this.mEditText.getPaddingBottom());
                }
            }
            else {
                this.removeView((View)this.mErrorView);
                this.mErrorView = null;
            }
            this.mErrorEnabled = mErrorEnabled;
        }
    }
    
    public void setHint(final CharSequence charSequence) {
        this.mHint = charSequence;
        this.mCollapsingTextHelper.setText(charSequence);
        this.sendAccessibilityEvent(2048);
    }
    
    private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)TextInputLayout.class.getSimpleName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(TextInputLayout.class.getSimpleName());
            final CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityNodeInfoCompat.setText(text);
            }
            if (TextInputLayout.this.mEditText != null) {
                accessibilityNodeInfoCompat.setLabelFor((View)TextInputLayout.this.mEditText);
            }
            CharSequence text2;
            if (TextInputLayout.this.mErrorView != null) {
                text2 = TextInputLayout.this.mErrorView.getText();
            }
            else {
                text2 = null;
            }
            if (!TextUtils.isEmpty(text2)) {
                accessibilityNodeInfoCompat.setContentInvalid(true);
                accessibilityNodeInfoCompat.setError(text2);
            }
        }
        
        @Override
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            final CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityEvent.getText().add(text);
            }
        }
    }
}
