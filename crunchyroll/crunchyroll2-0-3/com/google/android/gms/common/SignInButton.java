// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common;

import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.dynamic.zzg;
import android.util.Log;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.internal.zzy;
import android.widget.Button;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.FrameLayout;

public final class SignInButton extends FrameLayout implements View$OnClickListener
{
    private int mColor;
    private int mSize;
    private View zzVZ;
    private View$OnClickListener zzWa;
    
    public SignInButton(final Context context) {
        this(context, null);
    }
    
    public SignInButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SignInButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.zzWa = null;
        this.setStyle(0, 0);
    }
    
    private static Button zza(final Context context, final int n, final int n2) {
        final zzy zzy = new zzy(context);
        zzy.zza(context.getResources(), n, n2);
        return zzy;
    }
    
    private void zzaf(final Context context) {
        if (this.zzVZ != null) {
            this.removeView(this.zzVZ);
        }
        while (true) {
            try {
                this.zzVZ = zzx.zzb(context, this.mSize, this.mColor);
                this.addView(this.zzVZ);
                this.zzVZ.setEnabled(this.isEnabled());
                this.zzVZ.setOnClickListener((View$OnClickListener)this);
            }
            catch (zzg.zza zza) {
                Log.w("SignInButton", "Sign in button not found, using placeholder instead");
                this.zzVZ = (View)zza(context, this.mSize, this.mColor);
                continue;
            }
            break;
        }
    }
    
    public void onClick(final View view) {
        if (this.zzWa != null && view == this.zzVZ) {
            this.zzWa.onClick((View)this);
        }
    }
    
    public void setColorScheme(final int n) {
        this.setStyle(this.mSize, n);
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.zzVZ.setEnabled(b);
    }
    
    public void setOnClickListener(final View$OnClickListener zzWa) {
        this.zzWa = zzWa;
        if (this.zzVZ != null) {
            this.zzVZ.setOnClickListener((View$OnClickListener)this);
        }
    }
    
    public void setSize(final int n) {
        this.setStyle(n, this.mColor);
    }
    
    public void setStyle(final int mSize, final int mColor) {
        zzu.zza(mSize >= 0 && mSize < 3, "Unknown button size %d", mSize);
        zzu.zza(mColor >= 0 && mColor < 2, "Unknown color scheme %s", mColor);
        this.mSize = mSize;
        this.mColor = mColor;
        this.zzaf(this.getContext());
    }
}
