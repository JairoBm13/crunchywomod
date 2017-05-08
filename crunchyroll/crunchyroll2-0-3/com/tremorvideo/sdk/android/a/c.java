// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.webkit.WebSettings;
import android.os.Build$VERSION;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.tremorvideo.sdk.android.videoad.ac;
import com.tremorvideo.sdk.android.richmedia.ae;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import android.content.Context;
import java.util.Iterator;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.videoad.aw;
import java.util.List;
import java.io.File;

public class c
{
    private File a;
    private String b;
    private String c;
    private long d;
    private int e;
    private List<aw> f;
    private File g;
    private File h;
    private i i;
    
    public c(final JSONObject jsonObject) throws Exception {
        int i = 0;
        this.b = jsonObject.getString("xml-url");
        this.c = jsonObject.getString("template-url");
        if (jsonObject.has("template-crc32")) {
            this.d = jsonObject.getLong("template-crc32");
        }
        else {
            this.d = 0L;
        }
        if (jsonObject.has("auto-skip-seconds")) {
            this.e = jsonObject.getInt("auto-skip-seconds") * 1000;
        }
        else {
            this.e = 0;
        }
        if (!jsonObject.has("events")) {
            this.f = new ArrayList<aw>();
        }
        else {
            final JSONArray jsonArray = jsonObject.getJSONArray("events");
            this.f = new ArrayList<aw>(jsonArray.length());
            while (i < jsonArray.length()) {
                this.f.add(new aw(jsonArray.getJSONObject(i)));
                ++i;
            }
        }
    }
    
    private void a(final String s, final String s2) {
        final File file = new File(s2 + s);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }
    
    public int a() {
        return this.e;
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.f) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
    
    public aw a(final String s) {
        for (final aw aw : this.f) {
            if (aw.a().toString().equals(s)) {
                return aw;
            }
        }
        return null;
    }
    
    public void a(final Context context) {
        FileInputStream fileInputStream = null;
        ZipInputStream zipInputStream = null;
    Label_0311:
        while (true) {
            while (true) {
                String string;
                String name;
                try {
                    fileInputStream = new FileInputStream(this.h);
                    zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
                    this.a = new File(context.getFilesDir() + "/" + "BuyItNow");
                    if (this.a.exists()) {
                        ae.a(this.a);
                    }
                    this.a.mkdir();
                    string = this.a.getAbsolutePath() + "/";
                    while (true) {
                        final ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry == null) {
                            break Label_0311;
                        }
                        name = nextEntry.getName();
                        if (!nextEntry.isDirectory()) {
                            break;
                        }
                        this.a(name, string);
                    }
                }
                catch (IOException ex) {
                    ac.a(ex);
                    return;
                }
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final byte[] array = new byte[1024];
                final File file = new File(string + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                final FileOutputStream fileOutputStream = new FileOutputStream(string + name);
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
    }
    
    public void a(final Context context, final i.b b) {
        this.i = new i(context, b);
        final File file = new File(this.h().getAbsolutePath() + "/index.html");
        final WebSettings settings = this.i.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        this.i.loadUrl("file://" + file.getAbsolutePath());
    }
    
    public void a(final File g) {
        this.g = g;
    }
    
    public String b() {
        return this.b;
    }
    
    public void b(final File h) {
        this.h = h;
    }
    
    public String c() {
        return this.c;
    }
    
    public Long d() {
        return this.d;
    }
    
    public void e() {
        final File file = new File(this.a + "/source.xml");
        try {
            final FileInputStream fileInputStream = new FileInputStream(this.g);
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            final byte[] array = new byte[1048576];
            while (true) {
                final int read = fileInputStream.read(array, 0, 1048576);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(array, 0, read);
            }
            fileInputStream.close();
            fileOutputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void f() {
        if (this.i != null) {
            this.i.destroy();
            this.i = null;
            System.gc();
        }
    }
    
    public void g() {
        if ("BuyItNow" != null) {
            ae.a(this.a);
        }
        this.f();
    }
    
    public File h() {
        return this.a;
    }
}
