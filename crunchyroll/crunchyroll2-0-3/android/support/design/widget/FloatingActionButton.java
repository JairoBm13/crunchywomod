// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.ViewGroup;
import java.util.List;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.Animation;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewCompat;
import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.view.View$MeasureSpec;
import android.content.res.TypedArray;
import android.view.View;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.widget.ImageView;

@CoordinatorLayout.DefaultBehavior(Behavior.class)
public class FloatingActionButton extends ImageView
{
    private static final int SIZE_MINI = 1;
    private static final int SIZE_NORMAL = 0;
    private ColorStateList mBackgroundTint;
    private PorterDuff$Mode mBackgroundTintMode;
    private int mBorderWidth;
    private int mContentPadding;
    private final FloatingActionButtonImpl mImpl;
    private int mRippleColor;
    private final Rect mShadowPadding;
    private int mSize;
    
    public FloatingActionButton(final Context context) {
        this(context, null);
    }
    
    public FloatingActionButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public FloatingActionButton(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mShadowPadding = new Rect();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.FloatingActionButton, n, R.style.Widget_Design_FloatingActionButton);
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.FloatingActionButton_android_background);
        this.mBackgroundTint = obtainStyledAttributes.getColorStateList(R.styleable.FloatingActionButton_backgroundTint);
        this.mBackgroundTintMode = parseTintMode(obtainStyledAttributes.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.mRippleColor = obtainStyledAttributes.getColor(R.styleable.FloatingActionButton_rippleColor, 0);
        this.mSize = obtainStyledAttributes.getInt(R.styleable.FloatingActionButton_fabSize, 0);
        this.mBorderWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
        final float dimension = obtainStyledAttributes.getDimension(R.styleable.FloatingActionButton_elevation, 0.0f);
        final float dimension2 = obtainStyledAttributes.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        obtainStyledAttributes.recycle();
        final ShadowViewDelegate shadowViewDelegate = new ShadowViewDelegate() {
            @Override
            public float getRadius() {
                return FloatingActionButton.this.getSizeDimension() / 2.0f;
            }
            
            @Override
            public void setBackgroundDrawable(final Drawable drawable) {
                FloatingActionButton.access$201(FloatingActionButton.this, drawable);
            }
            
            @Override
            public void setShadowPadding(final int n, final int n2, final int n3, final int n4) {
                FloatingActionButton.this.mShadowPadding.set(n, n2, n3, n4);
                FloatingActionButton.this.setPadding(FloatingActionButton.this.mContentPadding + n, FloatingActionButton.this.mContentPadding + n2, FloatingActionButton.this.mContentPadding + n3, FloatingActionButton.this.mContentPadding + n4);
            }
        };
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = new FloatingActionButtonLollipop((View)this, shadowViewDelegate);
        }
        else {
            this.mImpl = new FloatingActionButtonEclairMr1((View)this, shadowViewDelegate);
        }
        n = (int)this.getResources().getDimension(R.dimen.fab_content_size);
        this.mContentPadding = (this.getSizeDimension() - n) / 2;
        this.mImpl.setBackgroundDrawable(drawable, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        this.mImpl.setElevation(dimension);
        this.mImpl.setPressedTranslationZ(dimension2);
        this.setClickable(true);
    }
    
    static /* synthetic */ void access$201(final FloatingActionButton floatingActionButton, final Drawable backgroundDrawable) {
        ((View)floatingActionButton).setBackgroundDrawable(backgroundDrawable);
    }
    
    static PorterDuff$Mode parseTintMode(final int n, final PorterDuff$Mode porterDuff$Mode) {
        switch (n) {
            default: {
                return porterDuff$Mode;
            }
            case 3: {
                return PorterDuff$Mode.SRC_OVER;
            }
            case 5: {
                return PorterDuff$Mode.SRC_IN;
            }
            case 9: {
                return PorterDuff$Mode.SRC_ATOP;
            }
            case 14: {
                return PorterDuff$Mode.MULTIPLY;
            }
            case 15: {
                return PorterDuff$Mode.SCREEN;
            }
        }
    }
    
    private static int resolveAdjustedSize(final int n, int size) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        switch (mode) {
            default: {
                return n;
            }
            case 0: {
                return n;
            }
            case Integer.MIN_VALUE: {
                return Math.min(n, size);
            }
            case 1073741824: {
                return size;
            }
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mImpl.onDrawableStateChanged(this.getDrawableState());
    }
    
    @Nullable
    public ColorStateList getBackgroundTintList() {
        return this.mBackgroundTint;
    }
    
    @Nullable
    public PorterDuff$Mode getBackgroundTintMode() {
        return this.mBackgroundTintMode;
    }
    
    final int getSizeDimension() {
        switch (this.mSize) {
            default: {
                return this.getResources().getDimensionPixelSize(R.dimen.fab_size_normal);
            }
            case 1: {
                return this.getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
            }
        }
    }
    
    @TargetApi(11)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mImpl.jumpDrawableToCurrentState();
    }
    
    protected void onMeasure(int min, final int n) {
        final int sizeDimension = this.getSizeDimension();
        min = Math.min(resolveAdjustedSize(sizeDimension, min), resolveAdjustedSize(sizeDimension, n));
        this.setMeasuredDimension(this.mShadowPadding.left + min + this.mShadowPadding.right, this.mShadowPadding.top + min + this.mShadowPadding.bottom);
    }
    
    public void setBackgroundDrawable(final Drawable drawable) {
        if (this.mImpl != null) {
            this.mImpl.setBackgroundDrawable(drawable, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        }
    }
    
    public void setBackgroundTintList(@Nullable final ColorStateList backgroundTintList) {
        this.mImpl.setBackgroundTintList(backgroundTintList);
    }
    
    public void setBackgroundTintMode(@Nullable final PorterDuff$Mode backgroundTintMode) {
        this.mImpl.setBackgroundTintMode(backgroundTintMode);
    }
    
    public void setRippleColor(final int n) {
        if (this.mRippleColor != n) {
            this.mRippleColor = n;
            this.mImpl.setRippleColor(n);
        }
    }
    
    public static class Behavior extends CoordinatorLayout.Behavior<FloatingActionButton>
    {
        private static final boolean SNACKBAR_BEHAVIOR_ENABLED;
        private boolean mIsAnimatingOut;
        private Rect mTmpRect;
        private float mTranslationY;
        
        static {
            SNACKBAR_BEHAVIOR_ENABLED = (Build$VERSION.SDK_INT >= 11);
        }
        
        private void animateIn(final FloatingActionButton floatingActionButton) {
            floatingActionButton.setVisibility(0);
            if (Build$VERSION.SDK_INT >= 14) {
                ViewCompat.animate((View)floatingActionButton).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
                return;
            }
            final Animation loadAnimation = android.view.animation.AnimationUtils.loadAnimation(floatingActionButton.getContext(), R.anim.fab_in);
            loadAnimation.setDuration(200L);
            loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            floatingActionButton.startAnimation(loadAnimation);
        }
        
        private void animateOut(final FloatingActionButton floatingActionButton) {
            if (Build$VERSION.SDK_INT >= 14) {
                ViewCompat.animate((View)floatingActionButton).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationCancel(final View view) {
                        Behavior.this.mIsAnimatingOut = false;
                    }
                    
                    @Override
                    public void onAnimationEnd(final View view) {
                        Behavior.this.mIsAnimatingOut = false;
                        view.setVisibility(8);
                    }
                    
                    @Override
                    public void onAnimationStart(final View view) {
                        Behavior.this.mIsAnimatingOut = true;
                    }
                }).start();
                return;
            }
            final Animation loadAnimation = android.view.animation.AnimationUtils.loadAnimation(floatingActionButton.getContext(), R.anim.fab_out);
            loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            loadAnimation.setDuration(200L);
            loadAnimation.setAnimationListener((Animation$AnimationListener)new AnimationUtils.AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animation animation) {
                    Behavior.this.mIsAnimatingOut = false;
                    floatingActionButton.setVisibility(8);
                }
                
                @Override
                public void onAnimationStart(final Animation animation) {
                    Behavior.this.mIsAnimatingOut = true;
                }
            });
            floatingActionButton.startAnimation(loadAnimation);
        }
        
        private float getFabTranslationYForSnackbar(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton) {
            float n = 0.0f;
            final List<View> dependencies = coordinatorLayout.getDependencies((View)floatingActionButton);
            float min;
            for (int i = 0; i < dependencies.size(); ++i, n = min) {
                final View view = dependencies.get(i);
                min = n;
                if (view instanceof Snackbar.SnackbarLayout) {
                    min = n;
                    if (coordinatorLayout.doViewsOverlap((View)floatingActionButton, view)) {
                        min = Math.min(n, ViewCompat.getTranslationY(view) - view.getHeight());
                    }
                }
            }
            return n;
        }
        
        private void updateFabTranslationForSnackbar(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final View view) {
            final float fabTranslationYForSnackbar = this.getFabTranslationYForSnackbar(coordinatorLayout, floatingActionButton);
            if (fabTranslationYForSnackbar != this.mTranslationY) {
                ViewCompat.animate((View)floatingActionButton).cancel();
                if (Math.abs(fabTranslationYForSnackbar - this.mTranslationY) == view.getHeight()) {
                    ViewCompat.animate((View)floatingActionButton).translationY(fabTranslationYForSnackbar).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null);
                }
                else {
                    ViewCompat.setTranslationY((View)floatingActionButton, fabTranslationYForSnackbar);
                }
                this.mTranslationY = fabTranslationYForSnackbar;
            }
        }
        
        public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final View view) {
            return Behavior.SNACKBAR_BEHAVIOR_ENABLED && view instanceof Snackbar.SnackbarLayout;
        }
        
        public boolean onDependentViewChanged(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final View view) {
            if (view instanceof Snackbar.SnackbarLayout) {
                this.updateFabTranslationForSnackbar(coordinatorLayout, floatingActionButton, view);
            }
            else if (view instanceof AppBarLayout) {
                final AppBarLayout appBarLayout = (AppBarLayout)view;
                if (this.mTmpRect == null) {
                    this.mTmpRect = new Rect();
                }
                final Rect mTmpRect = this.mTmpRect;
                ViewGroupUtils.getDescendantRect(coordinatorLayout, view, mTmpRect);
                if (mTmpRect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                    if (!this.mIsAnimatingOut && floatingActionButton.getVisibility() == 0) {
                        this.animateOut(floatingActionButton);
                    }
                }
                else if (floatingActionButton.getVisibility() != 0) {
                    this.animateIn(floatingActionButton);
                }
            }
            return false;
        }
    }
}
