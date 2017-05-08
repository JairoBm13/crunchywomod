// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v7.internal.view.SupportMenuInflater;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.view.ViewGroup;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.internal.NavigationMenuPresenter;
import android.view.MenuInflater;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.design.internal.ScrimInsetsFrameLayout;

public class NavigationView extends ScrimInsetsFrameLayout
{
    private static final int[] CHECKED_STATE_SET;
    private static final int[] DISABLED_STATE_SET;
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    private OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;
    private final MenuBuilder mMenu;
    private MenuInflater mMenuInflater;
    private final NavigationMenuPresenter mPresenter;
    
    static {
        CHECKED_STATE_SET = new int[] { 16842912 };
        DISABLED_STATE_SET = new int[] { -16842910 };
    }
    
    public NavigationView(final Context context) {
        this(context, null);
    }
    
    public NavigationView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NavigationView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mMenu = new MenuBuilder(context);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.NavigationView, n, R.style.Widget_Design_NavigationView);
        this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(R.styleable.NavigationView_android_background));
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation((View)this, obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows((View)this, obtainStyledAttributes.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList itemIconTintList;
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_itemIconTint)) {
            itemIconTintList = obtainStyledAttributes.getColorStateList(R.styleable.NavigationView_itemIconTint);
        }
        else {
            itemIconTintList = this.createDefaultColorStateList(16842808);
        }
        ColorStateList itemTextColor;
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_itemTextColor)) {
            itemTextColor = obtainStyledAttributes.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        else {
            itemTextColor = this.createDefaultColorStateList(16842806);
        }
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.NavigationView_itemBackground);
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_menu)) {
            this.inflateMenu(obtainStyledAttributes.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        this.mMenu.setCallback((MenuBuilder.Callback)new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
                return NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(menuItem);
            }
            
            @Override
            public void onMenuModeChange(final MenuBuilder menuBuilder) {
            }
        });
        (this.mPresenter = new NavigationMenuPresenter()).setId(1);
        this.mPresenter.initForMenu(context, this.mMenu);
        this.mPresenter.setItemIconTintList(itemIconTintList);
        this.mPresenter.setItemTextColor(itemTextColor);
        this.mPresenter.setItemBackground(drawable);
        this.mMenu.addMenuPresenter(this.mPresenter);
        this.addView((View)this.mPresenter.getMenuView((ViewGroup)this));
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_headerLayout)) {
            this.inflateHeaderView(obtainStyledAttributes.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        obtainStyledAttributes.recycle();
    }
    
    private ColorStateList createDefaultColorStateList(int data) {
        final TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(data, typedValue, true)) {
            final ColorStateList colorStateList = this.getResources().getColorStateList(typedValue.resourceId);
            if (this.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true)) {
                data = typedValue.data;
                final int defaultColor = colorStateList.getDefaultColor();
                return new ColorStateList(new int[][] { NavigationView.DISABLED_STATE_SET, NavigationView.CHECKED_STATE_SET, NavigationView.EMPTY_STATE_SET }, new int[] { colorStateList.getColorForState(NavigationView.DISABLED_STATE_SET, defaultColor), data, defaultColor });
            }
        }
        return null;
    }
    
    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.mMenuInflater;
    }
    
    public void addHeaderView(@NonNull final View view) {
        this.mPresenter.addHeaderView(view);
    }
    
    public Drawable getItemBackground() {
        return this.mPresenter.getItemBackground();
    }
    
    @Nullable
    public ColorStateList getItemIconTintList() {
        return this.mPresenter.getItemTintList();
    }
    
    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mPresenter.getItemTextColor();
    }
    
    public Menu getMenu() {
        return (Menu)this.mMenu;
    }
    
    public View inflateHeaderView(@LayoutRes final int n) {
        return this.mPresenter.inflateHeaderView(n);
    }
    
    public void inflateMenu(final int n) {
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
    }
    
    protected void onMeasure(final int n, final int n2) {
        int n3 = n;
        Label_0042: {
            switch (View$MeasureSpec.getMode(n)) {
                default: {
                    n3 = n;
                    break Label_0042;
                }
                case 0: {
                    n3 = View$MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
                    break Label_0042;
                }
                case Integer.MIN_VALUE: {
                    n3 = View$MeasureSpec.makeMeasureSpec(Math.min(View$MeasureSpec.getSize(n), this.mMaxWidth), 1073741824);
                }
                case 1073741824: {
                    super.onMeasure(n3, n2);
                }
            }
        }
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mMenu.restorePresenterStates(savedState.menuState);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuState);
        return (Parcelable)savedState;
    }
    
    public void removeHeaderView(@NonNull final View view) {
        this.mPresenter.removeHeaderView(view);
    }
    
    public void setItemBackground(final Drawable itemBackground) {
        this.mPresenter.setItemBackground(itemBackground);
    }
    
    public void setItemBackgroundResource(@DrawableRes final int n) {
        this.setItemBackground(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setItemIconTintList(@Nullable final ColorStateList itemIconTintList) {
        this.mPresenter.setItemIconTintList(itemIconTintList);
    }
    
    public void setItemTextColor(@Nullable final ColorStateList itemTextColor) {
        this.mPresenter.setItemTextColor(itemTextColor);
    }
    
    public void setNavigationItemSelectedListener(final OnNavigationItemSelectedListener mListener) {
        this.mListener = mListener;
    }
    
    public interface OnNavigationItemSelectedListener
    {
        boolean onNavigationItemSelected(final MenuItem p0);
    }
    
    public static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public Bundle menuState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            super(parcel);
            this.menuState = parcel.readBundle();
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(@NonNull final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuState);
        }
    }
}
