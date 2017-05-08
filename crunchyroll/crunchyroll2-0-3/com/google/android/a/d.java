// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.a;

import com.google.a.a.a;
import java.security.MessageDigest;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public abstract class d implements c
{
    protected MotionEvent a;
    protected DisplayMetrics b;
    protected i c;
    private j d;
    
    protected d(final Context context, final i c, final j d) {
        this.c = c;
        this.d = d;
        try {
            this.b = context.getResources().getDisplayMetrics();
        }
        catch (UnsupportedOperationException ex) {
            this.b = new DisplayMetrics();
            this.b.density = 1.0f;
        }
    }
    
    private String a(final Context context, final String s, final boolean b) {
        try {
            synchronized (this) {
                this.a();
                if (b) {
                    this.c(context);
                }
                else {
                    this.b(context);
                }
                final byte[] b2 = this.b();
                // monitorexit(this)
                if (b2.length == 0) {
                    return Integer.toString(5);
                }
                goto Label_0054;
            }
        }
        catch (NoSuchAlgorithmException ex) {
            return Integer.toString(7);
        }
        catch (UnsupportedEncodingException ex2) {
            return Integer.toString(7);
        }
        catch (IOException ex3) {
            return Integer.toString(3);
        }
    }
    
    private void a() {
        this.d.a();
    }
    
    private byte[] b() throws IOException {
        return this.d.b();
    }
    
    @Override
    public String a(final Context context) {
        return this.a(context, null, false);
    }
    
    @Override
    public String a(final Context context, final String s) {
        return this.a(context, s, true);
    }
    
    String a(byte[] array, final String s) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        byte[] b = array;
        if (array.length > 239) {
            this.a();
            this.a(20, 1L);
            b = this.b();
        }
        if (b.length < 239) {
            array = new byte[239 - b.length];
            new SecureRandom().nextBytes(array);
            array = ByteBuffer.allocate(240).put((byte)b.length).put(b).put(array).array();
        }
        else {
            array = ByteBuffer.allocate(240).put((byte)b.length).put(b).array();
        }
        final MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(array);
        array = ByteBuffer.allocate(256).put(instance.digest()).put(array).array();
        final byte[] array2 = new byte[256];
        new b().a(array, array2);
        if (s != null && s.length() > 0) {
            this.a(s, array2);
        }
        return this.c.a(array2, true);
    }
    
    protected void a(final int n, final long n2) throws IOException {
        this.d.a(n, n2);
    }
    
    protected void a(final int n, final String s) throws IOException {
        this.d.a(n, s);
    }
    
    void a(final String s, final byte[] array) throws UnsupportedEncodingException {
        String substring = s;
        if (s.length() > 32) {
            substring = s.substring(0, 32);
        }
        new a(substring.getBytes("UTF-8")).a(array);
    }
    
    protected abstract void b(final Context p0);
    
    protected abstract void c(final Context p0);
}
