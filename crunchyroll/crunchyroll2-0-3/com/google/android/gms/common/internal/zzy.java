// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.R;
import android.graphics.Typeface;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Button;

public final class zzy extends Button
{
    public zzy(final Context context) {
        this(context, null);
    }
    
    public zzy(final Context context, final AttributeSet set) {
        super(context, set, 16842824);
    }
    
    private void zza(final Resources resources) {
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setTextSize(14.0f);
        final float density = resources.getDisplayMetrics().density;
        this.setMinHeight((int)(density * 48.0f + 0.5f));
        this.setMinWidth((int)(density * 48.0f + 0.5f));
    }
    
    private int zzb(final int n, int n2, final int n3) {
        switch (n) {
            default: {
                throw new IllegalStateException("Unknown color scheme: " + n);
            }
            case 1: {
                n2 = n3;
            }
            case 0: {
                return n2;
            }
        }
    }
    
    private void zzb(final Resources resources, int n, final int n2) {
        switch (n) {
            default: {
                throw new IllegalStateException("Unknown button size: " + n);
            }
            case 0:
            case 1: {
                n = this.zzb(n2, R.drawable.common_signin_btn_text_dark, R.drawable.common_signin_btn_text_light);
                break;
            }
            case 2: {
                n = this.zzb(n2, R.drawable.common_signin_btn_icon_dark, R.drawable.common_signin_btn_icon_light);
                break;
            }
        }
        if (n == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        this.setBackgroundDrawable(resources.getDrawable(n));
    }
    
    private void zzc(final Resources resources, final int n, final int n2) {
        this.setTextColor(resources.getColorStateList(this.zzb(n2, R.color.common_signin_btn_text_dark, R.color.common_signin_btn_text_light)));
        switch (n) {
            default: {
                throw new IllegalStateException("Unknown button size: " + n);
            }
            case 0: {
                this.setText((CharSequence)resources.getString(R.string.common_signin_button_text));
            }
            case 1: {
                this.setText((CharSequence)resources.getString(R.string.common_signin_button_text_long));
            }
            case 2: {
                this.setText((CharSequence)null);
            }
        }
    }
    
    public void zza(final Resources resources, final int n, final int n2) {
        zzu.zza(n >= 0 && n < 3, "Unknown button size %d", n);
        zzu.zza(n2 >= 0 && n2 < 2, "Unknown color scheme %s", n2);
        this.zza(resources);
        this.zzb(resources, n, n2);
        this.zzc(resources, n, n2);
    }
}
