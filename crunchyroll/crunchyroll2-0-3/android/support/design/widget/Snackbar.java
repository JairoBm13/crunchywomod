// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.View$MeasureSpec;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.MotionEvent;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.text.TextUtils;
import android.view.View$OnClickListener;
import android.support.annotation.StringRes;
import android.view.ViewGroup$LayoutParams;
import android.support.annotation.Nullable;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.support.design.R;
import android.view.LayoutInflater;
import android.os.Message;
import android.os.Handler$Callback;
import android.os.Looper;
import android.view.ViewGroup;
import android.content.Context;
import android.os.Handler;

public class Snackbar
{
    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static final int MSG_DISMISS = 1;
    private static final int MSG_SHOW = 0;
    private static final Handler sHandler;
    private final Context mContext;
    private int mDuration;
    private final SnackbarManager.Callback mManagerCallback;
    private final ViewGroup mParent;
    private final SnackbarLayout mView;
    
    static {
        sHandler = new Handler(Looper.getMainLooper(), (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 0: {
                        ((Snackbar)message.obj).showView();
                        return true;
                    }
                    case 1: {
                        ((Snackbar)message.obj).hideView();
                        return true;
                    }
                }
            }
        });
    }
    
    Snackbar(final ViewGroup mParent) {
        this.mManagerCallback = new SnackbarManager.Callback() {
            @Override
            public void dismiss() {
                Snackbar.sHandler.sendMessage(Snackbar.sHandler.obtainMessage(1, (Object)Snackbar.this));
            }
            
            @Override
            public void show() {
                Snackbar.sHandler.sendMessage(Snackbar.sHandler.obtainMessage(0, (Object)Snackbar.this));
            }
        };
        this.mParent = mParent;
        this.mContext = mParent.getContext();
        this.mView = (SnackbarLayout)LayoutInflater.from(this.mContext).inflate(R.layout.layout_snackbar, this.mParent, false);
    }
    
    private void animateViewIn() {
        if (Build$VERSION.SDK_INT >= 14) {
            ViewCompat.setTranslationY((View)this.mView, this.mView.getHeight());
            ViewCompat.animate((View)this.mView).translationY(0.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(250L).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final View view) {
                    SnackbarManager.getInstance().onShown(Snackbar.this.mManagerCallback);
                }
                
                @Override
                public void onAnimationStart(final View view) {
                    Snackbar.this.mView.animateChildrenIn(70, 180);
                }
            }).start();
            return;
        }
        final Animation loadAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.snackbar_in);
        loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        loadAnimation.setDuration(250L);
        loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                SnackbarManager.getInstance().onShown(Snackbar.this.mManagerCallback);
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
        this.mView.startAnimation(loadAnimation);
    }
    
    private void animateViewOut() {
        if (Build$VERSION.SDK_INT >= 14) {
            ViewCompat.animate((View)this.mView).translationY(this.mView.getHeight()).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(250L).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final View view) {
                    Snackbar.this.onViewHidden();
                }
                
                @Override
                public void onAnimationStart(final View view) {
                    Snackbar.this.mView.animateChildrenOut(0, 180);
                }
            }).start();
            return;
        }
        final Animation loadAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.snackbar_out);
        loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        loadAnimation.setDuration(250L);
        loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                Snackbar.this.onViewHidden();
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
        this.mView.startAnimation(loadAnimation);
    }
    
    @Nullable
    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        View view2 = view;
        while (!(view2 instanceof CoordinatorLayout)) {
            ViewGroup viewGroup2 = viewGroup;
            if (view2 instanceof FrameLayout) {
                if (view2.getId() == 16908290) {
                    return (ViewGroup)view2;
                }
                viewGroup2 = (ViewGroup)view2;
            }
            if ((view = view2) != null) {
                final ViewParent parent = view2.getParent();
                if (parent instanceof View) {
                    view = (View)parent;
                }
                else {
                    view = null;
                }
            }
            viewGroup = viewGroup2;
            if ((view2 = view) == null) {
                return viewGroup2;
            }
        }
        return (ViewGroup)view2;
    }
    
    private boolean isBeingDragged() {
        final boolean b = false;
        final ViewGroup$LayoutParams layoutParams = this.mView.getLayoutParams();
        boolean b2 = b;
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
            b2 = b;
            if (behavior instanceof SwipeDismissBehavior) {
                b2 = b;
                if (((SwipeDismissBehavior)behavior).getDragState() != 0) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public static Snackbar make(final View view, final int n, final int n2) {
        return make(view, view.getResources().getText(n), n2);
    }
    
    public static Snackbar make(final View view, final CharSequence text, final int duration) {
        final Snackbar snackbar = new Snackbar(findSuitableParent(view));
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }
    
    private void onViewHidden() {
        this.mParent.removeView((View)this.mView);
        SnackbarManager.getInstance().onDismissed(this.mManagerCallback);
    }
    
    public void dismiss() {
        SnackbarManager.getInstance().dismiss(this.mManagerCallback);
    }
    
    public int getDuration() {
        return this.mDuration;
    }
    
    public View getView() {
        return (View)this.mView;
    }
    
    final void hideView() {
        if (this.mView.getVisibility() != 0 || this.isBeingDragged()) {
            this.onViewHidden();
            return;
        }
        this.animateViewOut();
    }
    
    public Snackbar setAction(@StringRes final int n, final View$OnClickListener view$OnClickListener) {
        return this.setAction(this.mContext.getText(n), view$OnClickListener);
    }
    
    public Snackbar setAction(final CharSequence text, final View$OnClickListener view$OnClickListener) {
        final TextView actionView = this.mView.getActionView();
        if (TextUtils.isEmpty(text) || view$OnClickListener == null) {
            actionView.setVisibility(8);
            actionView.setOnClickListener((View$OnClickListener)null);
            return this;
        }
        actionView.setVisibility(0);
        actionView.setText(text);
        actionView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                view$OnClickListener.onClick(view);
                Snackbar.this.dismiss();
            }
        });
        return this;
    }
    
    public Snackbar setActionTextColor(final int textColor) {
        this.mView.getActionView().setTextColor(textColor);
        return this;
    }
    
    public Snackbar setActionTextColor(final ColorStateList textColor) {
        this.mView.getActionView().setTextColor(textColor);
        return this;
    }
    
    public Snackbar setDuration(final int mDuration) {
        this.mDuration = mDuration;
        return this;
    }
    
    public Snackbar setText(@StringRes final int n) {
        return this.setText(this.mContext.getText(n));
    }
    
    public Snackbar setText(final CharSequence text) {
        this.mView.getMessageView().setText(text);
        return this;
    }
    
    public void show() {
        SnackbarManager.getInstance().show(this.mDuration, this.mManagerCallback);
    }
    
    final void showView() {
        if (this.mView.getParent() == null) {
            final ViewGroup$LayoutParams layoutParams = this.mView.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(0);
                behavior.setListener((SwipeDismissBehavior.OnDismissListener)new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(final View view) {
                        Snackbar.this.dismiss();
                    }
                    
                    @Override
                    public void onDragStateChanged(final int n) {
                        switch (n) {
                            default: {}
                            case 1:
                            case 2: {
                                SnackbarManager.getInstance().cancelTimeout(Snackbar.this.mManagerCallback);
                            }
                            case 0: {
                                SnackbarManager.getInstance().restoreTimeout(Snackbar.this.mManagerCallback);
                            }
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams)layoutParams).setBehavior(behavior);
            }
            this.mParent.addView((View)this.mView);
        }
        if (ViewCompat.isLaidOut((View)this.mView)) {
            this.animateViewIn();
            return;
        }
        this.mView.setOnLayoutChangeListener((OnLayoutChangeListener)new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4) {
                Snackbar.this.animateViewIn();
                Snackbar.this.mView.setOnLayoutChangeListener(null);
            }
        });
    }
    
    final class Behavior extends SwipeDismissBehavior<SnackbarLayout>
    {
        @Override
        public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final SnackbarLayout snackbarLayout, final MotionEvent motionEvent) {
            if (coordinatorLayout.isPointInChildBounds((View)snackbarLayout, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                switch (motionEvent.getActionMasked()) {
                    case 0: {
                        SnackbarManager.getInstance().cancelTimeout(Snackbar.this.mManagerCallback);
                        break;
                    }
                    case 1:
                    case 3: {
                        SnackbarManager.getInstance().restoreTimeout(Snackbar.this.mManagerCallback);
                        break;
                    }
                }
            }
            return super.onInterceptTouchEvent(coordinatorLayout, snackbarLayout, motionEvent);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }
    
    public static class SnackbarLayout extends LinearLayout
    {
        private TextView mActionView;
        private int mMaxInlineActionWidth;
        private int mMaxWidth;
        private TextView mMessageView;
        private OnLayoutChangeListener mOnLayoutChangeListener;
        
        public SnackbarLayout(final Context context) {
            this(context, null);
        }
        
        public SnackbarLayout(final Context context, final AttributeSet set) {
            super(context, set);
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SnackbarLayout);
            this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            this.mMaxInlineActionWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (obtainStyledAttributes.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            obtainStyledAttributes.recycle();
            this.setClickable(true);
            LayoutInflater.from(context).inflate(R.layout.layout_snackbar_include, (ViewGroup)this);
        }
        
        private static void updateTopBottomPadding(final View view, final int n, final int n2) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), n, ViewCompat.getPaddingEnd(view), n2);
                return;
            }
            view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), n2);
        }
        
        private boolean updateViewsWithinLayout(final int orientation, final int n, final int n2) {
            boolean b = false;
            if (orientation != this.getOrientation()) {
                this.setOrientation(orientation);
                b = true;
            }
            if (this.mMessageView.getPaddingTop() != n || this.mMessageView.getPaddingBottom() != n2) {
                updateTopBottomPadding((View)this.mMessageView, n, n2);
                b = true;
            }
            return b;
        }
        
        void animateChildrenIn(final int n, final int n2) {
            ViewCompat.setAlpha((View)this.mMessageView, 0.0f);
            ViewCompat.animate((View)this.mMessageView).alpha(1.0f).setDuration(n2).setStartDelay(n).start();
            if (this.mActionView.getVisibility() == 0) {
                ViewCompat.setAlpha((View)this.mActionView, 0.0f);
                ViewCompat.animate((View)this.mActionView).alpha(1.0f).setDuration(n2).setStartDelay(n).start();
            }
        }
        
        void animateChildrenOut(final int n, final int n2) {
            ViewCompat.setAlpha((View)this.mMessageView, 1.0f);
            ViewCompat.animate((View)this.mMessageView).alpha(0.0f).setDuration(n2).setStartDelay(n).start();
            if (this.mActionView.getVisibility() == 0) {
                ViewCompat.setAlpha((View)this.mActionView, 1.0f);
                ViewCompat.animate((View)this.mActionView).alpha(0.0f).setDuration(n2).setStartDelay(n).start();
            }
        }
        
        TextView getActionView() {
            return this.mActionView;
        }
        
        TextView getMessageView() {
            return this.mMessageView;
        }
        
        protected void onFinishInflate() {
            super.onFinishInflate();
            this.mMessageView = (TextView)this.findViewById(R.id.snackbar_text);
            this.mActionView = (TextView)this.findViewById(R.id.snackbar_action);
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            if (b && this.mOnLayoutChangeListener != null) {
                this.mOnLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
            }
        }
        
        protected void onMeasure(int n, final int n2) {
            super.onMeasure(n, n2);
            int measureSpec = n;
            if (this.mMaxWidth > 0) {
                measureSpec = n;
                if (this.getMeasuredWidth() > this.mMaxWidth) {
                    measureSpec = View$MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
                    super.onMeasure(measureSpec, n2);
                }
            }
            int dimensionPixelSize = this.getResources().getDimensionPixelSize(R.dimen.snackbar_padding_vertical_2lines);
            final int dimensionPixelSize2 = this.getResources().getDimensionPixelSize(R.dimen.snackbar_padding_vertical);
            if (this.mMessageView.getLayout().getLineCount() > 1) {
                n = 1;
            }
            else {
                n = 0;
            }
            final int n3 = 0;
            if (n != 0 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
                n = n3;
                if (this.updateViewsWithinLayout(1, dimensionPixelSize, dimensionPixelSize - dimensionPixelSize2)) {
                    n = 1;
                }
            }
            else {
                if (n == 0) {
                    dimensionPixelSize = dimensionPixelSize2;
                }
                n = n3;
                if (this.updateViewsWithinLayout(0, dimensionPixelSize, dimensionPixelSize)) {
                    n = 1;
                }
            }
            if (n != 0) {
                super.onMeasure(measureSpec, n2);
            }
        }
        
        void setOnLayoutChangeListener(final OnLayoutChangeListener mOnLayoutChangeListener) {
            this.mOnLayoutChangeListener = mOnLayoutChangeListener;
        }
        
        interface OnLayoutChangeListener
        {
            void onLayoutChange(final View p0, final int p1, final int p2, final int p3, final int p4);
        }
    }
}
