// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.Nullable;
import android.view.View;
import android.support.annotation.IdRes;

interface FragmentContainer
{
    @Nullable
    View findViewById(@IdRes final int p0);
    
    boolean hasView();
}
