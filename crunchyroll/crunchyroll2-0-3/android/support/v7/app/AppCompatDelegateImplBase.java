// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.View;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.WindowCallbackWrapper;
import android.util.AttributeSet;
import android.support.v7.internal.widget.TintTypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.internal.app.WindowDecorActionBar;
import android.support.v7.internal.view.SupportMenuInflater;
import android.view.KeyEvent;
import android.view.Window;
import android.view.Window$Callback;
import android.view.MenuInflater;
import android.content.Context;

abstract class AppCompatDelegateImplBase extends AppCompatDelegate
{
    private ActionBar mActionBar;
    final AppCompatCallback mAppCompatCallback;
    final Context mContext;
    boolean mHasActionBar;
    private boolean mIsDestroyed;
    boolean mIsFloating;
    private MenuInflater mMenuInflater;
    final Window$Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private CharSequence mTitle;
    final Window mWindow;
    boolean mWindowNoTitle;
    
    AppCompatDelegateImplBase(final Context mContext, final Window mWindow, final AppCompatCallback mAppCompatCallback) {
        this.mContext = mContext;
        this.mWindow = mWindow;
        this.mAppCompatCallback = mAppCompatCallback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        if (this.mOriginalWindowCallback instanceof AppCompatWindowCallbackBase) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        this.mWindow.setCallback(this.wrapWindowCallback(this.mOriginalWindowCallback));
    }
    
    abstract ActionBar createSupportActionBar();
    
    abstract boolean dispatchKeyEvent(final KeyEvent p0);
    
    final Context getActionBarThemedContext() {
        Context themedContext = null;
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            themedContext = supportActionBar.getThemedContext();
        }
        Context mContext;
        if ((mContext = themedContext) == null) {
            mContext = this.mContext;
        }
        return mContext;
    }
    
    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }
    
    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(this.getActionBarThemedContext());
        }
        return this.mMenuInflater;
    }
    
    @Override
    public ActionBar getSupportActionBar() {
        if (this.mHasActionBar) {
            if (this.mActionBar == null) {
                this.mActionBar = this.createSupportActionBar();
            }
        }
        else if (this.mActionBar instanceof WindowDecorActionBar) {
            this.mActionBar = null;
        }
        return this.mActionBar;
    }
    
    final CharSequence getTitle() {
        if (this.mOriginalWindowCallback instanceof Activity) {
            return ((Activity)this.mOriginalWindowCallback).getTitle();
        }
        return this.mTitle;
    }
    
    final Window$Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }
    
    final boolean isDestroyed() {
        return this.mIsDestroyed;
    }
    
    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return false;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        if (!obtainStyledAttributes.hasValue(R.styleable.Theme_windowActionBar)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.Theme_windowActionBar, false)) {
            this.mHasActionBar = true;
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.Theme_windowActionBarOverlay, false)) {
            this.mOverlayActionBar = true;
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.Theme_windowActionModeOverlay, false)) {
            this.mOverlayActionMode = true;
        }
        this.mIsFloating = obtainStyledAttributes.getBoolean(R.styleable.Theme_android_windowIsFloating, false);
        this.mWindowNoTitle = obtainStyledAttributes.getBoolean(R.styleable.Theme_windowNoTitle, false);
        obtainStyledAttributes.recycle();
    }
    
    @Override
    public final void onDestroy() {
        this.mIsDestroyed = true;
    }
    
    abstract boolean onKeyShortcut(final int p0, final KeyEvent p1);
    
    abstract boolean onMenuOpened(final int p0, final Menu p1);
    
    abstract boolean onPanelClosed(final int p0, final Menu p1);
    
    abstract void onTitleChanged(final CharSequence p0);
    
    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }
    
    @Override
    public void setHandleNativeActionModesEnabled(final boolean b) {
    }
    
    final void setSupportActionBar(final ActionBar mActionBar) {
        this.mActionBar = mActionBar;
    }
    
    @Override
    public final void setTitle(final CharSequence mTitle) {
        this.onTitleChanged(this.mTitle = mTitle);
    }
    
    abstract ActionMode startSupportActionModeFromWindow(final ActionMode.Callback p0);
    
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackBase(window$Callback);
    }
    
    private class ActionBarDrawableToggleImpl implements Delegate
    {
        @Override
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImplBase.this.getActionBarThemedContext();
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
            final Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
        
        @Override
        public boolean isNavigationVisible() {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            return supportActionBar != null && (supportActionBar.getDisplayOptions() & 0x4) != 0x0;
        }
        
        @Override
        public void setActionBarDescription(final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable homeAsUpIndicator, final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(homeAsUpIndicator);
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
    }
    
    class AppCompatWindowCallbackBase extends WindowCallbackWrapper
    {
        AppCompatWindowCallbackBase(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return super.dispatchKeyEvent(keyEvent) || AppCompatDelegateImplBase.this.dispatchKeyEvent(keyEvent);
        }
        
        @Override
        public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImplBase.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyShortcutEvent(keyEvent);
        }
        
        @Override
        public void onContentChanged() {
        }
        
        @Override
        public boolean onCreatePanelMenu(final int n, final Menu menu) {
            return (n != 0 || menu instanceof MenuBuilder) && super.onCreatePanelMenu(n, menu);
        }
        
        @Override
        public boolean onMenuOpened(final int n, final Menu menu) {
            return AppCompatDelegateImplBase.this.onMenuOpened(n, menu) || super.onMenuOpened(n, menu);
        }
        
        @Override
        public void onPanelClosed(final int n, final Menu menu) {
            if (AppCompatDelegateImplBase.this.onPanelClosed(n, menu)) {
                return;
            }
            super.onPanelClosed(n, menu);
        }
        
        @Override
        public boolean onPreparePanel(final int n, final View view, final Menu menu) {
            MenuBuilder menuBuilder;
            if (menu instanceof MenuBuilder) {
                menuBuilder = (MenuBuilder)menu;
            }
            else {
                menuBuilder = null;
            }
            boolean onPreparePanel;
            if (n == 0 && menuBuilder == null) {
                onPreparePanel = false;
            }
            else {
                if (menuBuilder != null) {
                    menuBuilder.setOverrideVisibleItems(true);
                }
                onPreparePanel = super.onPreparePanel(n, view, menu);
                if (menuBuilder != null) {
                    menuBuilder.setOverrideVisibleItems(false);
                    return onPreparePanel;
                }
            }
            return onPreparePanel;
        }
    }
}
