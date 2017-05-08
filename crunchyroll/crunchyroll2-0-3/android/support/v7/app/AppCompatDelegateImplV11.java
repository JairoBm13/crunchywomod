// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.LayoutInflater$Factory2;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.content.Context;

class AppCompatDelegateImplV11 extends AppCompatDelegateImplV7
{
    AppCompatDelegateImplV11(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }
    
    @Override
    View callActivityOnCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        final View callActivityOnCreateView = super.callActivityOnCreateView(view, s, context, set);
        if (callActivityOnCreateView != null) {
            return callActivityOnCreateView;
        }
        if (this.mOriginalWindowCallback instanceof LayoutInflater$Factory2) {
            return ((LayoutInflater$Factory2)this.mOriginalWindowCallback).onCreateView(view, s, context, set);
        }
        return null;
    }
}
