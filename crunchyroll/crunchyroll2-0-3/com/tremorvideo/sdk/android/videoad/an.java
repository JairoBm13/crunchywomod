// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.os.Build$VERSION;
import android.content.Context;
import android.app.ProgressDialog;

public class an extends ProgressDialog
{
    protected an(final Context context) {
        super(context);
        this.setCancelable(false);
    }
    
    public static an a(final Context context) {
        if (Integer.parseInt(Build$VERSION.SDK) < 5) {
            return new an(context);
        }
        return new ao(context);
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyDown(n, keyEvent);
    }
}
