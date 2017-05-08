// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.apache.http.HttpEntity;
import java.io.OutputStream;
import org.apache.http.client.HttpClient;
import android.os.Build$VERSION;
import org.apache.http.HttpResponse;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.io.File;
import org.apache.http.client.methods.HttpUriRequest;
import com.tremorvideo.sdk.android.richmedia.ae;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import java.util.zip.CRC32;
import java.util.Map;
import java.util.zip.Checksum;
import android.content.Context;

public class bh extends bf
{
    Context a;
    String b;
    String c;
    long d;
    Checksum e;
    boolean f;
    boolean g;
    boolean h;
    boolean i;
    
    public bh(final bf.a a, final Context a2, final String s, final Map<String, Object> map) {
        super(a);
        if (map.containsKey("checkCache")) {
            this.f = map.get("checkCache");
        }
        else {
            this.f = true;
        }
        if (map.containsKey("mraid")) {
            this.i = map.get("mraid");
        }
        else {
            this.i = false;
        }
        this.a = a2;
        this.b = (String)map.get("url");
        this.c = a(this.b, (String)null);
        if (map.containsKey("crc")) {
            this.d = (Long)(Object)map.get("crc");
        }
        else {
            this.d = 0L;
        }
        this.e = new CRC32();
    }
    
    private long a(final String s) throws Exception {
        final BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 18000);
        HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 18000);
        ((HttpParams)basicHttpParams).setBooleanParameter("http.protocol.expect-continue", false);
        ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_0);
        final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
        final HttpGet httpGet = new HttpGet(s);
        ae.a(httpGet, s);
        return ((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity().getContentLength();
    }
    
    public static String a(final String s, final String s2) {
        if (s2 == null) {
            return ae.a(s) + "_" + s.substring(s.lastIndexOf(File.separatorChar) + 1);
        }
        return ae.a(s) + "_" + s.substring(s.lastIndexOf(File.separatorChar) + 1) + s2;
    }
    
    private void a(final boolean b) {
        boolean b2 = true;
        boolean b3 = false;
        boolean b4;
        while (true) {
            try {
                if (this.b != null) {
                    this.j();
                }
                b2 = false;
                b4 = false;
                if (this.h) {
                    this.a(bf.b.e);
                    return;
                }
            }
            catch (SocketTimeoutException ex) {
                ac.a("Timeout Downloading Asset: ", ex);
                b4 = false;
                b3 = true;
                b2 = false;
                continue;
            }
            catch (a a) {
                ac.a("An error occured while downloading an asset: ", a);
                b4 = false;
                continue;
            }
            catch (Exception ex2) {
                ac.a("An error occured while downloading an asset: ", ex2);
                b4 = true;
                b2 = false;
                continue;
            }
            break;
        }
        if (b3) {
            this.a(bf.b.f);
            return;
        }
        if (b2) {
            this.a(bf.b.d);
            return;
        }
        if (b4) {
            this.a(bf.b.c);
            return;
        }
        this.a(bf.b.b);
    }
    
    private String i() {
        return this.c;
    }
    
    private void j() throws Exception {
        final String s = null;
        Object entity = null;
    Label_0608_Outer:
        while (true) {
            try {
                final File h = this.h();
                long length;
                if (!h.exists()) {
                    length = 0L;
                }
                else {
                    if (!this.f) {
                        h.delete();
                    }
                    length = h.length();
                }
                final long a = this.a(this.b);
                if (a != length) {
                    ac.a(this.e, h);
                    final FileOutputStream openFileOutput = this.a.openFileOutput(h.getName(), 32769);
                    while (true) {
                        Label_0758: {
                            InputStream content = null;
                            Label_0417: {
                                Object o = null;
                                Label_0075: {
                                    if (length != 0L) {
                                        ac.e("Resuming download from: " + Math.round(length / 1024.0f) + "kB");
                                        break Label_0075;
                                    }
                                    try {
                                        ac.e("Starting download");
                                        final BasicHttpParams basicHttpParams = new BasicHttpParams();
                                        HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 18000);
                                        HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 18000);
                                        ((HttpParams)basicHttpParams).setBooleanParameter("http.protocol.expect-continue", false);
                                        ((HttpParams)basicHttpParams).setParameter("http.protocol.version", (Object)HttpVersion.HTTP_1_0);
                                        final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
                                        entity = new HttpGet(this.b);
                                        ae.a((HttpGet)entity, this.b);
                                        ((HttpGet)entity).addHeader("Range", "bytes=" + length + "-");
                                        entity = ((HttpClient)defaultHttpClient).execute((HttpUriRequest)entity).getEntity();
                                        if (a == length) {
                                            break Label_0758;
                                        }
                                        if (a < 1L) {
                                            h.delete();
                                            throw new a("Content length is invalid: " + a);
                                        }
                                        break Label_0417;
                                    }
                                    finally {
                                        o = null;
                                        entity = openFileOutput;
                                    }
                                }
                                if (o != null) {
                                    ((InputStream)o).close();
                                }
                                if (entity != null) {
                                    ((OutputStream)entity).close();
                                }
                                throw content;
                            }
                            if (a < length) {
                                this.e.reset();
                                h.delete();
                                throw new a("Cache size miss-match.");
                            }
                            if (((HttpResponse)content).getStatusLine().getStatusCode() / 100 != 2) {
                                throw new Exception("Connection response is invalid: " + ((HttpResponse)content).getStatusLine().getStatusCode());
                            }
                            final InputStream inputStream = content = ((HttpEntity)entity).getContent();
                            Label_0737: {
                                try {
                                    final byte[] array = new byte[1024];
                                    do {
                                        content = inputStream;
                                        final int read = inputStream.read(array);
                                        entity = inputStream;
                                        if (read > 0) {
                                            content = inputStream;
                                            this.e.update(array, 0, read);
                                            content = inputStream;
                                            openFileOutput.write(array, 0, read);
                                            length += read;
                                            content = inputStream;
                                        }
                                        else {
                                            content = (InputStream)entity;
                                            if (this.h) {
                                                break Label_0737;
                                            }
                                            String string = s;
                                            content = (InputStream)entity;
                                            if (!ac.a(this.e, h, this.d)) {
                                                content = (InputStream)entity;
                                                string = "CRC is invalid got: " + this.e.getValue() + ", expected: " + this.d;
                                            }
                                            if (string != null) {
                                                content = (InputStream)entity;
                                                h.delete();
                                                content = (InputStream)entity;
                                                throw new a(string);
                                            }
                                            break Label_0737;
                                        }
                                    } while (!this.g);
                                    content = inputStream;
                                    this.h = true;
                                    entity = inputStream;
                                    continue;
                                }
                                finally {
                                    entity = openFileOutput;
                                    final Object o = content;
                                    final HttpResponse httpResponse;
                                    content = (InputStream)httpResponse;
                                    continue Label_0608_Outer;
                                }
                            }
                            if (entity != null) {
                                ((InputStream)entity).close();
                            }
                            if (openFileOutput != null) {
                                openFileOutput.close();
                                return;
                            }
                            return;
                        }
                        entity = null;
                        continue;
                    }
                }
                if (!ac.a(this.e, h, this.d)) {
                    h.delete();
                    throw new a("CRC Failure");
                }
                ac.e("Asset is fully cached, no need to download again.");
                if (false) {
                    throw new NullPointerException();
                }
                if (false) {
                    throw new NullPointerException();
                }
            }
            finally {
                final Object o = null;
                continue;
            }
            break;
        }
    }
    
    @Override
    public void a(final String s, final n n) throws Exception {
        n.a(s, this.h().getAbsolutePath());
    }
    
    @Override
    protected void e() {
        if (this.i && Integer.parseInt(Build$VERSION.SDK) < 7) {
            this.a(bf.b.c);
            return;
        }
        this.g = false;
        this.a(this.h = false);
    }
    
    @Override
    protected void f() {
        this.g = true;
    }
    
    @Override
    protected void g() {
        this.g = false;
        this.h = false;
        this.a(true);
    }
    
    public File h() {
        return x.a(this.a.getFilesDir(), this.i());
    }
    
    @Override
    public String toString() {
        return "Download Asset: " + this.c;
    }
    
    private class a extends Exception
    {
        public a(final String s) {
            super(s);
        }
    }
}
