// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.MenuItem;

public class MenuCompat
{
    @Deprecated
    public static void setShowAsAction(final MenuItem menuItem, final int n) {
        MenuItemCompat.setShowAsAction(menuItem, n);
    }
}
