// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.content.Context;
import android.app.Dialog;

public class ai extends Dialog
{
    public ai(final Context context) {
        super(context);
    }
    
    public static ai a(final Context context) {
        final int r = ac.r();
        if (r < 5) {
            return new ai(context);
        }
        if (r < 7) {
            return new aj(context);
        }
        return new ak(context);
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyDown(n, keyEvent);
    }
}
