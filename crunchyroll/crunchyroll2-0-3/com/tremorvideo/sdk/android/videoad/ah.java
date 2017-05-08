// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.app.DatePickerDialog$OnDateSetListener;
import android.content.Context;
import android.app.DatePickerDialog;

public class ah extends DatePickerDialog
{
    public ah(final Context context, final DatePickerDialog$OnDateSetListener datePickerDialog$OnDateSetListener, final int n, final int n2, final int n3) {
        super(context, datePickerDialog$OnDateSetListener, n, n2, n3);
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyLongPress(n, keyEvent);
    }
}
