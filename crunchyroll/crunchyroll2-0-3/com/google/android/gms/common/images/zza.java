// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.images;

import java.lang.ref.WeakReference;
import com.google.android.gms.common.internal.zzt;
import android.net.Uri;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.common.internal.zzb;
import android.graphics.Bitmap;
import com.google.android.gms.internal.zzkh;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.google.android.gms.internal.zzkj;
import android.content.Context;

public abstract class zza
{
    final zza zzZc;
    protected int zzZe;
    protected ImageManager.OnImageLoadedListener zzZg;
    protected int zzZk;
    
    private Drawable zza(final Context context, final zzkj zzkj, final int n) {
        final Resources resources = context.getResources();
        if (this.zzZk > 0) {
            final zzkj.zza zza = new zzkj.zza(n, this.zzZk);
            Drawable drawable;
            if ((drawable = zzkj.get(zza)) == null) {
                final Drawable drawable2 = drawable = resources.getDrawable(n);
                if ((this.zzZk & 0x1) != 0x0) {
                    drawable = this.zza(resources, drawable2);
                }
                zzkj.put(zza, drawable);
            }
            return drawable;
        }
        return resources.getDrawable(n);
    }
    
    protected Drawable zza(final Resources resources, final Drawable drawable) {
        return zzkh.zza(resources, drawable);
    }
    
    void zza(final Context context, final Bitmap bitmap, final boolean b) {
        zzb.zzq(bitmap);
        Bitmap zza = bitmap;
        if ((this.zzZk & 0x1) != 0x0) {
            zza = zzkh.zza(bitmap);
        }
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), zza);
        if (this.zzZg != null) {
            this.zzZg.onImageLoaded(this.zzZc.uri, (Drawable)bitmapDrawable, true);
        }
        this.zza((Drawable)bitmapDrawable, b, false, true);
    }
    
    void zza(final Context context, final zzkj zzkj, final boolean b) {
        Drawable zza = null;
        if (this.zzZe != 0) {
            zza = this.zza(context, zzkj, this.zzZe);
        }
        if (this.zzZg != null) {
            this.zzZg.onImageLoaded(this.zzZc.uri, zza, false);
        }
        this.zza(zza, b, false, false);
    }
    
    protected abstract void zza(final Drawable p0, final boolean p1, final boolean p2, final boolean p3);
    
    static final class zza
    {
        public final Uri uri;
        
        public zza(final Uri uri) {
            this.uri = uri;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof zza && (this == o || zzt.equal(((zza)o).uri, this.uri));
        }
        
        @Override
        public int hashCode() {
            return zzt.hashCode(this.uri);
        }
    }
    
    public static final class zzc extends zza
    {
        private WeakReference<ImageManager.OnImageLoadedListener> zzZm;
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof zzc)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            final zzc zzc = (zzc)o;
            final ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzZm.get();
            final ImageManager.OnImageLoadedListener onImageLoadedListener2 = zzc.zzZm.get();
            return onImageLoadedListener2 != null && onImageLoadedListener != null && zzt.equal(onImageLoadedListener2, onImageLoadedListener) && zzt.equal(zzc.zzZc, this.zzZc);
        }
        
        @Override
        public int hashCode() {
            return zzt.hashCode(this.zzZc);
        }
        
        @Override
        protected void zza(final Drawable drawable, final boolean b, final boolean b2, final boolean b3) {
            if (!b2) {
                final ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzZm.get();
                if (onImageLoadedListener != null) {
                    onImageLoadedListener.onImageLoaded(this.zzZc.uri, drawable, b3);
                }
            }
        }
    }
}
