// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.internal;

import android.support.v7.internal.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.view.menu.MenuView;
import android.widget.ListView;

public class NavigationMenuView extends ListView implements MenuView
{
    public NavigationMenuView(final Context context) {
        this(context, null);
    }
    
    public NavigationMenuView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NavigationMenuView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public int getWindowAnimations() {
        return 0;
    }
    
    public void initialize(final MenuBuilder menuBuilder) {
    }
}
