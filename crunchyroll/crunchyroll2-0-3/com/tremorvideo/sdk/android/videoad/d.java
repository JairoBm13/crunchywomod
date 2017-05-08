// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.app.Dialog;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.TableLayout$LayoutParams;
import android.content.Context;
import android.view.View;
import android.app.Activity;

public class d extends a
{
    public d(final a a, final Activity activity, final n n) {
        super(a, activity);
        activity.addContentView(new View((Context)activity), (ViewGroup$LayoutParams)new TableLayout$LayoutParams(-1, -1));
        ac.a(this.c, this.d.i());
        final Dialog a2 = be.a((Context)activity, n.q(), n.u(), this.d);
        a2.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                d.this.d.a(d.this);
            }
        });
        a2.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                d.this.d.a(d.this);
            }
        });
        final aw a3 = n.u().a(aw.b.P);
        if (a3 != null) {
            this.d.a(a3);
        }
        a2.show();
    }
    
    @Override
    public void a() {
    }
    
    @Override
    public void d() {
        super.d();
    }
    
    @Override
    public b n() {
        return b.c;
    }
}
