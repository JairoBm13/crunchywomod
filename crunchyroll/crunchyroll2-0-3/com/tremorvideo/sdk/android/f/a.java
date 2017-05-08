// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import org.apache.http.client.HttpClient;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import com.tremorvideo.sdk.android.videoad.ac;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.http.impl.client.DefaultHttpClient;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.io.File;
import android.os.Environment;
import android.content.Context;
import com.tremorvideo.sdk.android.videoad.ax;

public class a
{
    public String a;
    private ax b;
    private Context c;
    private boolean d;
    private boolean e;
    
    public a(final Context c) {
        this.d = false;
        this.e = false;
        this.c = c;
        if (this.c.getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", this.c.getApplicationContext().getPackageName()) == 0) {
            this.e = true;
        }
        else {
            this.e = false;
        }
        this.a = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tremor/mraid/";
        final File file = new File(this.a);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    private void a(final File file) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                this.a(listFiles[i]);
            }
        }
        else {
            file.delete();
        }
    }
    
    public String a(String s) {
        if (s.contains(".zip")) {
            return null;
        }
        if (s != null && s.length() > 0) {
            final String string = ae.a(s) + ".html";
            while (true) {
                Label_0257: {
                    if (!"mounted".equals(Environment.getExternalStorageState())) {
                        break Label_0257;
                    }
                    this.d = true;
                    final File file = new File(this.a + string);
                    if (file.exists()) {
                        file.delete();
                    }
                    final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                    ByteArrayOutputStream byteArrayOutputStream = null;
                    FileOutputStream fileOutputStream = null;
                    Label_0267: {
                        Label_0265: {
                            try {
                                byteArrayOutputStream = new ByteArrayOutputStream();
                                fileOutputStream = new FileOutputStream(file);
                                final HttpGet httpGet = new HttpGet(s);
                                ae.a(httpGet, s);
                                final HttpEntity entity = ((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity();
                                if (entity == null) {
                                    break Label_0265;
                                }
                                final InputStream content = entity.getContent();
                                final byte[] array = new byte[4096];
                                while (true) {
                                    final int read = content.read(array);
                                    s = string;
                                    if (read == -1) {
                                        break Label_0267;
                                    }
                                    byteArrayOutputStream.write(array, 0, read);
                                    byteArrayOutputStream.toByteArray();
                                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                                    byteArrayOutputStream.reset();
                                }
                            }
                            catch (Exception ex) {
                                ac.e("TremorLog_error::MRAID::Tag download " + ex);
                                return null;
                            }
                            break Label_0257;
                        }
                        s = null;
                    }
                    fileOutputStream.close();
                    byteArrayOutputStream.close();
                    return s;
                }
                this.d = false;
                continue;
            }
        }
        s = null;
        return s;
    }
    
    public void a() {
        this.a(new File(this.a));
    }
    
    public void a(final ax b) {
        this.b = b;
    }
    
    public boolean b() {
        return this.d && this.e;
    }
}
