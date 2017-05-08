// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.ListMenuPresenter;
import android.support.v7.internal.widget.TintManager;
import android.view.MotionEvent;
import android.support.v7.internal.view.StandaloneActionMode;
import android.support.v7.internal.widget.ViewStubCompat;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.res.Configuration;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.annotation.NonNull;
import android.app.Dialog;
import android.support.v7.internal.app.WindowDecorActionBar;
import android.app.Activity;
import android.view.LayoutInflater$Factory;
import android.util.AttributeSet;
import android.view.ViewGroup$MarginLayoutParams;
import android.util.AndroidRuntimeException;
import android.view.KeyCharacterMap;
import android.view.ViewParent;
import android.view.WindowManager$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.Log;
import android.media.AudioManager;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.content.res.Resources$Theme;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.support.v7.internal.widget.ViewUtils;
import android.support.v7.internal.widget.FitWindowsViewGroup;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.os.Build$VERSION;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window$Callback;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.ContentFrameLayout;
import android.view.Menu;
import android.view.Window;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.View;
import android.support.v7.internal.widget.DecorContentParent;
import android.support.v7.internal.app.AppCompatViewInflater;
import android.support.v7.internal.widget.ActionBarContextView;
import android.widget.PopupWindow;
import android.support.v7.view.ActionMode;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v4.view.LayoutInflaterFactory;

class AppCompatDelegateImplV7 extends AppCompatDelegateImplBase implements LayoutInflaterFactory, Callback
{
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    private AppCompatViewInflater mAppCompatViewInflater;
    private boolean mClosingActionMenu;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private int mInvalidatePanelMenuFeatures;
    private boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private TextView mTitleView;
    private ViewGroup mWindowDecor;
    
    AppCompatDelegateImplV7(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
        this.mInvalidatePanelMenuRunnable = new Runnable() {
            @Override
            public void run() {
                if ((AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                    AppCompatDelegateImplV7.this.doInvalidatePanelMenu(0);
                }
                if ((AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures & 0x100) != 0x0) {
                    AppCompatDelegateImplV7.this.doInvalidatePanelMenu(8);
                }
                AppCompatDelegateImplV7.this.mInvalidatePanelMenuPosted = false;
                AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures = 0;
            }
        };
    }
    
    private void applyFixedSizeWindow(final ContentFrameLayout contentFrameLayout) {
        contentFrameLayout.setDecorPadding(this.mWindowDecor.getPaddingLeft(), this.mWindowDecor.getPaddingTop(), this.mWindowDecor.getPaddingRight(), this.mWindowDecor.getPaddingBottom());
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        obtainStyledAttributes.getValue(R.styleable.Theme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(R.styleable.Theme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (obtainStyledAttributes.hasValue(R.styleable.Theme_windowFixedWidthMajor)) {
            obtainStyledAttributes.getValue(R.styleable.Theme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Theme_windowFixedWidthMinor)) {
            obtainStyledAttributes.getValue(R.styleable.Theme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Theme_windowFixedHeightMajor)) {
            obtainStyledAttributes.getValue(R.styleable.Theme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Theme_windowFixedHeightMinor)) {
            obtainStyledAttributes.getValue(R.styleable.Theme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }
    
    private void callOnPanelClosed(final int n, final PanelFeatureState panelFeatureState, final Menu menu) {
        PanelFeatureState panelFeatureState2 = panelFeatureState;
        Object menu2 = menu;
        if (menu == null) {
            PanelFeatureState panelFeatureState3;
            if ((panelFeatureState3 = panelFeatureState) == null) {
                panelFeatureState3 = panelFeatureState;
                if (n >= 0) {
                    panelFeatureState3 = panelFeatureState;
                    if (n < this.mPanels.length) {
                        panelFeatureState3 = this.mPanels[n];
                    }
                }
            }
            panelFeatureState2 = panelFeatureState3;
            menu2 = menu;
            if (panelFeatureState3 != null) {
                menu2 = panelFeatureState3.menu;
                panelFeatureState2 = panelFeatureState3;
            }
        }
        if (panelFeatureState2 == null || panelFeatureState2.isOpen) {
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null) {
                windowCallback.onPanelClosed(n, (Menu)menu2);
            }
        }
    }
    
    private void checkCloseActionMenu(final MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null && !this.isDestroyed()) {
            windowCallback.onPanelClosed(8, (Menu)menuBuilder);
        }
        this.mClosingActionMenu = false;
    }
    
    private void closePanel(final int n) {
        this.closePanel(this.getPanelState(n, true), true);
    }
    
    private void closePanel(final PanelFeatureState panelFeatureState, final boolean b) {
        if (b && panelFeatureState.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
            this.checkCloseActionMenu(panelFeatureState.menu);
        }
        else {
            final boolean isOpen = panelFeatureState.isOpen;
            final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
            if (windowManager != null && isOpen && panelFeatureState.decorView != null) {
                windowManager.removeView((View)panelFeatureState.decorView);
            }
            panelFeatureState.isPrepared = false;
            panelFeatureState.isHandled = false;
            panelFeatureState.isOpen = false;
            if (isOpen && b) {
                this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
            panelFeatureState.shownPanelView = null;
            panelFeatureState.refreshDecorView = true;
            if (this.mPreparedPanel == panelFeatureState) {
                this.mPreparedPanel = null;
            }
        }
    }
    
    private void doInvalidatePanelMenu(final int n) {
        final PanelFeatureState panelState = this.getPanelState(n, true);
        if (panelState.menu != null) {
            final Bundle frozenActionViewState = new Bundle();
            panelState.menu.saveActionViewStates(frozenActionViewState);
            if (frozenActionViewState.size() > 0) {
                panelState.frozenActionViewState = frozenActionViewState;
            }
            panelState.menu.stopDispatchingItemsChanged();
            panelState.menu.clear();
        }
        panelState.refreshMenuContent = true;
        panelState.refreshDecorView = true;
        if ((n == 8 || n == 0) && this.mDecorContentParent != null) {
            final PanelFeatureState panelState2 = this.getPanelState(0, false);
            if (panelState2 != null) {
                panelState2.isPrepared = false;
                this.preparePanel(panelState2, null);
            }
        }
    }
    
    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            final LayoutInflater from = LayoutInflater.from(this.mContext);
            if (!this.mWindowNoTitle) {
                if (this.mIsFloating) {
                    this.mSubDecor = (ViewGroup)from.inflate(R.layout.abc_dialog_title_material, (ViewGroup)null);
                    this.mOverlayActionBar = false;
                    this.mHasActionBar = false;
                }
                else if (this.mHasActionBar) {
                    final TypedValue typedValue = new TypedValue();
                    this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                    Object mContext;
                    if (typedValue.resourceId != 0) {
                        mContext = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
                    }
                    else {
                        mContext = this.mContext;
                    }
                    this.mSubDecor = (ViewGroup)LayoutInflater.from((Context)mContext).inflate(R.layout.abc_screen_toolbar, (ViewGroup)null);
                    (this.mDecorContentParent = (DecorContentParent)this.mSubDecor.findViewById(R.id.decor_content_parent)).setWindowCallback(this.getWindowCallback());
                    if (this.mOverlayActionBar) {
                        this.mDecorContentParent.initFeature(9);
                    }
                    if (this.mFeatureProgress) {
                        this.mDecorContentParent.initFeature(2);
                    }
                    if (this.mFeatureIndeterminateProgress) {
                        this.mDecorContentParent.initFeature(5);
                    }
                }
            }
            else {
                if (this.mOverlayActionMode) {
                    this.mSubDecor = (ViewGroup)from.inflate(R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
                }
                else {
                    this.mSubDecor = (ViewGroup)from.inflate(R.layout.abc_screen_simple, (ViewGroup)null);
                }
                if (Build$VERSION.SDK_INT >= 21) {
                    ViewCompat.setOnApplyWindowInsetsListener((View)this.mSubDecor, new OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                            final int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                            final int access$300 = AppCompatDelegateImplV7.this.updateStatusGuard(systemWindowInsetTop);
                            WindowInsetsCompat replaceSystemWindowInsets = windowInsetsCompat;
                            if (systemWindowInsetTop != access$300) {
                                replaceSystemWindowInsets = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), access$300, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            }
                            return ViewCompat.onApplyWindowInsets(view, replaceSystemWindowInsets);
                        }
                    });
                }
                else {
                    ((FitWindowsViewGroup)this.mSubDecor).setOnFitSystemWindowsListener((FitWindowsViewGroup.OnFitSystemWindowsListener)new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                        @Override
                        public void onFitSystemWindows(final Rect rect) {
                            rect.top = AppCompatDelegateImplV7.this.updateStatusGuard(rect.top);
                        }
                    });
                }
            }
            if (this.mSubDecor == null) {
                throw new IllegalArgumentException("AppCompat does not support the current theme features");
            }
            if (this.mDecorContentParent == null) {
                this.mTitleView = (TextView)this.mSubDecor.findViewById(R.id.title);
            }
            ViewUtils.makeOptionalFitsSystemWindows((View)this.mSubDecor);
            final ViewGroup viewGroup = (ViewGroup)this.mWindow.findViewById(16908290);
            final ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(R.id.action_bar_activity_content);
            while (viewGroup.getChildCount() > 0) {
                final View child = viewGroup.getChildAt(0);
                viewGroup.removeViewAt(0);
                contentFrameLayout.addView(child);
            }
            this.mWindow.setContentView((View)this.mSubDecor);
            viewGroup.setId(-1);
            contentFrameLayout.setId(16908290);
            if (viewGroup instanceof FrameLayout) {
                ((FrameLayout)viewGroup).setForeground((Drawable)null);
            }
            final CharSequence title = this.getTitle();
            if (!TextUtils.isEmpty(title)) {
                this.onTitleChanged(title);
            }
            this.applyFixedSizeWindow(contentFrameLayout);
            this.onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            final PanelFeatureState panelState = this.getPanelState(0, false);
            if (!this.isDestroyed() && (panelState == null || panelState.menu == null)) {
                this.invalidatePanelMenu(8);
            }
        }
    }
    
    private PanelFeatureState findMenuPanel(final Menu menu) {
        final PanelFeatureState[] mPanels = this.mPanels;
        int length;
        if (mPanels != null) {
            length = mPanels.length;
        }
        else {
            length = 0;
        }
        for (int i = 0; i < length; ++i) {
            final PanelFeatureState panelFeatureState = mPanels[i];
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }
    
    private PanelFeatureState getPanelState(final int n, final boolean b) {
        final PanelFeatureState[] mPanels = this.mPanels;
        PanelFeatureState[] array = null;
        Label_0055: {
            if (mPanels != null) {
                array = mPanels;
                if (mPanels.length > n) {
                    break Label_0055;
                }
            }
            final PanelFeatureState[] mPanels2 = new PanelFeatureState[n + 1];
            if (mPanels != null) {
                System.arraycopy(mPanels, 0, mPanels2, 0, mPanels.length);
            }
            array = mPanels2;
            this.mPanels = mPanels2;
        }
        PanelFeatureState panelFeatureState;
        if ((panelFeatureState = array[n]) == null) {
            panelFeatureState = new PanelFeatureState(n);
            array[n] = panelFeatureState;
        }
        return panelFeatureState;
    }
    
    private boolean initializePanelContent(final PanelFeatureState panelFeatureState) {
        if (panelFeatureState.createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
        }
        else {
            if (panelFeatureState.menu == null) {
                return false;
            }
            if (this.mPanelMenuPresenterCallback == null) {
                this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
            }
            panelFeatureState.shownPanelView = (View)panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
            if (panelFeatureState.shownPanelView == null) {
                return false;
            }
        }
        return true;
    }
    
    private boolean initializePanelDecor(final PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(this.getActionBarThemedContext());
        panelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }
    
    private boolean initializePanelMenu(final PanelFeatureState panelFeatureState) {
        final Context mContext = this.mContext;
        Object o = null;
        Label_0176: {
            if (panelFeatureState.featureId != 0) {
                o = mContext;
                if (panelFeatureState.featureId != 8) {
                    break Label_0176;
                }
            }
            o = mContext;
            if (this.mDecorContentParent != null) {
                final TypedValue typedValue = new TypedValue();
                final Resources$Theme theme = mContext.getTheme();
                theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                Resources$Theme theme2 = null;
                if (typedValue.resourceId != 0) {
                    theme2 = mContext.getResources().newTheme();
                    theme2.setTo(theme);
                    theme2.applyStyle(typedValue.resourceId, true);
                    theme2.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                else {
                    theme.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                Resources$Theme theme3 = theme2;
                if (typedValue.resourceId != 0) {
                    if ((theme3 = theme2) == null) {
                        theme3 = mContext.getResources().newTheme();
                        theme3.setTo(theme);
                    }
                    theme3.applyStyle(typedValue.resourceId, true);
                }
                o = mContext;
                if (theme3 != null) {
                    o = new ContextThemeWrapper(mContext, 0);
                    ((Context)o).getTheme().setTo(theme3);
                }
            }
        }
        final MenuBuilder menu = new MenuBuilder((Context)o);
        menu.setCallback((MenuBuilder.Callback)this);
        panelFeatureState.setMenu(menu);
        return true;
    }
    
    private void invalidatePanelMenu(final int n) {
        this.mInvalidatePanelMenuFeatures |= 1 << n;
        if (!this.mInvalidatePanelMenuPosted && this.mWindowDecor != null) {
            ViewCompat.postOnAnimation((View)this.mWindowDecor, this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }
    
    private boolean onKeyDownPanel(final int n, final KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            final PanelFeatureState panelState = this.getPanelState(n, true);
            if (!panelState.isOpen) {
                return this.preparePanel(panelState, keyEvent);
            }
        }
        return false;
    }
    
    private void onKeyUpPanel(final int n, final KeyEvent keyEvent) {
        if (this.mActionMode == null) {
            final boolean b = false;
            final PanelFeatureState panelState = this.getPanelState(n, true);
            int n2;
            if (n == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext))) {
                if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                    n2 = (b ? 1 : 0);
                    if (!this.isDestroyed()) {
                        n2 = (b ? 1 : 0);
                        if (this.preparePanel(panelState, keyEvent)) {
                            n2 = (this.mDecorContentParent.showOverflowMenu() ? 1 : 0);
                        }
                    }
                }
                else {
                    n2 = (this.mDecorContentParent.hideOverflowMenu() ? 1 : 0);
                }
            }
            else if (panelState.isOpen || panelState.isHandled) {
                n2 = (panelState.isOpen ? 1 : 0);
                this.closePanel(panelState, true);
            }
            else {
                n2 = (b ? 1 : 0);
                if (panelState.isPrepared) {
                    boolean preparePanel = true;
                    if (panelState.refreshMenuContent) {
                        panelState.isPrepared = false;
                        preparePanel = this.preparePanel(panelState, keyEvent);
                    }
                    n2 = (b ? 1 : 0);
                    if (preparePanel) {
                        this.openPanel(panelState, keyEvent);
                        n2 = 1;
                    }
                }
            }
            if (n2 != 0) {
                final AudioManager audioManager = (AudioManager)this.mContext.getSystemService("audio");
                if (audioManager != null) {
                    audioManager.playSoundEffect(0);
                    return;
                }
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
    }
    
    private void openPanel(final PanelFeatureState panelFeatureState, final KeyEvent keyEvent) {
        if (!panelFeatureState.isOpen && !this.isDestroyed()) {
            if (panelFeatureState.featureId == 0) {
                final Context mContext = this.mContext;
                boolean b;
                if ((mContext.getResources().getConfiguration().screenLayout & 0xF) == 0x4) {
                    b = true;
                }
                else {
                    b = false;
                }
                boolean b2;
                if (mContext.getApplicationInfo().targetSdkVersion >= 11) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                if (b && b2) {
                    return;
                }
            }
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, (Menu)panelFeatureState.menu)) {
                this.closePanel(panelFeatureState, true);
                return;
            }
            final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
            if (windowManager != null && this.preparePanel(panelFeatureState, keyEvent)) {
                final int n = -2;
                int n2;
                if (panelFeatureState.decorView == null || panelFeatureState.refreshDecorView) {
                    if (panelFeatureState.decorView == null) {
                        if (!this.initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                            return;
                        }
                    }
                    else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                        panelFeatureState.decorView.removeAllViews();
                    }
                    if (!this.initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                        return;
                    }
                    ViewGroup$LayoutParams layoutParams;
                    if ((layoutParams = panelFeatureState.shownPanelView.getLayoutParams()) == null) {
                        layoutParams = new ViewGroup$LayoutParams(-2, -2);
                    }
                    panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
                    final ViewParent parent = panelFeatureState.shownPanelView.getParent();
                    if (parent != null && parent instanceof ViewGroup) {
                        ((ViewGroup)parent).removeView(panelFeatureState.shownPanelView);
                    }
                    panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams);
                    n2 = n;
                    if (!panelFeatureState.shownPanelView.hasFocus()) {
                        panelFeatureState.shownPanelView.requestFocus();
                        n2 = n;
                    }
                }
                else {
                    n2 = n;
                    if (panelFeatureState.createdPanelView != null) {
                        final ViewGroup$LayoutParams layoutParams2 = panelFeatureState.createdPanelView.getLayoutParams();
                        n2 = n;
                        if (layoutParams2 != null) {
                            n2 = n;
                            if (layoutParams2.width == -1) {
                                n2 = -1;
                            }
                        }
                    }
                }
                panelFeatureState.isHandled = false;
                final WindowManager$LayoutParams windowManager$LayoutParams = new WindowManager$LayoutParams(n2, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
                windowManager$LayoutParams.gravity = panelFeatureState.gravity;
                windowManager$LayoutParams.windowAnimations = panelFeatureState.windowAnimations;
                windowManager.addView((View)panelFeatureState.decorView, (ViewGroup$LayoutParams)windowManager$LayoutParams);
                panelFeatureState.isOpen = true;
            }
        }
    }
    
    private boolean performPanelShortcut(final PanelFeatureState panelFeatureState, final int n, final KeyEvent keyEvent, final int n2) {
        boolean b;
        if (keyEvent.isSystem()) {
            b = false;
        }
        else {
            final boolean b2 = false;
            boolean performShortcut = false;
            Label_0060: {
                if (!panelFeatureState.isPrepared) {
                    performShortcut = b2;
                    if (!this.preparePanel(panelFeatureState, keyEvent)) {
                        break Label_0060;
                    }
                }
                performShortcut = b2;
                if (panelFeatureState.menu != null) {
                    performShortcut = panelFeatureState.menu.performShortcut(n, keyEvent, n2);
                }
            }
            b = performShortcut;
            if (performShortcut) {
                b = performShortcut;
                if ((n2 & 0x1) == 0x0) {
                    b = performShortcut;
                    if (this.mDecorContentParent == null) {
                        this.closePanel(panelFeatureState, true);
                        return performShortcut;
                    }
                }
            }
        }
        return b;
    }
    
    private boolean preparePanel(final PanelFeatureState mPreparedPanel, final KeyEvent keyEvent) {
        if (!this.isDestroyed()) {
            if (mPreparedPanel.isPrepared) {
                return true;
            }
            if (this.mPreparedPanel != null && this.mPreparedPanel != mPreparedPanel) {
                this.closePanel(this.mPreparedPanel, false);
            }
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null) {
                mPreparedPanel.createdPanelView = windowCallback.onCreatePanelView(mPreparedPanel.featureId);
            }
            boolean b;
            if (mPreparedPanel.featureId == 0 || mPreparedPanel.featureId == 8) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && this.mDecorContentParent != null) {
                this.mDecorContentParent.setMenuPrepared();
            }
            if (mPreparedPanel.createdPanelView == null) {
                if (mPreparedPanel.menu == null || mPreparedPanel.refreshMenuContent) {
                    if (mPreparedPanel.menu == null && (!this.initializePanelMenu(mPreparedPanel) || mPreparedPanel.menu == null)) {
                        return false;
                    }
                    if (b && this.mDecorContentParent != null) {
                        if (this.mActionMenuPresenterCallback == null) {
                            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                        }
                        this.mDecorContentParent.setMenu((Menu)mPreparedPanel.menu, this.mActionMenuPresenterCallback);
                    }
                    mPreparedPanel.menu.stopDispatchingItemsChanged();
                    if (!windowCallback.onCreatePanelMenu(mPreparedPanel.featureId, (Menu)mPreparedPanel.menu)) {
                        mPreparedPanel.setMenu(null);
                        if (b && this.mDecorContentParent != null) {
                            this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                            return false;
                        }
                        return false;
                    }
                    else {
                        mPreparedPanel.refreshMenuContent = false;
                    }
                }
                mPreparedPanel.menu.stopDispatchingItemsChanged();
                if (mPreparedPanel.frozenActionViewState != null) {
                    mPreparedPanel.menu.restoreActionViewStates(mPreparedPanel.frozenActionViewState);
                    mPreparedPanel.frozenActionViewState = null;
                }
                if (!windowCallback.onPreparePanel(0, mPreparedPanel.createdPanelView, (Menu)mPreparedPanel.menu)) {
                    if (b && this.mDecorContentParent != null) {
                        this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    mPreparedPanel.menu.startDispatchingItemsChanged();
                    return false;
                }
                int deviceId;
                if (keyEvent != null) {
                    deviceId = keyEvent.getDeviceId();
                }
                else {
                    deviceId = -1;
                }
                mPreparedPanel.qwertyMode = (KeyCharacterMap.load(deviceId).getKeyboardType() != 1);
                mPreparedPanel.menu.setQwertyMode(mPreparedPanel.qwertyMode);
                mPreparedPanel.menu.startDispatchingItemsChanged();
            }
            mPreparedPanel.isPrepared = true;
            mPreparedPanel.isHandled = false;
            this.mPreparedPanel = mPreparedPanel;
            return true;
        }
        return false;
    }
    
    private void reopenMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext)) || this.mDecorContentParent.isOverflowMenuShowPending())) {
            final Window$Callback windowCallback = this.getWindowCallback();
            if (!this.mDecorContentParent.isOverflowMenuShowing() || !b) {
                if (windowCallback != null && !this.isDestroyed()) {
                    if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                        this.mWindowDecor.removeCallbacks(this.mInvalidatePanelMenuRunnable);
                        this.mInvalidatePanelMenuRunnable.run();
                    }
                    final PanelFeatureState panelState = this.getPanelState(0, true);
                    if (panelState.menu != null && !panelState.refreshMenuContent && windowCallback.onPreparePanel(0, panelState.createdPanelView, (Menu)panelState.menu)) {
                        windowCallback.onMenuOpened(8, (Menu)panelState.menu);
                        this.mDecorContentParent.showOverflowMenu();
                    }
                }
            }
            else {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.isDestroyed()) {
                    windowCallback.onPanelClosed(8, (Menu)this.getPanelState(0, true).menu);
                }
            }
            return;
        }
        final PanelFeatureState panelState2 = this.getPanelState(0, true);
        panelState2.refreshDecorView = true;
        this.closePanel(panelState2, false);
        this.openPanel(panelState2, null);
    }
    
    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }
    
    private int updateStatusGuard(int visibility) {
        final int n = 0;
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = b;
        int n2 = visibility;
        if (this.mActionModeView != null) {
            b3 = b;
            n2 = visibility;
            if (this.mActionModeView.getLayoutParams() instanceof ViewGroup$MarginLayoutParams) {
                final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)this.mActionModeView.getLayoutParams();
                int n3 = 0;
                boolean b4 = false;
                boolean b7;
                int n5;
                if (this.mActionModeView.isShown()) {
                    if (this.mTempRect1 == null) {
                        this.mTempRect1 = new Rect();
                        this.mTempRect2 = new Rect();
                    }
                    final Rect mTempRect1 = this.mTempRect1;
                    final Rect mTempRect2 = this.mTempRect2;
                    mTempRect1.set(0, visibility, 0, 0);
                    ViewUtils.computeFitSystemWindows((View)this.mSubDecor, mTempRect1, mTempRect2);
                    int n4;
                    if (mTempRect2.top == 0) {
                        n4 = visibility;
                    }
                    else {
                        n4 = 0;
                    }
                    if (layoutParams.topMargin != n4) {
                        final boolean b5 = true;
                        layoutParams.topMargin = visibility;
                        if (this.mStatusGuard == null) {
                            (this.mStatusGuard = new View(this.mContext)).setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                            this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup$LayoutParams(-1, visibility));
                            b4 = b5;
                        }
                        else {
                            final ViewGroup$LayoutParams layoutParams2 = this.mStatusGuard.getLayoutParams();
                            b4 = b5;
                            if (layoutParams2.height != visibility) {
                                layoutParams2.height = visibility;
                                this.mStatusGuard.setLayoutParams(layoutParams2);
                                b4 = b5;
                            }
                        }
                    }
                    boolean b6;
                    if (this.mStatusGuard != null) {
                        b6 = true;
                    }
                    else {
                        b6 = false;
                    }
                    n3 = (b4 ? 1 : 0);
                    b7 = b6;
                    n5 = visibility;
                    if (!this.mOverlayActionMode) {
                        n3 = (b4 ? 1 : 0);
                        b7 = b6;
                        n5 = visibility;
                        if (b6) {
                            n5 = 0;
                            b7 = b6;
                            n3 = (b4 ? 1 : 0);
                        }
                    }
                }
                else {
                    b7 = b2;
                    n5 = visibility;
                    if (layoutParams.topMargin != 0) {
                        n3 = 1;
                        layoutParams.topMargin = 0;
                        b7 = b2;
                        n5 = visibility;
                    }
                }
                b3 = b7;
                n2 = n5;
                if (n3 != 0) {
                    this.mActionModeView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    n2 = n5;
                    b3 = b7;
                }
            }
        }
        if (this.mStatusGuard != null) {
            final View mStatusGuard = this.mStatusGuard;
            if (b3) {
                visibility = n;
            }
            else {
                visibility = 8;
            }
            mStatusGuard.setVisibility(visibility);
        }
        return n2;
    }
    
    @Override
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    View callActivityOnCreateView(View onCreateView, final String s, final Context context, final AttributeSet set) {
        if (this.mOriginalWindowCallback instanceof LayoutInflater$Factory) {
            onCreateView = ((LayoutInflater$Factory)this.mOriginalWindowCallback).onCreateView(s, context, set);
            if (onCreateView != null) {
                return onCreateView;
            }
        }
        return null;
    }
    
    public ActionBar createSupportActionBar() {
        this.ensureSubDecor();
        ActionBar actionBar = null;
        if (this.mOriginalWindowCallback instanceof Activity) {
            actionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
        }
        else if (this.mOriginalWindowCallback instanceof Dialog) {
            actionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
        }
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
        }
        return actionBar;
    }
    
    @Override
    public View createView(final View view, final String s, @NonNull final Context context, @NonNull final AttributeSet set) {
        final boolean b = Build$VERSION.SDK_INT < 21;
        if (this.mAppCompatViewInflater == null) {
            this.mAppCompatViewInflater = new AppCompatViewInflater();
        }
        return this.mAppCompatViewInflater.createView(view, s, context, set, b && this.mSubDecorInstalled && view != null && view.getId() != 16908290 && !ViewCompat.isAttachedToWindow(view), b, true);
    }
    
    @Override
    boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        int n;
        if (keyEvent.getAction() == 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            return this.onKeyDown(keyCode, keyEvent);
        }
        return this.onKeyUp(keyCode, keyEvent);
    }
    
    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }
    
    @Override
    public void installViewFactory() {
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        if (from.getFactory() == null) {
            LayoutInflaterCompat.setFactory(from, this);
            return;
        }
        Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
    }
    
    @Override
    public void invalidateOptionsMenu() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null && supportActionBar.invalidateOptionsMenu()) {
            return;
        }
        this.invalidatePanelMenu(0);
    }
    
    boolean onBackPressed() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        else {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar == null || !supportActionBar.collapseActionView()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.onConfigurationChanged(configuration);
            }
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mWindowDecor = (ViewGroup)this.mWindow.getDecorView();
        if (this.mOriginalWindowCallback instanceof Activity && NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback) != null) {
            final ActionBar peekSupportActionBar = this.peekSupportActionBar();
            if (peekSupportActionBar != null) {
                peekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                return;
            }
            this.mEnableDefaultActionBarUp = true;
        }
    }
    
    @Override
    public final View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        final View callActivityOnCreateView = this.callActivityOnCreateView(view, s, context, set);
        if (callActivityOnCreateView != null) {
            return callActivityOnCreateView;
        }
        return this.createView(view, s, context, set);
    }
    
    boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        boolean onKeyShortcut = false;
        switch (n) {
            default: {
                if (Build$VERSION.SDK_INT < 11) {
                    onKeyShortcut = this.onKeyShortcut(n, keyEvent);
                }
                return onKeyShortcut;
            }
            case 82: {
                this.onKeyDownPanel(0, keyEvent);
                return true;
            }
        }
    }
    
    @Override
    boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar == null || !supportActionBar.onKeyShortcut(n, keyEvent)) {
            if (this.mPreparedPanel == null || !this.performPanelShortcut(this.mPreparedPanel, keyEvent.getKeyCode(), keyEvent, 1)) {
                if (this.mPreparedPanel == null) {
                    final PanelFeatureState panelState = this.getPanelState(0, true);
                    this.preparePanel(panelState, keyEvent);
                    final boolean performPanelShortcut = this.performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
                    panelState.isPrepared = false;
                    if (performPanelShortcut) {
                        return true;
                    }
                }
                return false;
            }
            if (this.mPreparedPanel != null) {
                return this.mPreparedPanel.isHandled = true;
            }
        }
        return true;
    }
    
    boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        switch (n) {
            case 82: {
                this.onKeyUpPanel(0, keyEvent);
                return true;
            }
            case 4: {
                final PanelFeatureState panelState = this.getPanelState(0, false);
                if (panelState != null && panelState.isOpen) {
                    this.closePanel(panelState, true);
                    return true;
                }
                if (this.onBackPressed()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    @Override
    public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null && !this.isDestroyed()) {
            final PanelFeatureState menuPanel = this.findMenuPanel((Menu)menuBuilder.getRootMenu());
            if (menuPanel != null) {
                return windowCallback.onMenuItemSelected(menuPanel.featureId, menuItem);
            }
        }
        return false;
    }
    
    @Override
    public void onMenuModeChange(final MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }
    
    @Override
    boolean onMenuOpened(final int n, final Menu menu) {
        if (n == 8) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(true);
            }
            return true;
        }
        return false;
    }
    
    @Override
    boolean onPanelClosed(final int n, final Menu menu) {
        if (n == 8) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
            }
            return true;
        }
        if (n == 0) {
            final PanelFeatureState panelState = this.getPanelState(n, true);
            if (panelState.isOpen) {
                this.closePanel(panelState, false);
            }
        }
        return false;
    }
    
    @Override
    public void onPostCreate(final Bundle bundle) {
        this.ensureSubDecor();
    }
    
    @Override
    public void onPostResume() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(true);
        }
    }
    
    @Override
    public void onStop() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
    }
    
    void onSubDecorInstalled(final ViewGroup viewGroup) {
    }
    
    @Override
    void onTitleChanged(final CharSequence text) {
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setWindowTitle(text);
        }
        else {
            if (this.peekSupportActionBar() != null) {
                this.peekSupportActionBar().setWindowTitle(text);
                return;
            }
            if (this.mTitleView != null) {
                this.mTitleView.setText(text);
            }
        }
    }
    
    @Override
    public boolean requestWindowFeature(final int n) {
        switch (n) {
            default: {
                return this.mWindow.requestFeature(n);
            }
            case 8: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mHasActionBar = true;
            }
            case 9: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mOverlayActionBar = true;
            }
            case 10: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mOverlayActionMode = true;
            }
            case 2: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mFeatureProgress = true;
            }
            case 5: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mFeatureIndeterminateProgress = true;
            }
            case 1: {
                this.throwFeatureRequestIfSubDecorInstalled();
                return this.mWindowNoTitle = true;
            }
        }
    }
    
    @Override
    public void setContentView(final int n) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(n, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setSupportActionBar(final Toolbar toolbar) {
        if (!(this.mOriginalWindowCallback instanceof Activity)) {
            return;
        }
        if (this.getSupportActionBar() instanceof WindowDecorActionBar) {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
        }
        final ToolbarActionBar supportActionBar = new ToolbarActionBar(toolbar, ((Activity)this.mContext).getTitle(), this.mWindow);
        this.setSupportActionBar(supportActionBar);
        this.mWindow.setCallback(supportActionBar.getWrappedWindowCallback());
        supportActionBar.invalidateOptionsMenu();
    }
    
    @Override
    public ActionMode startSupportActionMode(final ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        final ActionModeCallbackWrapperV7 actionModeCallbackWrapperV7 = new ActionModeCallbackWrapperV7(callback);
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            this.mActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV7);
            if (this.mActionMode != null && this.mAppCompatCallback != null) {
                this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
        }
        if (this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(actionModeCallbackWrapperV7);
        }
        return this.mActionMode;
    }
    
    @Override
    ActionMode startSupportActionModeFromWindow(final ActionMode.Callback callback) {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        final ActionModeCallbackWrapperV7 actionModeCallbackWrapperV7 = new ActionModeCallbackWrapperV7(callback);
        ActionMode onWindowStartingSupportActionMode;
        Object mActionModeView = onWindowStartingSupportActionMode = null;
        while (true) {
            if (this.mAppCompatCallback == null) {
                break Label_0063;
            }
            onWindowStartingSupportActionMode = (ActionMode)mActionModeView;
            if (this.isDestroyed()) {
                break Label_0063;
            }
            try {
                onWindowStartingSupportActionMode = this.mAppCompatCallback.onWindowStartingSupportActionMode(actionModeCallbackWrapperV7);
                if (onWindowStartingSupportActionMode != null) {
                    this.mActionMode = onWindowStartingSupportActionMode;
                }
                else {
                    if (this.mActionModeView == null) {
                        if (this.mIsFloating) {
                            mActionModeView = new TypedValue();
                            final Resources$Theme theme = this.mContext.getTheme();
                            theme.resolveAttribute(R.attr.actionBarTheme, (TypedValue)mActionModeView, true);
                            Object mContext;
                            if (((TypedValue)mActionModeView).resourceId != 0) {
                                final Resources$Theme theme2 = this.mContext.getResources().newTheme();
                                theme2.setTo(theme);
                                theme2.applyStyle(((TypedValue)mActionModeView).resourceId, true);
                                mContext = new ContextThemeWrapper(this.mContext, 0);
                                ((Context)mContext).getTheme().setTo(theme2);
                            }
                            else {
                                mContext = this.mContext;
                            }
                            this.mActionModeView = new ActionBarContextView((Context)mContext);
                            (this.mActionModePopup = new PopupWindow((Context)mContext, (AttributeSet)null, R.attr.actionModePopupWindowStyle)).setContentView((View)this.mActionModeView);
                            this.mActionModePopup.setWidth(-1);
                            ((Context)mContext).getTheme().resolveAttribute(R.attr.actionBarSize, (TypedValue)mActionModeView, true);
                            this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(((TypedValue)mActionModeView).data, ((Context)mContext).getResources().getDisplayMetrics()));
                            this.mActionModePopup.setHeight(-2);
                            this.mShowActionModePopup = new Runnable() {
                                @Override
                                public void run() {
                                    AppCompatDelegateImplV7.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImplV7.this.mActionModeView, 55, 0, 0);
                                }
                            };
                        }
                        else {
                            final ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
                            if (viewStubCompat != null) {
                                viewStubCompat.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                                this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
                            }
                        }
                    }
                    if (this.mActionModeView != null) {
                        this.mActionModeView.killMode();
                        final Context context = this.mActionModeView.getContext();
                        mActionModeView = this.mActionModeView;
                        final StandaloneActionMode mActionMode = new StandaloneActionMode(context, (ActionBarContextView)mActionModeView, actionModeCallbackWrapperV7, this.mActionModePopup == null);
                        if (callback.onCreateActionMode(mActionMode, mActionMode.getMenu())) {
                            mActionMode.invalidate();
                            this.mActionModeView.initForMode(mActionMode);
                            this.mActionModeView.setVisibility(0);
                            this.mActionMode = mActionMode;
                            if (this.mActionModePopup != null) {
                                this.mWindow.getDecorView().post(this.mShowActionModePopup);
                            }
                            this.mActionModeView.sendAccessibilityEvent(32);
                            if (this.mActionModeView.getParent() != null) {
                                ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                            }
                        }
                        else {
                            this.mActionMode = null;
                        }
                    }
                }
                if (this.mActionMode != null && this.mAppCompatCallback != null) {
                    this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
                }
                return this.mActionMode;
            }
            catch (AbstractMethodError abstractMethodError) {
                onWindowStartingSupportActionMode = (ActionMode)mActionModeView;
                continue;
            }
            break;
        }
    }
    
    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            AppCompatDelegateImplV7.this.checkCloseActionMenu(menuBuilder);
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            final Window$Callback windowCallback = AppCompatDelegateImplV7.this.getWindowCallback();
            if (windowCallback != null) {
                windowCallback.onMenuOpened(8, (Menu)menuBuilder);
            }
            return true;
        }
    }
    
    class ActionModeCallbackWrapperV7 implements ActionMode.Callback
    {
        private ActionMode.Callback mWrapped;
        
        public ActionModeCallbackWrapperV7(final ActionMode.Callback mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }
        
        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (AppCompatDelegateImplV7.this.mActionModePopup != null) {
                AppCompatDelegateImplV7.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImplV7.this.mShowActionModePopup);
                AppCompatDelegateImplV7.this.mActionModePopup.dismiss();
            }
            else if (AppCompatDelegateImplV7.this.mActionModeView != null) {
                AppCompatDelegateImplV7.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImplV7.this.mActionModeView.getParent() != null) {
                    ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV7.this.mActionModeView.getParent());
                }
            }
            if (AppCompatDelegateImplV7.this.mActionModeView != null) {
                AppCompatDelegateImplV7.this.mActionModeView.removeAllViews();
            }
            if (AppCompatDelegateImplV7.this.mAppCompatCallback != null) {
                AppCompatDelegateImplV7.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImplV7.this.mActionMode);
            }
            AppCompatDelegateImplV7.this.mActionMode = null;
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }
    
    private class ListMenuDecorView extends FrameLayout
    {
        public ListMenuDecorView(final Context context) {
            super(context);
        }
        
        private boolean isOutOfBounds(final int n, final int n2) {
            return n < -5 || n2 < -5 || n > this.getWidth() + 5 || n2 > this.getHeight() + 5;
        }
        
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImplV7.this.dispatchKeyEvent(keyEvent);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && this.isOutOfBounds((int)motionEvent.getX(), (int)motionEvent.getY())) {
                AppCompatDelegateImplV7.this.closePanel(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        
        public void setBackgroundResource(final int n) {
            this.setBackgroundDrawable(TintManager.getDrawable(this.getContext(), n));
        }
    }
    
    private static final class PanelFeatureState
    {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;
        
        PanelFeatureState(final int featureId) {
            this.featureId = featureId;
            this.refreshDecorView = false;
        }
        
        void applyFrozenState() {
            if (this.menu != null && this.frozenMenuState != null) {
                this.menu.restorePresenterStates(this.frozenMenuState);
                this.frozenMenuState = null;
            }
        }
        
        public void clearMenuPresenters() {
            if (this.menu != null) {
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }
        
        MenuView getListMenuView(final MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                (this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout)).setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }
        
        public boolean hasPanelItems() {
            final boolean b = true;
            boolean b2;
            if (this.shownPanelView == null) {
                b2 = false;
            }
            else {
                b2 = b;
                if (this.createdPanelView == null) {
                    b2 = b;
                    if (this.listMenuPresenter.getAdapter().getCount() <= 0) {
                        return false;
                    }
                }
            }
            return b2;
        }
        
        void onRestoreInstanceState(final Parcelable parcelable) {
            final SavedState savedState = (SavedState)parcelable;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }
        
        Parcelable onSaveInstanceState() {
            final SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return (Parcelable)savedState;
        }
        
        void setMenu(final MenuBuilder menu) {
            if (menu != this.menu) {
                if (this.menu != null) {
                    this.menu.removeMenuPresenter(this.listMenuPresenter);
                }
                this.menu = menu;
                if (menu != null && this.listMenuPresenter != null) {
                    menu.addMenuPresenter(this.listMenuPresenter);
                }
            }
        }
        
        void setStyle(final Context context) {
            final TypedValue typedValue = new TypedValue();
            final Resources$Theme theme = context.getResources().newTheme();
            theme.setTo(context.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            final ContextThemeWrapper listPresenterContext = new ContextThemeWrapper(context, 0);
            ((Context)listPresenterContext).getTheme().setTo(theme);
            this.listPresenterContext = (Context)listPresenterContext;
            final TypedArray obtainStyledAttributes = ((Context)listPresenterContext).obtainStyledAttributes(R.styleable.Theme);
            this.background = obtainStyledAttributes.getResourceId(R.styleable.Theme_panelBackground, 0);
            this.windowAnimations = obtainStyledAttributes.getResourceId(R.styleable.Theme_android_windowAnimationStyle, 0);
            obtainStyledAttributes.recycle();
        }
        
        private static class SavedState implements Parcelable
        {
            public static final Parcelable$Creator<SavedState> CREATOR;
            int featureId;
            boolean isOpen;
            Bundle menuState;
            
            static {
                CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                    public SavedState createFromParcel(final Parcel parcel) {
                        return readFromParcel(parcel);
                    }
                    
                    public SavedState[] newArray(final int n) {
                        return new SavedState[n];
                    }
                };
            }
            
            private static SavedState readFromParcel(final Parcel parcel) {
                boolean isOpen = true;
                final SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                if (parcel.readInt() != 1) {
                    isOpen = false;
                }
                savedState.isOpen = isOpen;
                if (savedState.isOpen) {
                    savedState.menuState = parcel.readBundle();
                }
                return savedState;
            }
            
            public int describeContents() {
                return 0;
            }
            
            public void writeToParcel(final Parcel parcel, int n) {
                parcel.writeInt(this.featureId);
                if (this.isOpen) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                parcel.writeInt(n);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }
    }
    
    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, final boolean b) {
            final Object rootMenu = menuBuilder.getRootMenu();
            boolean b2;
            if (rootMenu != menuBuilder) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            final AppCompatDelegateImplV7 this$0 = AppCompatDelegateImplV7.this;
            if (b2) {
                menuBuilder = (MenuBuilder)rootMenu;
            }
            final PanelFeatureState access$600 = this$0.findMenuPanel((Menu)menuBuilder);
            if (access$600 != null) {
                if (!b2) {
                    AppCompatDelegateImplV7.this.closePanel(access$600, b);
                    return;
                }
                AppCompatDelegateImplV7.this.callOnPanelClosed(access$600.featureId, access$600, (Menu)rootMenu);
                AppCompatDelegateImplV7.this.closePanel(access$600, true);
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            if (menuBuilder == null && AppCompatDelegateImplV7.this.mHasActionBar) {
                final Window$Callback windowCallback = AppCompatDelegateImplV7.this.getWindowCallback();
                if (windowCallback != null && !AppCompatDelegateImplV7.this.isDestroyed()) {
                    windowCallback.onMenuOpened(8, (Menu)menuBuilder);
                }
            }
            return true;
        }
    }
}
