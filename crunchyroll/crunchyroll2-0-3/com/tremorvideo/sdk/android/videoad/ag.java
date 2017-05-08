// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.os.Build$VERSION;
import android.app.DatePickerDialog$OnDateSetListener;
import android.content.Context;
import android.app.DatePickerDialog;

public class ag extends DatePickerDialog
{
    public ag(final Context context, final DatePickerDialog$OnDateSetListener datePickerDialog$OnDateSetListener, final int n, final int n2, final int n3) {
        super(context, datePickerDialog$OnDateSetListener, n, n2, n3);
    }
    
    public static DatePickerDialog a(final Context context, final DatePickerDialog$OnDateSetListener datePickerDialog$OnDateSetListener, final int n, final int n2, final int n3) {
        if (Integer.parseInt(Build$VERSION.SDK) < 5) {
            return new ag(context, datePickerDialog$OnDateSetListener, n, n2, n3);
        }
        return new ah(context, datePickerDialog$OnDateSetListener, n, n2, n3);
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyDown(n, keyEvent);
    }
}
