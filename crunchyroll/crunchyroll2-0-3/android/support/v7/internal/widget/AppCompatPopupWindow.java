// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.internal.widget;

import android.annotation.TargetApi;
import android.view.View;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import android.view.ViewTreeObserver$OnScrollChangedListener;
import android.os.Build$VERSION;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.PopupWindow;

public class AppCompatPopupWindow extends PopupWindow
{
    private static final String TAG = "AppCompatPopupWindow";
    private final boolean mOverlapAnchor;
    
    public AppCompatPopupWindow(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.PopupWindow, n, 0);
        this.mOverlapAnchor = obtainStyledAttributes.getBoolean(R.styleable.PopupWindow_overlapAnchor, false);
        this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(R.styleable.PopupWindow_android_popupBackground));
        obtainStyledAttributes.recycle();
        if (Build$VERSION.SDK_INT < 14) {
            wrapOnScrollChangedListener(this);
        }
    }
    
    private static void wrapOnScrollChangedListener(final PopupWindow popupWindow) {
        try {
            final Field declaredField = PopupWindow.class.getDeclaredField("mAnchor");
            declaredField.setAccessible(true);
            final Field declaredField2 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            declaredField2.setAccessible(true);
            declaredField2.set(popupWindow, new ViewTreeObserver$OnScrollChangedListener() {
                final /* synthetic */ ViewTreeObserver$OnScrollChangedListener val$originalListener = (ViewTreeObserver$OnScrollChangedListener)declaredField2.get(popupWindow);
                
                public void onScrollChanged() {
                    try {
                        final WeakReference weakReference = (WeakReference)declaredField.get(popupWindow);
                        if (weakReference != null) {
                            if (weakReference.get() == null) {
                                return;
                            }
                            this.val$originalListener.onScrollChanged();
                        }
                    }
                    catch (IllegalAccessException ex) {}
                }
            });
        }
        catch (Exception ex) {
            Log.d("AppCompatPopupWindow", "Exception while installing workaround OnScrollChangedListener", (Throwable)ex);
        }
    }
    
    public void showAsDropDown(final View view, final int n, final int n2) {
        int n3 = n2;
        if (Build$VERSION.SDK_INT < 21) {
            n3 = n2;
            if (this.mOverlapAnchor) {
                n3 = n2 - view.getHeight();
            }
        }
        super.showAsDropDown(view, n, n3);
    }
    
    @TargetApi(19)
    public void showAsDropDown(final View view, final int n, final int n2, final int n3) {
        int n4 = n2;
        if (Build$VERSION.SDK_INT < 21) {
            n4 = n2;
            if (this.mOverlapAnchor) {
                n4 = n2 - view.getHeight();
            }
        }
        super.showAsDropDown(view, n, n4, n3);
    }
    
    public void update(final View view, final int n, final int n2, final int n3, final int n4) {
        int n5 = n2;
        if (Build$VERSION.SDK_INT < 21) {
            n5 = n2;
            if (this.mOverlapAnchor) {
                n5 = n2 - view.getHeight();
            }
        }
        super.update(view, n, n5, n3, n4);
    }
}
