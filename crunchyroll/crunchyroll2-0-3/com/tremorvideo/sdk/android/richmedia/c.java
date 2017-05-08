// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.util.Iterator;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.ArrayList;
import java.io.InputStream;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipFile;
import android.graphics.Bitmap;
import java.util.List;

public class c
{
    public List<Bitmap> a;
    private int b;
    
    private Bitmap a(final ZipFile zipFile, final i i, final String s) {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            final InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(s));
            byteArrayOutputStream = new ByteArrayOutputStream();
            final byte[] array = new byte[16384];
            while (true) {
                final int read = inputStream.read(array, 0, array.length);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
        }
        catch (Exception ex) {
            Log.v("", "", (Throwable)ex);
            return null;
        }
        return i.a(byteArrayOutputStream.toByteArray(), this.b);
    }
    
    public int a() {
        return this.b;
    }
    
    public Bitmap a(final int n) {
        return this.a.get(n);
    }
    
    public void a(final ZipFile zipFile, final i i, int j, final e e) {
        try {
            this.b = j;
            final int b = e.b();
            this.a = new ArrayList<Bitmap>(b);
            int b2;
            StringBuilder sb;
            int k;
            for (j = 0; j < b; ++j) {
                b2 = e.b();
                sb = new StringBuilder(b2);
                for (k = 0; k < b2; ++k) {
                    sb.append((char)e.b());
                }
                this.a.add(this.a(zipFile, i, sb.toString()));
                ac.b();
            }
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public void b() {
        for (final Bitmap bitmap : this.a) {
            if (ac.r() <= 10) {
                bitmap.recycle();
            }
        }
        this.a.clear();
    }
}
