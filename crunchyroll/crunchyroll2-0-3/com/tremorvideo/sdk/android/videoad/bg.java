// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import com.tremorvideo.sdk.android.richmedia.ae;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import android.content.Context;

public class bg extends bf
{
    boolean a;
    Context b;
    n c;
    private File d;
    
    public bg(final a a, final Context b, final n c) {
        super(a);
        this.a = false;
        this.b = b;
        this.c = c;
    }
    
    private void a(final String s) {
        FileInputStream fileInputStream = null;
        ZipInputStream zipInputStream = null;
    Label_0265:
        while (true) {
            while (true) {
                String name;
                try {
                    fileInputStream = new FileInputStream(s + "tempAdChoice.zip");
                    zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
                    while (true) {
                        final ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry == null) {
                            break Label_0265;
                        }
                        name = nextEntry.getName();
                        if (!nextEntry.isDirectory()) {
                            break;
                        }
                        this.a(name, s);
                    }
                }
                catch (IOException ex) {
                    ac.e("TremorLog_error::AdChoice::Zip Extract " + ex.getMessage());
                    this.a(bf.b.c);
                    return;
                }
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final byte[] array = new byte[1024];
                final File file = new File(s + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                final FileOutputStream fileOutputStream = new FileOutputStream(s + name);
                while (true) {
                    final int read = zipInputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                    byteArrayOutputStream.toByteArray();
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.reset();
                }
                fileOutputStream.close();
                zipInputStream.closeEntry();
                byteArrayOutputStream.close();
                continue;
            }
        }
        zipInputStream.close();
        fileInputStream.close();
        if (new File(s + "index.html").exists()) {
            ac.e("TremorLog_info::AdChoice::File present");
            this.a(bf.b.b);
            return;
        }
        ac.e("TremorLog_error::AdChoice::Unzip and Save Fail");
        this.a(bf.b.c);
    }
    
    private void a(final String s, final String s2) {
        final File file = new File(s2 + s);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }
    
    public void a(final Context context) {
        final BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, 4000);
        HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, 8000);
        final DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
        String string;
        File file;
        ByteArrayOutputStream byteArrayOutputStream;
        FileOutputStream fileOutputStream;
        try {
            this.d = new File(context.getFilesDir() + "/" + "adchoices");
            if (this.d.exists()) {
                ae.a(this.d);
            }
            this.d.mkdir();
            string = this.d.getAbsolutePath() + "/";
            file = new File(string + "tempAdChoice.zip");
            if (file.exists()) {
                file.delete();
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            fileOutputStream = new FileOutputStream(file);
            final HttpGet httpGet = new HttpGet("http://objects.tremormedia.com/mobile/AdChoices/adchoices_template.zip");
            ac.e("TremorLog_info::AdChoice::ADCHOICE_ZIP URL=http://objects.tremormedia.com/mobile/AdChoices/adchoices_template.zip");
            ae.a(httpGet, "http://objects.tremormedia.com/mobile/AdChoices/adchoices_template.zip");
            final HttpEntity entity = ((HttpClient)defaultHttpClient).execute((HttpUriRequest)httpGet).getEntity();
            if (entity != null) {
                final InputStream content = entity.getContent();
                final byte[] array = new byte[4096];
                while (true) {
                    final int read = content.read(array);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                    byteArrayOutputStream.toByteArray();
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.reset();
                }
            }
        }
        catch (Exception ex) {
            ac.e("TremorLog_error::AdChoice::Zip Download " + ex.getMessage());
            this.a(bf.b.c);
            return;
        }
        fileOutputStream.close();
        byteArrayOutputStream.close();
        if (file.exists()) {
            this.a(string);
            return;
        }
        ac.e("TremorLog_error::AdChoice:: Zip Download and save fail");
        this.a(bf.b.c);
    }
    
    @Override
    public void a(final String s, final n n) throws Exception {
        n.a(s, this.d.getAbsolutePath() + "/index.html");
    }
    
    @Override
    protected void e() {
        this.a(this.b);
    }
    
    @Override
    protected void f() {
        this.a(bf.b.e);
    }
    
    @Override
    protected void g() {
        this.a(this.b);
    }
    
    @Override
    public String toString() {
        return "JobDownloadAdChoices";
    }
}
