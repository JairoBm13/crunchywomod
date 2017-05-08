// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import com.tremorvideo.sdk.android.videoad.ac;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.WindowManager;
import android.content.Context;

public class f implements i
{
    Context a;
    
    public f(final Context a) {
        this.a = a;
    }
    
    @Override
    public int a(final int n, final int n2) {
        final Display defaultDisplay = ((WindowManager)this.a.getSystemService("window")).getDefaultDisplay();
        return (int)Math.min(3.0f, Math.max(1.0f, Math.min(n / defaultDisplay.getWidth(), n2 / defaultDisplay.getHeight())));
    }
    
    @Override
    public Bitmap a(final byte[] array, final int inSampleSize) {
        if (array == null) {
            return null;
        }
        if (ac.r() < 4) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
        }
        return new a().a(array);
    }
    
    class a
    {
        final /* synthetic */ int a;
        
        a(final int a) {
            this.a = a;
        }
        
        public Bitmap a(final byte[] array) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            bitmapFactory$Options.inDensity = 0;
            bitmapFactory$Options.inSampleSize = this.a;
            bitmapFactory$Options.inPurgeable = true;
            bitmapFactory$Options.inInputShareable = true;
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
            decodeByteArray.setDensity(0);
            return decodeByteArray;
        }
    }
}
