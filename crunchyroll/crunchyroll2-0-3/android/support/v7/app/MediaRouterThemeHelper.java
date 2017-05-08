// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.support.v7.mediarouter.R;
import android.content.Context;

final class MediaRouterThemeHelper
{
    public static Context createThemedContext(final Context context) {
        int n;
        if (isLightTheme(context)) {
            n = R.style.Theme_MediaRouter_Light;
        }
        else {
            n = R.style.Theme_MediaRouter;
        }
        return (Context)new ContextThemeWrapper(context, n);
    }
    
    public static Drawable getThemeDrawable(final Context context, int themeResource) {
        themeResource = getThemeResource(context, themeResource);
        if (themeResource != 0) {
            return context.getResources().getDrawable(themeResource);
        }
        return null;
    }
    
    public static int getThemeResource(final Context context, final int n) {
        final TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(n, typedValue, true)) {
            return typedValue.resourceId;
        }
        return 0;
    }
    
    private static boolean isLightTheme(final Context context) {
        final TypedValue typedValue = new TypedValue();
        return context.getTheme().resolveAttribute(R.attr.isLightTheme, typedValue, true) && typedValue.data != 0;
    }
}
