// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.a;

import android.view.KeyEvent;
import android.content.Context;

public class e extends d
{
    protected e(final Context context, final String s, final b.a a) {
        super(context, s, a);
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyLongPress(n, keyEvent);
    }
}
