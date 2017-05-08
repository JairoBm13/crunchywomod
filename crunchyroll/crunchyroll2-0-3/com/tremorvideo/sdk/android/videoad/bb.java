// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import android.os.Looper;
import java.net.URL;
import java.net.HttpURLConnection;
import android.os.Handler;

public class bb
{
    private String a;
    private String b;
    private String c;
    private a d;
    private String e;
    private int f;
    private boolean g;
    private Handler h;
    private b i;
    
    private bb(final String a, final String b, final String c, final a d, final int f) {
        this.g = false;
        this.h = null;
        this.i = null;
        this.a = a;
        this.b = b;
        this.c = c;
        this.e = null;
        this.d = d;
        this.f = f;
    }
    
    public static bb a(final String s, final String s2) {
        return new bb(s, s2, null, a.b, -1);
    }
    
    public static bb a(final String s, final String s2, final String s3, final int n) {
        return new bb(s, s2, s3, a.a, n);
    }
    
    public void a() throws UnsupportedEncodingException, IOException {
    Label_0192_Outer:
        while (true) {
            StringBuilder sb = null;
        Label_0328:
            while (true) {
                byte[] array = null;
                int read = 0;
            Label_0309:
                while (true) {
                    HttpURLConnection httpURLConnection = null;
                    Label_0248: {
                        try {
                            this.g = false;
                            httpURLConnection = (HttpURLConnection)new URL(this.a).openConnection();
                            httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=ISO-8859-1");
                            httpURLConnection.setDoInput(true);
                            this.h = new Handler(Looper.getMainLooper());
                            this.i = new b();
                            if (this.f > 0) {
                                final int f = this.f;
                            }
                            httpURLConnection.setReadTimeout(20000);
                            httpURLConnection.setConnectTimeout(20000);
                            this.h.postDelayed((Runnable)this.i, (long)20500);
                            if (this.b != null) {
                                httpURLConnection.setRequestProperty("User-Agent", this.b);
                            }
                            if (this.d != bb.a.b) {
                                break Label_0248;
                            }
                            httpURLConnection.setRequestMethod("GET");
                            final int responseCode = httpURLConnection.getResponseCode();
                            if (responseCode >= 200 && responseCode < 300) {
                                final BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                                sb = new StringBuilder(Math.max(0, httpURLConnection.getContentLength()));
                                array = new byte[1024];
                                read = bufferedInputStream.read(array);
                                if (read <= 0) {
                                    break Label_0328;
                                }
                                if (this.g) {
                                    throw new Exception("Timed Out");
                                }
                                break Label_0309;
                            }
                        }
                        catch (Exception ex) {
                            if (this.h != null && this.i != null) {
                                this.h.removeCallbacks((Runnable)this.i);
                            }
                        }
                        return;
                    }
                    if (this.d == bb.a.a) {
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                        bufferedWriter.write(this.c);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        continue Label_0192_Outer;
                    }
                    continue Label_0192_Outer;
                }
                sb.append(new String(array, 0, read));
                continue;
            }
            if (this.h != null && this.i != null) {
                this.h.removeCallbacks((Runnable)this.i);
            }
            this.e = sb.toString();
            z.b();
        }
    }
    
    public String b() {
        return this.e;
    }
    
    private enum a
    {
        a, 
        b;
    }
    
    private class b implements Runnable
    {
        @Override
        public void run() {
            bb.this.g = true;
        }
    }
}
