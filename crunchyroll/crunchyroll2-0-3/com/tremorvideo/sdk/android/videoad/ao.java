// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.content.Context;

public class ao extends an
{
    protected ao(final Context context) {
        super(context);
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyLongPress(n, keyEvent);
    }
}
