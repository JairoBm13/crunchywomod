// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.io.OutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.SocketTimeoutException;
import com.tremorvideo.sdk.android.logger.TestAppLogger;
import java.io.File;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.zip.CRC32;
import java.util.Map;
import java.util.zip.Checksum;
import android.content.Context;

public class bl extends bf
{
    Context a;
    n b;
    boolean c;
    boolean d;
    int e;
    Checksum f;
    
    public bl(final bf.a a, final Context a2, final Map<String, Object> map) {
        super(a);
        this.e = 0;
        this.a = a2;
        this.b = map.get("ad");
        this.e = (int)map.get("index");
        this.f = new CRC32();
    }
    
    private long a(String c) throws Exception {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(c).openConnection();
        httpURLConnection.setRequestMethod("HEAD");
        c = ae.c(c);
        if (c != null) {
            httpURLConnection.setRequestProperty("User-Agent", c);
        }
        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        httpURLConnection.connect();
        return httpURLConnection.getContentLength();
    }
    
    private void a(final File file) throws Exception {
        Label_0358: {
            Object o2 = null;
            String s2 = null;
            Object o4 = null;
        Label_0326:
            while (true) {
                final String s = null;
                Object o = null;
                o2 = null;
                s2 = null;
                Object o3 = o2;
            Label_0452_Outer:
                while (true) {
                    Label_1190: {
                        int b = 0;
                        long n = 0L;
                        Label_0382: {
                            try {
                                b = this.b.b(this.e);
                                o3 = o2;
                                n = file.length();
                                if (b != 0) {
                                    break Label_1190;
                                }
                                o3 = o2;
                                b = (int)this.a(this.b.a(this.e));
                                if (n != b) {
                                    break Label_0382;
                                }
                                o3 = o2;
                                if (ac.a(this.f, file, this.b.f(this.e))) {
                                    o3 = o2;
                                    ac.e("Video is fully cached.");
                                    o3 = o2;
                                    while (true) {
                                        try {
                                            if (ac.r) {
                                                if (b > 3145728) {
                                                    o3 = o2;
                                                    TestAppLogger.getInstance().logMediaDownloaded("file_size=" + b + " >3MB", "file_name=" + this.b.a(this.e), "fail");
                                                }
                                                else {
                                                    o3 = o2;
                                                    TestAppLogger.getInstance().logMediaDownloaded("file_size=" + b, "file_name=" + this.b.a(this.e), "pass");
                                                }
                                            }
                                            if (false) {
                                                throw new NullPointerException();
                                            }
                                            if (false) {
                                                throw new NullPointerException();
                                            }
                                            if (false) {
                                                throw new NullPointerException();
                                            }
                                            return;
                                        }
                                        catch (Exception ex) {
                                            o3 = o2;
                                            ac.e("Error logMediaDownloaded" + ex);
                                            continue Label_0452_Outer;
                                        }
                                        continue Label_0452_Outer;
                                    }
                                }
                                break Label_0358;
                            }
                            finally {
                                o2 = o;
                                o4 = o3;
                            }
                            break;
                        }
                    Label_1074_Outer:
                        while (true) {
                            Label_0861: {
                                if (n <= 0L) {
                                    break Label_0861;
                                }
                                ac.e("Resuming download from: " + n + " bytes...");
                                ac.a(this.f, file);
                                o2 = this.a.openFileOutput(file.getName(), 32769);
                            Label_1074:
                                while (true) {
                                    URL url = null;
                                Label_0998:
                                    while (true) {
                                        final String a = this.b.a(this.e);
                                        url = new URL(a);
                                        o4 = url.openConnection();
                                        o3 = s;
                                        while (true) {
                                            long f = 0L;
                                            Label_0892: {
                                                try {
                                                    ((URLConnection)o4).setReadTimeout(18000);
                                                    o3 = s;
                                                    ((URLConnection)o4).setConnectTimeout(18000);
                                                    o3 = s;
                                                    ((HttpURLConnection)o4).setRequestMethod("GET");
                                                    o3 = s;
                                                    final String c = ae.c(a);
                                                    if (c != null) {
                                                        o3 = s;
                                                        ((URLConnection)o4).setRequestProperty("User-Agent", c);
                                                    }
                                                    o3 = s;
                                                    ((URLConnection)o4).setRequestProperty("Range", "bytes=" + n + "-");
                                                    o3 = s;
                                                    o = (o3 = ((URLConnection)o4).getInputStream());
                                                    final byte[] array = new byte[1024];
                                                    while (true) {
                                                        do {
                                                            o3 = o;
                                                            final int read = ((InputStream)o).read(array);
                                                            if (read > 0) {
                                                                o3 = o;
                                                                this.f.update(array, 0, read);
                                                                o3 = o;
                                                                ((OutputStream)o2).write(array, 0, read);
                                                                n += read;
                                                                o3 = o;
                                                            }
                                                            else {
                                                                o3 = o;
                                                                if (this.d) {
                                                                    break Label_1074;
                                                                }
                                                                o3 = o;
                                                                n = file.length();
                                                                final String s3 = null;
                                                                o3 = o;
                                                                f = this.b.f(this.e);
                                                                if (f != 0L) {
                                                                    break Label_0892;
                                                                }
                                                                o3 = o;
                                                                ac.e("No CRC to verify against.");
                                                                o3 = s3;
                                                                Object string = o3;
                                                                if (n != b) {
                                                                    o3 = o;
                                                                    string = "Incomplete Download. Recieved: " + n + ". Expected: " + this.b.b(this.e) + ".";
                                                                }
                                                                if (string != null) {
                                                                    o3 = o;
                                                                    file.delete();
                                                                    o3 = o;
                                                                    throw new a((String)string);
                                                                }
                                                                break Label_0998;
                                                            }
                                                        } while (!this.c);
                                                        o3 = o;
                                                        this.d = true;
                                                        continue Label_1074_Outer;
                                                    }
                                                }
                                                finally {
                                                    s2 = (String)o4;
                                                    o4 = o2;
                                                    o2 = o3;
                                                    break Label_0326;
                                                }
                                                break Label_0861;
                                            }
                                            if (this.f.getValue() != f) {
                                                o3 = "CRC is invalid got: " + this.f.getValue() + ", expected: " + f;
                                                continue;
                                            }
                                            ac.e("CRC is GOOD: " + f);
                                            o3 = s2;
                                            continue;
                                        }
                                    }
                                    o3 = o;
                                    try {
                                        if (ac.r) {
                                            if (n > 3145728L) {
                                                o3 = o;
                                                TestAppLogger.getInstance().logMediaDownloaded("file_size=" + n + " >3MB", "file_name=" + url, "fail");
                                            }
                                            else {
                                                o3 = o;
                                                TestAppLogger.getInstance().logMediaDownloaded("file_size=" + n, "file_name=" + url, "pass");
                                            }
                                        }
                                        if (o != null) {
                                            ((InputStream)o).close();
                                        }
                                        if (o4 != null) {
                                            ((HttpURLConnection)o4).disconnect();
                                        }
                                        if (o2 != null) {
                                            ((OutputStream)o2).close();
                                        }
                                        return;
                                    }
                                    catch (Exception ex2) {
                                        o3 = o;
                                        ac.e("Error logMediaDownloaded" + ex2);
                                        continue Label_1074;
                                    }
                                    continue Label_1074;
                                }
                            }
                            ac.e("Begining Download...");
                            o2 = this.a.openFileOutput(file.getName(), 1);
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (o2 != null) {
                ((InputStream)o2).close();
            }
            if (s2 != null) {
                ((HttpURLConnection)s2).disconnect();
            }
            if (o4 != null) {
                ((OutputStream)o4).close();
            }
            throw file;
        }
        file.delete();
        throw new a("CRC Failure");
    }
    
    private void a(final boolean b) {
        boolean b2 = true;
        boolean b3 = false;
        boolean b4;
        while (true) {
            try {
                this.a(x.a(this.a.getFilesDir(), this.b.e(this.e)));
                b2 = false;
                b4 = false;
                if (this.d || this.c) {
                    this.a(bf.b.e);
                    return;
                }
            }
            catch (SocketTimeoutException ex) {
                ac.a("Timeout occured while downloading a video: ", ex);
                try {
                    if (ac.r) {
                        TestAppLogger.getInstance().logMediaDownloaded("SocketTimeoutException", "file_name=" + this.b.a(this.e), "fail");
                    }
                    b4 = false;
                    b3 = true;
                    b2 = false;
                }
                catch (Exception ex2) {
                    ac.e("Error logMediaDownloaded " + ex2);
                }
            }
            catch (a a) {
                ac.a("An error occured while downloading a video: ", a);
                try {
                    if (ac.r) {
                        TestAppLogger.getInstance().logMediaDownloaded("InvalidDownloadException", "file_name=" + this.b.a(this.e) + ", Exception=" + a.getMessage(), "fail");
                    }
                    b4 = false;
                }
                catch (Exception ex3) {
                    ac.e("Error logMediaDownloaded " + ex3);
                }
            }
            catch (Exception ex4) {
                ac.a("An error occured while downloading a video: ", ex4);
                try {
                    if (ac.r) {
                        TestAppLogger.getInstance().logMediaDownloaded("Exception", "file_name=" + this.b.a(this.e) + ", Exception=" + ex4.getMessage(), "fail");
                    }
                    b4 = true;
                    b2 = false;
                }
                catch (Exception ex5) {
                    ac.e("Error logMediaDownloaded " + ex5);
                    b4 = true;
                    b2 = false;
                }
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
    
    @Override
    protected void e() {
        this.c = false;
        this.a(this.d = false);
    }
    
    @Override
    protected void f() {
        this.c = true;
    }
    
    @Override
    protected void g() {
        this.c = false;
        this.d = false;
        this.a(true);
    }
    
    @Override
    public String toString() {
        return "Download Video (" + this.e + "): " + this.b.e(this.e);
    }
    
    private class a extends Exception
    {
        public a(final String s) {
            super(s);
        }
    }
}
